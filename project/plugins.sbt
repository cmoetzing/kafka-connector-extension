// Adds a `scalafmt` sbt plugin
// https://github.com/scalameta/sbt-scalafmt
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

// Adds a `wartremover` a flexible Scala code linting tool
// http://github.com/puffnfresh/wartremover
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.16")

// Adds Contrib Warts
// http://github.com/wartremover/wartremover-contrib/
addSbtPlugin("org.wartremover" % "sbt-wartremover-contrib" % "1.3.13")

// Adds a `assembly` task to create a fat JAR with all of its
// dependencies
// https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")

// Adds most common doc api mappings
// https://github.com/ThoughtWorksInc/sbt-api-mappings
addSbtPlugin("com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "3.0.0")

// Adds Scala Code Coverage (Scoverage) used during unit tests
// http://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.9.3")

// Adds SBT Coveralls plugin for uploading Scala code coverage to
// https://coveralls.io
// https://github.com/scoverage/sbt-coveralls
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.3.1")

// Adds a `dependencyUpdates` task to check Maven repositories for
// dependency updates
// http://github.com/rtimush/sbt-updates
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.1")

// Adds `scalastyle` a coding style checker and enforcer
// https://github.com/scalastyle/scalastyle-sbt-plugin
addSbtPlugin("org.scalastyle" % "scalastyle-sbt-plugin" % "1.0.0")

// Adds a `dependencyUpdates` task to check for dependency updates
// https://github.com/jrudolph/sbt-dependency-graph
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")

// Adds a `sbt-reproducible-builds` plugin
// https://github.com/raboof/sbt-reproducible-builds
addSbtPlugin("net.bzzt" % "sbt-reproducible-builds" % "0.30")
