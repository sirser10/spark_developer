object implicits extends App{
  def greeting(name: String)(implicit greeting: String): Unit = {
    println(s"$greeting, $name")
  }
  implicit val hello: String = "Hi"
  greeting("Dora")

  implicit def into_string(x: Int): String = x.toString

  val str: String = 42
  println(str)

  implicit class SquareInt(val v: Int) {
    def square: Int = v*v
  }
  println(9.square)
}


/////


object ImplicitObjectExample extends App{

  trait Show[A] {
    def show(x: A): String
  }

  implicit object IntShow extends Show[Int] {
    def show(x: Int): String = s"String $x"
  }

  implicit object StringShow extends Show[String]{
    def show(x: String): String = s"String $x"
  }

  def printShow[A](x: A)(implicit s: Show[A]): Unit = { //A символизирует абстрактный параметр, может быть любая буква
    println(s.show(x))
  }
  printShow(42)
  printShow("42")
}

