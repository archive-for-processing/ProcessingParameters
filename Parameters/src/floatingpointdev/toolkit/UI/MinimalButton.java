/**
 * 
 */
package floatingpointdev.toolkit.UI;

import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

/**
 * @author floatingpointdev
 *
 */
public class MinimalButton extends JButton {

  public MinimalButton(){
    setup();
  }
  public MinimalButton(String text){
    super(text);
    setup();
  }
  
  private void setup(){
    
    //set the font to size 11.
    Font f = getFont();
    f=f.deriveFont(11f);
    setFont(f);
    
    //adjust the margin between the text and the edge of the
    //button.
    setMargin(new Insets(-1, 0, -1,0));
    
  
  }
}
