import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession, Row}
import TripData._
import org.apache.spark.sql.types._

object homework_11_rdd {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("hw_11_rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config = conf)

    val rdd: RDD[Array[String]] = sc.textFile("HW/tripdata.csv")
      .map(line => line.split(","))
      .filter(_(0) !="VendorID")
    val rowRDD = rdd.map(attr => TripData(
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


    val spark: SparkSession = SparkSession.builder()
      .config(sc.getConf)
      .getOrCreate()

    import spark.implicits._
    val ds: Dataset[TripData] = spark.createDataset(rowRDD)
    ds.show()











  }
}