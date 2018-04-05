/**
 * 
 */
package floatingpointdev.parameters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import floatingpointdev.toolkit.util.XmlHelper;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public interface IController {

  public void receiveEvent(float value);
  public void setControlled(IControlled controlled);

  /**
   * Write out this controllers settings as a DOM Document
   * and attach it to parentElement.
   * @param parentElement the parent DOM element that this Controller
   * should add its element to.
   */
  public void saveToFile(Element parentElement);
  


  /**
   * Reads the settings for this Controller from an XML element.
   * @param element the XML element that contains a Controller.
   */
  public void readFromFile(Element element);
}
