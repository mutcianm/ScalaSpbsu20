lazy val scala212 = "2.12.11"
lazy val scala211 = "2.11.12"

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  version := "0.1",
  scalaVersion := scala212,
  test in assembly := {}
)

name := "lecture7"

lazy val supportedScalaVersions = Seq(scala211, scala212)

lazy val releaseNotes = settingKey[String]("release notes go here")

lazy val generateReleaseNotes = taskKey[Unit]("gen release notes and save to file")
/*
  root project

  - aggregation
 */
lazy val root = project.in(file("."))
  .aggregate(library, app)
  .settings(commonSettings)
  .settings(
    printProjectGraph / aggregate := false,
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
    crossScalaVersions := supportedScalaVersions,
    publishTo := Some(Resolver.bintrayRepo("org.jetbrains", "mylib"))
  )
/* app project

  - set main class
  - assembly jar
  - integration tests
  - source generation
  - release notes generation task
 */
lazy val app = project.in(file("app"))
  .settings(commonSettings)
  .dependsOn(library)
  .settings(
    Compile / run / mainClass := Some("org.spbsu.Main"),
    mainClass in assembly := Some("org.spbsu.Main"),
    assemblyJarName in assembly := "app.jar",
    publish := {},
    releaseNotes := "cool feature added",
    generateReleaseNotes := {
      println("generateReleaseNotes")
      val data = releaseNotes.value
      val newFile = productDirectories.in(Compile).value.head / "relnotes.txt"
      IO.write(newFile, data.getBytes)
    },
    Compile / packageBin := {
      generateReleaseNotes.value
      packageBin.in(Compile).value
    }
  )