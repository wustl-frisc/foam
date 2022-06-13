// See README.md for license details.

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "1.0-SNAPSHOT"
ThisBuild / organization     := "edu.wustl.sbs"

name := "foam"

val chiselVersion = "3.5.1"

githubOwner := "wustl-frisc"
githubRepository := "foam"
githubTokenSource := TokenSource.GitConfig("github.token")

lazy val root = (project in file("."))
  .settings(
    name := "fsm-library",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % "0.5.1" % "test"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:genBundleElements",
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
  )
