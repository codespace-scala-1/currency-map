lazy val commonSettings = Seq(
  organization := "com.example",
  scalaVersion := "2.12.1",
  scalacOptions ++= Seq("-unchecked","-deprecation", "-feature"
                         /* ,  "-Ymacro-debug-lite"  */
                         /*,   "-Ydebug"  ,  "-Ylog:lambdalift"  */
                     ),
  libraryDependencies ++= Seq (
    scalaVersion( "org.scala-lang" % "scala-reflect" % _ ).value,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
)

lazy val commonModel = (project in file("common-model")).settings(commonSettings: _*)

lazy val mainService = (project in file("main-service")).settings(commonSettings: _*)

lazy val currencyPoint = (project in file("currency-point")).settings(commonSettings: _*)

lazy val httpClient = (project in file("client/http")).settings(commonSettings: _*)

lazy val root = Project(id = "Cep", base = file("."), aggregate =
  Seq(commonModel,mainService,currencyPoint,httpClient))



