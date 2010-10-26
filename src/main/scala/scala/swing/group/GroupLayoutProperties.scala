/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import javax.swing.GroupLayout

/** Property wrappers for `GroupLayout`'s setters and getters.
  * 
  * @author Andreas Flierl
  */
trait GroupLayoutProperties {
  def layout: GroupLayout
  
  /** Indicates whether gaps between components are automatically created. */
  def autoCreateGaps = layout.getAutoCreateGaps
  
  /** Sets whether gaps between components are automatically created. */
  def autoCreateGaps_=(flag: Boolean) = layout.setAutoCreateGaps(flag)
  
  /** 
   * Indicates whether gaps between components and the container borders are 
   * automatically created. 
   */
  def autoCreateContainerGaps = layout.getAutoCreateContainerGaps
  
  /** 
   * Sets whether gaps between components and the container borders are 
   * automatically created. 
   */
  def autoCreateContainerGaps_=(flag: Boolean) = 
    layout.setAutoCreateContainerGaps(flag)
  
  /** Returns the layout style used. */
  def layoutStyle = layout.getLayoutStyle
  
  /** Assigns a layout style to use. */
  def layoutStyle_=(style: javax.swing.LayoutStyle) = layout.setLayoutStyle(style)
  
  /** 
   * Indicates whether the visibilty of components is considered for the layout.
   * If set to `false`, invisible components still take up space.
   * Defaults to `true`.
   */
  def honorsVisibility = layout.getHonorsVisibility
  
  /**
   * Sets whether the visibilty of components should be considered for the 
   * layout. If set to `false`, invisible components still take up 
   * space. Defaults to `true`. 
   */
  def honorsVisibility_=(flag: Boolean) =
    layout.setHonorsVisibility(flag)
}