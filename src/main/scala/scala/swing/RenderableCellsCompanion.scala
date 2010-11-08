/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing

import scala.swing.event._

/**
* Describes the structure of a component's companion object where pluggable cell renderers must be supported.
* @author Ken Scambler
*/
trait RenderableCellsCompanion {
  type Renderer[A] <: CellRenderer[A]
  protected type Owner <: Component with CellView[_]
  
  val Renderer: CellRendererCompanion

  trait CellRendererCompanion {
    type Peer // eg. javax.swing.table.TableCellRenderer, javax.swing.tree.TreeCellRenderer
    type CellInfo
    val emptyCellInfo: CellInfo
    def wrap[A](r: Peer): Renderer[A]
    def apply[A, B: Renderer](f: A => B): Renderer[A]
  }

  trait CellRenderer[-A] extends Publisher  { 
    val companion: CellRendererCompanion
    def peer: companion.Peer
    def componentFor(owner: Owner, value: A, cellInfo: companion.CellInfo): Component
  }
}

