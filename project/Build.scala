import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "AMM"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "org.webjars" %% "webjars-play" % "2.1.0-2",
    "org.webjars" % "bootstrap" % "3.0.0-rc1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
