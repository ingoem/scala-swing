package scala.swing.test.group

import scala.swing._
import scala.swing.group.GroupPanel

object GroupLayoutDemo extends SimpleSwingApplication {
  override def top = new MainFrame {
    title = "Find"

    contents = new GroupPanel {
      val label = new Label("Find what:")
      val textField = new TextField
      val caseCheckBox = new CheckBox("Match case")
      val wholeCheckBox = new CheckBox("Whole words")
      val wrapCheckBox = new CheckBox("Wrap around")
      val backCheckBox = new CheckBox("Search backwards")
      val findButton = new Button("Find")
      val cancelButton = new Button("Cancel")
      
      theHorizontalLayout is Sequential(
        label,
        Parallel(
          textField,
          Sequential(
            Parallel(caseCheckBox, wholeCheckBox),
            Parallel(wrapCheckBox, backCheckBox)
          )
        ),
        Spring(Unrelated),
        Parallel(findButton, cancelButton)
      )
      
      linkHorizontalSize(findButton, cancelButton)
      
      theVerticalLayout is Sequential(
        Parallel(Baseline)(label, textField, findButton),
        Parallel(
          Sequential(
            Parallel(Baseline)(caseCheckBox, wrapCheckBox),
            Parallel(Baseline)(wholeCheckBox, backCheckBox)
          ),
          cancelButton
        )
      )
    }
  }
}
