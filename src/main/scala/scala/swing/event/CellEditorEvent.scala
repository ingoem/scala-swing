package scala.swing.event

import scala.swing._

import javax.swing.event.CellEditorListener

trait CellEditorEvent[A] extends Event {
  val source: CellEditor[A]
}
case class CellEditingStopped[A](source: CellEditor[A]) extends CellEditorEvent[A]
case class CellEditingCancelled[A](source: CellEditor[A]) extends CellEditorEvent[A]
