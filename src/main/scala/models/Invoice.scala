package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait Total {
  def total: Int
}
case class Invoice(lines: List[InvoiceLine]) extends Total {
  override def total: Int = lines.foldLeft(0)(_ + _.total)
}

case class InvoiceLine(product: String, discount: Option[Discount], quantity: Int, unitPrice: Int) extends Total{
  private def rawTotal: Int = quantity * unitPrice
  override def total: Int = discount.map(disc =>  Math.floor(rawTotal * (1 - disc.value)).toInt ).getOrElse(rawTotal)
}

case class Discount(label: String, value: Double)

object Discount {
  //We have a free Discount reader because Discount has only mandatory attributes of basic type
  implicit val reads: Reads[Discount] = Json.reads[Discount]
}

object InvoiceLine {
  //We have a free Discount reader because Discount has attributes of basic type and an attributes of a type with a known reader (Discount)
  implicit val reads: Reads[InvoiceLine] = Json.reads[InvoiceLine]
}

