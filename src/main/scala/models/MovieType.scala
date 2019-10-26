package models

import play.api.libs.json._


sealed trait MovieType {
  def value: String
}


object MovieType {
  case object Horror extends MovieType {
    override val value = "horror"
  }

  case object Comedy extends MovieType {
    override val value = "comedy"
  }

  case object Romance extends MovieType {
    override val value = "romance"
  }

  case object SciFi extends MovieType {
    override val value = "sci-fi"
  }

  case object Action extends MovieType {
    override val value = "action"
  }

  case object Aventure extends MovieType {
    override val value = "aventure"
  }

  case object Thriller extends MovieType {
    override val value = "thriller"
  }

  case object Crime extends MovieType {
    override val value = "crime"
  }

  /*
    TODO 2.6 Create a Reads for the sealed trait MovieType. Once again, there is 2 ways:
    1) by implementing a new Reads (anonymous class), and especially the `reads(json: JsValue): JsResult[A]` method
    2) by passing a read function to `Reads.apply(f: JsValue => JsResult[A])`
    The second one is slightly more concise
   */
  val readsByReadsImpl: Reads[MovieType] = new Reads[MovieType] {
    override def reads(json: JsValue): JsResult[MovieType] = json match {
      case JsString(Horror.value) => JsSuccess(Horror)
      case JsString(Comedy.value) => JsSuccess(Comedy)
      case JsString(Romance.value) => JsSuccess(Romance)
      case JsString(SciFi.value) => JsSuccess(SciFi)
      case JsString(Action.value) => JsSuccess(Action)
      case JsString(Aventure.value) => JsSuccess(Aventure)
      case JsString(Thriller.value) => JsSuccess(Thriller)
      case JsString(Crime.value) => JsSuccess(Crime)
      case _ => JsError("Unclassified movie")
    }
  }

  val readsWithReadsApply: Reads[MovieType] = Reads {
    case JsString(Horror.value) => JsSuccess(Horror)
    case JsString(Comedy.value) => JsSuccess(Comedy)
    case JsString(Romance.value) => JsSuccess(Romance)
    case JsString(SciFi.value) => JsSuccess(SciFi)
    case JsString(Action.value) => JsSuccess(Action)
    case JsString(Aventure.value) => JsSuccess(Aventure)
    case JsString(Crime.value) => JsSuccess(Crime)
    case _ => JsError("Unclassified movie")
  }
  implicit val reads: Reads[MovieType] = readsWithReadsApply // <-- choose you preferred Reads[MovieType]

  /*
   TODO 3.6 Create a Writes for the sealed trait MovieType.
   As with 2.6, there is 2 syntaxes. One by implementing writes method of Writes trait, another by passing a function to Writes.apply.
   Once again, the second one is more concise.
   */
  val writesWithWritesImpl: Writes[MovieType] = null


  val writesWithWritesApply: Writes[MovieType] = null
  implicit val writes: Writes[MovieType] = writesWithWritesApply // <-- choose you preferred Writes[MovieType]
}