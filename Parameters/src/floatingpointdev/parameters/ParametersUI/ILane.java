/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import javax.swing.JPanel;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public interface ILane{

  public void setHorizontalScroll(float percent);
  public void setHorizontalZoom(long msPerPixel);
  public JPanel getPanel();
  /**
   * @return
   */
  public long getHorizontalZoom();

}
