name := """practice"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"

// ここから下自分で追加したもの

libraryDependencies ++= Seq(
	jdbc,
	"com.typesafe.play" %% "anorm" % "2.5.3",
	"mysql" % "mysql-connector-java" % "8.0.15",
	"org.jsoup" % "jsoup" % "1.7.3"
)