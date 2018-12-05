
object BT {
  abstract class Tree
  case class Nil() extends Tree
  case class Branch(l:Tree, i:Int, r:Tree) extends Tree

  def isLeaf(t: Tree): Boolean = {
    t match {
      case Nil() => true
      case _ => false
    }
  }

  def root(t: Tree): Int = {
    t match {
      case Nil() => error("root(Nil())")
      case Branch(_, v, _) => v
    }
  }

  def height(t: Tree): Int = {
    t match {
      case Nil() => 0
      case Branch(l, _, r) => 1 + Arithmetic.max(height(r), height(l))
    }
  }

  def size(t: Tree): Int = {
    t match {
      case Nil() => 0
      case Branch(l, _, r) => 1 + size(l) + size(r)
    }
  }

  def isSorted(t: Tree): Boolean = {
    t match {
      case Nil() => true
      case Branch(l, v, r) => 
        ( l match {
            case Nil() => true
            case Branch(_, lv, _) => lv < v
          } )
        &&
        ( r match {
            case Nil() => true
            case Branch(_, rv, _) => !(rv <= v)
          } )
    }
  }

  def toString(t: Tree): String = {
    t match {
      case Nil() => "Nil"
      case Branch(l, v, r) => 
        "(" ++ Std.digitToString(v) ++ " " ++ toString(l) ++ " " ++ toString(r) ++ ")"
    }
  }

  def toList(t: Tree): L.List = {
    t match {
      case Nil() => L.Nil()
      case Branch(l, v, r) => L.concat(L.Cons(v, toList(l)), toList(r))
    }
  }


}
