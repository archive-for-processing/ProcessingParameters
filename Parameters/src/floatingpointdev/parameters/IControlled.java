/**
 * 
 */
package floatingpointdev.parameters;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public interface IControlled {
  public void set(IController source, float value);
  public void addController(IController controller);
  public void removeController(IController controller);
  public void removeAllControllers();
}
