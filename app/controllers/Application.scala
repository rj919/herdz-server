package controllers

import models.{Marker, Protest}
import mongo.{MongoObj, Point, Query}
import play.api.mvc._
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import play.modules.reactivemongo.json._
import reactivemongo.api.ReadPreference.primary
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application extends Controller {

  import play.api.libs.json._
  import play.api.mvc._
  import reactivemongo.api.ReadPreference
  import reactivemongo.play.json._

  def protests(lat: Double, lng: Double, radius: Int, ts: Long) = Action.async {
    for {
      p <- MongoObj.protests
      list <- p.find(Json.obj()).cursor[Protest](primary).collect[List]()
    } yield Ok(Json.toJson(list))


    //protests [
    //    {
    //        pid
    //        name
    //        num_of_participant
    //        vector: [
    //             {
    //                uid
    //                pid
    //                lat
    //                lnd
    //                degree
    //                speed
    //            }
    //        ]
    //    }
    //]

    //    val d = MongoObj.protests.flatMap(
    //      _.find(Json.obj())
    //        .cursor[Protest](ReadPreference.primary)
    //        .collect[List]()
    //    )
    //    d.map(s => Ok(Json.toJson(s)))
  }


  def join = Action.async {
    Future(Ok(BSONObjectID.generate().stringify))
  }

  def movement(pid: Long, uid: Long, lat: Double, lng: Double, deg: Double, speed: Double, ts: Long) = Action.async {
    Future(Ok(""))
  }

  def getMarkers(lat: Double, lng: Double, radius: Int, ts: Long) = Action.async {
    for {
//      m <- MongoObj.marker
//      list <- m.find(Json.obj()).cursor[Marker](primary).collect[List]()
      list <- Query.queryGetMarkers(lat, lng, radius, ts)
    } yield Ok(Json.toJson(list))
  }

  def addMarker = Action.async { req =>
    Marker.form.bindFromRequest()(req).fold(
      formWithErrors => Future(BadRequest(formWithErrors.toString)),
      marker => {
        val pt: Point = {
            val formUrl = req.body.asFormUrlEncoded.get
            val lat = formUrl.get("lat").get.head.toDouble
            val lng = formUrl.get("lng").get.head.toDouble
            new Point(lng, lat)
        }

        for {
          m <- MongoObj.marker
          a <- m.insert(marker.copy(loc = pt))
        } yield Ok(a.ok.toString)
      }
    )
  }

  //  def addProtest() = Action.async {
  //    val d = MongoObj.protests.flatMap(
  //      _.insert(Protest("Black Lives Matter", "description"))
  //    )
  //    d.map(e => Ok(e.toString))
  //  }
  //
  //  def herd() = Action.async {
  //    val d = MongoObj.locVec.flatMap(
  //      _.find(Json.obj())
  //        .cursor[LocVec](ReadPreference.primary)
  //        .collect[List]()
  //    )
  //    d.map(e => Ok(e.toString))
  //  }
}
