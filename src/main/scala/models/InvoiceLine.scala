package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Invoice(lines: List[InvoiceLine])

case class InvoiceLine(product: String, discount: Option[Double])

case class Discount(label: String, value: Double)

object InvoiceLine {
  implicit val reads: Reads[InvoiceLine] = (
    (__ \ "product").read[String] and
      (__ \ "discount").readNullable((__ \ "value").read[Double])
    ) (InvoiceLine.apply _)
}