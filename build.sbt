import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := """andonescu-play-startup"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(SpacesAroundMultiImports, false)
  ): _*)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "com.h2database" % "h2" % "1.4.192",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "com.github.tminglei" %% "slick-pg" % "0.14.1",
  "com.github.tminglei" %% "slick-pg_date2" % "0.14.1",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.2.0",
  specs2 % Test
)

fork in run := true