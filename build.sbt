import pl.project13.scala.sbt.JmhPlugin

name := "bench-millis"

libraryDependencies += "com.google.guava" % "guava" % "32.0.1-jre"
libraryDependencies += "commons-codec" % "commons-codec" % "1.16.1"
libraryDependencies += "org.bouncycastle" % "bcpkix-jdk18on" % "1.76"
libraryDependencies += "org.scala-sbt" %% "io" % "1.10.0"
libraryDependencies += "commons-io" % "commons-io" % "2.17.0"

scalaVersion := "2.13.13"

enablePlugins(JmhPlugin)
