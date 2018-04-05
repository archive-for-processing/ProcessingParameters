/**
 * 
 */
package floatingpointdev.parameters;



/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public interface IParameterController {
  public void recieveEvent(IParameterController source, float value);
  
  public void addController(IParameterController reciever);
  public void deleteController(IParameterController reciever);
  public void deleteControllers();
  public void transmitToRecievers(IParameterController source, float value);
  
}
