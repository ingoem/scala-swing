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
  protected final class Alignment(private[group] val wrapped: GroupLayout.Alignment)
  
  /** Elements are aligned along their baseline. Only valid along the vertical axis. */
  final val Baseline = new Alignment(GroupLayout.Alignment.BASELINE)
  
  /** Elements are centered inside the group. */
  final val Center = new Alignment(GroupLayout.Alignment.CENTER)
  
  /** Elements are anchored to the leading edge (origin) of the group. */
  final val Leading = new Alignment(GroupLayout.Alignment.LEADING)
  
  /** Elements are anchored to the trailing edge (end) of the group. */
  final val Trailing = new Alignment(GroupLayout.Alignment.TRAILING)
}
