// The simplest possible sbt build file is just one line:

scalaVersion := "2.12.3"

name := "hello-world"
organization := "ch.epfl.scala"
version := "1.0"


libraryDependencies += "org.apache.avro" % "avro" % "1.8.2"
libraryDependencies += "net.sourceforge.jtds" % "jtds" % "1.3.1"
libraryDependencies += "com.typesafe.slick" % "slick_2.12" % "3.2.1"
libraryDependencies += "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1"
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.6.4"
libraryDependencies += "com.typesafe.akka" % "akka-stream_2.12" % "2.5.4"
