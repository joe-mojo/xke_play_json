package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Movie(
		timestamp: Long,
		name: String,
		additionalInfo: JsValue,
		author: Option[Author],
		types: Seq[MovieType]
)

object Movie {
	/*
	TODO 2.5 Create a custom Reads for Movie. In expected JSON, the timestamp attribute is named "startTimestamp"
	Examples:
	{"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : {"whatever": "you want"}, "author": "Guy Richie"}
	{"startTimestamp": 974271600000, "name": "Snatch", "additionalInfo" : "Whatever you want"}
	*/
	implicit val reads: Reads[Movie] = {
		(
			(__ \ "startTimestamp").read[Long] and
			(__ \ "name").read[String] and
			(__ \ "additionalInfo").read[JsValue] and
			(__ \ "author").readNullable[Author] and
			(__ \ "types").readWithDefault[Seq[MovieType]](Seq.empty)
		) (Movie.apply _)
	}
	/*
	TODO 3.5 Create a custom Writes for Movie. In expected JSON, the timestamp attribute is named "startTimestamp"
	 */
	implicit val writes: Writes[Movie] = {






		null
	}
}

case class Author(name: String)

object Author {
	/*
	 TODO 2.4 Create a Reads for Author. Inside Json, an Author is only a String:  {..., "author": "Lana Wachowsky", ...}
	 You would like to re-use the existing Reads of String, then mapping the String to an Author.
	 Creating a Reads[A] then mapping with a function A => B is creating a Reads[B]

	 There is 2 ways of doing this.
	 */
	val readsByPath: Reads[Author] = {
		//First way: you call reads on root then map
		__.read[String].map(Author(_))
	}
	val readsByStringReader: Reads[Author] = {
		//Second way: you ask Reads to give you the Reads of String then map
		Reads.of[String].map(Author.apply)
	}
	implicit val reads: Reads[Author] = readsByStringReader // <-- Assign to reads the Reader you want
	/*
	TODO 3.4 Create a Writes for Author
	 This is the exact reverse of Reads[Author]
	 Creating a Writes[A] then contramapping with a function Z => A is creating a Writes[Z]
	 */
	val writesByPath: Writes[Author] = {
		//call write on root then contramap
		__.write[String].contramap(_.name)
	}

	val writesByStringWriter: Writes[Author] = {
		//ask Writes to give you the Writes of String then contramap
		Writes.of[String].contramap(_.name)
	}
	implicit val writes: Writes[Author] = writesByStringWriter // <-- Assign to writes the Writer you want
}
