import sbt._
import Keys._

/**
  * based on http://www.scala-sbt.org/release/docs/Getting-Started/Multi-Project
  */
object SpraySortBuild extends Build {
  // aggregate: running a task on the aggregate project will also run it
  // on the aggregated projects.
  // dependsOn: a project depends on code in another project.
  // without dependsOn, you'll get a compiler error: "object bar is not a
  // member of package com.alvinalexander".
  lazy val root = Project(id = "spraySort",
    base = file(".")).aggregate(akkaSort).dependsOn(akkaSort)
  // sub-project in the Foo subdirectory
  lazy val akkaSort = Project(id = "akkaSort",
    base = file("akka-sort"))

}