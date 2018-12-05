object Patterns {
  x match {
  	case 0 => 0
  	case List(0, 0) => 0
  	case false => true
  	case () => y match {
  		case 0 => 0
  	}
  	case List(List(List(0, _))) => y
  	case "hello world" => ""
  	case _ => ()
  	case List.List(0) => error("")
  	case f => f
  	case Test => ()
  	case Test() => ()
  	case List(()) => ()
  	case List(List("asdf", _, Test(()), List(true, 0, Nil, Nil(), List(Nil)))) => 
  		if (true) {error("false")}
  		else {val x:Int = true; x + 1 / 3}
  }
}
