/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala.swing

import scala.swing.event._
import javax.swing.{CellEditor => JCellEditor, AbstractCellEditor => JAbstractCellEditor}
import javax.swing.event.{CellEditorListener, ChangeEvent}

/**
* Describes the structure of a component's companion object where pluggable cell editors must be supported.
* @author Ken Scambler
*/
trait EditableCellsCompanion {
  type Editor[A] <: CellEditor[A]
  protected type Owner <: Component with CellView[_]
  
  val Editor: CellEditorCompanion
  

  trait CellEditorCompanion {
    type Peer <: JCellEditor
    type CellInfo
    val emptyCellInfo: CellInfo
    def wrap[A](e: Peer): Editor[A]
    def apply[A, B: Editor](toB: A => B, toA: B => A): Editor[A]
  }
  
  trait CellEditor[A] extends Publisher with scala.swing.CellEditor[A] {
    val companion: CellEditorCompanion
    def peer: companion.Peer

    protected def fireCellEditingCancelled() {publish(CellEditingCancelled(CellEditor.this))}
    protected def fireCellEditingStopped() {publish(CellEditingStopped(CellEditor.this))}

    protected def listenToPeer(p: JCellEditor) {
      p.addCellEditorListener(new CellEditorListener {
        override def editingCanceled(e: ChangeEvent) {fireCellEditingCancelled()}
        override def editingStopped(e: ChangeEvent) {fireCellEditingStopped()}
      })
    }

    abstract class EditorPeer extends JAbstractCellEditor {
      override def getCellEditorValue(): AnyRef = value.asInstanceOf[AnyRef]
      listenToPeer(this)
    }

    def componentFor(owner: Owner, value: A, cellInfo: companion.CellInfo): Component
    
    def cellEditable = peer.isCellEditable(null)
    def shouldSelectCell = peer.shouldSelectCell(null)
    def cancelCellEditing() = peer.cancelCellEditing
    def stopCellEditing() = peer.stopCellEditing
  }
}