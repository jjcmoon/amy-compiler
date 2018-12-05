package amyc
package parsing

import grammarcomp.parsing._
import utils.Positioned
import ast.NominalTreeModule._
import Tokens._

// Implements the translation from parse trees to ASTs for the LL1 grammar,
// that is, this should correspond to Parser.amyGrammarLL1.
// We extend the plain ASTConstructor as some things will be the same -- you should
// override whatever has changed. You can look into ASTConstructor as an example.
class ASTConstructorLL1 extends ASTConstructor {

  // TODO: Override methods from ASTConstructor as needed

  override def constructExpr(ptree: NodeOrLeaf[Token]): Expr = {
    ptree match {
      case Node('Expr ::= _, List(l1expr)) =>
        constructl1Expr(l1expr)
    }
  }

  def constructl1Expr(ptree: NodeOrLeaf[Token]): Expr =
    ptree match {
      case Node('l1expr ::= List('l2expr, 'l1exprList), List(expr, exprlist)) =>
        constructOpOption(exprlist, constructl1Expr) match {
          case None => constructl2Expr(expr)
          case Some(e2) => Sequence(constructl2Expr(expr), e2)
        }
      case Node('l1expr ::= (VAL() :: _), List(Leaf(vt), param, _, value, _, body)) =>
        Let(constructParam(param), constructl2Expr(value), constructl1Expr(body)).setPos(vt)
    }

  def constructl2Expr(ptree: NodeOrLeaf[Token]): Expr = 
    ptree match {
      case Node('l2expr ::= _, List(e1, matchh)) =>
        matchh match {
          case Node(_, List()) =>
            constructl3Expr(e1)
          case Node(_, List(_, _, cases, _)) =>
            Match(constructl3Expr(e1), constructCases(cases))
        }
    }

  def constructCases(ptree: NodeOrLeaf[Token]): List[MatchCase] = ptree match {
    case Node('Cases ::= _, List(casse, rest)) =>
      constructCase(casse) :: constructList(rest, constructCase)
  }

  def constructl3Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l3expr ::= _, List(e, r)) => 
      constructOpExpr(constructl4Expr(e), r, constructl4Expr)
  }

  def constructl4Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l4expr ::= _, List(e, r)) => 
      constructOpExpr(constructl5Expr(e), r, constructl5Expr)
  }

  def constructl5Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l5expr ::= _, List(e, r)) => 
      constructOpExpr(constructl6Expr(e), r, constructl6Expr)
  }

  def constructl6Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l6expr ::= _, List(e, r)) => 
      constructOpExpr(constructl7Expr(e), r, constructl7Expr)
  }

  def constructl7Expr(ptree: NodeOrLeaf[Token]): Expr = {
    ptree match {
      case Node('l7expr ::= _, List(e, r)) => 
        constructOpExpr(constructl8Expr(e), r, constructl8Expr)
    }
  }

  def constructl8Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l8expr ::= _, List(e, r)) => 
      constructOpExpr(constructl9Expr(e), r, constructl9Expr)
  }

  def constructl9Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l9expr ::= (BANG()::_), List(Leaf(bt), e)) =>
      Not(constructl10Expr(e)).setPos(bt)
    case Node('l9expr ::= (MINUS()::_), List(Leaf(mt), e)) =>
      Neg(constructl10Expr(e)).setPos(mt)
    case Node('l9expr ::= _, List(e)) =>
      constructl10Expr(e)
  }

  def constructl10Expr(ptree: NodeOrLeaf[Token]): Expr = ptree match {
    case Node('l10expr ::= (IF() :: _), List(Leaf(it), _, cond, _, _, thenn, _, _, _, elze, _)) =>
      Ite(
        constructExpr(cond),
        constructExpr(thenn),
        constructExpr(elze)
      ).setPos(it)
    case Node('l10expr ::= (ERROR() :: _), List(Leaf(ert), _, msg, _)) =>
      Error(constructExpr(msg)).setPos(ert)
    case Node('l10expr ::= List('Literal2), List(lit)) =>  lit match {
      case Node('Literal2 ::= _, List(Leaf(lp@LPAREN()), m, Leaf(RPAREN()))) => {
        m match {
          case Node(_, List()) => UnitLiteral().setPos(lp)
          case Node(_, List(e)) => constructExpr(e)
        }
      }
      case Node('Literal2 ::= List(INTLITSENT), List(Leaf(it@INTLIT(i)))) =>
        IntLiteral(i).setPos(it)
      case Node('Literal2 ::= List(STRINGLITSENT), List(Leaf(st@STRINGLIT(s)))) =>
        StringLiteral(s).setPos(st)
      case Node('Literal2 ::= _, List(Leaf(tt@TRUE()))) =>
        BooleanLiteral(true).setPos(tt)
      case Node('Literal2 ::= _, List(Leaf(tf@FALSE()))) =>
        BooleanLiteral(false).setPos(tf)
    }
    case Node('l10expr ::= ('Id :: _), List(id, q2)) => {
      val (QualifiedName(mod, name), pos) = constructQname(ptree)
      q2 match {
        case Node(_, List()) =>
          Variable(name).setPos(pos)
        case Node(_, List(post, _, as, _)) =>
          Call(QualifiedName(mod,name), constructList(as, constructExpr, hasComma=true)).setPos(pos)
      }
    }
  }


  override def constructQname(ptree: NodeOrLeaf[Token]): (QualifiedName, Positioned) = {
    ptree match {
      case Node(_, List(id1, post)) => post match {
        case Node(_, List()) => {
          val (name, pos) = constructName(id1)
          (QualifiedName(None, name), pos)
        }
        case _ => {
          val qp = post match {
            case Node(_ ::= ('QPost::_), (post2::_)) => post2
            case _ => post
          }
          val (name, pos) = constructName(id1)
          qp match {
            case Node(_, List()) => 
              (QualifiedName(None, name), pos)
            case Node(_, List(_, id2)) => {
              val (name2, _) = constructName(id2)
              (QualifiedName(Some(name),name2), pos)
            }
          }
        }
      }
    }
  }

  override def constructPattern(ptree: NodeOrLeaf[Token]): Pattern = 
    ptree match {
      case Node('Pattern ::= ('Id::_), List(id, m)) =>{
        val (QualifiedName(mod, name), pos) = constructQname(ptree)
        m match {
          case Node(_, List()) => IdPattern(name).setPos(pos)
          case Node(_, List(post, _, patts, _)) => {
            val patterns = constructList(patts, constructPattern, hasComma = true)
            CaseClassPattern(QualifiedName(mod, name), patterns).setPos(pos)
          }
        }
      }
      case Node('Pattern ::= List(UNDERSCORE()), List(Leaf(ut))) =>
        WildcardPattern().setPos(ut)
      case Node('Pattern ::= List('Literal), List(lit)) =>
        val literal = constructLiteral(lit)
        LiteralPattern(literal).setPos(literal)
    }


  // Important helper method:
  // Because LL1 grammar is not helpful in implementing left associativity,
  // we give you this method to reconstruct it.
  // This method takes the left operand of an operator (leftopd)
  // as well as the tree that corresponds to the operator plus the right operand (ptree)
  // It parses the right hand side and then reconstruct the operator expression
  // with correct associativity.
  // If ptree is empty, it means we have no more operators and the leftopd is returned.
  // Note: You may have to override constructOp also, depending on your implementation
  def constructOpExpr(leftopd: Expr, ptree: NodeOrLeaf[Token], constructor: NodeOrLeaf[Token] => Expr): Expr = {
    ptree match {
      case Node(_, List()) => //epsilon rule of the nonterminals
        leftopd
      case Node(sym ::= _, List(op, rightNode))
        if Set('OrExpr, 'AndExpr, 'EqExpr, 'CompExpr, 'AddExpr, 'MultExpr) contains sym => {
        rightNode match {
          case Node(_, List(nextOpd, suf)) => // 'Expr? ::= Expr? ~ 'OpExpr,
            val nextAtom = constructor(nextOpd)
            constructOpExpr(constructOp(op)(leftopd, nextAtom).setPos(leftopd), suf, constructor) // captures left associativity
        }
    }
    }
  }

}

