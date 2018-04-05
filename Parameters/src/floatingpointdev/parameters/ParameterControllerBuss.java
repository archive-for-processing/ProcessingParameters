/**
 * 
 */
package floatingpointdev.parameters;

import java.util.ArrayList;

/**
 * 
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class ParameterControllerBuss implements IParameterController{
  private ArrayList<IParameterController> parameterControllers;
  private IParameterController lastSource;

  /** @see floatingpointdev.parameters.IParameterController#receiveEvent(floatingpointdev.parameters.IParameterController, java.lang.Object)*/
  @Override
  public void recieveEvent(IParameterController source, float value) {
    lastSource = source;
    transmitToRecievers(this, value); 
  }
  /** @see floatingpointdev.parameters.IParameterController#addController(floatingpointdev.parameters.IParameterController)*/
  @Override
  public void addController(IParameterController reciever) {
    parameterControllers.add(reciever);
    
  }
  
  /** @see floatingpointdev.parameters.IParameterController#deleteController(floatingpointdev.parameters.IParameterController)*/
  @Override
  public void deleteController(IParameterController reciever) {
    parameterControllers.remove(reciever);
    
  }
  /** @see floatingpointdev.parameters.IParameterController#deleteControllers()*/
  @Override
  public void deleteControllers() {
    parameterControllers.clear();
  }
  /** @see floatingpointdev.parameters.IParameterController#transmitToReceivers(floatingpointdev.parameters.IParameterController, java.lang.Object)*/
  @Override
  public void transmitToRecievers(IParameterController source,
      float value) {
    for(IParameterController each: parameterControllers){
      if(each != lastSource){
        each.recieveEvent(source, value);
      }
    }
  }
  
  
  
}
