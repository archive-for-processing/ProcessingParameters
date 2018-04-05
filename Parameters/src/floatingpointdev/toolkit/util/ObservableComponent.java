/**

 */
package floatingpointdev.toolkit.util;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Observable component class for use with the modified Observer Observable
 * design pattern described in the book Java Design by Peter Coad.
 * ObservableComponent basically maintains a list of objects that implement 
 * the IObservable interface.  
 * @author Ryan Hieber - floatingpoint@gmail.com
 */

public class ObservableComponent {
 
  ArrayList<IObserver> myIObservers = new ArrayList<IObserver>();
  public void addIObserver(IObserver anIObserver){
    myIObservers.add(anIObserver);
  }
  
  public void deleteIObserver(IObserver anIObserver){
    myIObservers.remove(anIObserver);
  }

  public void deleteIObservers(){
    myIObservers.removeAll(myIObservers);
  }
  
  public void notifyIObservers(Object theObserved, Object changeCode){
    //notify each observer.
    ListIterator<IObserver> iterator = myIObservers.listIterator();
    while(iterator.hasNext()){
      IObserver iObserver = iterator.next();
      iObserver.update(theObserved, changeCode);
    }
  }
}
