/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import javax.swing.LayoutStyle.ComponentPlacement

/** Provides placement constants for a `GroupPanel`.
  * 
  * @author Andreas Flierl
  */
trait Placements {
  /**
   * Specifies how two components are placed relative to each other.
   * 
   * @see javax.swing.LayoutStyle.ComponentPlacement
   */
  protected[Placements] sealed class Placement(
      private[group] val wrapped: ComponentPlacement)
  
  /**
   * Specifies if two components are related or not.
   * 
   * @see javax.swing.LayoutStyle.ComponentPlacement
   */    
  protected[Placements] final class RelatedOrUnrelated(
      cp: ComponentPlacement) extends Placement(cp)
  
  /** Used to request the distance between two visually related components. */
  final val Related = new RelatedOrUnrelated(ComponentPlacement.RELATED)
  
  /** Used to request the distance between two visually unrelated components. */
  final val Unrelated = new RelatedOrUnrelated(ComponentPlacement.UNRELATED)
  
  /**
   * Used to request the (horizontal) indentation of a component that is 
   * positioned underneath another component.
   */
  final val Indent = new Placement(ComponentPlacement.INDENT)
}
