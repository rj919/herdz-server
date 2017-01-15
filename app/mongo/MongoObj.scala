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
  createIndex

  def createIndex {
    import reactivemongo.api.indexes.Index
    import reactivemongo.api.indexes.IndexType.{Ascending, Geo2DSpherical}
    println("------creating index------")
    val index: Index = new Index(Seq(
      ("loc", Geo2DSpherical)
//      , ("date", Ascending)
    ))
    println("------index created------")

    marker.map(_.indexesManager.create(index))
  }

  private def collection(name: String): Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection](name))

  def users: Future[JSONCollection] = collection("user")

  def protests: Future[JSONCollection] = collection("protest")

  def locVec: Future[JSONCollection] = collection("locVec")

  def marker: Future[JSONCollection] = collection("marker")


}