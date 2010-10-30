/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import javax.swing.GroupLayout

/** Provides alignment constants for parallel groups in a `GroupPanel`.
  * 
  * @author Andreas Flierl
  */
trait Alignments {
  /**
   * Represents an alignment of a component (or group) within a parallel group.
   * 
   * @see javax.swing.GroupLayout.Alignment
   */
  protected sealed class Alignment(private[group] val wrapped: GroupLayout.Alignment)
  
  /** Elements are aligned along their baseline. Only valid along the vertical axis. */
  object Baseline extends Alignment(GroupLayout.Alignment.BASELINE)
  
  /** Elements are centered inside the group. */
  object Center extends Alignment(GroupLayout.Alignment.CENTER)
  
  /** Elements are anchored to the leading edge (origin) of the group. */
  object Leading extends Alignment(GroupLayout.Alignment.LEADING)
  
  /** Elements are anchored to the trailing edge (end) of the group. */
  object Trailing extends Alignment(GroupLayout.Alignment.TRAILING)
}
