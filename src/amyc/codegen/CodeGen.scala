package amyc
package codegen

import analyzer._
import ast.Identifier
import ast.SymbolicTreeModule.{Call => AmyCall, Div => AmyDiv, And => AmyAnd, Or => AmyOr, _}
import utils.{Context, Pipeline}
import wasm._
import Instructions._
import Utils._

// Generates WebAssembly code for an Amy program
object CodeGen extends Pipeline[(Program, SymbolTable), Module] {
  def run(ctx: Context)(v: (Program, SymbolTable)): Module = {
    val (program, table) = v


    // Generate code for an Amy module
    def cgModule(moduleDef: ModuleDef): List[Function] = {
      val ModuleDef(name, defs, optExpr) = moduleDef
      // Generate code for all functions
      defs.collect { 
        case fd@PureFunDef(_,_,_,_) if !builtInFunctions(fullName(name, fd.name)) =>
          cgFunction(fd, name, false, false)
        case fd@TailRecFunDef(_,_,_,_) =>
          cgFunction(fd, name, false, true)
        case fd@TrampFunDef(_,_,_,_) =>
          cgFunction(fd, name, false, false)
      } ++
      // Generate code for the "main" function, which contains the module expression
      optExpr.toList.map { expr =>
        val mainFd = PureFunDef(Identifier.fresh("main"), Nil, TypeTree(IntType), expr)
        cgFunction(mainFd, name, true, false)
      }
    }

    // Generate code for a function in module 'owner'
    def cgFunction(fd: FunDef, owner: Identifier, isMain: Boolean, isTailRec: Boolean): Function = {
      // Note: We create the wasm function name from a combination of
      // module and function name, since we put everything in the same wasm module.
      val index = table.getFunction(fd.name) match {
        case Some(x) => x.index
        case None => -1
      }
      Function(fullName(owner, fd.name), fd.params.size, isMain, index){ lh =>
        val locals = fd.paramNames.zipWithIndex.toMap
        val body = cgExpr(fd.body)(locals, lh)
        if (isTailRec)
          Loop32("tailjump") <:> 
          body <:> 
          End
        else if (isMain)
          body <:> Drop
        else
          body
      }
    }

    // Generate code for an expression expr.
    // Additional arguments are a mapping from identifiers (parameters and variables) to
    // their index in the wasm local variables, and a LocalsHandler which will generate
    // fresh local slots as required.
    def cgExpr(expr: Expr)(implicit locals: Map[Identifier, Int], lh: LocalsHandler): Code = {

      def binR(lhs:Expr, rhs:Expr): Code = 
        cgExpr(lhs) <:> cgExpr(rhs)

      def adt(args:List[Expr], index:Int): Code = {
        val loc = lh.getFreshLocal()
        // store old memory location
        GetGlobal(0) <:>
        SetLocal(loc) <:>
        // update memory pointer
        updateMemPointer(args.length+1) <:>
        // store adt type to mem
        GetLocal(loc) <:>
        Const(index) <:>
        Store <:>
        // store rest of fields
        cs2c(for ((arg,i) <- args.zipWithIndex) 
          yield i2c(GetLocal(loc)) <:>
          Const((i+1)*4) <:>
          Add <:>
          cgExpr(arg) <:>
          Store
        ) <:>
        // push pointer to adt on stack
        GetLocal(loc)
      }

      def updateMemPointer(amount:Int): Code = {
        GetGlobal(0) <:>
        Const(4*amount) <:>
        Add <:>
        SetGlobal(0)
      }


      def cgError(msg:Expr): Code = {
        cgExpr(msg) <:>
        Call("Std_printString") <:>
        Unreachable
      }

      expr match {
        case Variable(name) => GetLocal(locals(name))

        case IntLiteral(v) => Const(v)
        case BooleanLiteral(v) => Const(if (v) 1 else 0)
        case StringLiteral(v) => mkString(v)
        case UnitLiteral() => Const(0)

        case Plus(lhs,rhs) => binR(lhs,rhs) <:> Add
        case Minus(lhs,rhs) => binR(lhs,rhs) <:> Sub
        case Times(lhs,rhs) => binR(lhs,rhs) <:> Mul
        case AmyDiv(lhs,rhs) => binR(lhs,rhs) <:> Div
        case Mod(lhs,rhs) => binR(lhs,rhs) <:> Rem
        case LessThan(lhs,rhs) => binR(lhs,rhs) <:> Lt_s
        case LessEquals(lhs,rhs) => binR(lhs,rhs) <:> Le_s
        case AmyAnd(lhs,rhs) => {
          //Short circuit
          cgExpr(lhs) <:>
          If_i32 <:>
            cgExpr(rhs) <:>
          Else <:>
            Const(0) <:>
          End
        } 
        case AmyOr(lhs,rhs) => {
          //Short circuit
          cgExpr(lhs) <:>
          If_i32 <:>
            Const(1) <:>
          Else <:>
            cgExpr(rhs) <:>
          End
        } 
        case Equals(lhs,rhs) => binR(lhs,rhs) <:> Eq
        case Concat(lhs,rhs) => binR(lhs,rhs) <:> Call("String_concat")

        case Not(e) => cgExpr(e) <:> Eqz
        case Neg(e) => i2c(Const(0)) <:> cgExpr(e) <:> Sub

        case AmyCall(qn, args) => {
          val argsCode = is2c(args flatMap (x => cgExpr(x).instructions))
          table.getFunction(qn) match {
            case Some(FunSig(_, _, owner,_)) => argsCode <:> Call(fullName(owner, qn))
            case _ => table.getConstructor(qn) match {
              case Some(ConstrSig(_,_,index)) => adt(args,index)
              case _ => ctx.reporter.fatal("Name not found during codegen. This should have been caught by name analyzer!")

            }
          }
        }

        case TailCall(args) => {
          // execute arguments
          is2c(args flatMap (x => cgExpr(x).instructions)) <:>
          // pop args of stack and store in appropriate register
          is2c(args.zipWithIndex.map {case (arg,i) => 
            SetLocal(args.length-i-1)}) <:>
          // jump to start
          Br("tailjump")
        }

        case IndirectCall(qn, args) => {
          // lookup ID of function index
          val tableID = table.getFunction(qn).orNull.index
          // Set up arguments and id in ADT for trampoline
          adt(args, tableID)

          // TODO: might be better if Indirect calls are already translated to equivalent amy-ish code (combined with a library/TCE.scala ?) in TCE optimizer
        }

        case Trampoline(qn, args) => {
          val ret = lh.getFreshLocal()
          
          cgExpr(AmyCall(qn,args)) <:>
          SetLocal(ret) <:>
          Loop32("tramp") <:>
            // Check wheter the returned function wants to a call new function
            GetLocal(ret) <:>
            Const(-1) <:>
            Eq <:>
            If_i32 <:>
              // If no: get return value and free memory
              updateMemPointer(-1) <:>
              GetGlobal(0) <:>
              Load <:>
            Else <:>
              // If yes:
              // free memory
              GetLocal(ret) <:>
              SetGlobal(0) <:>
              // load param
              GetLocal(ret) <:>
              Const(4) <:>
              Add <:>
              Load <:>
              // load table ID
              GetGlobal(0) <:>
              Load <:>
              // Call function and repeat
              Call_indirect <:>
              SetLocal(ret) <:>
              Br("tramp") <:>
            End <:>//If_i32
          End//tramp

        }
        
        case TrampReturn(e2) =>
          // Store return value in memory
          GetGlobal(0) <:> cgExpr(e2) <:> Store <:> updateMemPointer(1) <:>
          // Return -1 (=> No trampoline call)
          Const(-1)

        case Sequence(e1,e2) => 
          cgExpr(e1) <:> Drop <:> cgExpr(e2)

        case Let(df, value, body) => {
          val new_local = lh.getFreshLocal()
          cgExpr(value) <:>
          SetLocal(new_local) <:>
          cgExpr(body)(locals+(df.name -> new_local), lh)
        }

        case Ite(cond,thenn,elze) => {
          cgExpr(cond) <:> If_i32 <:> cgExpr(thenn) <:> Else <:> cgExpr(elze) <:> End
        }

        case Match(scrut, cases) => {

          def cgPattern(pat:Pattern, mloc:Int): (Code, Map[Identifier, Int]) = pat match {
            case WildcardPattern() => (Const(1), Map())
            case LiteralPattern(lit) =>
              (GetLocal(mloc) <:> cgExpr(lit) <:> Eq, Map())
            case IdPattern(name) => {
              val fresh = lh.getFreshLocal()
              (GetLocal(mloc) <:> SetLocal(fresh) <:> Const(1), Map(name->fresh))
            }
            case CaseClassPattern(constr,args) => {
              val ConstrSig(_,_,ccId) = table.getConstructor(constr).orNull
              val argrec = args.zipWithIndex map { case (arg,i) =>
                val fresh = lh.getFreshLocal
                val (nestpatcode, morEnv) = cgPattern(arg,fresh)
                
                (
                  GetLocal(mloc) <:>
                  Const((i+1)*4) <:>
                  Add <:>
                  Load <:>
                  SetLocal(fresh) <:>
                  nestpatcode <:>
                  And, 
                  morEnv
                )
              }
              (GetLocal(mloc) <:> Load <:> Const(ccId) <:> Eq // Is type of case class correct,
              <:> cs2c(argrec map (_._1)), // and match arguments
              (argrec flatMap (_._2)).toMap )
            }
          }

          def cgCases(cases:List[MatchCase],scrutloc:Int): Code = cases match {
              case Nil =>
                cgError(StringLiteral("Match error"))
              case MatchCase(pat,expr)::cazes => {
                val (patcode, newlocs) = cgPattern(pat, scrutloc) 
                patcode <:>
                If_i32 <:>
                cgExpr(expr)(locals++newlocs,lh) <:>
                Else <:>
                cgCases(cazes,scrutloc) <:>
                End
              }
            }

          val scrutloc = lh.getFreshLocal()
          cgExpr(scrut) <:>
          SetLocal(scrutloc) <:>
          cgCases(cases,scrutloc)
        }


        case Error(msg) => 
          cgError(Concat(StringLiteral("Error: "), msg))


      }
    }

    Module(
      program.modules.last.name.name,
      defaultImports,
      globalsNo,
      wasmFunctions ++ (program.modules flatMap cgModule)
    )

  }
  
}
