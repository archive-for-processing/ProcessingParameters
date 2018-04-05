/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import floatingpointdev.parameters.MouseController;
import floatingpointdev.toolkit.util.IObserver;

/**
 * The panel for viewing and editing the mouse settings of a Parameter.
 * @author floatingpointdev
 *
 */
class ParameterSettingsUIMouse extends JPanel implements IObserver, ChangeListener{
  
  private MouseController mouseController;
  
  /**
   * @param parameterSettings
   */
  public ParameterSettingsUIMouse(MouseController mouseController) {
    super();
    this.mouseController = mouseController;
    
    setupUI();
  }

  /**
   * Build the mouse settings Panel.
   */
  private void setupUI() {
    //setup the components
    
    //Layout the components
    
    
    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
    //contents.add(new JLabel("Step Size:"));
    //contents.add(new JLabel("Increment:"));  
    //contents.add(new JLabel("Decrement:"));  
    contents.add(new JLabel("Not implemented yet..."));
    
    this.add(contents);
    
    
  }
  
  //*************** Interface stuff ***************
  
  /** ID for the Serializable interface.*/
  private static final long serialVersionUID = -3386105664943595550L;

  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    //make sure theObserved is an instance of ParameterSettings
    if(!(theObserved instanceof MouseController)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    MouseController theObservedTyped = (MouseController)theObserved;
    
    //make sure the change code is a string.
    if(!(changeCode instanceof String)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    String strChangeCode = (String)changeCode;
    
    
    //check the change code to see which element of the parameter has changed.
    if(strChangeCode.equalsIgnoreCase("mouseStepSize")){
      //float val = theObservedTyped.getStepSize();
      //update the UI

    } else if(strChangeCode.equalsIgnoreCase("mouseIncrementKey")){
      //char val = theObservedTyped.getIncrementKey();
      //update the UI
      
    } else if(strChangeCode.equalsIgnoreCase("mouseDecrementKey")){
      //char val = theObservedTyped.getDecrementKey();
      //update the UI

    }
    
  }

  /**
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    
  }
}
