package models

import play.api.data.Form
import play.api.data.Forms.{ignored, mapping}
import reactivemongo.bson.BSONObjectID
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

case class Protest(name: String, description: String,
                   numOfParticipator: Long = 0, _id: Option[BSONObjectID] = None)

object Protest {

  import play.api.libs.json._
  import play.modules.reactivemongo.json._


  implicit lazy val protestJson = Json.format[Protest]
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "numOfParticipator" -> longNumber,
      "_id" -> ignored[Option[BSONObjectID]](None)
    )(Protest.apply)(Protest.unapply)
  )
}