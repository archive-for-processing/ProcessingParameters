package floatingpointdev.parameters;


import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;
import floatingpointdev.toolkit.util.XmlHelper;

/**
 * A Parameter Object is simply a variable that holds a floating point
 * value that is bounded by a minimum and a maximum.  
 * @author floatingpointdev
 *
 */
public class Parameter implements IObservable, IControlled{

  private float min = 0;
  private float max = 1;
  /** The internal value of the Parameter. Should always be in the range (0-1).*/
  private float val = 0;
  private String parameterName = "Parameter";
  
  
  /** 
   * Gets the minimum possible value for this Parameter.
   * @return the value of this Parameter at its minimum. */
  public float getMin() {
    return min;
  }

  
  /**
   * Gets the maximum possible value of this Parameter.
   *  @return the value of this Parameter at its maximum. */
  public float getMax() {
    return max;
  }

  
  /**
   * Create a new Parameter with a given name.
   * @param parameterName the name that the Parameter should have.
   */
  public Parameter(String parameterName){
    this.parameterName = parameterName;
  }
  
  
  /**
   * Create a new Parameter with a given name and an initial value.
   * @param parameterName the name that the Parameter should have.
   * @param initialValNormalized the initial value for the parameter
   * in the range(0-1).
   */
  public Parameter(String parameterName, float initialValNormalized){
    this.parameterName = parameterName;
    setNormalized(initialValNormalized);
  }
  
  
  /**
   * Create a new Parameter with a given name, an initial value,
   * a minimum value, and a maximum value.  
   * @param parameterName
   * @param initialVal the initial value for the parameter.  This must be 
   * a value between min and max.
   * @param min The minimum value of the parameter.
   * @param max The maximum value of the parameter.
   */
  public Parameter(String parameterName, float initialVal, float valMin, float valMax){
    this.parameterName = parameterName;
    setMin(valMin);
    setMax(valMax);
    set(initialVal);
  }
  
  
/**
 * Gets the value of the Parameter.  
 * @return the current value of the Parameter.  This value will be between min and max.
 */
  public float get(){
    //return val;
    return (val * (max-min)) + min;
  }
  
  
  /**
   * Returns the value of this parameter normalized between 0 and 1.
   * @return the value of this parameter normalized between 0 and 1.
   * if min is 0 and max is 1, then getVal() and getValNormalized return
   * the same thing.
   */
  public float getNormalized(){
    return val;
  }
  
  
  /**
   * Set the value of this Parameter.
   * @param val the value to set this Parameter to.  Note that the value is bounded
   * by min and max.  So if you attempt to set the value above max then 
   * the value will simply be set to max. Likewise for min.
   */
  public void set(float val){
    setNormalized((val-min)/(max-min));
  }
  
  
  /**
   * Sets the value of this Parameter using the range (0-1).  So if you want to set
   * the Parameter to 75% you can call setValNormalized(.75). 
   * @param val the normalized value to set the parameter to. acceptable range: (0-1)
   * If min is 0 and max is 1, then setVal() and setValNormalized function exactly
   * the same.
   */
  public void setNormalized(float val){
    if(val > 1){
      this.val = 1;
    } else if(val < 0){
      this.val = 0;
    } else {
      this.val = val;
    }
    observableComponent.notifyIObservers(this, "val");
    //set(null, this.val);
    //parameterControllers.
  }
  

  
  /**
   * Sets the minimum and maximum values that the Parameter's value can have.
   * min must be less than max.  If min is greater or equal to max
   * then the function fails and does nothing.
   * @param min the minimum value for this Parameter. 
   * @param max the maximum value for this Parameter.
   */
  public void setMinAndMax(float min, float max){
    if(min < max){
      if(this.min != min){
        this.min = min;
        observableComponent.notifyIObservers(this, "min");
      }
      if(this.max != max){
        this.max = max;
        observableComponent.notifyIObservers(this, "max");
      }
    }
  }
  
  
  /**
   * Set the minimum value for this Parameter.  Default min is 0.
   * @param min the minimum value for this Parameter.  Note that if you attempt to set
   * min above max then min will not be changed.
   */
  public void setMin(float min){
    if(min <= max){
      this.min = min;
      if(min > val){
        set(this.min);
      }
    }
	  observableComponent.notifyIObservers(this, "min");
  }
  
  
  /**
   * Set the maximum value for this Parameter.  Default max is 1.
   * @param max the maximum value for this Parameter.  Note that if you
   * attempt to set max below min then max will not be changed.
   */
  public void setMax(float max){
    if(max >= this.min){
      this.max = max;
      if(max < val){
        set(this.max);
      }
    }
	observableComponent.notifyIObservers(this, "max");
  }
  
//  public float getUserVal(){
//    return userVal;
//  }
  
//  public void setUserVal(float userVal){
//    this.userVal = userVal;
//    observableComponent.notifyIObservers(this, "userval");
//    recalculateVal();
//  }
//  
//  private void recalculateVal(){
//    setVal(userVal);
//  }

  
  /** 
   * Gets the name of this Parameter.
   * @return String that is the parameter's name. */
  public String getParameterName() {
    return parameterName;
  }
  
  
  /**
   * @param parentElement the parent Element in the save file DOM that
   * this class will attach its own element too.
   */
  void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    Element elemParameter = docOwner.createElement("ParameterSettings");
    XmlHelper.createElementValuePair(elemParameter, "ValueMin", Float.toString(min));
    XmlHelper.createElementValuePair(elemParameter, "ValueMax", Float.toString(max));  
    XmlHelper.createElementValuePair(elemParameter, "Value", Float.toString(val));  
    parentElement.appendChild(elemParameter);
  }

  
  /**
   * Reads the settings for this Parameter from an XML element.
   * @param element the XML element that holds the values for this parameter.
   */
  void readFromFile(Element element) {
    //verify that this is a ParameterSettings node
    if(element.getNodeName().compareTo("ParameterSettings") != 0){
      return;
    }
    
    //get the values
    NodeList nodeLst = element.getChildNodes();
    String nodeName;
    for(int i = 0; i < nodeLst.getLength(); ++i){
      nodeName = nodeLst.item(i).getNodeName();
      if(nodeName.compareTo("ValueMin") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMin(Float.parseFloat(str));
        }
      } else if(nodeName.compareTo("ValueMax") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMax(Float.parseFloat(str));
        }
      } else if(nodeName.compareTo("Value") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setNormalized(Float.parseFloat(str));
        }
      }
    }
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


  ArrayList<IController> parameterControllers = new ArrayList<IController>();
  public void set(IController source, float value){
    setNormalized(value);
    for(IController each: parameterControllers){
      if(each != source){
        each.receiveEvent(value);
      }
    }
  }
  
  /** @see floatingpointdev.parameters.IControlled#addController(floatingpointdev.parameters.IController)*/
  @Override
  public void addController(IController controller) {
    parameterControllers.add(controller);
  }


  /** @see floatingpointdev.parameters.IControlled#removeController(floatingpointdev.parameters.IController)*/
  @Override
  public void removeController(IController controller) {
    parameterControllers.remove(controller);
  }


  /** @see floatingpointdev.parameters.IControlled#removeAllControllers()*/
  @Override
  public void removeAllControllers() {
    parameterControllers.clear();
  }
}
