import scala.annotation.tailrec

object test extends App {
  println("Hello world!")
}
test;

1+1


val funcMax = (f: Int => Int) => (a: Int, b: Int) =>
  if (f(a) > f(b)) a else b

val res = (funcMax(x => x)(5, 10))
val res2 = (funcMax(Math.abs)(5, -11))
val res3 = (funcMax(x=> x*x)(5,-11))


val _f: Int = 2
val f_f = 2.34 //type = Double
val f_f = 2.34f //type = Float

Int.MaxValue

val whichOne: String = if (false) "Not that one" else "This one"

val fruit: List[String] = List("apples", "oranges")
val fruit2 = "apples" :: ("oranges" :: ("pears" :: Nil))

val _t = ("oragenfe", "pears") // tuple

val fruit_3 = "banana" :: "apple" :: Nil

val fruit_4 = List.fill(10)("apples")//.

//ranges

val range_ = 1 to 10
val range_str = range_.mkString(" ")
val range_l = (11 to 20).toList

val rang_list_o_element =List(20 to 30)

val rnage_alph = 'a' to 'z'
val full_rnage_alph = rnage_alph.toList.take(3)
val full_rnage_alph = rnage_alph.toList.take(5).mkString("Simple as ", "", "!")


val colors = Map("red" -> "#FF000", "azure" -> "#F0FFFF")
val dict_ = colors.keys
val dict_2  = colors.values
val is_colors_emp = colors.isEmpty

println("Check if colors is empty: " + is_colors_emp)


//Options
val a: Option[Int] = Some(3) //Some(3) - это монада
val b: Option[Int] = None

val unpack_ = a.getOrElse(0)
val unpack_2 = b.getOrElse(10)

//for comprehension

val a = 1 to 10
val b = 1 to 10

val ab = for {
  a1 <- a
  b1 <- b
} yield(a1,b1)

val ab1 = a.flatMap( //flatMap unpacking the collection
  a1 => b.map(b1 => (a1, b1)) //List(a1, b1))
)

val ab1 = a.map(
  a1 => b.map(b1 => (a1, b1))
)


val ba1 = b.flatMap(
  (b1: Int) => a.map(a1 => (b1, a1))
)


// Recursion

def factorial(n: Int): BigInt ={
  if (n > 1) n * factorial(n-1)
  else 1
}

//factorial(5)

//@tailrec //хвостовая рекурсия
def tailFactorial(n: Int, acc: BigInt=1): BigInt = {
  if (n>1) tailFactorial(n - 1, acc * n)
  else acc
}

//tailFactorial(6, 1)

//Exceptions

def check(hasException: Boolean): String ={
  if (hasException) throw new RuntimeException("Позор!")
  else "OK"
}

try{
  check(true)
} catch{
  case e: RuntimeException => println(s"RuntimeException[$e]") //s"RuntimeException[$e]" аналог f"{var}" в python
}finally{
  println("Exception handling is over")
}

val ex = 5
println(s"This is number [$ex]") // This is number [5]
println(raw"This is number [${ex-1}]")


//Tuples
val t = (1,"word", Console, 4)

t._1 //  Аналог python a[0]
val sum = t._1 + t._4


//OOP
//Трейт - аналог интерфейса

class Point(xc: Int, yc: Int) {
  var x: Int = xc; var y: Int = yc

  def move(dx: Int, dy: Int) = {
    x = x + dx
    y = y + dy
    println("Point x location: " + x)
    println("Point y location: " + y)
  }
}

val pt = new Point(xc=10, yc=20) //use the new keyword when you want to refer to a class's own constructor

pt.move(10,15)

trait Greetings {
  def greet(): Unit
}

trait Hello extends Greetings {
  override def greet(): Unit = print("Hello")
}

trait GoodMorning extends Greetings:
  override def greet(): Unit = print(s"Good morning, sir")


class Hi_ extends GoodMorning:
  greet()

val f = new Hi_
f.greet()

class Person extends GoodMorning with Hello

val person = new Person
person.greet()
/*Вернулся hello, потому что сперва вызывается Goodmorning,
 а потом другой метод, который перезаписывает метод
 */

trait Warning {
  def warn_phrase(name: String): Unit ={
    println(s"Shut Up, $name!")
  }
}

//class Names(name: String) extends Warning
class Names extends Warning

//val name = new Names("Nikita")
val name = new Names
name.warn_phrase("Elsa")

///
class Employee(name: String) {
  def talk(msg: String): Unit = println(s"Employee says: $msg")
}

class Manager(name: String) extends Employee(name){
  override def talk(msg: String): Unit = println(s"Manager [$name] says: $msg")
}

val employee = new Employee("Sam"); val manager = new Manager("Dan")
employee.talk("Hello"); manager.talk("Congrats!")

{
  class Employee(name: String) {
    val id = 0
  }

  class Manager(val name: String, override val id: Int) extends Employee(name)

  val manager = new Manager("Dan", 1)

  manager.id
  manager.name // error if name without val at the class arguments else value
}

//Object
{
  class Student(id: Int, name: String) {
    override def toString: String = s"Student id [$id], name [$name]"
  }
  object Student {
    def create(s: String): Student={
      val params =s.split(",")
      new Student(params(0).toInt,params(1))
    }
  }

  val student = Student.create("1,Bob")
  println(student)

}
// Case class
//case class помогает сразу обращаться к классу, минуя его инициализацию в объект
// case class имеет метод .copy для быстрого копирования неизменяемых объектов

{
  case class Student(var id: Int, name: String)
  val student = Student(1, "Bob") // Student(1,Bob)
//  student.id = 3
  println(student)
  val student2 = Student(1, "Bob") // Student(1,Bob)
  println(student == student2) //true
}
 /*
  //feel the difference btw case class and class
  {
    class Student(var id: Int, name: String)
    val student = new Student(1, "Bob") // rs$line$74$Student@374e1006
    val student2 = new Student(1, "Bob") //rs$line$74$Student@53c62d37
    println(student); println(student2); println(student == student2) //false
  }
  */


// Pattern Matching
//// Match + unapply


val someVal: Int = 43

val description = someVal match {
  case 1 => "action1"
  case 2 => "action2"
  case 3 => "action3"
  case _ => "default action" //_ употребляем, как else (если ничего не совпадает)
}
println(description)

def printValue(x: Any): Unit = x match{
  case i: Int => println("Int: " + i)
  case s: String => println("String: " + s)
  case b: Boolean => println("Boolean: " + b)
  case _ => println("Other")
}

val res = printValue(true)

val pair = (1, 43)
pair match {
  case (i,s) => println(s"$i is $s")
  case _ => println("Other")
}







