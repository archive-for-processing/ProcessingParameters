/**
 * 
 * 
 */
package floatingpointdev.toolkit.util;

/**
 * IObservable interface for use with the modified Observer Observable
 * design pattern described in the book Java Design by Peter Coad.  
 * Interface for objects that should be observable by an IObserver object.
 * @author floatingpointdev
 *
 */
public interface IObservable {
  void addIObserver(IObserver anIObserver);
  void deleteIObserver(IObserver anIObserver);
  void deleteIObservers();
  
//copy and paste below to implement this interface.
  
//  // ---------- IOBservable methods -----------
//  private ObservableComponent observableComponent = new ObservableComponent();
//  public void addIObserver(IObserver anIObserver){
//    observableComponent.addIObserver(anIObserver);
//  }
//  
//  public void deleteIObserver(IObserver anIObserver){
//    observableComponent.deleteIObserver(anIObserver); 
//  }
//  
//  public void deleteIObservers(){
//    observableComponent.deleteIObservers();
//  }
}
