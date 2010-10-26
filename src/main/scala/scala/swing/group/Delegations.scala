/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import swing._
import javax.swing.GroupLayout
import javax.swing.SwingConstants

/** Several methods that delegate directly to the underlying `GroupLayout`.
  *  
  * @author Andreas Flierl
  */
trait Delegations {
  val layout: GroupLayout
  
  /**
   * The component will not take up any space when it's invisible (default).
   */
  def honorVisibilityOf(comp: Component) =
    layout.setHonorsVisibility(comp.peer, true)
    
  /**
   * The component will still take up its space even when invisible.
   */
  def ignoreVisibilityOf(comp: Component) =
    layout.setHonorsVisibility(comp.peer, false)
  
  /**
   * Links the sizes (horizontal and vertical) of several components.
   * 
   * @param comps the components to link
   */
  def linkSize(comps: Component*) = layout.linkSize(comps.map(_.peer): _*)
  
  /**
   * Links the sizes of several components horizontally.
   * 
   * @param comps the components to link
   */
  def linkHorizontalSize(comps: Component*) =
    layout.linkSize(SwingConstants.HORIZONTAL, comps.map(_.peer): _*)
    
  /**
   * Links the sizes of several components vertically.
   * 
   * @param comps the components to link
   */
  def linkVerticalSize(comps: Component*) =
    layout.linkSize(SwingConstants.VERTICAL, comps.map(_.peer): _*)
  
  /**
   * Replaces one component with another. Great for dynamic layouts.
   * 
   * @param existing the component to be replaced
   * @param replacement the component replacing the existing one
   */
  def replace(existing: Component, replacement: Component) =
    layout.replace(existing.peer, replacement.peer)
}