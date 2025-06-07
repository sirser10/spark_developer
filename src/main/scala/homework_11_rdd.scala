import ds_case_classes.{TaxiZoneLookup, TripData}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SparkSession}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter._

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
    val rdd_taxi_zone: RDD[Array[String]] = read_and_delimit_rdd("HW/taxi_zone_lookup.csv", sc)
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
    val rowRDD_taxi_zone: RDD[TaxiZoneLookup] = rdd_taxi_zone
      .map(
        attr => TaxiZoneLookup(
          attr(0).toInt,
          attr(1),
          attr(2),
          attr(3)
        )
      )

    val pairRdd_tripdata: RDD[(Long, TripData)] = rowRDD_tripdata.map(trip => (trip.PULocationID,trip))
    val pairRdd_taxi_zone: RDD[(Long,TaxiZoneLookup)] =  rowRDD_taxi_zone.map(zone => (zone.LocationID,zone))
    val joined_RDD: RDD[(Long, (TripData, TaxiZoneLookup))] = pairRdd_tripdata.join(pairRdd_taxi_zone)


    val processed_RDD = joined_RDD.map{
      case (key, (tripData: TripData, taxiZoneLookup: TaxiZoneLookup)) =>
        val pickup_date = LocalDateTime.parse(tripData.tpep_pickup_datetime, ISO_DATE_TIME)
        val hour = pickup_date.getHour
        (taxiZoneLookup.Zone, hour, tripData)
    }


    val result_RDD = processed_RDD
      .map{
        case (zone, hour, tripData: TripData) => ((zone, hour),1)
      }
      .reduceByKey(_+_)
      .map{
        case ((zone, hour), count) => (zone, hour, count)
      }
      .sortBy{
        case (zone,hour, count) => (-count, zone, hour)
      }

    val total_RDD: RDD[String] = result_RDD.map{
      case (zone, hour, count) => s"$zone, $hour, $count"
    }

    val output_RDD = sc.parallelize(Seq("zone, hour, orders_count")) ++ total_RDD
  //    output_RDD.take(10).foreach(println)
    output_RDD.saveAsTextFile("HW/hourly_orders_count.csv")

    spark.stop()













  }
}