
/*
1) Напишите программу на Scala,которая принимает имя пользователя с клавиатуры
 и выводит приветственное сообщение.
*/

def greetings(name: String): Unit ={
  val greeting_msg: String = s"Hello $name, nice to meet you!"
  println(greeting_msg)
}
greetings("Serafim")

/*
2) Напишите функцию, которая принимает два целых числа и возвращает их сумму.
*/

def sumtwo(n1: Int, n2: Int): Int ={
  val sum_two = n1 + n2
  sum_two
}

println("The sum of two digits is: " + sumtwo(4,9))

/*
3) Создайте список из нескольких чисел (например, List(1, 2, 3, 4, 5))
и примените к нему функцию, которая увеличивает каждое число на 1.
Выведите получившийся список на экран.
*/

val someList: List[Int] = List.range(1,10)

def plus_one(coll: List[Int]) : List[Int]={
  val res_lst: List[Int] = coll.map(n => n + 1)
  res_lst
}
println(plus_one(someList))

/*
4) Напишите программу, которая принимает число с клавиатуры и выводит, является ли оно четным или нечетным.
*/

def is_countable(v: Int): Boolean ={
  if (v % 2 == 0) true
  else false
}
println(is_countable(8))

/*
5) Создайте программу, которая принимает строку и выводит её длину
 */
def str_length(s: String): Int = {
  val res = s.length
  res
}

println(str_length("Prokhor"))

/*
6) Напишите функцию, которая принимает список строк и возвращает новую строку, состоящую из всех строк списка, разделенных пробелами.
 */
def concat_str(coll: List[String]): String ={
  val res_str = coll.mkString(" ")
  res_str
}
val some_str_ = "cat":: "dog" :: "monkey" :: "elephant" :: Nil

println("String with spaces: " + concat_str(some_str_))




