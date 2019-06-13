package models

import play.api.libs.json._

trait Total {
  def total: BigDecimal
}
case class Invoice(lines: List[InvoiceLine]) extends Total {
  override def total: BigDecimal = lines.foldLeft(BigDecimal(0))(_ + _.total)
}

case class InvoiceLine(product: String, discount: Option[Discount], quantity: Int, unitPrice: BigDecimal) extends Total{
  private def rawTotal: BigDecimal = quantity * unitPrice
  override def total: BigDecimal = discount.map(disc =>  rawTotal * (1 - disc.value)).getOrElse(rawTotal)
}

case class Discount(label: String, value: BigDecimal)

object Discount {
	//TODO 2.1 Create a Reads for Discount (the easy way should work)
	//We have a free Discount reader because Discount has only mandatory attributes of basic type
	implicit val reads: Reads[Discount] = Json.reads[Discount]
	//TODO 3.1 Create a Writes for Discount (the easy way should work)
	implicit val writes: Writes[Discount] = Json.writes[Discount]
}

object InvoiceLine {
	//TODO 2.2 Create 2 kinds of Reads for InvoiceLine
	//TODO 2.2.1 For InvoiceLine itself (the easy way should work)
	//We have a free Discount reader because Discount has attributes of basic type and an attributes of a type with a known reader (Discount)
	implicit val reads: Reads[InvoiceLine] = Json.reads[InvoiceLine]
	//TODO 2.2.2 For List[InvoiceLine]. You can have a Reads for Seq[InvoiceLine] for free; then use map to get a List from Seq
	implicit val readsList: Reads[List[InvoiceLine]] = Reads.seq[InvoiceLine].map(_.toList)
	//TODO 3.2 Create a Writes for InvoiceLine (the easy way should work)
	implicit val writes: Writes[InvoiceLine] = Json.writes[InvoiceLine]
}

object Invoice {
	//TODO 2.3 Create a Reads for Invoice (the easy way should work)
	import InvoiceLine.readsList
	implicit val reads: Reads[Invoice] = Json.reads[Invoice]
	//TODO 3.3 Create a Writes for Invoices (the easy way should work)
	implicit val writes: Writes[Invoice] = Json.writes[Invoice]
}

