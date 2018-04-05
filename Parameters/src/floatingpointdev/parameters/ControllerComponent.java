/**
 * 
 */
package floatingpointdev.parameters;

import org.w3c.dom.Element;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class ControllerComponent implements IController {

  private IControlled controlled;
  /** @see floatingpointdev.parameters.IController#receiveEvent(float)*/
  @Override
  public void receiveEvent(float value) {
    // TODO Auto-generated method stub

  }

  /** @see floatingpointdev.parameters.IController#setControlled(floatingpointdev.parameters.IControlled)*/
  @Override
  public void setControlled(IControlled controlled) {
    this.controlled = controlled;
  }
  
  public void set(IController source, float value){
    if(controlled != null){
      controlled.set(source, value);
    }
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
