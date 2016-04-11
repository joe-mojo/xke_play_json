import play.api.libs.json.{JsString, Writes}

package object models {

  object Implicits {

    implicit def OptionWrites[T](implicit fmt: Writes[T]): Writes[Option[T]] = new Writes[Option[T]] {
      def writes(o: Option[T]) = o match {
        case Some(value) => fmt.writes(value)
        case None => JsString("")
      }
    }

  }

}
