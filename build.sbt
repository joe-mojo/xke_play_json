name := "xke_play_json"

organization := "fr.xebia"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test",
  "com.typesafe.play" %% "play-json" % "2.3.0",
  "org.spire-math" %% "cats" % "0.3.0"
)

fork in Runtime := true

