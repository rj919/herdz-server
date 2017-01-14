//package models
//
//import play.api.libs.json.{Json, _}
//import play.modules.reactivemongo.json._
//import reactivemongo.bson.{BSONObjectID, _}
//
///**
//  * Created by toidiu on 1/14/17.
//  */
//case class User(name: String, _id: Option[BSONObjectID] = None)
//
//object User {
//  implicitly[Reads[BSONObjectID]]
//  implicitly[Writes[BSONObjectID]]
//  implicitly[Format[BSONObjectID]]
//  implicitly[Format[List[BSONObjectID]]]
//  implicitly[Reads[JsObject]]
//  implicitly[OWrites[BSONDocument]]
//
//  implicit lazy val userJson = Json.format[User]
//
//}