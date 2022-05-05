package io

import org.apache.spark.sql.{DataFrame, SparkSession}

object FileUtils {

  val relativeDataFolder = "spark-jobs/src/main/resources"

  val resultsFolder = "results"

  val filePrefix = "file://"

  def readDataFrame(spark: SparkSession, filePath: String): DataFrame = {

    val fullPath = s"$filePrefix/$getProjectRoot/$relativeDataFolder/$filePath"
    val dataFrame = spark.read
      .option("header", "true")
      .csv(fullPath)
    dataFrame
  }

  private def getProjectRoot = {
    System.getProperty("user.dir")
  }
}
