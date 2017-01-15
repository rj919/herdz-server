package models

import mongo.Point
import reactivemongo.bson.BSONObjectID

/**
  * Created by toidiu on 1/14/17.
  */

case class LocVec(uId: BSONObjectID
                  , pId: BSONObjectID
                  , loc: Point
                  , ts: Long
                  , deg: Int
                  , speed: Double
                  , _id: Option[BSONObjectID] = None)

object LocVec {

  import play.api.libs.json._
  import play.modules.reactivemongo.json._

  implicit lazy val vectorJson = Json.format[LocVec]

}