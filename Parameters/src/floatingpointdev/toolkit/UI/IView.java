/**
 * 
 */
package floatingpointdev.toolkit.UI;

/**
 * Classes implementing the IView interface respond to model changes.
 * @author floatingpointdev
 *
 */
public interface IView {
  public void attachToModel(IModel model);
  public void onModelChanged(IModel theChanged, Object change);

}
