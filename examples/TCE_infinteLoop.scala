object Loop {

  def loop():Unit = {
    loop()
  }

  // Without TCE this whould overflow
  // the stack almost instantaneously
  loop()
  
}
