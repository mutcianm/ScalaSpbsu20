name := "lecture7"

lazy val scala212 = "2.12.11"
lazy val scala211 = "2.11.12"

lazy val supportedScalaVersions = Seq(scala211, scala212)

lazy val releaseNotes = settingKey[String]("What has changed in the new version")
lazy val generateReleaseNotes = taskKey[Unit]("Write release notes to file")

lazy val commonSettings = Seq(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % Test,
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
    publishTo := Some(Resolver.bintrayRepo("org.spbsu", "kto-takoy-etot-vash-library")),
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
    Compile / run / mainClass := Some("org.spbsu.Main"),
    assembly / mainClass := Some("org.spbsu.Main"),
    assembly / assemblyJarName := "app.jar",
    publish := {},
    releaseNotes := "Removed Herobrine\n",
    generateReleaseNotes := {
      val data = releaseNotes.value
      val file = (Compile / productDirectories).value.head / "release_notes.txt"
      IO.write(file, data)
    },
    Compile / packageBin := {
      generateReleaseNotes.value
      (Compile / packageBin).value
    }
  )
