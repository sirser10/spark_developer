package final_spark_project

import org.apache.spark.sql.DataFrame

trait StageProcessor {
  def format_column_name(df: DataFrame): DataFrame = {
    val newColumns = df.columns.map(name => {
      name
        .replaceAll("\\*","")
        .replaceAll(" ", "_")
        .replaceAll("([a-z])([A-Z])", "$1_$2")
        .toLowerCase
    })

    df.toDF(newColumns: _*)
  }
}