/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.util.Iterator;

import floatingpointdev.parameters.automationcontroller.Sequence.SequenceChangeCodes;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */

class SequenceEventNode implements Iterator<SequenceEventNode>{
  // private UUID uuid;
  private Sequence sequence;
  private Event             event;
  private SequenceEvent     sequenceEvent;
  private SequenceEventNode previousNode = null;
  private SequenceEventNode nextNode     = null;
  private boolean isValid = true;

  /**
   * @param uuid
   * @param event
   */
  public SequenceEventNode(Sequence sequence, Event event) {
    super();
    // this.uuid = uuid;
    this.event = event;
    this.sequence = sequence;
    this.sequenceEvent = new SequenceEvent(this);
  }


  // /** @return the uuid */
  // public UUID getUuid() {
  // return uuid;
  // }


  /** @return the sequenceEvent */
  public SequenceEvent getSequenceEvent() {
    return sequenceEvent;
  }


  /** @return the event */
  public Event getEvent() {
    return event;
  }


  /** @see java.util.Iterator#hasNext()*/
  @Override
  public boolean hasNext() {
    if(nextNode != null){
      return true;
    }
    return false;
  }


  public boolean hasPrevious() {
    if(previousNode != null){
      return true;
    }
    return false;
  }


  /** @return the isValid */
  public boolean isValid() {
    return isValid;
  }


  /** @see java.util.Iterator#next()*/
  @Override
  public SequenceEventNode next() {
    return nextNode;
  }


  public SequenceEventNode previous() {
    return previousNode;
  }


  /** @param event the event to set */
  public void setEvent(Event event) {
    this.event = event;
  }

  public void setEventTime(long time){
    sequence.setEventTime(this, time);
  }

  public void setEventValue(float value){
    event.setValue(value);
    sequence.onSequenceChanged(SequenceChangeCodes.EVENT_VALUE_CHANGED);
  }
  
  public void setNext(SequenceEventNode next){
    nextNode = next;
  }


  public void setPrevious(SequenceEventNode previous){
    previousNode = previous;
  }


  /** @param sequenceEvent the sequenceEvent to set */
  public void setSequenceEvent(SequenceEvent sequenceEvent) {
    this.sequenceEvent = sequenceEvent;
  }



  /** @param isValid the isValid to set */
  public void setValid(boolean isValid) {
    this.isValid = isValid;
    if(isValid == false){
      setPrevious(null);
      setNext(null);
      setSequenceEvent(null);
    }
  }


  /** @see java.util.Iterator#remove()*/
  @Override
  public void remove() {
    sequence.remove(sequenceEvent);
  }
}
