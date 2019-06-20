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
    (
        (__ \ "firstName").read[String] and
        (__ \ "lastName").read[String] and
        Reads.of[Address] //A Reads[Address] must be defined, and it will be passed the complete JSON
    )(Customer.apply _)
  }
  /*
   TODO 3.8 create writes for Customer. Address root attributes must be at the same level as Customer attributes.
   TODO 3.8.1 create writes for AddressNumber, Street and PostalCode
   TODO 3.8.2 create writes for Address
   TODO 3.8.3 create writes for Customer
   */
  implicit val writes: Writes[Customer] = {
    (
        (__ \ "firstName").write[String] and
        (__ \ "lastName").write[String] and
        OWrites.apply[Address] { address =>
          Json.toJson(address).as[JsObject]
        }
    )(unlift(Customer.unapply))
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
  //Because each attribute of Address type has a Reads defined, Reads[Address] is given for free
  implicit val reads: Reads[Address] = Json.reads[Address]
  implicit val writes: Writes[Address] = Json.writes[Address]

}

case class AddressNumber(private val value: String) extends AnyVal {
  override def toString = value
}
object AddressNumber {
  final val addressNumberRegx = """\d+(?:\s?[a-zA-Z]+)?|\d+-\d+""".r
  //TODO Note about 2.8.1: AddressNumber can be build only for strings matching the above regex.
  implicit val reads: Reads[AddressNumber] = Reads.of[String].collect(JsonValidationError("string.not.matching.addressnumber")) {
    case n@addressNumberRegx() => new AddressNumber(n)
  }
  implicit val writes: Writes[AddressNumber] = Writes[AddressNumber] { number =>
    JsString(number.value)
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
  implicit val reads: Reads[Street] = Reads.of[String].map(Street(_))
  implicit val writes: Writes[Street] = Writes[Street] { street =>
    JsString(street.value)
  }
  implicit def fromString(value: String): Street = Street(value)
}

case class PostalCode(value: String) extends AnyVal {
  override def toString = value
}
object PostalCode {
  implicit val reads: Reads[PostalCode] = Reads.of[String].map(PostalCode(_))
  implicit val writes: Writes[PostalCode] = Writes[PostalCode] { postalCode =>
    JsString(postalCode.value)
  }
  implicit def fromString(value: String): PostalCode = PostalCode(value)
}

