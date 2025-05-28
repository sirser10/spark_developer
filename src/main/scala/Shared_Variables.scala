import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame

object Accumulators {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("Accumulators").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config = conf)
    val spark: SparkSession = SparkSession.builder()
      .config(sc.getConf)
      .getOrCreate()
    import spark.implicits._

    val data: Seq[(String, Int)]  =Seq(("Sagar",20), ("Alex",31), ("David", 45))
    val df: DataFrame = data.toDF("name", "age")
    df.show()

    val counter_age = sc.longAccumulator("SumAccumulator")
  }

  }