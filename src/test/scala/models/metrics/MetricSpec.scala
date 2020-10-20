package models.metrics

import org.scalatest.{FunSpec, Inside, Matchers}
import play.api.libs.json._
import models.metrics._
import models.metrics.Metric._

class MetricSpec extends FunSpec with Matchers with Inside{
	describe("MetricReads") {
		it("should load parse all attributes and map") {
			val sampleJsonTxt =
				"""
				  |{
				  |  "provider": "metrics",
				  |  "device_id": "device_id",
				  |  "device_type": "electricmeter",
				  |  "timestamp": 1601366400,
				  |  "ttl": 1603958400,
				  |  "OfftakeElecApparentPower": 400,
				  |  "OfftakeElecActiveIndexTotal": 100,
				  |  "OfftakeElecActiveIndex1": 100,
				  |  "OfftakeElecActiveIndex2": 100
				  |}
				  |""".stripMargin

			val actualRes = Json.parse(sampleJsonTxt).validate[Metric]
			println(actualRes)
			inside(actualRes) {
				case JsSuccess(Metric("metrics",  "electricmeter", "device_id", timestamp, ttl, map), _) =>
					timestamp shouldBe BigDecimal(1601366400)
					ttl shouldBe BigDecimal(1603958400)
					map.toSeq should contain theSameElementsAs Seq(
						"OfftakeElecApparentPower" -> BigDecimal(400),
						"OfftakeElecActiveIndexTotal" -> BigDecimal(100),
						"OfftakeElecActiveIndex1" -> BigDecimal(100),
						"OfftakeElecActiveIndex2" -> BigDecimal(100),
					)
			}

		}
	}
}
