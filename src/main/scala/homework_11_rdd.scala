import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}

object homework_11_rdd {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("hw_11_rdd").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config = conf)

    val rdd = sc.textFile("HW/tripdata.csv")
    val ds: DataFrame = rdd.to








  }
}