package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
  * Created by toidiu on 1/14/17.
  */

case class Protest(name: String, description: String, _id: Option[BSONObjectID] = None)

object Protest {
  import play.modules.reactivemongo.json._
  import reactivemongo.bson._
  import play.api.libs.json._


  implicit lazy val protestJson = Json.format[Protest]

}