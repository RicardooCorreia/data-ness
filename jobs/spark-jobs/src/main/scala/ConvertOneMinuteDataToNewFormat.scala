import io.FileUtils
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.regexp_replace
import spark.SparkSessionProvider

object ConvertOneMinuteDataToNewFormat extends App {

  val spark = SparkSessionProvider.getSparkSession("Convert data set application")

  private val dataFrame: DataFrame = FileUtils.readDataFrame(spark, "1minute_data_newyork.csv")

  import spark.implicits._

  dataFrame
    .selectExpr("dataid", "localminute", "stack(77, 'air1',air1,'air2',air2,'air3',air3,'airwindowunit1',airwindowunit1,'aquarium1',aquarium1,'bathroom1',bathroom1,'bathroom2',bathroom2,'bedroom1',bedroom1,'bedroom2',bedroom2,'bedroom3',bedroom3,'bedroom4',bedroom4,'bedroom5',bedroom5,'battery1',battery1,'car1',car1,'car2',car2,'circpump1',circpump1,'clotheswasher1',clotheswasher1,'clotheswasher_dryg1',clotheswasher_dryg1,'diningroom1',diningroom1,'diningroom2',diningroom2,'dishwasher1',dishwasher1,'disposal1',disposal1,'drye1',drye1,'dryg1',dryg1,'freezer1',freezer1,'furnace1',furnace1,'furnace2',furnace2,'garage1',garage1,'garage2',garage2,'grid',grid,'heater1',heater1,'heater2',heater2,'heater3',heater3,'housefan1',housefan1,'icemaker1',icemaker1,'jacuzzi1',jacuzzi1,'kitchen1',kitchen1,'kitchen2',kitchen2,'kitchenapp1',kitchenapp1,'kitchenapp2',kitchenapp2,'lights_plugs1',lights_plugs1,'lights_plugs2',lights_plugs2,'lights_plugs3',lights_plugs3,'lights_plugs4',lights_plugs4,'lights_plugs5',lights_plugs5,'lights_plugs6',lights_plugs6,'livingroom1',livingroom1,'livingroom2',livingroom2,'microwave1',microwave1,'office1',office1,'outsidelights_plugs1',outsidelights_plugs1,'outsidelights_plugs2',outsidelights_plugs2,'oven1',oven1,'oven2',oven2,'pool1',pool1,'pool2',pool2,'poollight1',poollight1,'poolpump1',poolpump1,'pump1',pump1,'range1',range1,'refrigerator1',refrigerator1,'refrigerator2',refrigerator2,'security1',security1,'sewerpump1',sewerpump1,'shed1',shed1,'solar',solar,'solar2',solar2,'sprinkler1',sprinkler1,'sumppump1',sumppump1,'utilityroom1',utilityroom1,'venthood1',venthood1,'waterheater1',waterheater1,'waterheater2',waterheater2,'wellpump1',wellpump1,'winecooler1',winecooler1,'leg1v',leg1v,'leg2v',leg2v)")
    .withColumnRenamed("col0", "sensor name")
    .withColumnRenamed("col1", "reading value")
    .withColumnRenamed("dataid", "house id")
    .filter($"reading value".isNotNull)
    .withColumn("type", regexp_replace($"sensor name", "\\d+", ""))
    .withColumn("type", regexp_replace($"type", "_", " "))
    .show()
}
