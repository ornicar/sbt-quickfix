package com.dscleaver.sbt.quickfix

import sbt._
import sbt.internal.util.{ ObjectEvent, StringEvent }

import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.message.{ Message, ObjectMessage, ReusableObjectMessage }

object QuickFixLogger {
  def write(output: File, prefix: String, message: String): Unit =
    IO.append(output, "[%s] %s\n".format(prefix, message))

  def write(output: File, prefix: String, file: File, line: Int, message: String): Unit =
    write(output, prefix, "%s:%d: %s".format(file, line, message))
}

class QuickFixLogger(val output: File, vimExec: String, enableServer: Boolean)
  extends AbstractAppender("VimQuickFix", null, PatternLayout.createDefaultLayout()) {

  import QuickFixLogger._
  // import VimInteraction._

  override def append(event: LogEvent): Unit = {
    val level = sbt.internal.util.ConsoleAppender toLevel event.getLevel
    log(level, formatMessage(event.getMessage))
  }

  private def log(level: Level.Value, message: => String): Unit = level match {
    case Level.Info => handleInfoMessage(message)
    case Level.Error => handleErrorMessage(message)
    case Level.Warn => handleWarnMessage(message)
    case _ => handleDebugMessage(message)
  }

  private def handleDebugMessage(message: String): Unit = ()
  // if (enableServer && message.toLowerCase.contains("compilation failed")) {
  //   val _ = call(vimExec, "<esc>:cfile %s<cr>".format(output.toString))
  // }

  private def handleInfoMessage(message: String): Unit = {
    if (message startsWith "Compiling") {
      IO.delete(output)
      IO.touch(List(output))
    }
    else ()
  }

  private def handleErrorMessage(message: String): Unit = write(output, "error", message)

  private def handleWarnMessage(message: String): Unit = write(output, "warn", message)

  private def formatMessage(msg: Message): String = msg match {
    case o: ObjectMessage => formatMessageContent(o.getParameter)
    case o: ReusableObjectMessage => formatMessageContent(o.getParameter)
    case _ => msg.getFormattedMessage
  }

  private def formatMessageContent(o: AnyRef): String =
    o match {
      case x: StringEvent => x.message
      case x: ObjectEvent[_] =>
        val full = x.message.toString
        if (full startsWith "[") full dropWhile (' ' != _) drop 1
        else full
      case x => x.toString
    }
}
