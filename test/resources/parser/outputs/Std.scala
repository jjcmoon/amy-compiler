object Std {

  def printInt(i: Int): Unit = {
    error("")
  }

  def printString(s: String): Unit = {
    error("")
  }

  def printBoolean(b: Boolean): Unit = {
    printString(booleanToString(b))
  }

  def readString(): String = {
    error("")
  }

  def readInt(): Int = {
    error("")
  }

  def intToString(i: Int): String = {
    (if((i < 0)) {
      ("-" ++ intToString(-(i)))
    } else {
      (
        val rem: Int =
          (i % 10);
        val div: Int =
          (i / 10);
        (if((div == 0)) {
          digitToString(rem)
        } else {
          (intToString(div) ++ digitToString(rem))
        })
      )
    })
  }

  def digitToString(i: Int): String = {
    error("")
  }

  def booleanToString(b: Boolean): String = {
    (if(b) {
      "true"
    } else {
      "false"
    })
  }
}

