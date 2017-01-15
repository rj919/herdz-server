package mongo


import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by toidiu on 9/10/15.
  */

object MongoObj {
  private lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  println("------creating index------")
  //  PerformObj.createIndex
  println("------index created------")

  private def collection(name: String): Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection](name))

  def users: Future[JSONCollection] = collection("user")

  def protests: Future[JSONCollection] = collection("protest")

  def locVec: Future[JSONCollection] = collection("locVec")

  def marker: Future[JSONCollection] = collection("marker")


}