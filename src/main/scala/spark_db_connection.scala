import org.apache.spark.sql.{DataFrame, SparkSession}
import java.util.Properties
import java.io.FileInputStream

case class PostgresConfig(host: String, port: Int, dbName: String, user: String, password: String)

object spark_db_connection {
  def main(argss: Array[String]): Unit = {

    val props = new Properties()
    props.load(new FileInputStream("src/main/scala/configs/local_postgres.properties"))

    val pgConfig = PostgresConfig(
      host = props.getProperty("host"),
      port = props.getProperty("port").toInt,
      dbName = props.getProperty("dbName"),
      user = sys.env.getOrElse("LOCAL_POSTGRES_LOGIN", "defaultlogin"),
      password = sys.env.getOrElse("LOCAL_POSTGRES_PASSWORD", "defaultpass")
    )


    val spark: SparkSession = SparkSession.builder().appName("db_connection").master("local[*]").getOrCreate()
    val jdbcUrl: String = s"jdbc:postgresql://${pgConfig.host}:${pgConfig.port}/${pgConfig.dbName}"

    val dbProperties = new Properties()
    dbProperties.setProperty("user", pgConfig.user)
    dbProperties.setProperty("password", pgConfig.password)
    dbProperties.setProperty("driver", "org.postgresql.Driver")

    val schema_name: String = "src"
    val table_name: String = schema_name + "." + "test_v2"

    val df: DataFrame = spark.read.jdbc(jdbcUrl, table_name, dbProperties)

    df.show()
  }
}
