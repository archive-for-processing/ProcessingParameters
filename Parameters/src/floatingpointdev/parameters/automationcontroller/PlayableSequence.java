/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.XmlHelper;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class PlayableSequence extends Sequence{//implements IObservable {
  // private AutomationMaster automationMaster;
  //private Sequence                             sequence                   = this;
  private ArrayList<IPlayableSequenceReceiver> receivers                  = new ArrayList<IPlayableSequenceReceiver>();
  private boolean                              isPlaying                  = false;
  private boolean                              isReadEnabled              = false;
  private boolean                              isWriteEnabled             = false;
  private boolean                              isRecording                = false;

  private SequenceEvent                        sequenceEventPlayhead;
  private long lastPosition = 0;
  private long currentPosition = 0;
  // private UUID e1, e2, e3;
  long                                         x1                         = -1;
  long                                         x2                         = -1;

  float                                        y1                         = -1;
  float                                        y2                         = -1;
  float                                        timeOfLastReceivedValue    = -1;
//  private ObservableComponent observableComponent = new ObservableComponent();
//  private Deque<Sequence> undoStack;
  /**
   * @param automationMaster
   */
  public PlayableSequence() {
    super();
     this.addIObserver(new IObserver(){

      @Override
      public void update(Object theObserved, Object changeCode) {
        // the sequence changed.
        resetSequenceIterator(currentPosition);
        if (isReadEnabled()) {
          sendCurrentValue();
        }
      }
     });
     resetSequenceIterator(0);
  }



  void addPlayableSequenceReceiver(IPlayableSequenceReceiver receiver) {
    receivers.add(receiver);
  }


  private void resetSequenceIterator(long position) {
    sequenceEventPlayhead = null;
    seekRight(position);
  }


  private void seekRight(long position) {
    currentPosition = position;
    if (this.size() == 0) {
      // if sequence is empty we can just return;
      sequenceEventPlayhead = null;
      return;
    }
    if (sequenceEventPlayhead == null) {
      sequenceEventPlayhead = this.getFirst();
      if (sequenceEventPlayhead.getTime() > position) {
        sequenceEventPlayhead = null;
        return;
      }
    }

    while (sequenceEventPlayhead.next() != null) {
      if (sequenceEventPlayhead.getTime() >= position) {
        return;
      }
      sequenceEventPlayhead = sequenceEventPlayhead.next();
    }
  }


  private void seekLeft(long position) {
    currentPosition = position;

    if (this.size() == 0) {
      // if sequence is empty we can just return;
      sequenceEventPlayhead = null;
      return;
    }
    while (sequenceEventPlayhead != null) {
      if (sequenceEventPlayhead.getTime() <= position) {
        return;
      }
      sequenceEventPlayhead = sequenceEventPlayhead.previous();
    }
  }


  private void seekToPosition(long position) {
    if (this.size() == 0) {
      // if sequence is empty we can just return;
      sequenceEventPlayhead = null;
      return;
    }
    long playHeadTime = 0;
    if (sequenceEventPlayhead != null) {
      playHeadTime = sequenceEventPlayhead.getTime();
    }

    // we are at the new position
    if (playHeadTime == position) {
      return;
    }

    // newPosition is to the left
    if (playHeadTime > position) {
      seekLeft(position);
    }

    // newPosition is to the right
    if (playHeadTime < position) {
      seekRight(position);
    }
  }

  private void onPositionChanged() {
    // if we are recording
    if (isRecording()) {
      long startTimeToDelete = 0;
      if (sequenceEventPlayhead != null) {
        startTimeToDelete = lastPosition+1;
      }
      long endTimeToDelete = currentPosition;

      this.remove(startTimeToDelete, endTimeToDelete);
      seekToPosition(currentPosition);
    } else {
      seekToPosition(currentPosition);
    }

    if (isReadEnabled() && !(isRecording())) {
      seekToPosition(currentPosition);
      this.sendCurrentValue();
    }
  }
  
  
//  public void onSequenceChanged(SequenceChangeCodes changeCode) {
//    // the sequence changed.
//    resetSequenceIterator(currentPosition);
//    if (isReadEnabled()) {
//      this.sendCurrentValue();
//    }
//  }


  public void pause() {
    isPlaying = false;
    isRecording = false;
    if (isWriteEnabled) {
      x1 = -1;
      x2 = -1;
      // e1 = null;
      // e2 = null;
    }
  }


  public void play() {
    isPlaying = true;
  }


  private void startRecording(){
    this.pushUndoRestorePoint();
    isRecording = true;
    // e2 = leftNode.getUuid();
    x2 = -1;// leftNode.getEvent().getTime();
    y2 = -1;// leftNode.getEvent().getValue();
  }
 
  public void receiveEvent(float value) {

    // the parameter changed value.
    // if we are recording

    if (isWriteEnabled && isPlaying()) {
      // if we are playing, and write is enabled, and we have now received
      // the first event, then we can start actually recording.
      if (isRecording() == false) {
        startRecording();

      }
      if (timeOfLastReceivedValue == currentPosition) {
        return;
      }
      timeOfLastReceivedValue = currentPosition;
      if (isRecording()) {
        // add the new node.
        // //first check to see that the node has changed value enough to
        // warrant adding.
        // //if the new point is close to the line drawn between left and right
        // then we can
        // //filter it out and reduce the total number of events.
        //

        if ((x1 != -1) && (x2 != -1)) {
          float x3 = currentPosition;
          float y3 = value;
          float n12Slope = (y2 - y1) / (x2 - x1);
          // float n13Slope = (y3 - y1) / (x3 - x1);
          float n23Slope = (y3 - y2) / (x3 - x2);

          // float n13Distance = (float) Math.sqrt((x3 - x1) * (x3 - x1)
          // + (y3 - y1) * (y3 - y1));
          // float n23Distance = (float) Math.sqrt((x2 - x1) * (x2 - x1)
          // + (y2 - y1) * (y2 - y1));
          float n23DistanceScaled = (float) Math.sqrt((x2 - x1) * (x2 - x1)
                                                      + 10000 * (y2 - y1)
                                                      * 10000 * (y2 - y1));

          //float distance = calcDistance(x1, x2, x3, y1, y2, y3);// *
                                                                // n13Distance;
          // good if((n23Distance < 500)){
          // good if((n23DistanceScaled < 200){
          // good if((distance * n23DistanceScaled) < 5){
          if ((Math.abs(n12Slope - n23Slope) < .0003)
              && (n23DistanceScaled < 2000)) {
            this.remove(this.getEventClosestTo(x2, y2, 0, 0));
            seekToPosition(currentPosition);

            // x1 = x2;
            // y1 = y2;
            x2 = currentPosition;
            y2 = value;

          } else {
            x1 = x2;
            y1 = y2;
            x2 = currentPosition;
            y2 = value;
          }
        } else {
          x1 = x2;
          y1 = y2;
          x2 = currentPosition;
          y2 = value;
        }

        this.add(new Event(currentPosition, value));

        // seek so that the newly added node is to the left.
        seekToPosition(currentPosition);
      }
    }

  }
//
//
//  private float calcDistance(float x1, float x2, float x3, float y1, float y2,
//      float y3) {
//    double distance;
//    double xPrime;
//    double yPrime;
//
//    x1 = x1 - x1;
//    x2 = x2 - x1;
//    x3 = x3 - x1;
//
//    y1 = y1 - y1;
//    y2 = y2 - y1;
//    y3 = y3 - y1;
//
//    double m1;
//    double m2;
//
//    m1 = (y2 - y1) / (x2 - x1);
//    m2 = (y3 - y1) / (x3 - x1);
//
//    xPrime = (x2 + m1 * m2 * x2) / (m2 * m2 + 1);
//    yPrime = -xPrime / m2 + x2 / m2 + m1 * x2;
//    distance = Math.sqrt((xPrime - x2) * (xPrime - x2) + (yPrime - y2)
//                         * (yPrime - y2));
//
//    return (float) distance;
//  }


  void removePlayableSequenceReceiver(IPlayableSequenceReceiver receiver) {
    receivers.remove(receiver);
  }


  private void sendToPlayableSequenceReceivers(float value) {
    for (IPlayableSequenceReceiver each : receivers) {
      each.receiveValue(value);
    }
  }


  // public void recordEnable() {
  // isWriteEnabled = true;
  // }
  
  
  
  private void sendCurrentValue(){
  
    float newValue = 0;
    if (sequenceEventPlayhead == null) {
      if (this.getFirst() != null) {
        // if position is before our first event.
        newValue = this.getFirst().getValue();
      }
    } else {
  
      if (sequenceEventPlayhead.next() == null) {
        // if position is after our last event.
        newValue = sequenceEventPlayhead.getValue();
      } else {
        // position is between our playhead and the next event.
        if ((sequenceEventPlayhead != null)
            && (sequenceEventPlayhead.next() != null)) {
          long timeLeft = sequenceEventPlayhead.getTime();
          long timeRight = sequenceEventPlayhead.next().getTime();
          float valLeft = sequenceEventPlayhead.getValue();
          float valRight = sequenceEventPlayhead.next().getValue();
  
  
          float valueDifference = valRight - valLeft;
          float scale = ((float) (currentPosition - timeLeft))
                        / (float) (timeRight - timeLeft);
  
          newValue = valLeft + (valueDifference * scale);
        }
      }

    }
    this.sendToPlayableSequenceReceivers(newValue);
  }


  public void setPosition(long position) {
    lastPosition = currentPosition;
    currentPosition = position;
    onPositionChanged();
  }


  /** @param isReadEnabled the isReadEnabled to set */
  public void setReadEnabled(boolean isReadEnabled) {
    this.isReadEnabled = isReadEnabled;
  }


  /** @param isWriteEnabled the isWriteEnabled to set */
  public void setWriteEnabled(boolean isWriteEnabled) {
    this.isWriteEnabled = isWriteEnabled;
  }


  


  // public void recordEnable() {
  // isWriteEnabled = true;
  // }
  
  
  
  /** @return whether or not the sequence is playing */
  public boolean isPlaying() {
    return isPlaying;
  }


  /** @return the isPlaying */
  public boolean isReadEnabled() {
    return isReadEnabled;
  }


  // /** @param isPlaying the isPlaying to set */
  // public void setPlaying(boolean isPlaying) {
  // this.isPlaying = isPlaying;
  // }


  /** @return the isRecording */
  public boolean isRecording() {
    return isRecording;
  }


  /** @return the isWriteEnabled */
  public boolean isWriteEnabled() {
    return isWriteEnabled;
  }



  /**
   * @param element
   */
  public void readFromFile(Element element) {
    //verify that this is a PlayableSequence node
    if(element.getNodeName().compareTo("PlayableSequence") != 0){
      return;
    }
    
    //get the values
    NodeList nodeLst = element.getChildNodes();
    String nodeName;
    for(int i = 0; i < nodeLst.getLength(); ++i){
      nodeName = nodeLst.item(i).getNodeName();
      if(nodeName.compareTo("readEnabled") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setReadEnabled(Boolean.parseBoolean(str));
        }
      } else if(nodeName.compareTo("writeEnabled") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setWriteEnabled(Boolean.parseBoolean(str));
        }
      
      }
      nodeLst = element.getElementsByTagName("Sequence");  
      if(nodeLst.getLength() > 0){
        if(nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE){
          super.readFromFile((Element)nodeLst.item(0));
        }
      }
    }
  }
  

  /**
   * @param thisElement
   */
  public void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    
    Element thisElement = docOwner.createElement("PlayableSequence");
    XmlHelper.createElementValuePair(thisElement, "readEnabled", Boolean.toString(isReadEnabled()));
    XmlHelper.createElementValuePair(thisElement, "writeEnabled", Boolean.toString(isWriteEnabled()));
    
    super.saveToFile(thisElement);
    parentElement.appendChild(thisElement);
  }



//
//  /** @see floatingpointdev.toolkit.util.IObservable#addIObserver(floatingpointdev.toolkit.util.IObserver)*/
//  @Override
//  public void addIObserver(IObserver anIObserver) {
//    observableComponent.addIObserver(anIObserver);
//  }
//
//  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObserver(floatingpointdev.toolkit.util.IObserver)*/
//  @Override
//  public void deleteIObserver(IObserver anIObserver) {
//    observableComponent.deleteIObserver(anIObserver);
//  }
//
//
//  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObservers()*/
//  @Override
//  public void deleteIObservers() {
//    observableComponent.deleteIObservers();
//  }

//
//  public int size() {
//    return sequence.size();
//  }
//
//
//  /**
//   * @return
//   * @see floatingpointdev.parameters.automationcontroller.Sequence#iterator()
//   */
//  public SequenceIterator iterator() {
//    return sequence.iterator();
//  }
//  
  
}
