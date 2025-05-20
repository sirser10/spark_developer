import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object homework_12_dataframe {

  def main(args: Array[String]): Unit = {

    def readAnyFile(
                     spark: SparkSession,
                     file_path: String,
                     config: Map[String, String]
                   ): DataFrame = {

      val format: String = config.getOrElse("format", "parquet")
      val options: Map[String, String] = config - "format"

      spark.read
        .format(format)
        .options(options)
        .load(file_path)

    }

    def removeNullsFromStructFields(df: DataFrame, structFieldName: String): DataFrame = {
      val structFields = {
        df
          .schema(structFieldName)
          .dataType.asInstanceOf[org.apache.spark.sql.types.StructType].fields
      }
      val columnArray = structFields.map(field => col(s"$structFieldName.${field.name}"))
      val filteredArray = array(columnArray: _*)
      val newColumn = filter(filteredArray, x => x.isNotNull)

      return df.withColumn(s"${structFieldName}_filtered", newColumn)

    }

    val spark = SparkSession.builder()
      .appName("homework_11_dataframe_v2")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val config = Map(
      "format" -> "json",
      "header" -> "true",
      "multiline" -> "true",
      "inferSchema" -> "true",
    )
    val df: DataFrame = readAnyFile(
      spark = spark,
      file_path = "HW/countries.json",
      config = config
    )
    /*
    More than 5 borders
    */
      val count_in_seq = udf((arr: Seq[String]) => arr.count(_ => true))
      val const: Int = 5
      val df_five_more_borders: DataFrame = {
                df
                  .withColumn("country_name",col("name.common"))
                  .withColumn("borders_comma", concat_ws(",",col("borders")))
                  .withColumn("count_borders", count_in_seq(col("borders")))
                  .filter(col("count_borders") >= const)
                  .select(
                          "country_name",
                          "borders_comma",
                          "count_borders"
                  )
      }
      if (
            df_five_more_borders
              .filter(col("count_borders") < 5)
              .limit(1)
              .count() > 0) {
          throw new Exception(s"The dataframe contains countries with < $const border counties")
      }
        df_five_more_borders.write
          .mode("overwrite")
          .partitionBy("count_borders")
          .parquet("HW/five_more_borders")


  /*
  languages count
  */
      val cleanedDF: DataFrame = removeNullsFromStructFields(df, "languages")
      val explode_df: DataFrame = {
        cleanedDF
          .withColumn("country_name", col("name.common"))
          .select("country_name", "languages_filtered")
          .withColumn("exploded_langs", explode(col("languages_filtered")))
          .cache()
      }
    val df_languages_speak_count: DataFrame = {
      explode_df.
        groupBy("exploded_langs")
        .agg(
            collect_list("country_name").as("countries"),
            count("country_name").as("countries_speak")
        )
        .orderBy(desc("countries_speak"))
    }

    df_languages_speak_count.write
      .mode("overwrite")
      .partitionBy("countries_speak")
      .parquet("HW/languages_speak_count")


    spark.stop()


  }
}
