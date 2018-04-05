/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.util.Iterator;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */

public class SequenceEvent implements Iterator<SequenceEvent> {
  private SequenceEventNode internalNode;


  public SequenceEvent(SequenceEventNode internalNode) {
    this.internalNode = internalNode;

  }


  public float getValue() {
    return internalNode.getEvent().getValue();
  }


  public void setValue(float value) {
    internalNode.getEvent().setValue(value);
  }


  public long getTime() {
    return internalNode.getEvent().getTime();
  }


  public void setTime(long time) {
    internalNode.setEventTime(time);
  }


  /** @see java.util.Iterator#hasNext() */
  @Override
  public boolean hasNext() {
    return internalNode.hasNext();
  }


  /** @see java.util.Iterator#next() */
  @Override
  public SequenceEvent next() {
    if (internalNode.hasNext())
      return internalNode.next().getSequenceEvent();
    else {
      return null;
    }
  }


  public boolean hasPrevious() {
    return internalNode.hasPrevious();
  }


  public SequenceEvent previous() {
    if (internalNode.hasPrevious()) {
      return internalNode.previous().getSequenceEvent();
    } else {
      return null;
    }
  }


  /** @see java.util.Iterator#remove() */
  @Override
  public void remove() {
    internalNode.remove();
  }
  
  
}