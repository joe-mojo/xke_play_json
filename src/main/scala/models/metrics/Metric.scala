package models.metrics

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.Map

case class Metric(
					 provider: String,
					 device_type: String,
					 device_id: String,
					 timestamp: BigDecimal,
					 ttl: BigDecimal,
					 metrics: Map[String, BigDecimal]
				 )

object Metric {
	val FixedAttributes: Seq[String] = List("provider", "device_type", "device_id", "timestamp", "ttl")
	val filterFixedAttributes: JsObject => JsObject = { jsObj =>
		JsObject(jsObj.fields.filterNot(entry => FixedAttributes.contains(entry._1)))
	}

	implicit val MetricMapReads: Reads[Map[String, BigDecimal]] = {
		case JsObject(objMap) => JsSuccess(objMap.flatMap(entry => entry._2.asOpt[BigDecimal].map(value => (entry._1, value))))
		case wrong: JsValue => JsError(s"Not an object: ${Json.stringify(wrong)}")
	}

	implicit val MetricReads: Reads[Metric] = {
		(
			(__ \ "provider").read[String] and
			(__ \ "device_type").read[String] and
			(__ \ "device_id").read[String] and
			(__ \ "timestamp").read[BigDecimal] and
			(__ \ "ttl").read[BigDecimal] and
			__.read[JsObject].map(filterFixedAttributes).andThen(implicitly[Reads[Map[String, BigDecimal]]])
		)(Metric.apply _)
	}
}