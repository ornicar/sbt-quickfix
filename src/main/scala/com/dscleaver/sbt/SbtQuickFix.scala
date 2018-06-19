package com.dscleaver.sbt

import quickfix.{ QuickFixLogger }
import sbt._
// import sbt.IO._
import sbt.Keys._

object SbtQuickFix extends AutoPlugin {

  object QuickFixKeys {
    val QuickFixDirectory = target in config("quickfix")
    // val quickFixInstall = TaskKey[Unit]("install-vim-plugin")
    // val VimEnableServer = SettingKey[Boolean]("vim-enable-server", "Enables communication with the Vim server - requires that Vim has been compiled with +clientserver")
    // val VimExecutable = SettingKey[String]("vim-executable", "The path to the vim executable, or just 'vim' if it's in the PATH already")
  }

  import QuickFixKeys._

  override def trigger = allRequirements

  override val projectSettings = Seq(
    QuickFixDirectory := ((target(_ / "quickfix"))).value,
    // vimEnableServer in ThisBuild := true,
    extraLoggers := {
      val currentFunction = extraLoggers.value
      (key: ScopedKey[_]) => {
        val loggers = currentFunction(key)
        val taskOption = key.scope.task.toOption
        if (taskOption.exists(_.label.startsWith("compile"))) {
          val logger: org.apache.logging.log4j.core.Appender =
            new QuickFixLogger(
              QuickFixDirectory.value / "sbt.quickfix", 
              "vim", //VimExecutable.value, 
              false) //VimEnableServer.value)
          logger.start()
          logger +: loggers
        }
        else
          loggers
      }
    },
    // testListeners <+= (quickFixDirectory, sources in Test, vimExecutable, vimEnableServer) map { (target, testSources, vimExec, enableServer) =>
    //   QuickFixTestListener(target / "sbt.quickfix", testSources, vimExec, enableServer)
    // },
    // quickFixInstall in ThisBuild <<= (vimPluginBaseDirectory, streams) map VimPlugin.install,
    // vimExecutable in ThisBuild := (if (System.getProperty("os.name").startsWith("Win")) "gvim.bat" else "gvim")
  )
}
