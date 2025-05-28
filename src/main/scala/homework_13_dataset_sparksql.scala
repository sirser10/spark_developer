import org.apache.spark.sql.{DataFrame, Dataset}
import common.SparkBaseProcessor
import ds_case_classes.{TaxiZoneLookup, YellowTaxiJanuary}

object homework_13_dataset_sparksql {

  def main(args: Array[String]): Unit = {

    val spark_processor: SparkBaseProcessor = new SparkBaseProcessor("HW13_DS_SPARKSQL")
    import spark_processor.spark.implicits._

    val df_taxi_january: DataFrame/*Dataset[YellowTaxiJanuary]*/ = spark_processor
      .read_any_file(file_path=/*"HW/tripdata.csv"*/"HW/yellow_taxi_jan_25_2018"/*,config = Map("header"-> "true", "format" -> "csv", "inferSchema"-> "true")*/)
//      .as[YellowTaxiJanuary]
    val df_dim_taxi_zones: DataFrame/*Dataset[TaxiZoneLookup]*/ = spark_processor
      .read_any_file(
                      file_path="HW/taxi_zones.csv",
                      config=Map(
                                "header" -> "true",
                                "inferSchema" -> "true",
                                "format" -> "csv",

                      )
      )
//      .as[TaxiZoneLookup]

    df_taxi_january.show()
    df_dim_taxi_zones.show()

    val dd = df_taxi_january.select("PULocationID").orderBy("PULocationID")
//    println(dd.explain())
    dd.show(20)

//    val joinded = df_dim_taxi_zones.join(
//      right=df_taxi_january,
//      df_taxi_january("PULocationID") === df_dim_taxi_zones("LocationID"),
//      "left_outer"
//    )
//    joinded.show()




  }

}
