/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;
import floatingpointdev.parameters.KeyboardController;
import floatingpointdev.toolkit.UI.*;
import floatingpointdev.toolkit.util.IObserver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * The panel for viewing and editing the keyboard settings of a Parameter.
 * @author floatingpointdev
 *
 */
class ParameterSettingsUIKeyboard extends JPanel implements IObserver, ChangeListener, ActionListener{
  private KeyboardController keyboardController;
  private DecimalField stepSizeField;
  private NumberFormat stepSizeFieldFormat;
  private JTextField incrementKeyField = new JTextField(1);
  private JTextField decrementKeyField = new JTextField(1);
  private MinimalButton btnLearnIncrementKey = new MinimalButton("Learn");
  private MinimalButton btnLearnDecrementKey = new MinimalButton("Learn");
  
  /**
   * @param keyboardController the ParameterSettings object that this keyboard UI observes.
   */
  public ParameterSettingsUIKeyboard(KeyboardController keyboardController) {
    super();
    this.keyboardController = keyboardController;
    keyboardController.addIObserver(this);


    stepSizeField = new DecimalField(keyboardController.getStepSize(), 5);
    //stepSizeField.setHorizontalAlignment(JTextField.CENTER);
    //stepSizeField.setColumns(5);
    
    stepSizeField.addActionListener(this);
    incrementKeyField.addActionListener(this);
    decrementKeyField.addActionListener(this);
    
    setupUI();
  }
  
  

  /**
   * Build the keyboard settings Panel.
   */
  private void setupUI() {
    //setup the panel.
    //this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    this.setLayout(new BorderLayout());
    
    
//    Font f = getFont();
//    f=f.deriveFont(11f);
//   
//    //this.setFont(f);
//    btnLearnIncrementKey.setFont(f);
//    btnLearnIncrementKey.setMargin(new Insets(0, 0, 0,0));
    //btnLearnIncrementKey.setPreferredSize(new Dimension(50,10));
    
    
    //setup the components.
    
    
    //Layout The Components
    JPanel stepSizePanel = new JPanel();
    stepSizePanel.setLayout(new BoxLayout(stepSizePanel,BoxLayout.X_AXIS));
    stepSizePanel.add(new JLabel("Step Size:"));
    //stepSizeField.setMaximumSize(stepSizeField.getPreferredSize());
    stepSizePanel.add(stepSizeField);
    stepSizePanel.add(Box.createHorizontalGlue());
    
    
    JPanel incrementKeyPanel = new JPanel();
    incrementKeyPanel.setLayout(new BoxLayout(incrementKeyPanel,BoxLayout.X_AXIS));
    incrementKeyPanel.add(new JLabel("Increment Key:"));
    incrementKeyField.setMaximumSize(incrementKeyField.getPreferredSize());
    incrementKeyPanel.add(incrementKeyField); 
    incrementKeyPanel.add(btnLearnIncrementKey);
    incrementKeyPanel.add(Box.createHorizontalGlue());

    
    JPanel decrementKeyPanel = new JPanel();
    decrementKeyPanel.setLayout(new BoxLayout(decrementKeyPanel,BoxLayout.X_AXIS));
    decrementKeyPanel.add(new JLabel("Decrement Key:"));
    decrementKeyField.setMaximumSize(decrementKeyField.getPreferredSize());
    decrementKeyPanel.add(decrementKeyField); 
    decrementKeyPanel.add(btnLearnDecrementKey);
    decrementKeyPanel.add(Box.createHorizontalGlue());
    
    //add the components.
    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
    contents.add(stepSizePanel);
    contents.add(incrementKeyPanel);
    contents.add(decrementKeyPanel);
    add(contents,BorderLayout.NORTH);

  }


  //*************** Interface stuff ***************
  /** ID for the serializable interface. */
  private static final long serialVersionUID = -227198682380089733L;

  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    //make sure theObserved is an instance of ParameterSettings
    if(!(theObserved instanceof KeyboardController)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    KeyboardController theObservedTyped = (KeyboardController)theObserved;
    
    //make sure the change code is a string.
    if(!(changeCode instanceof String)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    String strChangeCode = (String)changeCode;
    
    
    //check the change code to see which element of the parameter has changed.
    if(strChangeCode.equalsIgnoreCase("stepSize")){
      float val = theObservedTyped.getStepSize();
      stepSizeField.setValue(val);

    } else if(strChangeCode.equalsIgnoreCase("incrementKey")){
      char val = theObservedTyped.getIncrementKey();
      incrementKeyField.setText(String.valueOf(val));
      
    } else if(strChangeCode.equalsIgnoreCase("decrementKey")){
      char val = theObservedTyped.getDecrementKey();
      decrementKeyField.setText(String.valueOf(val));

    }
    
  }

  /**
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  @Override
  public void stateChanged(ChangeEvent e) {

    
  }



  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent e) {
    Object objSource = e.getSource();
    if(objSource == stepSizeField){
      keyboardController.setStepSize((float)stepSizeField.getValue());
    }else if(objSource == incrementKeyField){
    
      char val = incrementKeyField.getText().charAt(0);
      keyboardController.setIncrementKey(val);
    } else if(objSource == decrementKeyField){
      keyboardController.setDecrementKey(decrementKeyField.getText().charAt(0));
    }
    
  }
  
  

}
