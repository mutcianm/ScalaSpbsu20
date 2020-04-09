import scala.tools.nsc.transform.LambdaLift

name := "lecture7"

version := "0.1"

scalaVersion := scala212

lazy val scala212 = "2.12.11"
lazy val scala211 = "2.11.12"
lazy val supportedScalaVersions = Seq(scala211, scala212)
lazy val releaseNotes  = settingKey[String]("release notes go here")
lazy val generateReleaseNotes = taskKey[Unit]("write release note to file")

lazy val commonSettings = Seq (
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
  .dependsOn(app, library)
  .settings(
    publish := {}
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
      publishTo := Some(Resolver.bintrayIvyRepo("org.jetbrainspp", "mylib")),
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
    Compile / run / mainClass := Some ("org.spbsu.Main"),
    releaseNotes := "this is good release",
    generateReleaseNotes := {
      println("generate release notes")
      val data = releaseNotes.value
    },
    publish := {}
  )
