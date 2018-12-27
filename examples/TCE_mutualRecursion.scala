object MutualRecursion {

  def odd(n:Int):Boolean = {
    if (n==0) {
      false
    } else {
      even(n-1)
    }
  }

  def even(n:Int):Boolean = {
    if (n==0) {
      true
    } else {
      odd(n-1)
    }
  }

  Std.printBoolean(odd(100000001))
  
}
