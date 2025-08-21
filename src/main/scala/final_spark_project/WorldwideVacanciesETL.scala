package final_spark_project

import common.SparkBaseProcessor
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window

case class RussianJobs(
                        vacancy: String,
                        url: String,
                        created: String,
                        has_test: Boolean,
                        salary_from: Long,
                        salary_to: Long,
                        currency: String,
                        experience: String,
                        schedule: String,
                        skills: String,
                        employer: String,
                        area: String,
                        description: String
                      )
case class JobPosting(
                       website_domain: String,
                       ticker: String,
                       job_opening_title: String,
                       job_opening_url: String,
                       first_seen_at: java.sql.Timestamp,
                       last_seen_at:  java.sql.Timestamp,
                       location: String,
                       location_data: String,
                       category: String,
                       seniority: String,
                       keywords: String,
                       description: String,
                       salary: String,
                       salary_data: String,
                       contract_types: String,
                       job_status: String,
                       job_language: String,
                       job_last_processed_at:java.sql.Timestamp,
                       onet_code: String,
                       onet_family: String,
                       onet_occupation_name: String
                     )
object WorldwideVacanciesETL extends App with StageProcessor{

    val spark_processor: SparkBaseProcessor = new SparkBaseProcessor("WorldwideVacanciesETL")
    val spark: SparkSession = spark_processor.spark
    import spark.implicits._

    val root: String = "src/main/resources/source_data/"
    val csvFiles = List(
      root + "data2021-07-21.csv",
      root + "df2021-07-21.csv",
      root + "df2021-08-03.csv",
      root + "ds2021-07-20.csv",
      root + "Java2021-08-04.csv",
      root + "Kotlin2021-08-04.csv",
      root + "Python2021-08-04.csv",
      root + "job_posting.csv"
    )
    var combinedDS: Dataset[RussianJobs] = spark.emptyDataset[RussianJobs]
    var ds_job_posting: Dataset[JobPosting] = null

    for (file <- csvFiles) {

      val config_ = if (file.startsWith(root + "job")) {
        Map(
          "header" -> "true",
          "delimiter" -> ",",
          "quote" -> "\"",
          "nullValue" -> "",
          "escape" -> "\"",
          "inferSchema" -> "true",
          "multiline" -> "true",
          "format" -> "csv"
        )
      } else {
        Map(
          "header" -> "true",
          "delimiter" -> ",",
          "quote" -> "\"",
          "escape" -> "\"",
          "inferSchema" -> "true",
          "format" -> "csv"
        )
      }

      val df_raw: DataFrame = (
        spark_processor
          .read_any_file(
            file_path = file,
            config = config_
          )
        )

      val formattedDF: DataFrame = format_column_name(df_raw)

      if (file.startsWith(root + "job")) {
        ds_job_posting = formattedDF.as[JobPosting]
      }
      else {
        val ds_russian_jobs: Dataset[RussianJobs] = formattedDF.as[RussianJobs]
        combinedDS = combinedDS.union(ds_russian_jobs)
      }
    }

  val currentTimestamp = current_timestamp()
  val windowSpecCombined = Window.partitionBy("vacancy", "url").orderBy(col("created").desc)
  val combinedDFDistinct = combinedDS
    .withColumn("rn", row_number().over(windowSpecCombined))
    .filter($"rn" === 1)
    .withColumn("etl_name", lit("worldwide_vacancies_et"))
    .withColumn("updated_dttm", currentTimestamp)

  spark_processor.write_any_file(
    df = combinedDFDistinct,
    file_path = "src/main/resources/results/russian_jobs",
    write_config = Map(
      "write_mode" -> "overwrite",
      "format" -> "parquet"
    )
  )

  val windowSpecJobPosting = Window.partitionBy("job_opening_title", "job_opening_url", "location").orderBy(col("last_seen_at").desc)
  val ds_job_postingDistinct = ds_job_posting
    .withColumn("row_number", row_number().over(windowSpecJobPosting))
    .filter($"row_number" === 1)
    .withColumn("etl_name", lit("worldwide_vacancies_et"))
    .withColumn("updated_dttm", currentTimestamp)

  spark_processor.write_any_file(
    df = ds_job_postingDistinct,
    file_path = "src/main/resources/results/australian_jobs",
    write_config = Map(
      "write_mode" -> "overwrite",
      "format" -> "parquet"
    )
  )


}
