/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala.swing.event

import scala.swing._

import javax.swing.event.CellEditorListener

trait CellEditorEvent[A] extends Event {
  val source: CellEditor[A]
}
case class CellEditingStopped[A](source: CellEditor[A]) extends CellEditorEvent[A]
case class CellEditingCancelled[A](source: CellEditor[A]) extends CellEditorEvent[A]
