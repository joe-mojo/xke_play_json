package service

import models.{Author, Film, FilmUpdated}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.collection.mutable

object Step4 {
	//TODO Step 4: 2 real life cases
	val db: mutable.Map[String, Film] = mutable.Map(
		"tt0208092" -> Film(974271600000L, "Snatch", Json.obj("imdbScore" -> JsNumber(8.3D), "imdbId" -> "tt0208092"), Some(Author("Guy Ritchie"))),
		"tt0094012" -> Film(1400000000L, "Spaceballs", Json.obj("imdbScore" -> JsNumber(7.1D), "imdbId" -> "tt0094012"), Some(Author("Mel Brooks")))
	)
	//TODO 4.1 implement readsOnlyAdditionalInfo that take a Film and return a Reads[Film] that actually reads only additionalInfo from input JSON,
	//  and replace all other attributes by the specified film attributes
	def readsOnlyAdditionalInfo(filmFromDb: Film): Reads[Film] = {
		(
				Reads.pure[Long](filmFromDb.timestamp) and
				Reads.pure[String](filmFromDb.name) and
				(__ \ "additionalInfo").read[JsObject] and
				Reads.pure(filmFromDb.author)
		)(Film.apply _)
	}

	private def toString(errors: Seq[(JsPath, Seq[JsonValidationError])]): Either[String, FilmUpdated] = Left(errors.toString())
	private def toFilmUpdated(valid: Film): Either[String, FilmUpdated] = Right(FilmUpdated(valid))

	//TODO 4.2 implement createdUpdate that take a film Id and a JSON entity representing a Film with updated values and return the corresponding FilmUpdated event.
	// this method must use readsOnlyAdditionalInfo in order to ensure only additionalInfos are used from input JSON.
	def createdUpdate(filmId: String, filmEntity: JsValue): Either[String, FilmUpdated] = {
		db.get(filmId).map { film =>
			filmEntity.validate[Film](readsOnlyAdditionalInfo(film)).fold[Either[String, FilmUpdated]](toString, toFilmUpdated)
		}.getOrElse(Left(s"Film #$filmId not found"))
	}

	def updateInfos(filmId: String, filmEntity: JsValue): Either[String, FilmUpdated] = {
		createdUpdate(filmId, filmEntity).map { evt =>
			db.put(filmId, evt.data)
			evt
		}
	}

	//TODO 4.3 Create writes that hide an attribute. We're going to study 2 ways.
	// We want a light view of Film JSON (for example: returned in an http request) with only film name and author.
	//TODO 4.3.1 Create a Writes[Film] that create a JSON object with only needed attributes.
	def writesSimpleJsObj: Writes[Film] = Writes { film =>
		film.author.foldLeft(Json.obj("name" -> film.name)) { (jsObj, author) =>
		jsObj + ("author", JsString(author.name))
		}
	}
	//TODO 4.3.2 Create a Writes[Film] using explicit pathes (__ \ "attr").writes... and Film.unapply
	// BUT You need one more thing because Film.unapply is giving all attributes in a tuple and
	// we want only 2 of them.
	def ignore[T] = OWrites[T](_ => Json.obj())
	def writesWithIgnore: Writes[Film] = {
		(
				ignore and
				(__ \ "name").write[String] and
				ignore and
				(__ \ "author").writeNullable[Author]
		)(unlift(Film.unapply))
	}

	def getFilmLightView(filmId: String)(implicit writesFilm: Writes[Film]): Either[String, JsValue] = {
		db.get(filmId).map { film =>
			Json.toJson(film)
		}.fold[Either[String, JsValue]](Left(s"Not found: film #$filmId")){ json: JsValue =>
			Right(json)
		}
	}
}
