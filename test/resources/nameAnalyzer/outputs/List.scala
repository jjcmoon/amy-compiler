object Std_0 {

  def printInt_0(i_0: Int): Unit = {
    error("")
  }

  def printString_0(s_0: String): Unit = {
    error("")
  }

  def printBoolean_0(b_0: Boolean): Unit = {
    printString_0(booleanToString_0(b_0))
  }

  def readString_0(): String = {
    error("")
  }

  def readInt_0(): Int = {
    error("")
  }

  def intToString_0(i_1: Int): String = {
    (if((i_1 < 0)) {
      ("-" ++ intToString_0(-(i_1)))
    } else {
      (
        val rem_0: Int =
          (i_1 % 10);
        val div_0: Int =
          (i_1 / 10);
        (if((div_0 == 0)) {
          digitToString_0(rem_0)
        } else {
          (intToString_0(div_0) ++ digitToString_0(rem_0))
        })
      )
    })
  }

  def digitToString_0(i_2: Int): String = {
    error("")
  }

  def booleanToString_0(b_1: Boolean): String = {
    (if(b_1) {
      "true"
    } else {
      "false"
    })
  }
}


object O_0 {

  abstract class Option_0

  case class None_0() extends Option_0

  case class Some_0(v: Int) extends Option_0

  def isDefined_0(o_0: Option_0): Boolean = {
    o_0 match {
      case None_0() =>
        false
      case _ =>
        true
    }
  }

  def get_0(o_1: Option_0): Int = {
    o_1 match {
      case Some_0(i_3) =>
        i_3
      case None_0() =>
        error("get(None)")
    }
  }

  def getOrElse_0(o_2: Option_0, i_4: Int): Int = {
    o_2 match {
      case None_0() =>
        i_4
      case Some_0(oo_0) =>
        oo_0
    }
  }

  def orElse_0(o1_0: Option_0, o2_0: Option_0): Option_0 = {
    o1_0 match {
      case Some_0(_) =>
        o1_0
      case None_0() =>
        o2_0
    }
  }

  def toList_0(o_3: Option_0): List_0 = {
    o_3 match {
      case Some_0(i_5) =>
        Cons_0(i_5, Nil_0())
      case None_0() =>
        Nil_0()
    }
  }
}


object L_0 {

  abstract class List_0

  case class Nil_0() extends List_0

  case class Cons_0(v: Int, v: List_0) extends List_0

  def isEmpty_0(l_0: List_0): Boolean = {
    l_0 match {
      case Nil_0() =>
        true
      case _ =>
        false
    }
  }

  def length_0(l_1: List_0): Int = {
    l_1 match {
      case Nil_0() =>
        0
      case Cons_0(_, t_0) =>
        (1 + length_0(t_0))
    }
  }

  def head_0(l_2: List_0): Int = {
    l_2 match {
      case Cons_0(h_0, _) =>
        h_0
      case Nil_0() =>
        error("head(Nil)")
    }
  }

  def headOption_0(l_3: List_0): Option_0 = {
    l_3 match {
      case Cons_0(h_1, _) =>
        Some_0(h_1)
      case Nil_0() =>
        None_0()
    }
  }

  def reverse_0(l_4: List_0): List_0 = {
    reverseAcc_0(l_4, Nil_0())
  }

  def reverseAcc_0(l_5: List_0, acc_0: List_0): List_0 = {
    l_5 match {
      case Nil_0() =>
        acc_0
      case Cons_0(h_2, t_1) =>
        reverseAcc_0(t_1, Cons_0(h_2, acc_0))
    }
  }

  def indexOf_0(l_6: List_0, i_6: Int): Int = {
    l_6 match {
      case Nil_0() =>
        -(1)
      case Cons_0(h_3, t_2) =>
        (if((h_3 == i_6)) {
          0
        } else {
          (
            val rec_0: Int =
              indexOf_0(t_2, i_6);
            (if((0 <= rec_0)) {
              (rec_0 + 1)
            } else {
              -(1)
            })
          )
        })
    }
  }

  def range_0(from_0: Int, to_0: Int): List_0 = {
    (if((to_0 < from_0)) {
      Nil_0()
    } else {
      Cons_0(from_0, range_0((from_0 + 1), to_0))
    })
  }

  def sum_0(l_7: List_0): Int = {
    l_7 match {
      case Nil_0() =>
        0
      case Cons_0(h_4, t_3) =>
        (h_4 + sum_0(t_3))
    }
  }

  def concat_0(l1_0: List_0, l2_0: List_0): List_0 = {
    l1_0 match {
      case Nil_0() =>
        l2_0
      case Cons_0(h_5, t_4) =>
        Cons_0(h_5, concat_0(t_4, l2_0))
    }
  }

  def contains_0(l_8: List_0, elem_0: Int): Boolean = {
    l_8 match {
      case Nil_0() =>
        false
      case Cons_0(h_6, t_5) =>
        ((h_6 == elem_0) || contains_0(t_5, elem_0))
    }
  }

  abstract class LPair_0

  case class LP_0(v: List_0, v: List_0) extends LPair_0

  def merge_0(l1_1: List_0, l2_1: List_0): List_0 = {
    l1_1 match {
      case Nil_0() =>
        l2_1
      case Cons_0(h1_0, t1_0) =>
        l2_1 match {
          case Nil_0() =>
            l1_1
          case Cons_0(h2_0, t2_0) =>
            (if((h1_0 <= h2_0)) {
              Cons_0(h1_0, merge_0(t1_0, l2_1))
            } else {
              Cons_0(h2_0, merge_0(l1_1, t2_0))
            })
        }
    }
  }

  def split_0(l_9: List_0): LPair_0 = {
    l_9 match {
      case Cons_0(h1_1, Cons_0(h2_1, t_6)) =>
        (
          val rec_1: LPair_0 =
            split_0(t_6);
          rec_1 match {
            case LP_0(rec1_0, rec2_0) =>
              LP_0(Cons_0(h1_1, rec1_0), Cons_0(h2_1, rec2_0))
          }
        )
      case _ =>
        LP_0(l_9, Nil_0())
    }
  }

  def mergeSort_0(l_10: List_0): List_0 = {
    l_10 match {
      case Nil_0() =>
        l_10
      case Cons_0(h_7, Nil_0()) =>
        l_10
      case l_11 =>
        split_0(l_11) match {
          case LP_0(l1_2, l2_2) =>
            merge_0(mergeSort_0(l1_2), mergeSort_0(l2_2))
        }
    }
  }

  def toString_0(l_12: List_0): String = {
    l_12 match {
      case Nil_0() =>
        "List()"
      case more_0 =>
        (("List(" ++ toString1_0(more_0)) ++ ")")
    }
  }

  def toString1_0(l_13: List_0): String = {
    l_13 match {
      case Cons_0(h_8, Nil_0()) =>
        intToString_0(h_8)
      case Cons_0(h_9, t_7) =>
        ((intToString_0(h_9) ++ ", ") ++ toString1_0(t_7))
    }
  }

  def take_0(l_14: List_0, n_0: Int): List_0 = {
    (if((n_0 <= 0)) {
      Nil_0()
    } else {
      l_14 match {
        case Nil_0() =>
          Nil_0()
        case Cons_0(h_10, t_8) =>
          Cons_0(h_10, take_0(t_8, (n_0 - 1)))
      }
    })
  }
}

