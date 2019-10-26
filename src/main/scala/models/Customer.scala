package models

import play.api.libs.json.Reads
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Customer(
    firstName: String,
    lastName: String,
    address: Address
)

object Customer {
  /*
    TODO 2.8 create a Reads for Customer. Address root attributes must be at the same level as Customer attributes.
    TODO 2.8.1 create Reads for AddressNumber, Street and PostalCode (same as models.Author).
    TODO 2.8.2 create Reads for Address
    TODO 2.8.3 create Reads for Customer
   */
  implicit val reads: Reads[Customer] = {
    //2.8.3 here



    new Reads[Customer] { override def reads(json: JsValue): JsResult[Customer] = JsError("Not implemented") } //TODO remove this line
  }
  /*
   TODO 3.8 create writes for Customer. Address root attributes must be at the same level as Customer attributes.
   TODO 3.8.1 create writes for AddressNumber, Street and PostalCode
   TODO 3.8.2 create writes for Address
   TODO 3.8.3 create writes for Customer
   */
  implicit val writes: Writes[Customer] = {
    //3.8.3 here





    new Writes[Customer] { override def writes(o: Customer): JsValue = JsNull } //TODO remove this line
  }
}

case class Address(
    number: AddressNumber,
    street: Street,
    postalCode: PostalCode,
    city: String,
    state: Option[String],
    country: String
)

object Address {
  //Because each attribute type of Address has a Reads defined, Reads[Address] is given for free
  implicit val reads: Reads[Address] = null //2.8.2
  implicit val writes: Writes[Address] = null //3.8.2 replace null by the most simple Writes. Why can we use it here ?

}

case class AddressNumber(private val value: String) extends AnyVal {
  override def toString = value
}
object AddressNumber {
  final val addressNumberRegx = """\d+(?:\s?[a-zA-Z]+)?|\d+-\d+""".r
  //TODO Note about 2.8.1: AddressNumber can be build only for strings matching the above regex.
  implicit val reads: Reads[AddressNumber] = null //2.8.1


  implicit val writes: Writes[AddressNumber] = Writes[AddressNumber] { number =>
    JsNull //3.8.1: fix this !
  }

  def apply(value: String): Option[AddressNumber] = {
    value match {
      case addressNumberRegx() => Some(new AddressNumber(value))
      case other => None
    }
  }
}

case class Street(value: String) extends AnyVal {
  override def toString = value
}
object Street {
  implicit val reads: Reads[Street] = null //2.8.1
  implicit val writes: Writes[Street] = null //3.8.1


  implicit def fromString(value: String): Street = Street(value)
}

case class PostalCode(value: String) extends AnyVal {
  override def toString = value
}
object PostalCode {
  implicit val reads: Reads[PostalCode] = null //2.8.1
  implicit val writes: Writes[PostalCode] = null //3.8.1


  implicit def fromString(value: String): PostalCode = PostalCode(value)
}

