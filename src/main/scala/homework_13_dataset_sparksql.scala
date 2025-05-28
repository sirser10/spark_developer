import org.apache.spark.sql.{DataFrame, Dataset}
import common.SparkBaseProcessor
import ds_case_classes.{TaxiZoneLookup, YellowTaxiJanuary}
import org.apache.spark.sql.functions._

object homework_13_dataset_sparksql {

  def main(args: Array[String]): Unit = {

    val spark_processor: SparkBaseProcessor = new SparkBaseProcessor("HW13_DS_SPARKSQL")
    import spark_processor.spark.implicits._

    val df_taxi_january: Dataset[YellowTaxiJanuary] = spark_processor
      .read_any_file(file_path="HW/yellow_taxi_jan_25_2018")
      .as[YellowTaxiJanuary]
    val df_dim_taxi_zones: Dataset[TaxiZoneLookup]= spark_processor
      .read_any_file(
                      file_path="HW/taxi_zones.csv",
                      config=Map(
                                "header" -> "true",
                                "inferSchema" -> "true",
                                "format" -> "csv",
                      )
      )
      .as[TaxiZoneLookup]

    df_taxi_january.show()
    df_dim_taxi_zones.show()


    val joinded = df_dim_taxi_zones
      .join(
            right = df_taxi_january,
            joinExprs = df_taxi_january("PULocationID") === df_dim_taxi_zones("LocationID"),
            joinType = "left_outer"
      )
      .groupBy("Zone")
      .agg(
        count("tpep_pickup_datetime").as("pickup_count"),
        round(min("trip_distance"),2).as("min_distance"),
        round(avg("trip_distance"),2).as("avg_distance"),
        round(max("trip_distance"),2).as("max_distance"),
        round(stddev_pop("trip_distance"),2).as("std_dev")
      )
      .orderBy(desc("pickup_count"))

    joinded.show()

//    spark_processor.write_any_file(
//                                  df = joinded,
//                                  file_path = "HW/yellow_taxi_jan_25_2018_res",
//                                  write_config = Map(
//                                    "write_mode" -> "overwrite",
//                                    "format" -> "parquet",
//                                    "partitionBy" -> Seq("Zone")
//                                  )
//    )
  }

}
