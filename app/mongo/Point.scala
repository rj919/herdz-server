package mongo

import play.api.data._
import play.api.data.validation.Constraint
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/**
  * Created by toidiu on 11/9/15.
  */

//http://stackoverflow.com/a/20629821/2369122
case class Point(lng: Double, lat: Double)


class PointMapping(val lng: Double = 0, val lat: Double = 0) extends Mapping[Point] {
  val constraints = Nil
  val mappings = Seq(this)

  override def bind(data: Map[String, String]): Either[Seq[FormError], Point] = {
    val optPoint = for {
      sLat <- data.get("lat")
      sLng <- data.get("lng")
      lat = sLat.toDouble
      lng = sLng.toDouble
    } yield Point(lng, lat)

    optPoint match {
      case Some(pnt) => Right(pnt)
      case None => Left(Seq(FormError(key, "unable to create location from lat/lng")))
    }

  }

  override def verifying(constraints: Constraint[Point]*): Mapping[Point] = {
    WrappedMapping[Point, Point](this, x => x, x => x, constraints)
  }

  override def unbind(pt: Point): Map[String, String] = {
    Map("lng" -> pt.lng.toString, "lat" -> pt.lat.toString)
  }

  override def withPrefix(prefix: String): Mapping[Point] = new PointMapping(lng, lat)

  override def unbindAndValidate(pt: Point): (Map[String, String], Seq[FormError]) = {
    (Map("lng" -> pt.lng.toString, "lat" -> pt.lat.toString), Nil)
  }

  override val key: String = lng.toString + lat.toString
}


object Point {

  val pointWrites = Writes[Point](p =>
    Json.obj(
      "type" -> JsString("Point"),
      "coordinates" -> Json.arr(JsNumber(p.lng), JsNumber(p.lat))
    )
  )

  val pointReads =
    (__ \ 'type).read[String](constraints.verifying[String](_ == "Point")) andKeep
      (__ \ 'coordinates).read[Point](minLength[List[Double]](2).map(l => Point(l.head, l(1))))

  implicit val pointFormat = Format(pointReads, pointWrites)


  val pointMapping: PointMapping = new PointMapping()

}

