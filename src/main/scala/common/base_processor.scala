package common
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}

class SparkBaseProcessor(val SparkAppName: String) {

  private val AppName: String = SparkAppName

  def spark_session_init(): SparkSession = {

    val spark_job_conf: SparkConf = new SparkConf().setAppName(AppName).setMaster("local[*]")
    val sc: SparkContext = new SparkContext(config=spark_job_conf)
    val spark: SparkSession = SparkSession.builder().config(sc.getConf).getOrCreate()

    spark
  }

   lazy val spark: SparkSession=spark_session_init()
   import spark.implicits._

  def read_any_file(
                   file_path: String,
                   config: Map[String, String]=Map()
                 ): DataFrame = {

    val format: String = config.getOrElse("format", "parquet")
    val options: Map[String, String] = config - "format"
    spark.read
      .format(format)
      .options(options)
      .load(file_path)
  }

  def write_any_file(
                    df: DataFrame,
                    file_path: String,
                    write_config: Map[String, Any]
                    ): Unit = {

    val mode: String = write_config.get("write_mode").map(_.toString).getOrElse("overwrite")
    val format: String = write_config.get("format").map(_.toString).getOrElse("parquet")

    val writer = df.write.mode(mode).format(format)

    write_config.get("partitionBy") match {
      case Some(cols: Seq[_]) if cols.nonEmpty =>
        val partitionCols = cols.collect { case s: String => s }
        if (partitionCols.nonEmpty)
          writer.partitionBy(partitionCols: _*).save(file_path)
        else
          writer.save(file_path)
      case _ =>
        writer.save(file_path)
    }

  }
}
