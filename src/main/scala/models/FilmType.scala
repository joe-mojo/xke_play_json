package models

import play.api.libs.json._


sealed trait FilmType {
  def value: String
}

case object Horror extends FilmType {
  override val value = "horror"
}

case object Comedy extends FilmType {
  override val value = "comedy"
}

case object Drama extends FilmType {
  override val value = "drama"
}

object FilmType {
  implicit val reads: Reads[FilmType] = new Reads[FilmType] {
    override def reads(json: JsValue): JsResult[FilmType] = json match {
      case JsString(Horror.value) => JsSuccess(Horror)
      case JsString(Comedy.value) => JsSuccess(Comedy)
      case JsString(Drama.value) => JsSuccess(Drama)
      case _ => JsError("Unclassified film")
    }
  }

  implicit val writes: Writes[FilmType] = new Writes[FilmType] {
    override def writes(o: FilmType): JsValue = JsString(o.value)
  }
}