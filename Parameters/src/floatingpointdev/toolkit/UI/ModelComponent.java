/**
 * 
 */
package floatingpointdev.toolkit.UI;

import java.util.Enumeration;
import java.util.Vector;


/** ModelComponent class for making the implementation of the
 * IModel interface a little bit easier.
 * ModelComponent basically maintains a list of objects that implement 
 * the IView interface.  
 * @author floatingpointdev
 *
 */
public class ModelComponent{
  private Vector<IView> myIViews = new Vector<IView>();

  /** @see floatingpointdev.toolkit.UI.IModel#addIView(floatingpointdev.toolkit.UI.IView)*/
  public void addIView(IView view) {
    myIViews.add(view);
    
  }

  /** @see floatingpointdev.toolkit.UI.IModel#removeAllIViews()*/
  public void removeIView(IView view) {
    myIViews.removeElement(view);
    
  }

  /** @see floatingpointdev.toolkit.UI.IModel#removeIView(floatingpointdev.toolkit.UI.IView)*/
  public void removeAllIViews() {
    myIViews.removeAllElements();
    
  }

  public void notifyIViews(IModel theViewed, Object change){
    //iterate through the list of Observers and tell
    //each one that the model has changed.
    Enumeration<IView> myIViewsList = myIViews.elements();
    while(myIViewsList.hasMoreElements()){
      IView anIView =myIViewsList.nextElement();
      anIView.onModelChanged(theViewed, change);
    }
  }


}





