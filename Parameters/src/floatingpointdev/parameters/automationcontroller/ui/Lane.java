/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

import floatingpointdev.parameters.AutomationMaster;
import floatingpointdev.parameters.ParametersUI.ILane;
import floatingpointdev.parameters.automationcontroller.PlayableSequence;


/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class Lane extends JPanel implements ILane{
  private AutomationMaster automationMaster;

  private LaneView               lv;
  private LaneLocator             laneLocator;
  private PlayableSequenceUiGraph playableSequenceUiGraph;

  private static long DEFAULT_ZOOM_VIEW_MS = 20000;
  private  long viewMs = DEFAULT_ZOOM_VIEW_MS;
  /**
   * @param automationController
   */
  public Lane(PlayableSequence sequence, AutomationMaster automationMaster) {
    super();
    this.automationMaster = automationMaster;
    lv = new LaneView();

    
    laneLocator = new LaneLocator(lv,
                                  automationMaster);
    
    playableSequenceUiGraph = new PlayableSequenceUiGraph(lv, sequence);
    this.setLayout(new BorderLayout());
    playableSequenceUiGraph.setLayout(new BorderLayout());
    playableSequenceUiGraph.add(laneLocator, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(600,600));
    this.add(playableSequenceUiGraph, BorderLayout.CENTER);
    this.addComponentListener(new ComponentListener(){
      @Override
      public void componentHidden(ComponentEvent arg0) {
        lv.setWidthPixels(getWidth());
        lv.setHeightPixels(getHeight());
      }

      @Override
      public void componentMoved(ComponentEvent arg0) {
        lv.setWidthPixels(getWidth());
        lv.setHeightPixels(getHeight());
      }

      @Override
      public void componentResized(ComponentEvent arg0) {
        lv.setWidthPixels(getWidth());
        lv.setHeightPixels(getHeight());
      }

      @Override
      public void componentShown(ComponentEvent arg0) {
        lv.setWidthPixels(getWidth());
        lv.setHeightPixels(getHeight());
      }
    });
    setHorizontalScroll(0);
    setHorizontalZoom(DEFAULT_ZOOM_VIEW_MS);
  }



  /** @see floatingpointdev.parameters.ParametersUI.ILane#getHorizontalZoom()*/
  @Override
  public long getHorizontalZoom() {
    return viewMs;
  }


  /**
   * @param f
   */
  @Override
  public void setHorizontalScroll(float percent) {
    long newTime = (long)(percent * automationMaster.getEndTime());
    //lv.setViewStartTime(newTime);
    playableSequenceUiGraph.setViewStartTime(newTime);
    playableSequenceUiGraph.setViewEndTime(playableSequenceUiGraph.getViewStartTime() + viewMs);
  }
  
  @Override
  public void setHorizontalZoom(long viewMs){
    this.viewMs = viewMs;
    //playableSequenceUiGraph.setViewStartTime(newTime);
    playableSequenceUiGraph.setViewEndTime(playableSequenceUiGraph.getViewStartTime() + viewMs);
    
  }
  

  @Override
  public JPanel getPanel(){
    return this;
  }
}
