/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2007-2010, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */


package scala.swing.test


import scala.xml._
import scala.swing._
import Swing._
import scala.swing.event._
import Tree._
import java.awt.Color

object TreeDemo extends SimpleSwingApplication {
  import Tree._
  import java.io._

  object Data {
    // Contrived class hierarchy
    case class Customer(id: Int, title: String, firstName: String, lastName: String)
    case class Product(id: String, name: String, price: Double)
    case class Order(id: Int, customer: Customer, product: Product, quantity: Int) {
      def price = product.price * quantity
    }

    // Contrived example data
    val bob = Customer(1, "Mr", "Bob", "Baxter")
    val fred = Customer(2, "Dr", "Fred", "Finkelstein")
    val susan = Customer(3, "Ms", "Susan", "Smithers")
    val powerSaw = Product("X-123", "Power Saw", 99.95) 
    val nailGun = Product("Y-456", "Nail gun", 299.95)
    val boxOfNails = Product("Z-789", "Box of nails", 23.50)
    val orders = List(
      Order(1, fred, powerSaw, 1), 
      Order(2, fred, boxOfNails, 3),
      Order(3, bob, boxOfNails, 44),
      Order(4, susan, nailGun, 1))
      
    lazy val xmlDoc: Node = try {XML load resourceFromClassloader("/scala/swing/example/sample.xml")}
                            catch {case _ => <error> Error reading XML file. </error>}
  }

        
  // Common functionality for using files
  trait FileTree { this: Tree[File] =>
    treeData = TreeModel(new File(".")) {f => 
      if (f.isDirectory) f.listFiles.toSeq 
      else Seq()
    }
  
    renderer = new LabelRenderer({f => 
      val iconFile = "/scala/swing/example/" + (if (f.isDirectory) "folder.png" else "file.png")
      val iconURL = resourceFromClassloader(iconFile) ensuring (_ != null, "Couldn't find icon " + iconFile)
      (Icon(iconURL), f.getName)
    })
  }

  def top = new MainFrame {
    title = "Scala Swing Tree Demo"
  
    contents = new TabbedPane {
      import Data._
    
      // Use case 1: Show an XML document
      val xmlTree = new Tree[Node] {
        treeData = TreeModel(xmlDoc)(_.child filterNot (_.text.trim.isEmpty))
        renderer = Renderer(n => 
            if (n.label startsWith "#") n.text.trim 
            else n.label)
            
        expandAll()
      }

      
      // Use case 2: Show the filesystem with filter
      val fileSystemTree = new Tree[File] with FileTree {
        expandRow(0)
      }

      // Use case 3: Object graph containing diverse elements, reacting to clicks
      val objectGraphTree = new Tree[Any] {
        treeData = TreeModel[Any](orders: _*)({
          case o @ Order(_, cust, prod, qty) => Seq(cust, prod, "Qty" -> qty, "Cost" -> ("$" + o.price))
          case Product(id, name, price) => Seq("ID" -> id, "Name" -> name, "Price" -> ("$" + price))
          case Customer(id, _, first, last) => Seq("ID" -> id, "First name" -> first, "Last name" -> last)
          case _ => Seq.empty
        })

        renderer = Renderer({
          case Order(id, _, _, 1) => "Order #" + id
          case Order(id, _, _, qty) => "Order #" + id + " x " + qty
          case Product(id, _, _) => "Product " + id
          case Customer(_, title, first, last) => title + " " + first + " " + last
          case (field, value) => field + ": " + value
          case x => x.toString
        })
        
        listenTo(selection)
        reactions += {
          case TreeNodeSelected(node) => println("Selected: " + node)
        }
        
        expandAll()
      }
      
      // Use case 4: Infinitely deep structure
      val infiniteTree = new Tree(TreeModel(1000) {n => 1 to n filter (n % _ == 0)})
      infiniteTree expandRow 0
      
      // Use case 5: Editable file system
      val editableFileSystemTree = new Tree[File] with FileTree {
        treeData = treeData updatableWith { 
          (path, newValue) => val existing = path.last
            val renamedFile = new File(existing.getParent + File.separator + newValue.getName)
            existing renameTo renamedFile
            renamedFile
        }

        editor = Editor((_:File).getName, new File(_:String))
        expandRow(0)
      }

      import TabbedPane.Page
      import BorderPanel.Position._
      
      def northAndCenter(north: Component, center: Component) = new BorderPanel {
        layout(north) = North
        layout(center) = Center
      }
      
      pages += new Page("1: XML file", new ScrollPane(xmlTree))
      pages += new Page("2: File system", new ScrollPane(fileSystemTree))
      pages += new Page("3: Diverse object graph", new ScrollPane(objectGraphTree))
      pages += new Page("4: Infinite structure", new ScrollPane(infiniteTree))
      pages += new Page("5: Editable file system", northAndCenter(
        new Label("Warning! Editing will actually rename files.") {foreground = Color.red}, 
        new ScrollPane(editableFileSystemTree)))

    }
    
    size = (800, 600): Dimension
  }
}