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
  println("------index created------")

  def createIndex {
    import reactivemongo.api.indexes.Index
    import reactivemongo.api.indexes.IndexType.{Ascending, Geo2DSpherical}
    val markerIndex = Index(Seq(
      ("loc", Geo2DSpherical)
    ))
    val locVecIndex = Index(Seq(
      ("loc", Geo2DSpherical)
      , ("ts", Ascending)
    ))

    for {
      m <- marker
      mi <- m.indexesManager.create(markerIndex)
      l <- locVec
      li <- l.indexesManager.create(locVecIndex)
    } yield (mi, li)
  }

  private def collection(name: String): Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection[JSONCollection](name))

  def users: Future[JSONCollection] = collection("user")

  def protests: Future[JSONCollection] = collection("protest")

  def locVec: Future[JSONCollection] = collection("locVec")

  def marker: Future[JSONCollection] = collection("marker")


}