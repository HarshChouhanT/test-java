name := """task-test"""
organization := "com.task"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "org.mongodb" % "mongodb-driver-sync" % "4.7.1"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20"
libraryDependencies += "io.reactivex.rxjava3" % "rxjava" % "3.1.6"
