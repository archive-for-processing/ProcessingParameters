/**
 * 
 */
package floatingpointdev.toolkit.UI;

import javax.swing.JTextField;

/**
 * @author floatingpointdev
 *
 */
public class TextField extends JTextField {

  public TextField(int columns){
    super(columns);
    //this.setMaximumSize(this.getPreferredSize());
    this.setHorizontalAlignment(JTextField.CENTER);
  }
}
