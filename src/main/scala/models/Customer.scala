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
  implicit val reads: Reads[Customer] = {
    (
        (__ \ "firstName").read[String] and
        (__ \ "lastName").read[String] and
        Reads.of[Address] //A Reads[Address] must be defined, and it will be passed the complete JSON
    )(Customer.apply _)
  }

  /*TODO les writes
  implicit val writes: Writes[Customer] = {
    (
        (__ \ "firstName").write[String] and
        (__ \ "lastName").write[String] and
        OWrites.of[Address]
    )(unlift(Customer.unapply))
  }*/
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
  //Because each attribute of Address type has a Reads defined, Reads[Address] is given for free
  implicit val reads: Reads[Address] = Json.reads[Address]
}

//TODO add constraint on string value \d+(?:\s?[a-zA-Z]+)?|\d+-\d+
case class AddressNumber(value: String) extends AnyVal {
  override def toString = value
}
object AddressNumber {
  implicit val reads: Reads[AddressNumber] = Reads.of[String].map(AddressNumber(_))
  implicit def fromString(value: String): AddressNumber = AddressNumber(value)
}

case class Street(value: String) extends AnyVal {
  override def toString = value
}
object Street {
  implicit val reads: Reads[Street] = Reads.of[String].map(Street(_))
  implicit def fromString(value: String): Street = Street(value)
}

case class PostalCode(value: String) extends AnyVal {
  override def toString = value
}
object PostalCode {
  implicit val reads: Reads[PostalCode] = Reads.of[String].map(PostalCode(_))
  implicit def fromString(value: String): PostalCode = PostalCode(value)
}

