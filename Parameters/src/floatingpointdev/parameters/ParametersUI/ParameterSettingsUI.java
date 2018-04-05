package floatingpointdev.parameters.ParametersUI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import floatingpointdev.parameters.Parameter;
import floatingpointdev.parameters.ParameterManager;
import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.util.IObserver;

/**
 * The UI that displays a Parameter's settings.
 * @author floatingpointdev
 */
class ParameterSettingsUI implements IObserver, ChangeListener{
  private JFrame parameterSettingsFrame;
  private ParameterManager parameterManager;
  private Parameter parameter;
  //private ParameterSettingsUIKeyboard keyboardTab;
  //private ParameterSettingsUIMouse mouseTab; 
  private MidiManagerUI midiTab;  
  //private ParameterSettingsUIOsc oscTab;    
  private JTabbedPane iOSettingsTabbedPane;
  
  /**
   * @param parameter the ParameterSettings object that this UI.
   * represents.
   */
  public ParameterSettingsUI(ParameterManager parameterManager) {
    this.parameterManager = parameterManager;
    this.parameter = parameterManager.getParameter();
    //parameter.addIObserver(this);

    setupUI();
  }


  
  /**
   *  Constructs the UI for ParameterSettings.
   */
  private void setupUI() {
    parameterSettingsFrame = new JFrame();
    parameterSettingsFrame.setTitle("Edit:" + parameter.getParameterName());

    //Setup the event handler for closing the window.
    parameterSettingsFrame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          //System.exit(0);
        }
      });
    
    //setup the components.
    //keyboardTab = new ParameterSettingsUIKeyboard(parameterManager.getKeyboardManager());
    //mouseTab = new ParameterSettingsUIMouse(parameterManager.getMouseManager());
    midiTab = new MidiManagerUI(parameterManager.getMidiController());
    //oscTab = new ParameterSettingsUIOsc(parameter.settings);
    iOSettingsTabbedPane = new JTabbedPane();
    iOSettingsTabbedPane.addTab("Midi", midiTab);
    //iOSettingsTabbedPane.addTab("Keyboard", keyboardTab);
    //iOSettingsTabbedPane.addTab("Mouse", mouseTab); 
    //iOSettingsTabbedPane.addTab("OSC", oscTab);
    iOSettingsTabbedPane.setSelectedIndex(0);
    //add the components
    parameterSettingsFrame.getContentPane().add(iOSettingsTabbedPane);
    
    parameterSettingsFrame.pack();
  }



  //*************** Interface stuff ***************
  /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
  @Override
  public void update(Object theObserved, Object changeCode) {
    //make sure theObserved is an instance of Parameters
    //if(!(theObserved instanceof ParameterSettings)){
      //error condition, bail out.  
      //DEBUG_ERROR
    //  return;
    //}
    //ParameterSettings paramTheObserved = (ParameterSettings)theObserved;
    
    //make sure the change code is a string.
    //if(changeCode instanceof String){
      //error condition, bail out.  
      //DEBUG_ERROR
    //  return;
    //}
    //String strChangeCode = (String)changeCode;
    
    
    //check the change code to see which element of the parameter has changed.
    //if(strChangeCode.equalsIgnoreCase("userval")){
      //float userVal = paramTheObserved.getUserVal();
      //update the UI
      //sliderUserVal.setValue((int)(userVal*10f));
    //}
    
  }

  /**
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    
  }





  /**
   * call this when destroying the ParameterSettingsUI.
   */
  public void destruct() {
    parameterSettingsFrame.setVisible(false);
    parameterSettingsFrame.removeAll();
  }



  /**
   * @return
   */
  public boolean isVisible() {
    return parameterSettingsFrame.isVisible();
  }



  /**
   * @param b
   */
  public void setVisible(boolean b) {
    parameterSettingsFrame.setVisible(b);
    
  }
 
}
