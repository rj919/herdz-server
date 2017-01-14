package controllers

import models.Protest
import mongo.MongoObj
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.json.ImplicitBSONHandlers._
import play.modules.reactivemongo.json._
import reactivemongo.api.ReadPreference

import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {

  def index = Action {
    Ok("Your new application is ready.")
  }
  import models.Protest.protestJson


  import play.api.Logger
  import play.api.libs.json._
  import play.api.mvc._
  import play.modules.reactivemongo._
  import reactivemongo.api.ReadPreference
  import reactivemongo.play.json._
  import reactivemongo.play.json.collection._

  def protest() = Action.async {
    val d = MongoObj.protests.flatMap(
      _.find(Json.obj())
        .cursor[Protest](ReadPreference.primary)
        .collect[List]()
    )

    d.map(s => Ok(Json.toJson(s)))
    //    Future(Ok("ads"))
  }

  def addProtest() = Action.async {
    val d = MongoObj.protests.flatMap(
      _.insert(Protest("Black Lives Matter", "description"))
    )

    d.map(e => Ok(e.toString))

  }
}