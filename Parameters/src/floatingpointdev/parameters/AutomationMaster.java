/**
 * 
 */
package floatingpointdev.parameters;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;
import floatingpointdev.toolkit.util.XmlHelper;

/**
 * Master automation object that controls all automation managers.
 * 
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class AutomationMaster implements IObservable {
  private long                      startTime    = 0;
  private long                      endTime      = 20000;
  private long                      position     = 0;
  private boolean                   isRunning    = false;
  private Date                      lastTime     = new Date();
  private Timer                     timer;
  private AutomationMasterTimerTask timerTask;
  private static int                TIMER_PERIOD = 50;
  private TimeSnapModes             timeSnapMode = TimeSnapModes.SECONDS;
  
  enum TimeSnapModes {
    BPM,
    SECONDS
  }
  

  /**
   * 
   */
  public AutomationMaster() {
    super();
    timerTask = new AutomationMasterTimerTask();
  }


  public void play() {
    if (!isRunning()) {
      isRunning = true;
      lastTime = new Date();
      timer = new Timer("AutomationTimer");
      timerTask = new AutomationMasterTimerTask();
      timer.scheduleAtFixedRate(timerTask, new Date(), TIMER_PERIOD);
      observableComponent.notifyIObservers(this, "playStart");
    }
  }


  public void pause() {
    if(isRunning){
      isRunning = false;
      timerTask.cancel();
      timer.cancel();
      timer = null;
      observableComponent.notifyIObservers(this, "playStop");
    }
  }


  public boolean isRunning() {
    return isRunning;
  }


  public void setPosition(long time) {
    position = time;
    observableComponent.notifyIObservers(this, "position");
  }


  public void incrementtime() {
    if (isRunning()) {
      Date currentTime = new Date();
      long realTimeSinceLastIncrement = currentTime.getTime()
                                        - lastTime.getTime();
      setPosition(getPosition() + realTimeSinceLastIncrement);

    }
  }


  public long getPosition() {
    return position;
  }


  public long getEndTime() {
    return endTime;
  }


  public void setEndTime(long time) {
    endTime = time;
  }


  /** @param timeSnapMode the timeSnapMode to set */
  public void setTimeSnapMode(TimeSnapModes timeSnapMode) {
    this.timeSnapMode = timeSnapMode;
  }


  public long getStartTime() {
    return startTime;
  }


  public void setStartTime(long time) {
    startTime = time;
  }


  /** @return the timeSnapMode */
  public TimeSnapModes getTimeSnapMode() {
    return timeSnapMode;
  }


  public void timerCallback() {
    setPosition(position + TIMER_PERIOD);
  }

  private class AutomationMasterTimerTask extends TimerTask {
    /** @see java.util.TimerTask#run() */
    @Override
    public void run() {
      timerCallback();
    }
  }

  // ---------- IOBservable methods -----------
  private ObservableComponent observableComponent = new ObservableComponent();


  public void addIObserver(IObserver anIObserver) {
    observableComponent.addIObserver(anIObserver);
  }


  public void deleteIObserver(IObserver anIObserver) {
    observableComponent.deleteIObserver(anIObserver);
  }


  public void deleteIObservers() {
    observableComponent.deleteIObservers();
  }


  /**
   * @param element
   */
  public void readFromFile(Element element) {
    //verify that this is a MidiController node
    if(element.getNodeName().compareTo("AutomationMaster") != 0){
      return;
    }
    
    //get the values
    NodeList nodeLst = element.getChildNodes();
    String nodeName;
    for(int i = 0; i < nodeLst.getLength(); ++i){
      nodeName = nodeLst.item(i).getNodeName();
      if(nodeName.compareTo("endTime") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setEndTime(Long.parseLong(str));
        }
      } else if(nodeName.compareTo("timeSnapMode") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setTimeSnapMode(TimeSnapModes.valueOf(str));
        }
      } 
    }
  }
  

  /**
   * @param parentElement
   */
  public void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    
    Element thisElement = docOwner.createElement("AutomationMaster");
    XmlHelper.createElementValuePair(thisElement, "endTime", Long.toString(endTime));
    XmlHelper.createElementValuePair(thisElement, "timeSnapMode", getTimeSnapMode().toString());

    parentElement.appendChild(thisElement); 
  }
}
