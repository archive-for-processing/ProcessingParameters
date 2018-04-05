/**
 * 
 */
package floatingpointdev.parameters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import floatingpointdev.parameters.ParametersUI.ParameterUi;
import floatingpointdev.parameters.automationcontroller.AutomationController;
import floatingpointdev.toolkit.midi.IoMidi;

/**
 * Class used to coordinate a parameter, and the various classes that
 * control it. ie MidiController, (and eventually) OscManager, 
 * KeyboardController, MouseController.
 * @author floatingpointdev
 *
 */
public class ParameterManager{
  private Parameter parameter;
  private ParameterUi parameterUi;
  private MidiController midiController;

  //private AutomationController automationController;
  //OscController oscController;
  private IoMidi ioMidi;
  //private AutomationMaster automationMaster;
  
  ParameterManager(String parameterName, IoMidi ioMidi, AutomationMaster automationMaster){
   // setAutomationMaster(automationMaster);
    parameter = new Parameter(parameterName);

    this.ioMidi = ioMidi;
    midiController = new MidiController(ioMidi);
    //automationController = new AutomationController(automationMaster);
    parameterUi = new ParameterUi(this);
    
    //attach the controllers
    midiController.setControlled(parameter);
    parameter.addController(midiController);
    //automationController.setControlled(parameter);
    //parameter.addController(automationController);

    parameterUi.setControlled(parameter);
    parameter.addController(parameterUi);
  }
  
 // public void setAutomationMaster(AutomationMaster automationMaster){
 //   this.automationMaster = automationMaster;
 // }
  
  public Parameter getParameter(){
    return this.parameter;
  }
  
  
  /**
   * 
   * @return the name of the parameter that this ParameterManager controls.
   * Returns null if no parameter is assigned.
   */
  public String getParameterName(){
    if(this.parameter != null){
      return this.parameter.getParameterName();
    }
    return null;
  }

  
  public ParameterUi getParameterUi(){
    return parameterUi;
  }
  /**
   * 
   * @return
   */
  public MidiController getMidiController(){
    return this.midiController;
  }
  

//  /**
//   * @return
//   */
//  public MouseController getMouseManager() {
//    return mouseManager;
//  }
//
//
//  /**
//   * @return
//   */
//  public KeyboardController getKeyboardManager() {
//    return this.keyboardManager;
//  }
  
  
//  public AutomationController getAutomationController(){
//    return this.automationController;
//  }
//  
  
  
  
  public void saveToFile(Element parentElement){
    Document docOwner = parentElement.getOwnerDocument();
    Element elemParameterManager = docOwner.createElement("Parameter");
    elemParameterManager.setAttribute("name", getParameterName());
    
    //Tell the parameter to save to file.
    this.getParameter().saveToFile(elemParameterManager);
    
    parentElement.appendChild(elemParameterManager);
    //Save ParameterManager settings to xml file.
      //nothing to save yet.
    
    //Save midi settings.
    getMidiController().saveToFile(elemParameterManager);
    
    //save AutomationSettings
//    automationController.saveToFile(elemParameterManager);
    
    //Save OSC settings.
    //TODO: OSC settings save to file.
  }


  /**
   * @param element
   */
  public void readFromFile(Element element) {
    //verify that this is a ParameterManager node
    if(element.getNodeName().compareTo("Parameter") != 0){
      return;
    }
    NodeList nodeLst = element.getElementsByTagName("ParameterSettings");  
    if(nodeLst.getLength() > 0){
      if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
        parameter.readFromFile((Element)nodeLst.item(0));
      }
    }
    
    nodeLst = element.getElementsByTagName("MidiController");  
    if(nodeLst.getLength() > 0){
      if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
        midiController.readFromFile((Element)nodeLst.item(0));
      }
    }
    
    nodeLst = element.getElementsByTagName("AutomationController");  
    if(nodeLst.getLength() > 0){
      if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
        //automationController.readFromFile((Element)nodeLst.item(0));
      }
      
    }
  }
}