package service

import models.{Author, Movie, MovieType, MovieUpdated}
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.collection.mutable

object Step4 {
	val db: mutable.Map[String, Movie] = mutable.Map(
		"tt0208092" -> Movie(
			974271600000L,
			"Snatch",
			Json.obj("imdbScore" -> JsNumber(8.3D), "imdbId" -> "tt0208092"),
			Some(Author("Guy Ritchie")),
			Seq(MovieType.Comedy, MovieType.Crime)
		),
		"tt0094012" -> Movie(
			1400000000L,
			"Spaceballs",
			Json.obj("imdbScore" -> JsNumber(7.1D), "imdbId" -> "tt0094012"),
			Some(Author("Mel Brooks")),
			Seq(MovieType.Comedy, MovieType.SciFi)
		)
	)
	//TODO 4.1 implement readsOnlyAdditionalInfo that take a Movie and return a Reads[Movie] that actually reads only additionalInfo from input JSON,
	//  and replace all other attributes by the specified movie attributes
	def readsOnlyAdditionalInfo(movieFromDb: Movie): Reads[Movie] = {
		(
				Reads.pure[Long](movieFromDb.timestamp) and
				Reads.pure[String](movieFromDb.name) and
				(__ \ "additionalInfo").read[JsObject] and
				Reads.pure(movieFromDb.author) and
				Reads.pure(movieFromDb.types)
		)(Movie.apply _)
	}

	private def toString(errors: Seq[(JsPath, Seq[JsonValidationError])]): Either[String, MovieUpdated] = Left(errors.toString())
	private def toMovieUpdated(valid: Movie): Either[String, MovieUpdated] = Right(MovieUpdated(valid))

	//TODO 4.2 implement createdUpdate that take a movie Id and a JSON entity representing a Movie with updated values and return the corresponding MovieUpdated event.
	// this method must use readsOnlyAdditionalInfo in order to ensure only additionalInfos are used from input JSON.
	def createdUpdate(movieId: String, movieEntity: JsValue): Either[String, MovieUpdated] = {
		db.get(movieId).map { movie =>
			movieEntity.validate[Movie](readsOnlyAdditionalInfo(movie)).fold[Either[String, MovieUpdated]](toString, toMovieUpdated)
		}.getOrElse(Left(s"Movie #$movieId not found"))
	}

	def updateInfos(movieId: String, movieEntity: JsValue): Either[String, MovieUpdated] = {
		createdUpdate(movieId, movieEntity).map { evt =>
			db.put(movieId, evt.data)
			evt
		}
	}

	//TODO 4.3 Create writes that hide an attribute. We're going to study 2 ways.
	// We want a light view of Movie JSON (for example: returned in an http request) with only movie name and author.
	//TODO 4.3.1 Create a Writes[Movie] that create a JSON object with only needed attributes.
	def writesSimpleJsObj: Writes[Movie] = Writes { movie =>
		movie.author.foldLeft(Json.obj("name" -> movie.name)) { (jsObj, author) =>
		jsObj + ("author", JsString(author.name))
		}
	}
	//TODO 4.3.2 Create a Writes[Movie] using explicit pathes (__ \ "attr").writes... and Movie.unapply
	// BUT You need one more thing because Movie.unapply is giving all attributes in a tuple and
	// we want only 2 of them.
	def ignore[T]: OWrites[T] = OWrites[T](_ => Json.obj())
	def writesWithIgnore: Writes[Movie] = {
		//(
		//??? and
		//??? and
		//??? and
		//??? and
		???
		//)(unlift(Movie.unapply))
	}

	def getMovieLightView(movieId: String)(implicit writesMovie: Writes[Movie]): Either[String, JsValue] = {
		db.get(movieId).map { movie =>
			Json.toJson(movie)
		}.fold[Either[String, JsValue]](Left(s"Not found: movie #$movieId")){ json: JsValue =>
			Right(json)
		}
	}
}
