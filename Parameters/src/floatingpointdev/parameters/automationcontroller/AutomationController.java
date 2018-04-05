/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import floatingpointdev.parameters.AutomationMaster;
import floatingpointdev.parameters.ControllerComponent;
import floatingpointdev.parameters.IControlled;
import floatingpointdev.parameters.IController;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.IReceiver;
import floatingpointdev.toolkit.util.XmlHelper;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class AutomationController implements IController {


  private ControllerComponent controllerComponent = new ControllerComponent();
  private PlayableSequence    playableSequence    = new PlayableSequence();
  private AutomationMaster    automationMaster;


  /**
   * @param automationMaster
   */
  public AutomationController(AutomationMaster automationMaster) {
    super();
    this.automationMaster = automationMaster;
    automationMaster.addIObserver(new AutomationMasterListener());
    playableSequence.addPlayableSequenceReceiver(new PlayableSequenceReceiver(this));
  }


  /** @see floatingpointdev.parameters.IController#receiveEvent(float) */
  @Override
  public void receiveEvent(float value) {
    playableSequence.receiveEvent(value);

  }


  /** @see floatingpointdev.parameters.IController#setControlled(floatingpointdev.parameters.IControlled) */
  @Override
  public void setControlled(IControlled controlled) {
    controllerComponent.setControlled(controlled);

  }

  private class PlayableSequenceReceiver implements IPlayableSequenceReceiver {
    private AutomationController automationController;


    /**
     * @param automationController
     */
    public PlayableSequenceReceiver(AutomationController automationController) {
      super();
      this.automationController = automationController;
    }


    /** @see floatingpointdev.parameters.automationcontroller.IPlayableSequenceReceiver#receiveValue(float) */
    @Override
    public void receiveValue(float value) {
      controllerComponent.set(automationController, value);

    }


  }

  private class AutomationMasterListener implements IObserver {

    /**
     * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public void update(Object theObserved, Object changeCode) {
      if (changeCode == "position") {
        playableSequence.setPosition(automationMaster.getPosition());
      }
      if (changeCode == "playStart") {
        playableSequence.play();
      }
      if (changeCode == "playStop") {
        playableSequence.pause();

      }
    }


  }

  /**
   * @return
   */
  public AutomationMaster getAutomationMaster() {
    return automationMaster;
  }

  public PlayableSequence getSequence(){
    return playableSequence;
  }


  /**
   * @param isReadEnabled
   */
  public void setReadEnabled(boolean isReadEnabled) {
    playableSequence.setReadEnabled(isReadEnabled);
  }


  /**
   * @param isWriteEnabled
   */
  public void setWriteEnabled(boolean isWriteEnabled) {
    playableSequence.setWriteEnabled(isWriteEnabled);

  }


  /** @see floatingpointdev.parameters.IController#saveToFile(org.w3c.dom.Element)*/
  @Override
  public void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    Element thisElement = docOwner.createElement("AutomationController");

    parentElement.appendChild(thisElement);

    automationMaster.saveToFile(thisElement);
    playableSequence.saveToFile(thisElement);
    
  }


  /** @see floatingpointdev.parameters.IController#readFromFile(org.w3c.dom.Element)*/
  @Override
  public void readFromFile(Element element) {
    //verify that this is a MidiController node
    if(element.getNodeName().compareTo("AutomationController") != 0){
      return;
    }
   
    NodeList nodeLst = element.getElementsByTagName("AutomationMaster");  
    if(nodeLst.getLength() > 0){
      if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
        automationMaster.readFromFile((Element)nodeLst.item(0));
      }
    }
    
    nodeLst = element.getElementsByTagName("PlayableSequence");  
    if(nodeLst.getLength() > 0){
      if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
        playableSequence.readFromFile((Element)nodeLst.item(0));
      }
    }
  }
}
