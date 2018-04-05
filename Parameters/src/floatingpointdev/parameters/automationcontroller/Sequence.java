/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;


import java.util.ArrayList;
import java.util.Collection;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;
import floatingpointdev.toolkit.util.XmlHelper;

/**
 * An ordered set of Event objects.
 * 
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class Sequence implements IObservable, Iterable<SequenceEvent> {

  public enum SequenceChangeCodes {
    EVENT_ADDED,
    EVENT_REMOVED,
    EVENT_TIME_CHANGED,
    EVENT_VALUE_CHANGED
  }

  private int               size            = 0;
  private static int        NUMBER_OF_UNDOS = 20;
  private LinkedList<ArrayList<Event>> undoStack = new LinkedList<ArrayList<Event>>();
  private LinkedList<ArrayList<Event>> redoStack = new LinkedList<ArrayList<Event>>();
  private SequenceEventNode first           = null;
  private SequenceEventNode last            = null;


  /**
   * Add a Event to this sequence. events that are duplicates of time and value
   * are not added.
   * 
   * @param event the event to add.
   */
  public synchronized SequenceEvent add(Event event) {
    if (event == null) {
      return null;
    }

    // make sure this time and value isnt already in the sequence.
    for (SequenceEvent each : this) {
      if ((each.getTime() == event.getTime())
          && (each.getValue() == event.getValue())) {
        return each;
      }
      if (each.getTime() > event.getTime()) {
        break;
      }
    }

    Event newEvent = new Event(event);
    SequenceEventNode newSequenceEventNode = new SequenceEventNode(this, newEvent);
    insertInOrder(newSequenceEventNode);
    ++size;
    onSequenceChanged(SequenceChangeCodes.EVENT_ADDED);
    return newSequenceEventNode.getSequenceEvent();
  }
  
  
  /**
   * Add a new sequenceEvent from an existing sequence event(probably from another sequence).
   * @param sequenceEvent
   * @return returns the SequenceEvent of a the created and added sequence event.
   */
  public synchronized SequenceEvent add(SequenceEvent sequenceEvent) {
    return add(new Event(sequenceEvent.getTime(), sequenceEvent.getValue()));
  }

  public synchronized ArrayList<SequenceEvent> addAll(Sequence sequence, long timeOffset) {
    ArrayList<SequenceEvent> ret = new ArrayList<SequenceEvent>(sequence.size());
    for (SequenceEvent each : sequence) {
      ret.add(this.add(new Event(each.getTime() + timeOffset, each.getValue())));
    }
    return ret;
  }
  public synchronized void addAll(Collection<SequenceEvent> collection, long timeOffset) {
    for (SequenceEvent each : collection) {
      this.add(new Event(each.getTime() + timeOffset, each.getValue()));
    }
  }


  /**
   * Inserts the SequenceEvent in the correct place in the list.
   * 
   * @param eventNode the SequenceEvent to insert.
   */
  private void insertInOrder(SequenceEventNode event) {
    // insert the event into the correct place in the linked list.
    long time = event.getEvent().getTime();

    if (first == null) {
      first = event;
      last = event;
      return;
    }

    SequenceEventNode previous = null;
    SequenceEventNode current = first;
    while (current != null) {
      // when we hit the first event that is past our time.
      if (current.getEvent().getTime() > time) {
        if (previous != null) {
          previous.setNext(event);
        } else {
          first = event;
        }
        event.setPrevious(previous);
        event.setNext(current);
        current.setPrevious(event);

        return;
      }
      previous = current;
      current = current.next();
    }

    // if we hit the end of the list.
    previous.setNext(event);
    event.setPrevious(previous);
    last = event;
    return;
  }


  //
  // public synchronized void thinEvents(long startTime, long endTime,
  // float slopeDeviation) {
  // boolean eventsRemoved = false;
  // if (this.size() <= 3) {
  // return;
  // }
  //
  // ListIterator<SequenceEvent> it = events.listIterator();
  //
  // SequenceEvent n1 = it.next();
  // SequenceEvent n2 = it.next();
  // SequenceEvent n3 = null;
  //
  // while (it.hasNext()) {
  // n3 = it.next();
  //
  // float x1 = n1.getEvent().getTime();
  // float x2 = n2.getEvent().getTime();
  // float x3 = n3.getEvent().getTime();
  // float y1 = n1.getEvent().getValue();
  // float y2 = n2.getEvent().getValue();
  // float y3 = n3.getEvent().getValue();
  // float n12Slope = (y2 - y1)
  // / (x2 - x1);
  // float n13Slope = (y3 - y1)
  // / (x3 - x1);
  //
  // float distance = y2 - (n13Slope*(x2-x1));
  // if (Math.abs(distance) < slopeDeviation) {
  // it.previous();
  // it.previous();
  // it.remove();
  // it.next();
  // eventsRemoved = true;
  // if(it.hasNext()){
  // n1 = n3;
  // n2 = it.next();
  // } else {
  // break;
  // }
  // } else {
  // n1 = n2;
  // n2 = n3;
  // }
  //
  //
  // }
  // if (eventsRemoved) {
  // notifySequenceChanged();
  // }
  //
  // }

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


  public synchronized void clear() {
    this.first = null;
    this.last = null;
    this.size = 0;
  }


  /**
   * Checks if this sequence contains an event with the specified UUID.
   * 
   * @param event the event to check.
   * @return true if the event is an event in this Sequence
   */

  public synchronized boolean contains(SequenceEvent event) {
    for (SequenceEvent each : this) {
      if (each == event) {
        return true;
      }
    }
    return false;

  }


/**
   * @param index
   * @return the event at the specified index. Returns null if index is out of bounds.
   */
  public synchronized SequenceEvent get(int index) {
    if (index >= size()) {
      return null;
    }
    int i = 0;
    for(SequenceEvent each: this){
      if(i == index){
        return each;
      }
      ++i;
    }
    return null;
  }


/**
 * Get all of the events in the <code>Sequence</code>.
 * 
 * @return An <code>ArrayList</code> containing all of the events in the
 *         <code>Sequence</code> in order.
 */
public synchronized ArrayList<SequenceEvent> getAllEvents() {
  ArrayList<SequenceEvent> ret = new ArrayList<SequenceEvent>(size);
  for (SequenceEvent each : this) {
    ret.add(each);
  }
  return ret;
}


/**
 *  Get the event that is closest to time and value but with in timeDifference and ValueDifference
 * @param time 
 * @param value
 * @param timeDifference the maximum time difference
 * @param valueDifference the maximum value difference
 * @return
 */
  public synchronized SequenceEvent getEventClosestTo(long time, float value, long timeDifference, float valueDifference) {
    SequenceEvent closestEvent = null;
    double closestDistance = Double.POSITIVE_INFINITY;

    for (SequenceEvent each : this) {
      float eachTime = each.getTime();
      float eachValue = each.getValue();

      double distance = Math.sqrt((time - eachTime) * (time - eachTime)
                                  + (value - eachValue) * (value - eachValue));

      long eachTimeDifference = (long) Math.abs((float)time-(float)eachTime);
      float eachValueDifference = Math.abs(value - eachValue);

      if ((eachTimeDifference <= timeDifference)
          && (eachValueDifference <= valueDifference)) {
        if (distance < closestDistance) {
          closestEvent = each;
          closestDistance = distance;
        }
      }
    }
    return closestEvent;
  }


  //
  // public synchronized void thinEvents(long startTime, long endTime,
  // float slopeDeviation) {
  // boolean eventsRemoved = false;
  // if (this.size() <= 3) {
  // return;
  // }
  //
  // ListIterator<SequenceEvent> it = events.listIterator();
  //
  // SequenceEvent n1 = it.next();
  // SequenceEvent n2 = it.next();
  // SequenceEvent n3 = null;
  //
  // while (it.hasNext()) {
  // n3 = it.next();
  //
  // float x1 = n1.getEvent().getTime();
  // float x2 = n2.getEvent().getTime();
  // float x3 = n3.getEvent().getTime();
  // float y1 = n1.getEvent().getValue();
  // float y2 = n2.getEvent().getValue();
  // float y3 = n3.getEvent().getValue();
  // float n12Slope = (y2 - y1)
  // / (x2 - x1);
  // float n13Slope = (y3 - y1)
  // / (x3 - x1);
  //
  // float distance = y2 - (n13Slope*(x2-x1));
  // if (Math.abs(distance) < slopeDeviation) {
  // it.previous();
  // it.previous();
  // it.remove();
  // it.next();
  // eventsRemoved = true;
  // if(it.hasNext()){
  // n1 = n3;
  // n2 = it.next();
  // } else {
  // break;
  // }
  // } else {
  // n1 = n2;
  // n2 = n3;
  // }
  //
  //
  // }
  // if (eventsRemoved) {
  // notifySequenceChanged();
  // }
  //
  // }



  /**
   * Gets all events bounded by start time and endTime including events exactly
   * at the specified times.
   * 
   * @param startTime
   * @param endTime
   * @return
   */
  public synchronized ArrayList<SequenceEvent> getEventsByTime(long startTime, long endTime) {
    ArrayList<SequenceEvent> ret = new ArrayList<SequenceEvent>();

    long eachTime;
    for (SequenceEventNode each = first; each != null; each = each.next()) {
      eachTime = each.getEvent().getTime();
      if ((eachTime >= startTime) && (eachTime <= endTime)) {
        ret.add(each.getSequenceEvent());
      }

    }
    return ret;
  }


  /**
   * @return the first event in the sequence. null if there are no events.
   */
  public synchronized SequenceEvent getFirst() {
    if (first != null) {
      return first.getSequenceEvent();
    } else {
      return null;
    }
  }


  /**
   * Get the first sequenceEvent prior to the time given.
   * @param time the time to get.
   * @return the first SequenceEvent prior to the time given.   If time is
   * before the first sequence event or there are no sequence events
   * then null is returned.
   */
  public synchronized SequenceEvent getEventBeforeTime(long time) {

    SequenceEvent ret = null;
    for (SequenceEvent each : this) {
      if(each.getTime() > time){
        break;
      } else {
        ret = each;
      }
    }
    return ret;
  }


  /**
   * Get the first sequenceEvent after the time given.
   * @param time the time to get.
   * @return the first SequenceEvent after the time given.  if there are no sequence events
   * then null is returned. If there are no sequence events after the time given then null is returned.
   */
  public synchronized SequenceEvent getEventAfterTime(long time) {

    SequenceEvent ret = null;
    for (SequenceEvent each : this) {
      if (each.getTime() > time) {
        ret = each;
        break;
      }
    }
    return ret;
  }


  /**
   * @return
   */
  public synchronized SequenceEvent getLast() {
    return last.getSequenceEvent();
  }


  public synchronized long getFirstTime() {
    long ret = 0;
    if (first != null) {
      ret = first.getEvent().getTime();
    }
    return ret;
  }


  public synchronized long getLastTime() {
    long ret = 0;
    if (last != null) {
      ret = last.getEvent().getTime();
    }
    return ret;
  }


  private synchronized SequenceEventNode getSequenceEventNode(SequenceEvent event) {
    SequenceEventNode node = first;

    while (node != null) {
      if (node.getSequenceEvent().equals(event)) {
        return node;
      }
      node = node.next();
    }
    return null;
  }


  @Override
  public SequenceIterator iterator() {
    return new SequenceIterator(this);
  }


  public void onSequenceChanged(SequenceChangeCodes changeCode){
    observableComponent.notifyIObservers(this, changeCode);
  }
  
  
  
  // ---------- IOBservable methods -----------
  private ObservableComponent observableComponent = new ObservableComponent();


  /**
   * @param startTimeToDelete
   * @param endTimeToDelete
   */
  public synchronized void remove(long startTimeToDelete, long endTimeToDelete) {

    ArrayList<SequenceEvent> eventsToRemove = this.getEventsByTime(startTimeToDelete,
                                                                   endTimeToDelete);
    remove(eventsToRemove);
  }


  public synchronized void remove(Collection<SequenceEvent> eventsToRemove) {
    for (SequenceEvent each : eventsToRemove) {
      this.remove(each);
    }
  }


  public synchronized void remove(SequenceEvent event) {
    SequenceEventNode node = getSequenceEventNode(event);

    if (node != null) {
      removeFromLinkedList(node);
      size--;
      onSequenceChanged(SequenceChangeCodes.EVENT_REMOVED);

    }


  }


  private synchronized void removeFromLinkedList(SequenceEventNode node) {
    if (node != null) {
      if (node == first) {
        first = node.next();
      }

      if (node == last) {
        last = node.previous();
      }


      if (node.hasPrevious()) {
        node.previous().setNext(node.next());
      }
      if (node.hasNext()) {
        node.next().setPrevious(node.previous());
      }
      node.setNext(null);
      node.setPrevious(null);
    }
  }


  /**
   * 
   * @param SequenceEventNode the event node whose time needs to be modified.
   * @param time the new time for the node.
   */
  synchronized void setEventTime(SequenceEventNode node, long time) {
    if (node != null) {
      removeFromLinkedList(node);
      node.setEvent(new Event(time, node.getEvent().getValue()));
      insertInOrder(node);
      node.setValid(true);
      onSequenceChanged(SequenceChangeCodes.EVENT_TIME_CHANGED);
    }
  }


  public int size() {
    return size;
  }


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
   * Restore the state of this Sequence to the state that was last saved on the undo stack.
   */
  public synchronized void undo() {
    if (undoStack.size() > 0) {
      redoStack.push(toRestorePoint());
      ArrayList<Event> currentRestorePoint= undoStack.pop();
      restoreFromRestorePoint(currentRestorePoint);
    }
  }

  /**
   * Restore the state of this sequence to the state that was last popped off of the undo stack.
   * 
   */
  public synchronized void redo(){
    if (redoStack.size() > 0) {
      undoStack.push(toRestorePoint());
      ArrayList<Event> currentRestorePoint = redoStack.pop();
      restoreFromRestorePoint(currentRestorePoint);
    }
  }

  /**
   * @param element
   */
  public void readFromFile(Element element) {
    //verify that this is a Sequence node
    if(element.getNodeName().compareTo("Sequence") != 0){
      return;
    }
  
    // get the values
    this.clear();
    NodeList nodeLst = element.getChildNodes();
    nodeLst = element.getElementsByTagName("sequenceEvents");
    if (nodeLst.getLength() > 0) {
  
      if (nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE) {
        Element sequenceEventsElement = (Element) nodeLst.item(0);
        String nodeName = "";
  
        NodeList SequenceEventNodeList = sequenceEventsElement.getChildNodes();
        Element eventElement;
        for (int i = 0; i < SequenceEventNodeList.getLength(); ++i) {
          if (SequenceEventNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
            eventElement = (Element) SequenceEventNodeList.item(i);
  
            nodeName = eventElement.getNodeName();
            if (nodeName.equals("e")) {
              readEvent(eventElement);
            }
          }
        }
      }
    }
  }


  /**
   * @param parentElement
   */
  public void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    Element thisElement = docOwner.createElement("Sequence");
    parentElement.appendChild(thisElement);
    String eachTimeStr;
    String eachValueStr;
    Element sequenceEventsElement = docOwner.createElement("sequenceEvents");
    Element eventElement;
  
    for(SequenceEvent each:this){
      eventElement = docOwner.createElement("e");
      sequenceEventsElement.appendChild(eventElement);
      
      eachTimeStr = Long.toString(each.getTime());
      eachValueStr = Float.toString(each.getValue());
      XmlHelper.createElementValuePair(eventElement, "v", eachValueStr);
      XmlHelper.createElementValuePair(eventElement, "t", eachTimeStr);
  
    }
    thisElement.appendChild(sequenceEventsElement);
    
  }


  public synchronized void pushUndoRestorePoint() {

    ArrayList<Event>currentRestorePoint = toRestorePoint();
   
    redoStack.clear();
    undoStack.push(currentRestorePoint);
    
    if(undoStack.size() > NUMBER_OF_UNDOS){
      undoStack.remove(undoStack.getLast());
    }
  }
  
  /**
   * Convert this sequence into a restore point.
   * @return this sequence as a restore point.
   */
  private ArrayList<Event> toRestorePoint(){
    ArrayList<Event> ret = new ArrayList<Event>(size());
    SequenceEventNode current = first;
    while (current != null) {
      ret.add(new Event(current.getEvent()));
      current = current.next();
    }
    return ret;
  }
  
  /**
   * Restore the state of this sequence from a restore point.  
   * @param restorePoint the restore point to restore from.
   */
  private void restoreFromRestorePoint(ArrayList<Event> restorePoint){
    this.clear();
    for (Event each : restorePoint) {
      this.add(each);
    }
  }


  private void readEvent(Element eventElement) {
    // verify that this is an Event node
    if (eventElement.getNodeName().compareTo("e") != 0) {
      return;
    }

    // get the values
    String timeStr = "";
    String valueStr = "";
    
    NodeList nodeLst = eventElement.getChildNodes();
    nodeLst = eventElement.getElementsByTagName("t");
    if (nodeLst.getLength() > 0) {
      if (nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE) {
        Element timeElement = (Element) nodeLst.item(0);
        timeStr = XmlHelper.getTextData(timeElement);
      }
    }
    
    nodeLst = eventElement.getElementsByTagName("v");
    if (nodeLst.getLength() > 0) {
      if (nodeLst.item(0).getNodeType() == Node.ELEMENT_NODE) {
        Element valueElement = (Element) nodeLst.item(0);
        valueStr = XmlHelper.getTextData(valueElement);
      }
    }
    

    this.add(new Event(Long.parseLong(timeStr), Float.parseFloat(valueStr)));
    
  }
  
  
}//class

//
//for(int i = 0; i < nodeLst.getLength(); ++i){
//  nodeName = nodeLst.item(i).getNodeName();
//  if(nodeName.compareTo("MidiInputBus") == 0){
//    String str = XmlHelper.getTextData((Element)nodeLst.item(i));
//    if(str != null){
//      this.setMidiInputBus(str);
//    }
//  } else if(nodeName.compareTo("MidiInputType") == 0){
//    String str = XmlHelper.getTextData((Element)nodeLst.item(i));
//    if(str != null){
//      this.setMidiInputType(MidiInputTypesFromString(str));
//    }
//  }