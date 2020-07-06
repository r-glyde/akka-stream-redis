scalaVersion := "2.13.2"
name := "akka-stream-redis"
version := "0.1.0"

libraryDependencies ++= Seq(
  "io.lettuce"        % "lettuce-core" % "6.0.0.M1",
  "com.typesafe.akka" %% "akka-stream" % "2.6.6",
  "org.scalatest"     %% "scalatest"   % "3.1.2" % Test
)
