import ds_case_classes.{TaxiZoneLookup, TripData}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.plans.JoinType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset,  SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object homework_11_rdd {

  def main(args: Array[String]): Unit = {

    def read_and_delimit_rdd(file_path: String, sc: SparkContext): RDD[Array[String]] = {
      val rdd = sc.textFile(file_path)
      val header: String = rdd.first()
      val delimited_rdd: RDD[Array[String]] = rdd
        .filter(line => line != header)
        .map(line => line.split(','))

      delimited_rdd
    }

    val conf: SparkConf = new SparkConf().setAppName("hw_11_rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config = conf)
    val spark: SparkSession = SparkSession.builder()
      .config(sc.getConf)
      .getOrCreate()

    import spark.implicits._

    val rdd_tripdata: RDD[Array[String]] = read_and_delimit_rdd("HW/tripdata.csv", sc)
    val rowRDD_tripdata: RDD[TripData] = rdd_tripdata.flatMap(attr => {
      if (attr.length > 18) {
        Some(TripData(
          attr(0).toInt,
          attr(1),
          attr(2),
          attr(3).toLong,
          attr(4).toFloat,
          attr(5).toInt,
          attr(6),
          attr(7).toInt,
          attr(8).toInt,
          attr(9).toInt,
          attr(10).toFloat,
          attr(11).toFloat,
          attr(12).toFloat,
          attr(13).toFloat,
          attr(14).toFloat,
          attr(15).toFloat,
          attr(16).toFloat,
          attr(17).toFloat,
          attr(18).toFloat
        ))
      } else {None}
  }
  )
    val ds_tripdata: Dataset[TripData] = rowRDD_tripdata.toDS()

    val rdd_taxi_zone: RDD[Array[String]] = read_and_delimit_rdd("HW/taxi_zone_lookup.csv", sc)
    val rowRDD_taxi_zone: RDD[TaxiZoneLookup] = rdd_taxi_zone.map(attr => TaxiZoneLookup(
                                                                                          attr(0).toInt,
                                                                                          attr(1),
                                                                                          attr(2),
                                                                                          attr(3)
    ))
    val ds_taxi_zone: Dataset[TaxiZoneLookup] = rowRDD_taxi_zone.toDS()

    val df_result: DataFrame = ds_tripdata
      .join(
        ds_taxi_zone,
        ds_tripdata("PULocationID") === ds_taxi_zone("LocationID"),
        "left"
      )
      .withColumn("date",to_date(to_timestamp(col("tpep_pickup_datetime")), "yyyy-MM-dd"))
      .withColumn("hour", hour(to_timestamp(col("tpep_pickup_datetime"))))
      .groupBy("Zone", "hour")
      .agg(count("tpep_pickup_datetime").as("orders_count"))
      .orderBy(desc("orders_count"))

    df_result.write.mode("overwrite").partitionBy("Zone").parquet("HW/hourly_orders_count")

    df_result.show()


    spark.stop()













  }
}