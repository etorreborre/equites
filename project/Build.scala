import sbt._
import Keys._

object EquitesBuild extends Build {
  import BuildSettings._

  lazy val root = Project(id = "equites-root", base = file("."))
    .settings(rootSettings: _*)
    .aggregate(cli, core, gfx)

  lazy val cli = Project(id = "equites-cli", base = file("cli"))
    .settings(cliSettings: _*)
    .dependsOn(core)

  lazy val core = Project(id = "equites-core", base = file("core"))
    .settings(coreSettings: _*)

  lazy val gfx = Project(id = "equites-gfx", base = file("gfx"))
    .settings(gfxSettings: _*)
    .dependsOn(core)
}
