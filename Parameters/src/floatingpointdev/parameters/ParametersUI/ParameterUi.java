/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

import org.w3c.dom.Element;

import floatingpointdev.parameters.ControllerComponent;
import floatingpointdev.parameters.IControlled;
import floatingpointdev.parameters.IController;
import floatingpointdev.parameters.Parameter;
import floatingpointdev.parameters.ParameterManager;
import floatingpointdev.parameters.automationcontroller.ui.AutomationLane;
import floatingpointdev.toolkit.UI.MinimalButton;
import floatingpointdev.toolkit.util.IObserver;
/**
 * @author floatingpointdev
 *
 */
public class ParameterUi implements IController, ChangeListener, ActionListener {
  /** The parameter that this UI represents.*/
  private Parameter parameter;
  private ParameterManager parameterManager;
  private ParameterSettingsUI parameterSettingsUI;
  private JPanel parameterPanel = new JPanel();
  private JSlider sliderUserVal;
  private MinimalButton btnEdit = new MinimalButton();
  private static int SLIDER_MAXVALUE = 255;
  private SetSliderRunable setSliderRunable;
  
  public ParameterUi(ParameterManager parameterManager){

    this.parameterManager = parameterManager;
    this.parameter = parameterManager.getParameter();
    parameterSettingsUI = new ParameterSettingsUI(parameterManager);
    setSliderRunable = new SetSliderRunable(parameter.getNormalized());
    setupUI();
    //parameter.addIObserver(this);
    updateValFromParameter();
  }
  
  /**
   * Builds the user interface for the parameter.
   */
  private void setupUI(){
    JLabel labelParameterName = new JLabel(parameter.getParameterName());
    
    //setup the components:
    sliderUserVal = new JSlider();

    sliderUserVal.setMinimum(0);
    sliderUserVal.setMaximum(SLIDER_MAXVALUE);
    updateValFromParameter(); //set the slider to the value of the Parameter.
    sliderUserVal.addChangeListener(this);
    btnEdit.setText("edit");
    btnEdit.addActionListener(this);
    
    JPanel parameterSliderPanel = new JPanel();
    parameterSliderPanel.setLayout(new BoxLayout(parameterSliderPanel,BoxLayout.Y_AXIS));

    labelParameterName.setAlignmentX(Component.CENTER_ALIGNMENT);
    sliderUserVal.setAlignmentX(Component.CENTER_ALIGNMENT);
    btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    parameterSliderPanel.add(labelParameterName);
    parameterSliderPanel.add(sliderUserVal);
    parameterSliderPanel.add(btnEdit);
    
    parameterPanel.setLayout(new BorderLayout());
    parameterPanel.add(parameterSliderPanel, BorderLayout.WEST);
    //parameterPanel.add(new AutomationLane(parameterManager.getAutomationManager()), BorderLayout.CENTER);
  }
  /** 
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)
   */
//  @Override
//  public void update(Object theObserved, Object changeCode) {
//    
//    //make sure theObserved is an instance of Parameters
//    if(!(theObserved instanceof Parameter)){
//      //error condition, bail out.  
//      //DEBUG_ERROR
//      return;
//    }
//    Parameter paramTheObserved = (Parameter)theObserved;
//    
//    //make sure the change code is a string.
//    if(!(changeCode instanceof String)){
//      //error condition, bail out.  
//      //DEBUG_ERROR
//      return;
//    }
//    String strChangeCode = (String)changeCode;
//    
//    
//    //check the change code to see which element of the parameter has changed.
////    if(strChangeCode.equalsIgnoreCase("val")){
////      updateValFromParameter();
////    }
//  }
//  
  
  private void updateValFromParameter(){
    float val = parameter.getNormalized();
    //update the UI
    if(!SwingUtilities.isEventDispatchThread()){
      setSliderRunable.setValue(val);

      //Invoke later so that we dont have to wait for the
      //ui to update to continue.
      SwingUtilities.invokeLater(setSliderRunable); 

    } else {
      sliderUserVal.setValue((int)(val*SLIDER_MAXVALUE)); 
    }
  }
  
  
  /**
   * Returns true if p matches the parameter that this UI
   * is attached to.
   * 
   * @param p the parameter to test.
   * @return
   */
  public boolean isUIForParameter(ParameterManager p){
    if(p == this.parameterManager){
      return true;
    }
    return false;
  }

  /**
   * @return The Swing component that is the UI.
   */
  public JPanel getUiPanel() {
    return parameterPanel;
  }

  
  /**
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    Object objSource = e.getSource();
    if(objSource == sliderUserVal){
      float val = ((float)(sliderUserVal.getValue())) / (float)SLIDER_MAXVALUE;
      //parameter.setNormalized( val);
      if(!ignoreStateChange){
        controllerComponent.set(this, val);
      }
    } 
  }

  
  /**
   * 
   */
  public void destruct() {
    parameterSettingsUI.destruct();
    parameterSettingsUI = null;
    
  }

  
  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent e) {
    Object objSource = e.getSource();
    if(objSource == btnEdit){
      if(parameterSettingsUI.isVisible()){
        parameterSettingsUI.setVisible(false);
      }else{
        parameterSettingsUI.setVisible(true);
      }
    }
  }

  
  /**
   * Runnable class that for updating the value JSlider from a non
   * event dispatch thread.
   * @author Ryan Hieber (floatingpointdev@gmail.com)
   *
   */
  private class SetSliderRunable implements Runnable{
    private float val;
    
    /**
     * 
     * @param val the value that this class will set the slider to
     * when the run() method is invoked.
     */
    SetSliderRunable(float val){
      this.val = val;
    }
    
    /**
     * Sets the value that the slider should be set to when the run() method is
     * invoked.  
     * @param val the value that this class will set the slider to
     * when the run() method is invoked.
     */
    public void setValue(float val){
      this.val = val;
    }
    public void run(){
      ignoreStateChange = true;
      sliderUserVal.setValue((int)(val*SLIDER_MAXVALUE));
      ignoreStateChange = false;
    }
  }
  private boolean ignoreStateChange = false;

  private ControllerComponent controllerComponent = new ControllerComponent();
  /** @see floatingpointdev.parameters.IController#receiveEvent(float)*/
  @Override
  public void receiveEvent(float value) {
    //update the UI
    if(!SwingUtilities.isEventDispatchThread()){
      setSliderRunable.setValue(value);

      //Invoke later so that we dont have to wait for the
      //ui to update to continue.
      SwingUtilities.invokeLater(setSliderRunable); 

    } else {
      this.ignoreStateChange = true;
      sliderUserVal.setValue((int)(value*SLIDER_MAXVALUE)); 
      this.ignoreStateChange = false;
    }
  }

  /** @see floatingpointdev.parameters.IController#setControlled(floatingpointdev.parameters.IControlled)*/
  @Override
  public void setControlled(IControlled controlled) {
    controllerComponent.setControlled(controlled);
  }

  /** @see floatingpointdev.parameters.IController#saveToFile(org.w3c.dom.Element)*/
  @Override
  public void saveToFile(Element parentElement) {
    // TODO Auto-generated method stub
    
  }

  /** @see floatingpointdev.parameters.IController#readFromFile(org.w3c.dom.Element)*/
  @Override
  public void readFromFile(Element element) {
    // TODO Auto-generated method stub
    
  }
}
