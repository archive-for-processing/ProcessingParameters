/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;
import java.math.*;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;

import floatingpointdev.parameters.AutomationMaster;
import floatingpointdev.toolkit.util.IObserver;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class LaneLocator extends JComponent implements IObserver{
  /** */
  private static final long serialVersionUID = 1L;
  private AutomationMaster automationMaster;
  private LaneView laneView;
  private long currentPosition;
  
  
  /**
   * @param automationMaster
   */
  public LaneLocator(LaneView laneView, AutomationMaster automationMaster) {
    super();
    this.automationMaster = automationMaster;
    this.laneView = laneView;
    this.automationMaster.addIObserver(this);
    this.setOpaque(false);

  }


  /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
  @Override
  protected void paintComponent(Graphics g) {
    //super.paintComponent(g);
    g.setColor(Color.white);
    float currentX = laneView.timeToGraphX(currentPosition);
    laneView.drawLine(currentX, 0f, currentX, 1f, g);
  }


  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object,
   *      java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    if (changeCode == "position") {
      long newPosition = automationMaster.getPosition();
      // only repaint if the position has moved by more than a pixel.
      long difference = Math.abs(newPosition - currentPosition);

      if (difference >= laneView.ScreenXToTime(1)) {
        currentPosition = newPosition;
        this.repaint();
      }
    }
  }

}
