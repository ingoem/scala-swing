/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.group

import javax.swing.GroupLayout
import javax.swing.GroupLayout._

/** Provides the definitions for parallel and vertical grouping.
  * 
  * @author Andreas Flierl
  */
trait Groups 
    extends Resizing
    with BaselineAnchors { this: GroupPanel =>
    
  /**
   * Elements with this trait may only appear in groups corresponding to the 
   * type `A`.
   * 
   * @tparam A the type of the group: sequential, parallel or both
   */
  protected trait InGroup[A <: G] {
    private[group] def build(parent: A): G
  }
  
  /** Elements of this type may only appear inside sequential groups. */
  private[group] type InSequential = InGroup[GroupLayout#SequentialGroup]
  
  /** Elements of this type may only appear inside parallel groups. */
  private[group] type InParallel = InGroup[GroupLayout#ParallelGroup]
  
  /**
   * A group of components, either parallel or sequential.
   */
  protected trait Group {
    private[group] def buildChildren: G
  }
      
  /**
   * Generic superclass for sequential and parallel groups.
   * 
   * @tparam A the type of the (swing layout) group that surrounds the elements
   *        of this group
   */
  private class GroupImpl[A <: G](elems: List[InGroup[A]], 
      val peerGroup: A) extends Group {
        
    override private[group] def buildChildren = {
      elems.foreach(_.build(peerGroup))
      peerGroup
    }
  }
  
  /** Implicit conversion that puts a group into the correct context on demand. */
  protected final implicit def groupInSequential(grp: Group) =
    new GroupInSequential(grp, None)
  
  /** Implicit conversion that puts a group into the correct context on demand. */
  protected final implicit def groupInParallel(grp: Group) =
    new GroupInParallel(grp, None)
      
  /**
   * Wraps a group of components to extend it with actions only allowed inside
   * a sequential group.
   * 
   * @see javax.swing.GroupLayout.SequentialGroup
   */
  protected class GroupInSequential(wrapped: Group, 
      useAsBaseline: Option[Boolean]) extends InSequential {
    override private[group] def build(parent: GroupLayout#SequentialGroup) =
      if (useAsBaseline.isDefined) parent.addGroup(useAsBaseline.get, wrapped.buildChildren)
      else parent.addGroup(wrapped.buildChildren)
    
    /** 
     * Use this group to calculate the baseline for the surrounding group. Only
     * one member of that group should be marked this way.
     */  
    def asBaseline = new GroupInSequential(wrapped, Some(true))
    
    /** Do not use this group to calculate the baseline for the surrounding group. */
    def notAsBaseline = new GroupInSequential(wrapped, Some(false))
  }
  
  /**
   * Wraps a group of components to extend it with actions only allowed inside
   * a parallel group.
   * 
   * @see javax.swing.GroupLayout.ParallelGroup
   */
  protected class GroupInParallel(wrapped: Group, align: Option[Alignment]) extends InParallel {
    override private[group] def build(parent: GroupLayout#ParallelGroup) =
      if (align.isDefined) parent.addGroup(align.get.wrapped, wrapped.buildChildren)
      else parent.addGroup(wrapped.buildChildren)
    
    /** 
     * Specifies an alignment for this component.
     * 
     * @param newAlign the alignment to use
     */
    def aligned(newAlign: Alignment) = new GroupInParallel(wrapped, Some(newAlign))
  }
  
  /**
   * Declares a sequential group, i.e. a group whose child components appear
   * one after another within the available space.
   * 
   * @see GroupLayout.SequentialGroup
   */
  protected object Sequential {
    /** 
     * Creates a new sequential group of the specified elements.
     * 
     * @param elems the elements to group
     * @see GroupLayout#createSequentialGroup
     */
    def apply(elems: InSequential*): Group = new GroupImpl(elems.toList,
      layout.createSequentialGroup)
  }
  
  /**
   * Declares a parallel group, i.e. a group whose child components share the
   * same space. Because some components may (and will) be bigger than others,
   * an alignment can be specified to resolve placement of the components
   * relative to each other.
   * 
   * @see GroupLayout.ParallelGroup
   */
  protected object Parallel {
    /** 
     * Creates a new parallel group of the specified elements. The default
     * alignment is `leading`.
     * 
     * @param elems the elements to group
     * @see GroupLayout#createParallelGroup()
     */
    def apply(elems: InParallel*): Group = 
      new GroupImpl(elems.toList, layout.createParallelGroup)
    
    /** 
     * Creates a new parallel group of the specified elements that aligns them
     * in the specified way.
     * 
     * @param align how to align the elements
     * @param elems the elements to group
     * @see GroupLayout#createParallelGroup(GroupLayout.Alignment)
     */
    def apply(align: Alignment)(elems: InParallel*): Group =
      new GroupImpl(elems.toList, layout.createParallelGroup(align.wrapped))
    
    /** 
     * Creates a new parallel group of the specified elements that aligns them
     * in the specified way and is resizable or not.
     * 
     * @param align how to align the elements
     * @param resizability whether the group is resizable or of fixed size
     *        (default: resizable)
     * @param elems the elements to group
     * @see GroupLayout#createParallelGroup(GroupLayout.Alignment, boolean)
     */
    def apply(align: Alignment, resizability: Resizability = Resizable)(elems: InParallel*): Group =
      new GroupImpl(elems.toList, 
                   layout.createParallelGroup(align.wrapped, resizability.wrapped))
    
    /** 
     * Creates a new parallel group of the specified elements that aligns them
     * along their baseline, has the specified resizing behaviour and anchors
     * the baseline to the top or the bottom of the group.
     * 
     * @param resizability whether the group is resizable or of fixed size
     *        (default: resizable)
     * @param anchor whether to anchor the baseline to the top or the bottom of 
     *        the group
     * @param elems the elements to group
     * @see GroupLayout#createBaselineGroup(boolean, boolean)
     */
    def baseline(resizability: Resizability = Resizable, 
                 anchor: BaselineAnchor)(elems: InParallel*): Group =
      new GroupImpl(elems.toList, layout.createBaselineGroup(resizability.wrapped, 
        anchor.wrapped))
  }
}