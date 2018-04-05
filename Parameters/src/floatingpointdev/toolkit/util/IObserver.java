/**

 */
package floatingpointdev.toolkit.util;

/**
 * IObserver interface for use with the modified Observer Observable
 * design pattern described in the book Java Design by Peter Coad.
 * This is an interface for objects that observe an IObservable object.
 * @author floatingpointdev
 *
 */
public interface IObserver {
  void update(Object theObserved, Object changeCode);
}
