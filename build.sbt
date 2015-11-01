name := "todoTaf"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	// Add your project dependencies here
	jdbc,
	"com.typesafe.play" %% "anorm" % "2.4.0",
	evolutions
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
