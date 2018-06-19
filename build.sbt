sbtPlugin := true

name := "sbt-quickfix"

organization := "com.dscleaver.sbt"

scalaVersion := "2.12.6"

licenses += ("BSD 3-Clause", url("https://opensource.org/licenses/BSD-3-Clause"))

version := "1.0.1-THIB"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/service/local/repositories/releases/content/"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  // "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions"
)

publishMavenStyle := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "provided"
)
