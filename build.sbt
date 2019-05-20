name := "xke_play_json"

organization := "fr.xebia"
version := "0.1"
scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.typesafe.play" %% "play-json" % "2.6.10",
  "org.typelevel" %% "cats-core" % "1.4.0"
)

fork in Runtime := true

