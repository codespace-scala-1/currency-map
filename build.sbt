lazy val commonSettings = Seq(
  organization := "com.example",
  scalaVersion := "2.12.1",
  scalacOptions ++= Seq("-unchecked","-deprecation", "-feature"
                         /* ,  "-Ymacro-debug-lite"  */
                         /*,   "-Ydebug"  ,  "-Ylog:lambdalift"  */
                     ),
  libraryDependencies ++= Seq (
    scalaVersion( "org.scala-lang" % "scala-reflect" % _ ).value,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "org.typelevel" %% "cats" % "0.9.0"
  )
)

lazy val commonModel = (project in file("common-model")).settings(commonSettings: _*)

lazy val mainService = (project in file("main-service")).
  settings(commonSettings: _*).
  dependsOn(commonModel).
  settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.17",
      "com.typesafe.akka" %% "akka-http" % "10.0.5",
      "com.typesafe.akka" %% "akka-testkit" % "2.4.17" % "test",
      "com.typesafe.akka" %% "akka-stream" % "2.4.17",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.17" % "test",
      "com.typesafe.akka" %% "akka-persistence" % "2.4.17",
      "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.17"
    )
)

lazy val currencyPoint = (project in file("currency-point")).settings(commonSettings: _*)
    .dependsOn(commonModel)
    .settings(
     libraryDependencies ++= Seq(
       "com.typesafe.akka" %% "akka-http" % "10.0.5",
       "com.typesafe.akka" %% "akka-stream" % "2.4.17",
       "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.17" % "test",
       "io.circe" %% "circe-generic" % "0.7.0",
       "io.circe" %% "circe-parser" % "0.7.0",
       "de.heikoseeberger" %% "akka-http-circe" % "1.15.0"
     )
    )

lazy val httpClient = (project in file("client/http")).settings(commonSettings: _*)

lazy val root = Project(id = "Cep", base = file("."), aggregate =
  Seq(commonModel,mainService,currencyPoint,httpClient))



