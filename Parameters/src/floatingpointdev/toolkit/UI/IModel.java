/**
 * 
 */
package floatingpointdev.toolkit.UI;


/** classes implementing the IModel interface respond to changes in 
 * the associated views and also updates its views whenever it changes
 * itself.  The IModel IView association is basically the same 
 * as an IObserver IObservable association that goes both ways.
 * @author floatingpointdev
 *
 */
public interface IModel {
  void onViewChanged(IView theChanged, Object change);
  void addIView(IView view);
  void removeIView(IView view);
  void removeAllIViews();
  
}



///cut paste, uncomment the below when implementing IModel.

//---------- IModel methods -----------
/*ModelComponent modelComponent = new ModelComponent();

*//** @see floatingpointdev.toolkit.UI.IModel#onViewChanged(floatingpointdev.toolkit.UI.IView, java.lang.Object)*//*
@Override
public void onViewChanged(IView theChanged, Object change) {


}

*//** @see floatingpointdev.toolkit.UI.IModel#addIView(floatingpointdev.toolkit.UI.IView)*//*
@Override
public void addIView(IView view) {
modelComponent.addIView(view);
}

*//** @see floatingpointdev.toolkit.UI.IModel#removeIView(floatingpointdev.toolkit.UI.IView)*//*
@Override
public void removeIView(IView view) {
modelComponent.removeIView(view);
}

*//** @see floatingpointdev.toolkit.UI.IModel#removeAllIViews()*//*
@Override
public void removeAllIViews() {
modelComponent.removeAllIViews();

}*/