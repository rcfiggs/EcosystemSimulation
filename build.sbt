name := "EcosystemSimulation"

version := "0.1"

scalaVersion := "3.4.0"

libraryDependencies ++= Seq( 
  "org.scalafx" %% "scalafx" % "21.0.0-R32",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test"
)

scalacOptions ++= Seq("-deprecation" ,"-feature")