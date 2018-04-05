/**
 * 
 */
package floatingpointdev.parameters;

import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;

/**
 * @author floatingpointdev
 *
 */
public class MouseController implements IObservable{
  private Parameter parameter;
  
  MouseController(Parameter parameter){
    this.parameter = parameter;
  }
  
  // ---------- Mouse -----------
  /** defines the sources of mouse clicks for modifying the Parameter. */
  enum MouseClickSources {
    BUTTONLEFT, BUTTONRIGHT, BUTTONMIDDLE, BUTTON3, BUTTON4, BUTTON5, SCROLLWHEELUP, SCROLLWHEELDOWN, NONE
  };

  /** defines the sources of mouse coordinates for modifying the Parameter. */
  enum MouseCoordinateSources {
    MOUSEX, MOUSEY, MOUSECLICKX, MOUSECLICKY, NONE
  };

  /** the amount to increment or decrement the parameter. */
  private float mouseStepSize;

  /** the increment key on the mouse. */
  private MouseClickSources mouseIncrementKey = MouseClickSources.NONE;
  
  /** the decrement key on the mouse. */
  private  MouseClickSources mouseDecrementKey = MouseClickSources.NONE;

  /** the selected source for mouse coordinates. */
  private MouseCoordinateSources mouseCoordinateSource = MouseCoordinateSources.NONE;
  
  
  /** @return the mouseStepSize. */
  public float getMouseStepSize() {
    return mouseStepSize;
  }

  /** @param mouseStepSize  the mouseStepSize to set.*/
  public void setMouseStepSize(float mouseStepSize) {
    this.mouseStepSize = mouseStepSize;
    observableComponent.notifyIObservers(this, "mouseStepSize");
  }

  /** @return the mouseIncrementKey. */
  public MouseClickSources getMouseIncrementKey() {
    return mouseIncrementKey;
  }

  /** @param mouseIncrementKey the mouseIncrementKey to set. */
  public void setMouseIncrementKey(MouseClickSources mouseIncrementKey) {
    this.mouseIncrementKey = mouseIncrementKey;
    observableComponent.notifyIObservers(this, "mouseIncrementKey");
  }

  /** @return the mouseDecrementKey*/
  public MouseClickSources getMouseDecrementKey() {
    return mouseDecrementKey;
  }

  /** @param mouseDecrementKey the mouseDecrementKey to set*/
  public void setMouseDecrementKey(MouseClickSources mouseDecrementKey) {
    this.mouseDecrementKey = mouseDecrementKey;
    observableComponent.notifyIObservers(this, "mouseDecrementKey");
  }

  /** @return the mouseCoordinateSource */
  public MouseCoordinateSources getMouseCoordinateSource() {
    return mouseCoordinateSource;
  }

  /**@param mouseCoordinateSource the mouseCoordinateSource to set.*/
  public void setMouseCoordinateSource(
      MouseCoordinateSources mouseCoordinateSource) {
    this.mouseCoordinateSource = mouseCoordinateSource;
    observableComponent.notifyIObservers(this, "mouseCoordinateSource");
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
