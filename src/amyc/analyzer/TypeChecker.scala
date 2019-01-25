package amyc
package analyzer

import utils._
import ast.SymbolicTreeModule._
import ast.Identifier

// The type checker for Amy
// Takes a symbolic program and rejects it if it does not follow the Amy typing rules.
object TypeChecker extends Pipeline[(Program, SymbolTable), (Program, SymbolTable)] {

  def run(ctx: Context)(v: (Program, SymbolTable)): (Program, SymbolTable) = {
    import ctx.reporter._

    val (program, table) = v

    case class Constraint(found: Type, expected: Type, pos: Position)

    // Represents a type variable.
    // It extends Type, but it is meant only for internal type checker use,
    //  since no Amy value can have such type.
    case class TypeVariable private (id: Int) extends Type
    object TypeVariable {
      private val c = new UniqueCounter[Unit]
      def fresh(): TypeVariable = TypeVariable(c.next(()))
    }

    // Generates typing constraints for an expression `e` with a given expected type.
    // The environment `env` contains all currently available bindings (you will have to
    //  extend these, e.g., to account for local variables).
    // Returns a list of constraints among types. These will later be solved via unification.
    def genConstraints(e: Expr, expected: Type)(implicit env: Map[Identifier, Type]): List[Constraint] = {
      
      // This helper returns a list of a single constraint recording the type
      //  that we found (or generated) for the current expression `e`
      def topLevelConstraint(found: Type): List[Constraint] =
        List(Constraint(found, expected, e.position))

      def binConstraint(out:Type, in:Type, lhs:Expr, rhs:Expr): List[Constraint] =
        topLevelConstraint(out) ++ genConstraints(lhs, in) ++ genConstraints(rhs, in)
      
      e match {
        case IntLiteral(_) =>
          topLevelConstraint(IntType)

        case StringLiteral(_) =>
          topLevelConstraint(StringType)

        case BooleanLiteral(_) =>
          topLevelConstraint(BooleanType)

        case UnitLiteral() =>
          topLevelConstraint(UnitType)


        case Variable(name) =>
          topLevelConstraint(env(name))


        case Plus(lhs, rhs) => binConstraint(IntType, IntType, lhs, rhs)
        case Minus(lhs, rhs) => binConstraint(IntType, IntType, lhs, rhs)
        case Times(lhs, rhs) => binConstraint(IntType, IntType, lhs, rhs)
        case Div(lhs, rhs) => binConstraint(IntType, IntType, lhs, rhs)
        case Mod(lhs, rhs) => binConstraint(IntType, IntType, lhs, rhs)
        case LessThan(lhs, rhs) => binConstraint(BooleanType, IntType, lhs, rhs)
        case LessEquals(lhs, rhs) => binConstraint(BooleanType, IntType, lhs, rhs)
        case And(lhs, rhs) => binConstraint(BooleanType, BooleanType, lhs, rhs)
        case Or(lhs, rhs) => binConstraint(BooleanType, BooleanType, lhs, rhs)
        case Concat(lhs, rhs) => binConstraint(StringType, StringType, lhs, rhs)
        case Equals(lhs, rhs) => binConstraint(BooleanType, TypeVariable.fresh(), lhs, rhs)
        
        case Not(e0) =>
          topLevelConstraint(BooleanType) ++ genConstraints(e0, BooleanType)
        case Neg(e0) =>
          topLevelConstraint(IntType) ++ genConstraints(e0, IntType)

        case Sequence(e1,e2) => {
          val tp1 = TypeVariable.fresh()
          val tp2 = TypeVariable.fresh()
          topLevelConstraint(tp2) ++ genConstraints(e1, tp1) ++ genConstraints(e2, tp2)
        }

        case Error(msg) =>
          genConstraints(msg, StringType) ++ topLevelConstraint(TypeVariable.fresh())

        case Ite(cond, thenn, elze) => {
          val iftp = TypeVariable.fresh()
          genConstraints(cond, BooleanType) ++ topLevelConstraint(iftp) ++ genConstraints(thenn, iftp) ++ genConstraints(elze, iftp)
        }

        case Let(df, value, body) => {
          val vartp = df.tt.tpe
          val tottp = TypeVariable.fresh()
          genConstraints(value, vartp) ++ genConstraints(body, tottp)(env + (df.name->vartp)) ++ topLevelConstraint(tottp)
        }

        case Call(qname, args) => {
          val (argTypes, retType) = table.getFunction(qname) match {
            case Some(FunSig(argTypes, retType, _, _)) => (argTypes, retType)
            case _ => table.getConstructor(qname) match {
                case Some(ConstrSig(argTypes, parent, _)) => (argTypes, ClassType(parent))
                case _ => ctx.reporter.fatal("Name not found whilst type checking. This should have been caught by name analyzer!")
              }
          }
          (args zip argTypes).flatMap{case (arg,argtp) => genConstraints(arg,argtp)} ++ topLevelConstraint(retType)
        }
        
        case Match(scrut, cases) =>
          // Returns additional constraints from within the pattern with all bindings
          // from identifiers to types for names bound in the pattern.
          // (This is analogous to `transformPattern` in NameAnalyzer.)
          def handlePattern(pat: Pattern, scrutExpected: Type):
            (List[Constraint], Map[Identifier, Type]) =
          {
            pat match {
              case WildcardPattern() => (Nil, Map())
              case IdPattern(name) => (Nil, Map(name->scrutExpected))
              case LiteralPattern(lit) => (genConstraints(lit,scrutExpected), Map())
              case CaseClassPattern(constr, args) => {
                val ConstrSig(argtps, parent, _) = table.getConstructor(constr).orNull
                val argen = (args zip argtps).map {case (arg, exptp) => handlePattern(arg, exptp)}
                (Constraint(ClassType(parent), scrutExpected, pat.position) :: argen.flatMap(_._1), argen.flatMap(_._2).toMap)
              }
            }
          }

          def handleCase(cse: MatchCase, scrutExpected: Type): List[Constraint] = {
            val (patConstraints, moreEnv) = handlePattern(cse.pat, scrutExpected)
            genConstraints(cse.expr, expected)(env++moreEnv) ++ patConstraints
          }

          val st = TypeVariable.fresh()
          genConstraints(scrut, st) ++ cases.flatMap(cse => handleCase(cse, st))

      }
    }


    // Given a list of constraints `constraints`, replace every occurence of type variable
    //  with id `from` by type `to`.
    def subst_*(constraints: List[Constraint], from: Int, to: Type): List[Constraint] = {
      // Do a single substitution.
      def subst(tpe: Type, from: Int, to: Type): Type = {
        tpe match {
          case TypeVariable(`from`) => to
          case other => other
        }
      }

      constraints map { case Constraint(found, expected, pos) =>
        Constraint(subst(found, from, to), subst(expected, from, to), pos)
      }
    }

    // Solve the given set of typing constraints and
    //  call `typeError` if they are not satisfiable.
    // We consider a set of constraints to be satisfiable exactly if they unify.
    def solveConstraints(constraints: List[Constraint]): Unit = {
      constraints match {
        case Nil => ()
        case Constraint(found, expected, pos) :: more => {
          expected match {
            case TypeVariable(idn) => solveConstraints(subst_*(more, idn, found))
            case _ => {
              found match {
                case TypeVariable(idn) => solveConstraints(subst_*(more, idn, expected))
                case _ =>
                  if (found != expected) 
                    ctx.reporter.fatal(s"Type error: found type '${found}' but expected type '${expected}'", pos) 
                  else 
                    solveConstraints(more)

              }
            }
          }
        }
          // HINT: You can use the `subst_*` helper above to replace a type variable
          //       by another type in your current set of constraints.
      }
    }

    // Putting it all together to type-check each module's functions and main expression.
    program.modules.foreach { mod =>
      // Put function parameters to the symbol table, then typecheck them against the return type
      mod.defs.collect { case PureFunDef(_, params, retType, body) =>
        val env = params.map{ case ParamDef(name, tt) => name -> tt.tpe }.toMap
        solveConstraints(genConstraints(body, retType.tpe)(env))
      }

      // Type-check expression if present. We allow the result to be of an arbitrary type by
      // passing a fresh (and therefore unconstrained) type variable as the expected type.
      val tv = TypeVariable.fresh()
      mod.optExpr.foreach(e => solveConstraints(genConstraints(e, tv)(Map())))
    }

    v

  }
}
