import org.apache.spark.sql.{DataFrame, SparkSession}

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("first_spark_app")
      .master("local[*]")
      .config("spark.driver.bindAddress","127.0.0.1")
      .getOrCreate()


    val df: DataFrame = spark.read
      .option("header",value = true)
      .option("inferSchema", value=true)
      .csv("HW/tripdata.csv")

    df.show()
    df.printSchema()
  }

}
