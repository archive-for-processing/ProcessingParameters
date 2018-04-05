/**
 * 
 */
package floatingpointdev.toolkit.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author floatingpointdev
 *
 */
public class XmlHelper {
  
  /**
   * Creates an element with a value and attaches it to a parent element.
   * 
   * @param parent The parent element to attach the created element to.
   * @param elementName The tag name of the element to create.
   * @param value the value for the element to create.
   * @return A reference to the element that was created.
   */
  public static Element createElementValuePair(Element parent, String elementName, String value){
    if((parent == null) || (elementName == null) || (value == null)){
      throw new IllegalArgumentException();
    }
    
    Element element = parent.getOwnerDocument().createElement(elementName);
    element.setTextContent(value);
    parent.appendChild(element);
    return element;
  }

  /**
   * Returns the text data associated with an element.
   * @param element The element that contains text data.
   * @return the text data of the element.  If no text
   * data is present then null is returned.
   */
  public static String getTextData(Element element) {
    String value;
    NodeList list = element.getChildNodes();
    for (int i = 0 ; i < list.getLength() ; i ++ ){
      value =((Node)list.item(i)).getNodeValue().trim();
      if((value.compareTo("") == 0) || (value.compareTo("\r") == 0)){
        continue; //keep iterating
      }else{
        return value;
      }
    }
    return null;
  }
  

}
