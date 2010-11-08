/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing

/**
* Common superclass of cell editors.
* @author Ken Scambler
*/
trait CellEditor[A] extends Publisher {
  def peer: AnyRef
  def value: A
  def cellEditable: Boolean
  def shouldSelectCell: Boolean
  def cancelCellEditing(): Unit
  def stopCellEditing(): Boolean
}


