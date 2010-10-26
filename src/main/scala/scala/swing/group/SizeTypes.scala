/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import javax.swing.GroupLayout

/** Provides types and constants to ensure the correct use of pixel sizes and 
  * size hints in a `GroupLayout`. Integer values will be converted to a size 
  * type via an implicit conversion. These pixel sizes must always be >= 0. 
  * 
  * @author Andreas Flierl
  */
trait SizeTypes {
  /** Pixel size and all size hints. Used to specify sizes of a component and
    * the maximum size of `PreferredGap`s and `ContainerGap`s. 
    */
  sealed protected trait Size {
    private[group] def pixels: Int
  }

  /** Pixel size and `Infinite`. Used to specify the size of a `Gap`. */
  sealed protected trait GapSize extends Size
  
  /** Pixel size, `UseDefault` and `Infinite`. Used to specify the preffered
    * size of `PreferredGap`s and `ContainerGap`s.  
    */
  sealed protected trait PreferredGapSize extends Size
  
  sealed private[this] class SizeImpl(private[group] val pixels: Int) extends Size
  
  /** Instructs the layout to use a component's default size. */
  final val UseDefault: Size  with PreferredGapSize =
    new SizeImpl(GroupLayout.DEFAULT_SIZE) with PreferredGapSize
  
  /** Instructs the layout to use a component's preferred size. */
  final val UsePreferred: Size = new SizeImpl(GroupLayout.PREFERRED_SIZE)
  
  /** Represents an arbitrarily large size. */
  final val Infinite: Size with GapSize with PreferredGapSize = 
    new SizeImpl(Int.MaxValue) with GapSize with PreferredGapSize
  
  /** Implicitly converts an Int to a Size object when needed. 
    * 
    * @param pixels a size in pixels; must be >= 0
    */
  protected implicit def int2Size(pixels: Int): Size with GapSize with PreferredGapSize = {
    require(pixels >= 0, "size must be >= 0")
    new SizeImpl(pixels) with GapSize with PreferredGapSize
  }
}