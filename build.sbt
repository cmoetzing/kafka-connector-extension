import com.exasol.cloudetl.sbt.Dependencies
import com.exasol.cloudetl.sbt.IntegrationTestPlugin
import com.exasol.cloudetl.sbt.Settings

credentials += Credentials(Path.userHome / ".sbt" / "space-middleware.credentials")

publishTo := Some("space-middleware" at "https://maven.pkg.jetbrains.space/sensalytics/p/middleware/middleware")

lazy val orgSettings = Seq(
  name := "kafka-connector-extension",
  description := "Exasol Kafka Connector Extension",
  organization := "com.exasol",
  organizationHomepage := Some(url("http://www.exasol.com"))
)

lazy val buildSettings = Seq(
  scalaVersion := "2.13.8"
)

lazy val root =
  project
    .in(file("."))
    .settings(moduleName := "exasol-kafka-connector-extension")
    .settings(version := "1.5.3-sly3")
    .settings(orgSettings)
    .settings(buildSettings)
    .settings(Settings.projectSettings(scalaVersion))
    .settings(
      resolvers ++= Dependencies.Resolvers,
      libraryDependencies ++= Dependencies.AllDependencies,
      excludeDependencies ++= Dependencies.ExcludedDependencies
    )
    .enablePlugins(IntegrationTestPlugin, ReproducibleBuildsPlugin)

addCommandAlias("pluginUpdates", ";reload plugins;dependencyUpdates;reload return")

assembly / artifact := {
  val art = (assembly / artifact).value
  art.withClassifier(Some("assembly"))
}

addArtifact(assembly / artifact, assembly)