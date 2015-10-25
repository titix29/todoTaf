name := "todoTaf"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	// Add your project dependencies here
	jdbc,
	anorm
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
