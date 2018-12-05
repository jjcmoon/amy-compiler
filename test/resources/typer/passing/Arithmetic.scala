object Arithmetic {
  def hello(m:Int):String = {
    "hello"++" world!"
  }
  val r:Int = if (false && (true || error(hello(0)))) {
    0
  } else {
    1
  };
  val s:Int = r + 0;
  0
}
