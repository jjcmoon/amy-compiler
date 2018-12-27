package amyc
package optimizer

import utils._
import analyzer._
import ast.SymbolicTreeModule._
import codegen.Utils.fullName

object TailcallOptimizer extends Pipeline[(Program, SymbolTable), (Program, SymbolTable)] {



  def run(ctx: Context)(v: (Program, SymbolTable)): (Program, SymbolTable) = {
    import ctx.reporter._

    val (program, table) = v

    def hasTailWith(e: Expr)(implicit p: Call=>Boolean):Boolean = e match {
      case c@Call(_,_) => p(c)
      case Ite(cond, thenn, elze) =>
        hasTailWith(thenn) || hasTailWith(elze)
      case Match(_, cases) =>
        cases.foldLeft (false) {case (x,y) => x || hasTailWith(y.expr) }
      case Let(_, _, body) => hasTailWith(body)
      case Sequence(e1, e2) =>
        hasTailWith(e2)
      case _ => false
    }

    def isFunctionCall(c:Call): Boolean = c match {
      case Call(qn, args) => table.getFunction(qn) match {
        case None => false
        case _ => true 
      }
    }

    def hasRecTailCall(e: Expr, owner: QualifiedName): Boolean =
      hasTailWith(e)(x => x.qname==owner)

    def hasNonRecTailCall(e: Expr, owner: QualifiedName): Boolean =
      hasTailWith(e)(x => x.qname!=owner)

    val trampFunDefs: List[QualifiedName] = program.modules.flatMap(mod => mod.defs.filter {
      case x:PureFunDef => 
        hasNonRecTailCall(x.body, x.name) && mod.name.toString!="Std" && x.params.length == 1
      case _ => false
    }.map(x => x.name))


    def rewriteProgram(p:Program, f:ClassOrFunDef=>ClassOrFunDef): Program = {
      Program(
        p.modules map { 
          case ModuleDef(name, defs, optExpr) =>
            ModuleDef(name, defs.map(f), optExpr.map(rewriteTrampRef))
        }
      )
    }

    def rewriteDef(df:ClassOrFunDef):ClassOrFunDef =
      df match {
        case PureFunDef(n,p,r,body) =>
          if (trampFunDefs.contains(n))
            TrampFunDef(n,p,r,rewriteTrampBody(body))
          else if (hasRecTailCall(body, n))
            TailRecFunDef(n,p,r,rewriteTailCall(body))
          else
            PureFunDef(n,p,r,rewriteTrampRef(body))
        case _ => df
      }

    def rewriteTail(e:Expr, isCall: Call=>Expr, noCall: Expr=>Expr):Expr = e match {
      case c@Call(_,_) if isFunctionCall(c) =>
        isCall(c)
      case Ite(cond,thenn,elze) =>
        Ite(cond,rewriteTail(thenn, isCall, noCall),rewriteTail(elze, isCall, noCall))
      case Sequence(e1, e2) =>
        Sequence(e1, rewriteTail(e2, isCall, noCall))
      case Match(scrut, cases) =>
        Match(scrut, cases.map {case MatchCase(pat,body) => 
                                    MatchCase(pat, rewriteTail(body, isCall, noCall))} )
      case Let(df,value,body) =>
        Let(df,value,rewriteTail(body, isCall, noCall))
      case _ => noCall(e)
    }

    def rewriteTailCall(e: Expr):Expr =
      rewriteTail(e, {case Call(n,args) => TailCall(args)}, e=>e)

    def rewriteTrampBody(e:Expr):Expr =
      rewriteTail(e, {case Call(n,args) => IndirectCall(n,args)}, e=>TrampReturn(e))

    def rewriteTrampRef(e:Expr):Expr =
      rewriteTail(e, {case Call(n,args) => 
        if (trampFunDefs.contains(n))
          Trampoline(n,args map rewriteTrampRef)
        else 
          Call(n,args map rewriteTrampRef)
        },
        (x=>x))



    (rewriteProgram(program, rewriteDef), table)

  }
}