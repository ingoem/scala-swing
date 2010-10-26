/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

/** Provides baseline anchor constants for a `GroupPanel`. Baseline anchors
  * may either be explicitly specified (using the Parallel.baseline(...) factory)
  * or will be determined based on its elements: the baseline will be anchored 
  * to the bottom if and only if all the elements with a baseline, and that are
  * aligned to the baseline, have a baseline resize behavior of
  * `CONSTANT_DESCENT`.
  * 
  * @author Andreas Flierl
  */
trait BaselineAnchors {
  /** 
   * Allows to specify whether to anchor the baseline to the top or the bottom 
   * of a baseline-aligned parallel group.
   */
  protected class BaselineAnchor(private[group] val wrapped: Boolean)

  /** Anchor the baseline to the top of the group. */
  final val AnchorToTop = new BaselineAnchor(true)
  
  /** Anchor the baseline to the bottom of the group. */
  final val AnchorToBottom = new BaselineAnchor(false)
}