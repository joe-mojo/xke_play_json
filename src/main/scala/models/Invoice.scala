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
  implicit val reads: Reads[Discount] = (
      (__ \ "label").read[String] and
          (__ \ "value").read[Double]
      ) (Discount.apply _)
}

object InvoiceLine {
  implicit val reads: Reads[InvoiceLine] = (
      (__ \ "product").read[String] and
          (__ \ "discount").readNullable[Discount] and
          (__ \ "quantity").read[Int] and
          (__ \ "unitPrice").read[Int]
      ) (InvoiceLine.apply _)
}

