/**
 * 
 */
package floatingpointdev.toolkit.UI;

/**
 * @author floatingpointdev
 *
 */
public class ViewMessage<IDTYPE> {
  IDTYPE componentID; 
  Object newValue;
  
  /**
   * @param componentID
   * @param newValue
   */
  public ViewMessage(IDTYPE componentID, Object newValue) {
    this.componentID = componentID;
    this.newValue = newValue;
  }
  /** @return the componentID */
  public IDTYPE getComponentID() {
    return componentID;
  }
  /** @param componentID the componentID to set */
  public void setComponentID(IDTYPE componentID) {
    this.componentID = componentID;
  }
  /** @return the newValue */
  public Object getNewValue() {
    return newValue;
  }
  /** @param newValue the newValue to set */
  public void setNewValue(Object newValue) {
    this.newValue = newValue;
  }


}
