package controllers

import models.{LocVec, Marker, Protest}
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

  def addProtest() = Action.async { req =>
    Protest.form.bindFromRequest()(req).fold(
      formWithErrors => Future(BadRequest(formWithErrors.toString)),
      protest => {
        for {
          m <- MongoObj.protests
          a <- m.insert(protest)
        } yield Ok(a.ok.toString)
      }
    )
  }

  def protests(lat: Double, lng: Double, radius: Int, ts: Long) = Action.async {
    for {
      list <- Query.queryGetProtests(lat, lng, radius, ts)
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
    //                lng
    //                ts
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

    //fixme add index for geo and time

  }


  def join = Action.async {
    //fixme increment the # of participation
    Future(Ok(BSONObjectID.generate().stringify))
  }

//  def movement(pid: String, uid: String, lat: Double, lng: Double, deg: Double, speed: Double, ts: Long) = Action.async {
  def movement = Action.async { req =>
    LocVec.form.bindFromRequest()(req).fold(
      formWithErrors => Future(BadRequest(formWithErrors.toString)),
      locVec => {
        for {
          m <- MongoObj.locVec
          a <- m.insert(locVec)
        } yield Ok(a.ok.toString)
      }
    )
  }

  def getMarkers(lat: Double, lng: Double, radius: Int, ts: Long) = Action.async {
    for {
      list <- Query.queryGetMarkers(lat, lng, radius, ts)
    } yield Ok(Json.toJson(list))
  }

  def addMarker = Action.async { req =>
    Marker.form.bindFromRequest()(req).fold(
      formWithErrors => Future(BadRequest(formWithErrors.toString)),
      marker => {
        for {
          m <- MongoObj.marker
          a <- m.insert(marker)
        } yield Ok(a.ok.toString)
      }
    )
  }

}
