name := """planning-siasi"""
organization := "org.siasi"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.0"

libraryDependencies += guice
