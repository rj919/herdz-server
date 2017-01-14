package models

import play.api.libs.json.Json

/**
  * Created by toidiu on 1/14/17.
  */

case class Protest(name: String, description: String)

object Protest {
  implicit lazy val protestJson = Json.format[Protest]

}