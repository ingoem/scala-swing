/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scala.swing
import collection._


/**
* Describes components that have a concept of a "cell", each of which contains a value, may be selected, 
* and may support pluggable Renderers and Editors.
*
* @author Ken Scambler
*/
trait CellView[+A] {
  this: Component =>
  def editable: Boolean 
  def cellValues: Iterator[A]
  
  /**
  * Provides common functionality for the `selection` object found in CellView implementation.  Each 
  * will have one or more selection sets based on different types of cell coordinate, such as row, 
  * column, index or tree path.  All events published from `selection` objects will derive from 
  * scala.swing.event.SelectionEvent.
  */
  trait CellSelection extends Publisher {
    /**
    * Allows querying and modification of the current selection state, for some unique coordinate S.
    * There may be more than one selection set supporting different coordinates, such as rows and columns.
    */
    protected abstract class SelectionSet[S](a: => Seq[S]) extends mutable.Set[S] { 
      def -=(s: S): this.type 
      def +=(s: S): this.type
      def --=(ss: Seq[S]): this.type 
      def ++=(ss: Seq[S]): this.type
      override def size = nonNullOrEmpty(a).length
      def contains(s: S) = nonNullOrEmpty(a) contains s
      def iterator = nonNullOrEmpty(a).iterator
      protected def nonNullOrEmpty[A](s: Seq[A]) = if (s != null) s else Seq.empty
    }
    
    /**
    *  Returns an iterator that traverses the currently selected cell values.
    */
    def cellValues: Iterator[A]
    
    /**
    * Whether or not the current selection is empty.
    */
    def empty: Boolean
    
    /**
    * Returns the number of cells currently selected.
    */
    def count: Int
  } 
  
  val selection: CellSelection
}

/**
* This should be mixed in to CellView implementations that support pluggable renderers.
* @author Ken Scambler
*/
trait RenderableCells[A] {
  this: CellView[A] =>
  val companion: RenderableCellsCompanion
  def renderer: companion.Renderer[A]
}

/**
* This should be mixed in to CellView implementations that support pluggable editors.
* @author Ken Scambler
*/
trait EditableCells[A]  {
  this: CellView[A] =>
  val companion: EditableCellsCompanion
  def editor: companion.Editor[A]
}