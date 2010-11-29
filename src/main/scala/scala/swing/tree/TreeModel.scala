package scala.swing
package tree

import javax.swing.event.{TreeModelListener, TreeModelEvent}
import javax.swing.tree.{MutableTreeNode, TreeNode, DefaultTreeModel, TreePath, TreeModel => JTreeModel}
import scala.reflect.ClassManifest
import scala.collection.mutable.ListBuffer
import Tree.Path
import scala.swing.event._

object TreeModel {
  
  /**
   * This value is the root node of every TreeModel's underlying javax.swing.tree.TreeModel.  As we wish to support multiple root 
   * nodes in a typesafe manner, we need to maintain a permanently hidden dummy root to hang the user's "root" nodes off.
   */
  private[tree] case object hiddenRoot
  
  /*
  class Node[A](private var userObj: A) {
    def children = ListBuffer[A]()
    def userObject = userObj
    def userObject_=(a: A) {userObj = a}
  }*/
  
  
  def empty[A] = new TreeModel[A](Seq.empty, _ => Seq.empty) // Needs to be a method rather than a val, because A is invariant.
  def apply[A](roots: A*)(children: A => Seq[A]) = new TreeModel(roots, children)
}

/**
 * Represents tree data as a sequence of root nodes, and a function that can retrieve child nodes.  
 */
class TreeModel[A](val roots: Seq[A], 
                   children: A => Seq[A]) {
  self =>
  
  import TreeModel._
  
  def filter(p: A => Boolean) = new TreeModel[A](roots filter p, a => children(a) filter p)
  
  def foreach[U](f: A => U): Unit = depthFirstIterator foreach f
  
  /** 
   * A function to update a value in the model, at a given path.  By default this will throw an exception; to 
   * make a TreeModel updatable, call updatableWith() to provide a new TreeModel with the specified update method.
   */
  protected val updateFunc: (Path[A], A) => A = {
    (_,_) => error("Update is not supported on this tree")
  }

  def getChildrenOf(parent: A): Seq[A] = children(parent)
  
  def update(path: Path[A], newValue: A) {
    val existing = path.last
    val result = updateFunc(path, newValue)

    // If the result is actually replacing the node with a different reference object, then 
    // fire "tree structure changed".
    if (existing.isInstanceOf[AnyRef] && (existing.asInstanceOf[AnyRef] ne result.asInstanceOf[AnyRef])) {
      peer.fireTreeStructureChanged(pathToTreePath(path), result)
    }
    // If the result is a value type or is a modification of the same node reference, then
    // just fire "nodes changed".
    else {
      peer.fireNodesChanged(pathToTreePath(path), result)
    }
  }

  /**
   * Returns a new TreeModel that is updatable with the given function. The returned TreeModel will have 
   * the same roots and child function.
   */
  def updatableWith(updater: (Path[A], A) => A): TreeModel[A] = new TreeModel(roots, children) {
    override val updateFunc = updater
    this.peer.treeModelListeners foreach self.peer.addTreeModelListener
  }

  
  /**
   * Underlying tree model that exposes the tree structure to Java Swing.
   *
   * This implementation of javax.swing.tree.TreeModel takes advantage of its abstract nature, so that it respects 
   * the tree shape of the underlying structure provided by the user.
   */
  lazy val peer = new JTreeModel {
    private val treeModelListenerList = ListBuffer[TreeModelListener]()

    private def getChildrenOf(parent: Any) = parent match {
      case `hiddenRoot` => roots
      case a: A => children(a)
    }
    
    def getChild(parent: Any, index: Int): AnyRef = {
      val ch = getChildrenOf(parent)
      if (index >= 0 && index < ch.size) 
        ch(index).asInstanceOf[AnyRef] 
      else 
        error("No child of \"" + parent + "\" found at index " + index)
    }
    def getChildCount(parent: Any): Int = getChildrenOf(parent).size
    def getIndexOfChild(parent: Any, child: Any): Int = getChildrenOf(parent) indexOf child
    def getRoot(): AnyRef = hiddenRoot
    def isLeaf(node: Any): Boolean = getChildrenOf(node).isEmpty
    
    
    def treeModelListeners: Seq[TreeModelListener] = treeModelListenerList
    
    def addTreeModelListener(tml: TreeModelListener) {
      treeModelListenerList += tml
    }
    
    def removeTreeModelListener(tml: TreeModelListener) {
      treeModelListenerList -= tml
    }
    
    def valueForPathChanged(path: TreePath, newValue: Any) {
      update(treePathToPath(path), newValue.asInstanceOf[A])
    }
    
    private def createEvent(path: TreePath, newValue: Any) = new TreeModelEvent(this, path, 
        Array(getChildrenOf(path.getPath.last) indexOf newValue), 
        Array(newValue.asInstanceOf[AnyRef]))
    
    def fireTreeStructureChanged(path: TreePath, newValue: Any) {
      treeModelListenerList foreach (_.treeStructureChanged(createEvent(path, newValue)))
    }
    
    def fireNodesChanged(path: TreePath, newValue: Any) {
      treeModelListenerList foreach (_.treeNodesChanged(createEvent(path, newValue)))
    }
    
    def fireNodesInserted(path: TreePath, newValue: Any, index: Int) {
      treeModelListenerList foreach (_.treeNodesInserted(createEvent(path, newValue)))
    }
  }

  def pathToTreePath(path: Tree.Path[A]) = {
    val array = (hiddenRoot :: path).map(_.asInstanceOf[AnyRef]).toArray(ClassManifest.Object)
    new TreePath(array)
  }
  
  def treePathToPath(tp: TreePath): Tree.Path[A] = {
    if (tp == null) null 
    else tp.getPath.map(_.asInstanceOf[A]).toList.tail
  }   
  
  /**
   * Iterates sequentially through each item in the tree, either in breadth-first or depth-first ordering, 
   * as decided by the abstract pushChildren() method.
   */
  private trait TreeIterator extends Iterator[A] {
    protected var openNodes: Iterator[A] = roots.iterator

    def pushChildren(item: A): Unit
    def hasNext = openNodes.nonEmpty
    def next() = if (openNodes.hasNext) {
      val item = openNodes.next
      pushChildren(item)
      item
    }
    else error("No more items")
  }
  
  def breadthFirstIterator: Iterator[A] = new TreeIterator {
    override def pushChildren(item: A) {openNodes ++= children(item).iterator}
  }
  
  def depthFirstIterator: Iterator[A] = new TreeIterator {
    override def pushChildren(item: A) {
      val open = openNodes
      openNodes = children(item).iterator ++ open // ++'s argument is by-name, and should not directly pass in a var
    }
  }
  
  def size = depthFirstIterator.size
  
}

