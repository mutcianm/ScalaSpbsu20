import scala.tools.nsc.transform.LambdaLift

name := "lecture7"

version := "0.1"

scalaVersion := scala212

lazy val scala212 = "2.12.11"
lazy val scala211 = "2.11.12"

lazy val supportedScalaVersions = Seq(scala211, scala212)

lazy val releaseNotes = taskKey[String]("release notes go here")
lazy val generateReleaseNotes = taskKey[Unit]("gen release notes")

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  version := "0.1",
  scalaVersion := scala212

)

/*
  root project

  - aggregation
 */
lazy val root = project.in(file("."))
  .aggregate(library, app)
  .settings(commonSettings)
  .settings(
    publish := {},
    printProjectGraph / aggregate := false
  )

/*
  Library project

  - cross building
  - publishing
  - unit tests
  - license
 */
lazy val library = project.in(file("library"))
  .settings(commonSettings)
  .settings(
    publishTo := Some(Resolver.bintrayRepo("org.spbsu", "mylib")),
    crossScalaVersions := supportedScalaVersions
  )

/* app project

  - set main class
  - assembly jar
  - integration tests
  - source generation
  - release notes generation task
 */
lazy val app = project.in(file("app"))
  .dependsOn(library)
  .settings(commonSettings)
  .settings(
    releaseNotes := {"cool feature added"},
    generateReleaseNotes := {
      val data = releaseNotes.value
      val newFile = productDirectories.in(Compile).value.head / "relnotes.txt"
      IO.write(newFile, data.getBytes)
    },
    Compile / packageBin := {
      generateReleaseNotes.value
      packageBin.in(Compile).value
    },
    publish := {},
    Compile / run / mainClass := Some("org.spbsu.Main"),
    mainClass in assembly := Some("org.spbsu.Main"),
    assemblyJarName in assembly := "app.jar"
  )
