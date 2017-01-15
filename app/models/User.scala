package models

import play.modules.reactivemongo.json._
import reactivemongo.bson.BSONObjectID

/**
  * Created by toidiu on 1/14/17.
  */
case class User(name: String
                , permission: Int = 0
                , _id: Option[BSONObjectID] = None)

object User {

  import play.api.libs.json._
  import play.modules.reactivemongo.json._

  implicit lazy val userJson = Json.format[User]

}