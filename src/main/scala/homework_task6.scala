import scala.io

object DataInput extends App{

  val name: String = io.StdIn.readLine("Your name is: ")
  print("Your age: ")
  val age: Int = io.StdIn.readInt()
  print("Are you a student? ")
  val student_status: Boolean = io.StdIn.readBoolean()

  def age_definer(age: Int): String = {
    if (age < 30) "Young"
    else "Old"
  }

  println(s"Hello, $name! I see you are $age y.o! You are $student_status student." +
    s"So you are ${age_definer(age)}!")
}
