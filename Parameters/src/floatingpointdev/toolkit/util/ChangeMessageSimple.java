/**
 * 
 */
package floatingpointdev.toolkit.util;

/**
 * @author floatingpointdev
 *
 */
public class ChangeMessageSimple {
  private Object changeCode;
  private Object newValue;
  
  public ChangeMessageSimple(Object changeCode, Object newValue){
    this.changeCode = changeCode;
    this.newValue = newValue;
  }
  
  /** @return the changeCode */
  public Object getChangeCode() {
    return changeCode;
  }
  
  /** @param changeCode the changeCode to set */
  public void setChangeCode(Object changeCode) {
    this.changeCode = changeCode;
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
