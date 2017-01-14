name := "herdz"

version := "1.0"

lazy val `herdz` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val mongoV = "0.12.1"
  Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.11.12"
    , "com.google.inject" % "guice" % "4.0"

    , specs2 % Test
  )
}

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)
