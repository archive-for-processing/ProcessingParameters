/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import floatingpointdev.toolkit.util.IObserver;


/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class SequenceIterator implements Iterator<SequenceEvent>, IObserver {
  private Sequence      sequence;
  private SequenceEvent next;
  private boolean       iteratorDone = false;


  SequenceIterator(Sequence sequence) {
    this.sequence = sequence;
    next = sequence.getFirst();;
    //sequence.addIObserver(this);

  }


  /** @see java.util.Iterator#hasNext() */
  @Override
  public boolean hasNext() {
    if (next != null) {
      return true;
    } else {
      return false;
    }
  }


  /** @see java.util.Iterator#next() */
  @Override
  public SequenceEvent next() {
    SequenceEvent oldNext = next;
    if (next != null) {
      next = next.next();
    }

    if (next == null) {
      iteratorDone = true;
      sequence.deleteIObserver(this);
    }
    return oldNext;
  }


  /** @see java.util.Iterator#remove() */
  @Override
  public void remove() {
    next.remove();
  }


  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object,
   *      java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    // the underlying sequence has changed.
    if (!iteratorDone) {
      throw new ConcurrentModificationException("Attempting to modify the underlying structure"
                                                + "of the sequence while an iterator on the sequence is active.");
    }


  }


}
