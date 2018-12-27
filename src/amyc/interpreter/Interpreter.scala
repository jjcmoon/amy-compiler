package amyc
package interpreter

import utils._
import ast.SymbolicTreeModule._
import ast.Identifier
import analyzer.SymbolTable

// An interpreter for Amy programs, implemented in Scala
object Interpreter extends Pipeline[(Program, SymbolTable), Unit] {

  // A class that represents a value computed by interpreting an expression
  abstract class Value {
    def asInt: Int = this.asInstanceOf[IntValue].i
    def asBoolean: Boolean = this.asInstanceOf[BooleanValue].b
    def asString: String = this.asInstanceOf[StringValue].s

    override def toString: String = this match {
      case IntValue(i) => i.toString
      case BooleanValue(b) => b.toString
      case StringValue(s) => s
      case UnitValue => "()"
      case CaseClassValue(constructor, args) =>
        constructor.name + "(" + args.map(_.toString).mkString(", ") + ")"
    }
  }
  case class IntValue(i: Int) extends Value
  case class BooleanValue(b: Boolean) extends Value
  case class StringValue(s: String) extends Value
  case object UnitValue extends Value
  case class CaseClassValue(constructor: Identifier, args: List[Value]) extends Value

  def static_interpret(expr:Expr): Value = expr match {
    case IntLiteral(i) =>
      IntValue(i)
    case BooleanLiteral(b) =>
      BooleanValue(b)
    case StringLiteral(s) =>
      StringValue(s)
    case UnitLiteral() =>
      UnitValue
    case Plus(lhs, rhs) =>
      IntValue(static_interpret(lhs).asInt + static_interpret(rhs).asInt)
    case Minus(lhs, rhs) =>
      IntValue(static_interpret(lhs).asInt - static_interpret(rhs).asInt)
    case Times(lhs, rhs) =>
      IntValue(static_interpret(lhs).asInt * static_interpret(rhs).asInt)
    case Div(lhs, rhs) => 
      IntValue(static_interpret(lhs).asInt / static_interpret(rhs).asInt)
    case Mod(lhs, rhs) =>
      IntValue(static_interpret(lhs).asInt % static_interpret(rhs).asInt)
    case LessThan(lhs, rhs) =>
      BooleanValue(static_interpret(lhs).asInt < static_interpret(rhs).asInt)
    case LessEquals(lhs, rhs) =>
      BooleanValue(static_interpret(lhs).asInt <= static_interpret(rhs).asInt)
    case And(lhs, rhs) =>
      BooleanValue(static_interpret(lhs).asBoolean && static_interpret(rhs).asBoolean)
    case Or(lhs, rhs) =>
      BooleanValue(static_interpret(lhs).asBoolean || static_interpret(rhs).asBoolean)
    case Equals(lhs, rhs) => {
      val ilhs = static_interpret(lhs);
      val irhs = static_interpret(rhs);
      ilhs match {
        case UnitValue => BooleanValue(true)
        case IntValue(_) => BooleanValue(ilhs.asInt == irhs.asInt)
        case BooleanValue(_) => BooleanValue(ilhs.asBoolean == irhs.asBoolean)
        case StringValue(_) => BooleanValue(ilhs eq irhs)
        case CaseClassValue(_, _) => BooleanValue(ilhs eq irhs)
      }
    }
    case Concat(lhs, rhs) =>
      StringValue(static_interpret(lhs).asString concat static_interpret(rhs).asString )
    case Not(e) =>
      BooleanValue(!static_interpret(e).asBoolean)
    case Neg(e) =>
      IntValue(-static_interpret(e).asInt)
  }

  def value2TreeMod(v:Value):Expr = v match {
    case IntValue(n) => IntLiteral(n)
    case UnitValue => UnitLiteral()
    case BooleanValue(b) => BooleanLiteral(b)
    case StringValue(s) => StringLiteral(s)
    case CaseClassValue(constr, args) => Call(constr, args map value2TreeMod)
  }

  def run(ctx: Context)(v: (Program, SymbolTable)): Unit = {
    val (program, table) = v

    // These built-in functions do not have an Amy implementation in the program,
    // instead their implementation is encoded in this map
    val builtIns: Map[(String, String), (List[Value]) => Value] = Map(
      ("Std", "printInt")    -> { args => println(args.head.asInt); UnitValue },
      ("Std", "printString") -> { args => println(args.head.asString); UnitValue },
      ("Std", "readString")  -> { args => StringValue(scala.io.StdIn.readLine()) },
      ("Std", "readInt")     -> { args =>
        val input = scala.io.StdIn.readLine()
        try {
          IntValue(input.toInt)
        } catch {
          case ne: NumberFormatException =>
            ctx.reporter.fatal(s"""Could not parse "$input" to Int""")
        }
      },
      ("Std", "intToString")   -> { args => StringValue(args.head.asInt.toString) },
      ("Std", "digitToString") -> { args => StringValue(args.head.asInt.toString) }
    )

    // Utility functions to interface with the symbol table.
    def isConstructor(name: Identifier) = table.getConstructor(name).isDefined
    def findFunctionOwner(functionName: Identifier) = table.getFunction(functionName).get.owner.name
    def findFunction(owner: String, name: String) = {
      program.modules.find(_.name.name == owner).get.defs.collectFirst {
        case fd@PureFunDef(fn, _, _, _) if fn.name == name => fd
      }.get
    }

    // Interprets a function, using evaluations for local variables contained in 'locals'
    def interpret(expr: Expr)(implicit locals: Map[Identifier, Value]): Value = {
      expr match {
        case Variable(name) =>
          locals(name)
        case IntLiteral(i) =>
          IntValue(i)
        case BooleanLiteral(b) =>
          BooleanValue(b)
        case StringLiteral(s) =>
          StringValue(s)
        case UnitLiteral() =>
          UnitValue
        case Plus(lhs, rhs) =>
          IntValue(interpret(lhs).asInt + interpret(rhs).asInt)
        case Minus(lhs, rhs) =>
          IntValue(interpret(lhs).asInt - interpret(rhs).asInt)
        case Times(lhs, rhs) =>
          IntValue(interpret(lhs).asInt * interpret(rhs).asInt)
        case Div(lhs, rhs) => {
          val ilhs = interpret(lhs).asInt;
          val irhs = interpret(rhs).asInt;
          if (irhs == 0) ctx.reporter.fatal("Division by 0")
          else IntValue(ilhs / irhs)
        }
        case Mod(lhs, rhs) =>
          IntValue(interpret(lhs).asInt % interpret(rhs).asInt)
        case LessThan(lhs, rhs) =>
          BooleanValue(interpret(lhs).asInt < interpret(rhs).asInt)
        case LessEquals(lhs, rhs) =>
          BooleanValue(interpret(lhs).asInt <= interpret(rhs).asInt)
        case And(lhs, rhs) =>
          BooleanValue(interpret(lhs).asBoolean && interpret(rhs).asBoolean)
        case Or(lhs, rhs) =>
          BooleanValue(interpret(lhs).asBoolean || interpret(rhs).asBoolean)
        case Equals(lhs, rhs) => {
          val ilhs = interpret(lhs);
          val irhs = interpret(rhs);
          ilhs match {
            case UnitValue => BooleanValue(true)
            case IntValue(_) => BooleanValue(ilhs.asInt == irhs.asInt)
            case BooleanValue(_) => BooleanValue(ilhs.asBoolean == irhs.asBoolean)
            case StringValue(_) => BooleanValue(ilhs eq irhs)
            case CaseClassValue(_, _) => BooleanValue(ilhs eq irhs)
          }
        }
        case Concat(lhs, rhs) =>
          StringValue(interpret(lhs).asString concat interpret(rhs).asString )
        case Not(e) =>
          BooleanValue(!interpret(e).asBoolean)
        case Neg(e) =>
          IntValue(-interpret(e).asInt)
        case Call(qname, args) => {

          val argsval = args map (x=>interpret(x))
          if (isConstructor(qname)) {
            return CaseClassValue(qname, argsval)
          }

          val owner = findFunctionOwner(qname)
          if (builtIns contains ((owner.toString, qname.toString)) ) {
            builtIns((owner.toString, qname.toString))(argsval)
          }

          else {
            val f = findFunction(owner.toString, qname.toString)
            val argmap = ((f.params map (x=>x.name)) zip argsval).toMap
            interpret(f.body)(locals ++ argmap)
          }
        }
        case Sequence(e1, e2) => { 
          interpret(e1);
          interpret(e2)
        }
        case Let(df, value, body) =>
          val newloc = (df.name -> interpret(value))
          interpret(body)(locals + newloc)
        case Ite(cond, thenn, elze) =>
          if (interpret(cond).asBoolean) {
            interpret(thenn)
          } else {
            interpret(elze)
          }
        case Match(scrut, cases) =>

          val evS = interpret(scrut)

          // Returns a list of pairs id -> value,
          // where id has been bound to value within the pattern.
          // Returns None when the pattern fails to match.
          // Note: Only works on well typed patterns (which have been ensured by the type checker).
          def matchesPattern(v: Value, pat: Pattern): Option[List[(Identifier, Value)]] = {
            ((v, pat): @unchecked) match {
              case (_, WildcardPattern()) =>
                Some(List())
              case (_, IdPattern(name)) =>
                Some(List(name -> v))
              case (IntValue(i1), LiteralPattern(IntLiteral(i2))) =>
                if (i1 == i2) Some(List())
                else None
              case (BooleanValue(b1), LiteralPattern(BooleanLiteral(b2))) =>
                if (b1 == b2) Some(List())
                else None
              case (StringValue(_), LiteralPattern(StringLiteral(_))) =>
                None
              case (UnitValue, LiteralPattern(UnitLiteral())) =>
                Some(List())
              case (CaseClassValue(con1, realArgs), CaseClassPattern(con2, formalArgs)) => {
                if (con1 == con2) {
                  val mtch = (realArgs zip formalArgs) map {
                    case (a,b) => matchesPattern(a,b)
                  }
                  mtch.fold(Some(List())) {
                    case (Some(x), Some(y)) => Some(x++y)
                    case _ => None
                  }
                }
                else {
                  None
                }
              }
              }
          }

          // Main "loop" of the implementation: Go through every case,
          // check if the pattern matches, and if so return the evaluation of the case expression
          for {
             MatchCase(pat, rhs) <- cases
            moreLocals <- matchesPattern(evS, pat)
          } {
            return interpret(rhs)(locals ++ moreLocals)
          }
          // No case matched: The program fails with a match error
          ctx.reporter.fatal(s"Match error: ${evS.toString}@${scrut.position}")

        case Error(msg) =>
          ctx.reporter.fatal(interpret(msg).asString)
      }
    }

    // Body of the interpreter: Go through every module in order
    // and evaluate its expression if present
    for {
      m <- program.modules
      e <- m.optExpr
    } {
      interpret(e)(Map())
    }
  }
}
