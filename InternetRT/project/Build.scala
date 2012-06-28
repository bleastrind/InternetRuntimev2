import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "InternetRT"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.mockito" % "mockito-all" % "1.9.0" % "test",
      "commons-httpclient" % "commons-httpclient" % "3.1",
      "me.prettyprint" % "hector-core" % "1.0-5",
      "net.liftweb" %% "lift-json" % "2.4"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
