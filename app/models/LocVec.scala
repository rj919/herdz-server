package models

import mongo.Point
import play.api.data.Form
import play.api.data.Forms.{ignored, mapping}
import reactivemongo.bson.BSONObjectID
import play.api.data.Forms.{ignored, mapping}
import reactivemongo.bson.BSONObjectID
import mongo.Point
import play.api.data.Form
import reactivemongo.bson.BSONObjectID
import play.api.data._
import play.api.data.Forms._
import play.api.data.Forms._
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraint

/**
  * Created by toidiu on 1/14/17.
  */

case class LocVec(uId: BSONObjectID
                  , pId: BSONObjectID
                  , loc: Point
                  , ts: Long
                  , deg: Double
                  , speed: Double
                  , _id: Option[BSONObjectID] = None)

object LocVec {

  import play.api.libs.json._
  import play.modules.reactivemongo.json._

  implicit lazy val vectorJson = Json.format[LocVec]

  val form = Form(
    mapping(
      "pId" -> new BSONObjMapping("pId"),
      "uId" -> new BSONObjMapping("uId"),
      "loc" -> Point.pointMapping,
      "ts" -> longNumber,
      "deg" -> of[Double],
      "speed" -> of[Double],
      "_id" -> ignored[Option[BSONObjectID]](None)
    )(LocVec.apply)(LocVec.unapply)
  )

}

class BSONObjMapping(id: String) extends Mapping[BSONObjectID] {
  val constraints = Nil
  val mappings = Seq(this)

  override def bind(data: Map[String, String]): Either[Seq[FormError], BSONObjectID] = {
    val optBsid = for {
      sId <- data.get(id)
    } yield {
      BSONObjectID(sId)
    }

    optBsid match {
      case Some(bsid) => Right(bsid)
      case None => Left(Seq(FormError(key, "unable to create bson object")))
    }

  }

  override def verifying(constraints: Constraint[BSONObjectID]*): Mapping[BSONObjectID] = {
    WrappedMapping[BSONObjectID, BSONObjectID](this, x => x, x => x, constraints)
  }

  override def unbind(bsid: BSONObjectID): Map[String, String] = {
    Map("id" -> bsid.stringify)
  }

  override def withPrefix(prefix: String): Mapping[BSONObjectID] = new BSONObjMapping(id)

  override def unbindAndValidate(bsid: BSONObjectID): (Map[String, String], Seq[FormError]) = {
    (Map("id" -> bsid.stringify), Nil)
  }

  override val key: String = id
}

