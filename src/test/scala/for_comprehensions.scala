
object for_comprehensions {
  def main(args: Array[String]): Unit ={

    case class Car(marka: String, model: String, year: Int)
    val cars = Car("VW", "Passat", 2005) :: Car ("Lexus", "UX", 2019) :: Car("BMW", "14",2025) :: Nil
    case class Garage(name: String, index: Int)
    val garages: List[Garage] = Garage("BMW", 1) :: Garage("Lexus", 2) :: Nil

    garages.flatMap {
      garage => cars.filter(car => car.marka == garage.name).map(car => (car.marka, car.model, garage.index))
    }.foreach{case (m,g,i) => println(s"$m $g $i")}

    var res = for {
      car <- cars
      garage <- garages
      if car.marka == garage.name
    } yield {
      (car.marka, car.model, garage.index)
    }

    res.foreach({
      case (marka, model, index) => println(s"$marka, $model, $index")
    })
  }
}