/**
 * 
 */
package floatingpointdev.parameters;
import floatingpointdev.toolkit.util.*;
/**
 * @author floatingpointdev
 *
 */
public class KeyboardController implements IObservable{
  private Parameter parameter;
  
  KeyboardController(Parameter parameter){
    this.parameter = parameter;
  }
  
  
  // ---------- Keyboard -----------
  /** the amount to increment or decrement the parameter. */
  private float stepSize = .01f;
  /** the increment key on the keyboard. */
  private char incrementKey =  0;
  /** the decrement key on the keyboard. */
  private char decrementKey = 0;

  /** @return the stepSize. */
  public float getStepSize() {
    return stepSize;
  }

  /** @param stepSize the stepSize to set. */
  public void setStepSize(float stepSize) {
    this.stepSize = stepSize;
    observableComponent.notifyIObservers(this, "stepSize");
  }

  /*** @return the incrementKey.*/
  public char getIncrementKey() {
    return incrementKey;
  }

  /** @param incrementKey the incrementKey to set. */
  public void setIncrementKey(char incrementKey) {
    this.incrementKey = incrementKey;
    observableComponent.notifyIObservers(this, "incrementKey");
  }

  /** @return the decrementKey.*/
  public char getDecrementKey() {
    return decrementKey;
  }

  /** @param decrementKey the decrementKey to set.*/
  public void setDecrementKey(char decrementKey) {
    this.decrementKey = decrementKey;
    observableComponent.notifyIObservers(this, "decrementKey");
  }
  
  
  // ---------- IOBservable methods -----------
  private ObservableComponent observableComponent = new ObservableComponent();
  public void addIObserver(IObserver anIObserver){
    observableComponent.addIObserver(anIObserver);
  }
  
  public void deleteIObserver(IObserver anIObserver){
    observableComponent.deleteIObserver(anIObserver); 
  }
  
  public void deleteIObservers(){
    observableComponent.deleteIObservers();
  }

}
