name := "Akka Word Count"

version := "1.0"

scalaVersion := "2.10.5"

autoScalaLibrary := false

// Enables publishing to maven repo
publishMavenStyle := true

// Do not append Scala versions to the generated artifacts
crossPaths := false

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % "2.3.14",
	"org.scala-lang" % "scala-library" % scalaVersion.value,
	"org.scala-lang" % "scala-compiler" % scalaVersion.value % "scala-tool"
)

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

