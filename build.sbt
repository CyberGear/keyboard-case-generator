ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "keyboard-case-generator",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies += "com.github.dzufferey" %% "scadla" % "0.1.1",
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.2",
    libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.15.2",
    libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.15.2"
  )
