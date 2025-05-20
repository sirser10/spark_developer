package scala.scala2

import scala.collection.immutable.List

object  intro extends App {
  def apply_operation(a: Int, b: Int, operation: (Int,Int) => Int) : Int ={
    operation(a,b)
  }

  val add = (x:Int, y: Int) => {x+ y}

  println(add(2,4))

  val result = apply_operation(3,4,add)

  println(result)


  val numbers = (1 to 10).toList
  val  newnumber: List[Int] = numbers.map(_*2) // "_" - означает ВСЁ (как * в sql)

  def apply_twice(foo: Int => Int, param: Int): Int = {
    foo(foo(param))
  }

  val int_f = (param: Int) => param + 1
  println(int_f(4))
  println(apply_twice(int_f, 90))

}

trait Logger {
  def log(msg: String): Unit = ???
}

class ConsolLogger extends Logger{
  override def log(msg: String): Unit = ???

}


//own examples

trait HRBU {
  def print_bu_nam(): Unit = println("This is Business units")
}

trait OrgStruct {
  def print_org_struct(): Unit = println("This is org structure")
}

class HrDep(val dep_name: String) {
  def call_business_struct(): Unit ={

    println("This is generic business struct")
  }

}
class VkVideo(name: String) extends HrDep(name) with OrgStruct {
  override def call_business_struct(): Unit = println(s"This is new bu struct: $name")
}
class VkSocialNetwork(name: String) extends HrDep(name) with HRBU {
  override def print_bu_nam(): Unit = println(s"This is new by_name: $name")

}


object traits extends App {
  val vk_v = new VkVideo("VK Video");
  val sos_n = new VkSocialNetwork("Vkontakte");
  vk_v.call_business_struct();
  sos_n.print_bu_nam()
}

