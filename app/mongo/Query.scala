package mongo

import models.{Marker, Protest}
import mongo.Point.pointWrites
import org.joda.time.DateTime
import play.api.libs.json.Json.obj
import reactivemongo.api.ReadPreference.primary
import reactivemongo.bson.BSONString
import reactivemongo.core.commands.Group

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by toidiu on 1/15/17.
  */
object Query {

  import java.text.SimpleDateFormat

  import mongo.{MongoObj, Point}
  import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
  import org.joda.time.{DateTime, DateTimeZone}
  import play.api.data.Forms._
  import play.api.data._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import play.api.libs.json.Json._
  import play.api.libs.json.{JsObject, Json}
  import play.modules.reactivemongo.json._
  import reactivemongo.bson.BSONObjectID

  import scala.concurrent.Future
  import scala.language.postfixOps


  def queryGetProtests(lat: Double, lng: Double, radius: Int, ts: Long): Future[Vector[Protest]] = {

    //Location---------------
    val qLoc = obj("loc" ->
      obj("$near" ->
        obj(
          "$geometry" -> pointWrites.writes(new Point(lng, lat))
          , "$maxDistance" -> radius
        )
      )
    )

    //Date---------------
    val maxD = new DateTime(ts)
    val minD = maxD.minusSeconds(60)
    val qDate = obj("date" -> Map("$gt" -> minD, "$lt" -> maxD))

    val query = qDate.deepMerge(qLoc)
    for {
      m <- MongoObj.protests
//    d <- m.aggregate(Group(BSONString("$state"))(
//      "maxPop" -> MaxField("population")
//    )).map(_.firstBatch))
      list <- m.find(query).cursor[Protest](primary).collect[Vector]()
    } yield list
  }

  def queryGetMarkers(lat: Double, lng: Double, radius: Int, ts: Long): Future[Vector[Marker]] = {

    //Location---------------
    val qLoc = obj("loc" ->
      obj("$near" ->
        obj(
          "$geometry" -> pointWrites.writes(new Point(lng, lat))
          , "$maxDistance" -> radius
        )
      )
    )

    for {
      m <- MongoObj.marker
      list <- m.find(qLoc).cursor[Marker](primary).collect[Vector]()
    } yield list
  }

}
