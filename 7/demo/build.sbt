lazy val supportedScalaVer = Seq(scala212, scala211)

lazy val releaseNotes = taskKey[String]("release notes")
lazy val genReleaseNotes = taskKey[Unit]("generate release notes")

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % "test",
  version := "0.1",
  scalaVersion := scala212
)

lazy val scala212 = "2.12.11"
lazy val scala211 = "2.11.12"

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
    crossScalaVersions := supportedScalaVer,
    publishTo := Some(Resolver.bintrayRepo("org.spbsu", "mylib"))
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
    releaseNotes := {"release notes"},
    genReleaseNotes := {
      val data = releaseNotes.value
      val newFile = productDirectories.in(Compile).value.head / "relnotes.txt"
      IO.write(newFile, data.getBytes)
    },
    Compile / packageBin := {
      genReleaseNotes.value
      packageBin.in(Compile).value
    },
    publish := {},
    Compile / run / mainClass := Some("org.spbsu.Main"),
    mainClass in assembly := Some("org.spbsu.Main"),
    assemblyJarName in assembly := "app.jar"
  )