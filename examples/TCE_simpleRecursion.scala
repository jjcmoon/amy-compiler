object SimpleRecursion {

  def fib(n:Int, acc1:Int, acc2:Int):Int = {
    if (n==0) {
      acc2
    } else {
      fib(n-1, acc2, acc1+acc2)
    }
  }



  Std.printInt(fib(100))
  
}
