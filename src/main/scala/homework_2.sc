// 1) Создайте переменные следующих типов:
val age: Int = 20
val weight: Double = 93.1
val name: String = "Mike"
val is_student: Boolean = false
//2) Выведите значения всех переменных на экран с помощью функции println.
println(s"age is $age, weight is $weight, name is $name, is_student is $is_student")

//3) Напишите функцию, которая принимает два целых числа и возвращает их сумму.
def twoSum(a: Int, b: Int): Int = a + b
print(twoSum(1,9))

//4) Напишите функцию, которая принимает возраст и возвращает строку "Молодой", если возраст меньше 30, и "Взрослый", если возраст 30 или больше.

def age_definer(age: Int) : String = {
  if (age < 30) "Young"
  else "Old"
}
println(age_definer(age))

//5) Напишите цикл, который выводит на экран числа от 1 до 10.
//5.1. Создайте список имен студентов и выведите каждое имя на экран с помощью цикла.
val num_lst: List[Int] = (1 to 15).toList
//for (n <- num_lst) println(n)

val name_lst: List[String] = ("Mike","Angela","Brad","Lionel").toList
for (name <- name_lst) println(name)


//7) Изучить и научиться использовать for comprehension в языке программирования Scala для работы с коллекциями и опциями.
//
//Создайте список чисел от 1 до 10.
//Используйте for comprehension, чтобы создать новый список, содержащий квадраты чисел из исходного списка.
//Используйте for comprehension, чтобы создать новый список, содержащий только четные числа из исходного списка.
//а)
for (n <- 1 to 10) {
  val sqrt = n*n
  println(sqrt)
}
//b)
val sqrs: List[Int] = (1 to 10).toList.map(n => n*n)
println(sqrs)

var even: List[Int] = List()
for (n <-sqrs) {
  if (n % 2 == 0){
     even = even :+ n
}
}
println(even)















