organization := "com.ageorgin"
name := "http-server-benchmark"

scalaVersion := "2.12.5"
val entryPoint = Some("com.ageorgin.httpserverbenchmark.Main")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:-unused,_",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)

resolvers ++= Seq(
  "Spray repo" at "http://repo.spray.io/",
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Apache repo" at "https://repository.apache.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finchx-core" % "0.27.0"
)

mainClass in Compile := entryPoint

// Assembly
mainClass in assembly := entryPoint
logLevel in assembly := Level.Info
test in assembly := {}

// Artifact with assembly jar
artifact in(Compile, assembly) := {
  (artifact in(Compile, assembly)).value.copy(`classifier` = Some("assembly"))
}
addArtifact(artifact in(Compile, assembly), assembly)

// Assembly merge strategy
assemblyMergeStrategy in assembly := {
  case "META-INF/io.netty.versions.properties" => MergeStrategy.first
  case x => (assemblyMergeStrategy in assembly).value(x)
}

//for IntelliJ IDEA compatibility, scalastyle_config.xml (with _ instead of -) must be in /project folder
scalastyleConfig := file("project/scalastyle_config.xml")

// Tests settings
parallelExecution := false
coverageEnabled in Test := true
