package models

import mongo.Point
import play.api.data.Form
import reactivemongo.bson.BSONObjectID
import play.api.data._
import play.api.data.Forms._
import play.api.data.Forms._
import play.api.data._
  import mongo.Point._

/**
  * Created by toidiu on 1/14/17.
  */

case class Marker(mId: Int
                  , loc: Point
                  , _id: Option[BSONObjectID] = None)

object Marker {

  import play.api.libs.json._
  import play.modules.reactivemongo.json._

  implicit lazy val markerJson = Json.format[Marker]

  val form = Form(
    mapping(
      "mId" -> number,
      "loc" -> pointMapping,
      "_id" -> ignored[Option[BSONObjectID]](None)
    )(Marker.apply)(Marker.unapply)
  )

}