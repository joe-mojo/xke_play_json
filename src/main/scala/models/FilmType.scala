package models

import play.api.libs.json._


sealed trait FilmType {
  def value: String
}


object FilmType {
  case object Horror extends FilmType {
    override val value = "horror"
  }

  case object Comedy extends FilmType {
    override val value = "comedy"
  }

  case object Drama extends FilmType {
    override val value = "drama"
  }

  /*
    TODO 2.6 Create a Reads for the sealed trait FilmType. Once again, there is 2 ways:
    1) by implementing a new Reads (anonymous class), and especially the `reads(json: JsValue): JsResult[A]` method
    2) by passing a read function to `Reads.apply(f: JsValue => JsResult[A])`
    The second one is slightly more concise
   */
  val readsByReadsImpl: Reads[FilmType] = new Reads[FilmType] {
    override def reads(json: JsValue): JsResult[FilmType] = json match {
      case JsString(Horror.value) => JsSuccess(Horror)
      case JsString(Comedy.value) => JsSuccess(Comedy)
      case JsString(Drama.value) => JsSuccess(Drama)
      case _ => JsError("Unclassified film")
    }
  }

  val readsWithReadsApply: Reads[FilmType] = Reads {
    case JsString(Horror.value) => JsSuccess(Horror)
    case JsString(Comedy.value) => JsSuccess(Comedy)
    case JsString(Drama.value) => JsSuccess(Drama)
    case _ => JsError("Unclassified film")
  }
  implicit val reads: Reads[FilmType] = readsWithReadsApply // <-- choose you preferred Reads[FilmType]

  /*
   TODO 3.6 Create a Writes for the sealed trait FilmType.
   As with 2.6, there is 2 syntaxes. One by implementing writes method of Writes trait, another by passing a function to Writes.apply.
   Once again, the second one is more concise.
   */
  implicit val writesWithWritesImpl: Writes[FilmType] = new Writes[FilmType] {
    override def writes(o: FilmType): JsValue = JsString(o.value)
  }
  val writesWithWritesApply: Writes[FilmType] = Writes(filmType => JsString(filmType.value))
  implicit val writes: Writes[FilmType] = writesWithWritesApply // <-- choose you preferred Writes[FilmType]
}