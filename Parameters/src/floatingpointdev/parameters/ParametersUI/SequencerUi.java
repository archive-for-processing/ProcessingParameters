/**
 * 
 */

package floatingpointdev.parameters.ParametersUI;
import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.ScrollPaneConstants;

import javax.swing.UIManager;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileFilter;

import floatingpointdev.parameters.AutomationMaster;
import floatingpointdev.parameters.ParameterManager;
import floatingpointdev.parameters.ParametersManager;
import floatingpointdev.parameters.automationcontroller.PlayableSequence;
import floatingpointdev.parameters.automationcontroller.PlayableSequenceAuxPanel;
import floatingpointdev.parameters.automationcontroller.ui.AutomationLane;
import floatingpointdev.parameters.automationcontroller.ui.AutomationMasterUi;
import floatingpointdev.parameters.automationcontroller.ui.Lane;
import floatingpointdev.parameters.automationcontroller.ui.PlayableSequenceUiGraph;
import floatingpointdev.toolkit.UI.MinimalButton;

public class SequencerUi {
  JFrame                              frame;
  private JPanel                      lanesPanel;
  private JPanel                      locatorHeaderPanel;
  private JPanel                      rowHeaderPanel;
  private MainMenu                    mainMenu;
  private JScrollPane                 scrollPane;

  /** The distance between tracks that clicking will cause the tracks to resize. */
  private static int                  INTERTRACK_RESIZE_BAR_SIZE = 8;
  /** The minimum height (in pixels) that a track can be. */
  private static int                  TRACK_MINIMUM_HEIGHT       = 20;

  /** The ParametersManaager that this UI is for. */
  private ParametersManager           parametersManager;

  /** The list of parameters this ParameterManager controls. */
  private ArrayList<ParameterManager> parameters                 = new ArrayList<ParameterManager>();

  /** The list of parameterUI objects this ParameterManager controls. */
  private ArrayList<ParameterUi>      parameterUis               = new ArrayList<ParameterUi>();

  private ArrayList<ITrack>           tracks                     = new ArrayList<ITrack>();


  public SequencerUi(ParametersManager parametersManager) {
    if (parametersManager == null) {
      throw new IllegalArgumentException("A parametersManager object must be "
                                         + "supplied with the constructor");
    }
    this.parametersManager = parametersManager;

    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (Exception e) {
    }


    frame = new JFrame();

    // attach the main menu to the main frame.
    mainMenu = new MainMenu(parametersManager, frame);
    frame.setJMenuBar(mainMenu);

    frame.getContentPane().setLayout(new BorderLayout());
    JPanel automationMasterUi = new AutomationMasterUi(parametersManager.getAutomationMaster());
    frame.add(automationMasterUi, BorderLayout.NORTH);
    setupScrollPane();
    
    MinimalButton zoomPlusBtn = new MinimalButton("Zoom +");
    MinimalButton zoomMinusBtn = new MinimalButton("Zoom -");
    
    zoomPlusBtn.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent event) {
        for(ITrack each:tracks){
          each.setHorizontalZoom((long)(each.getHorizontalZoom() * .75f));
        }
      }
      
    });
    zoomMinusBtn.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent event) {
        for(ITrack each:tracks){
          each.setHorizontalZoom((long)(each.getHorizontalZoom() * 1.25f));
        }
      }
      
    });
    JPanel zoomBtns = new JPanel();
    zoomBtns.setLayout(new BoxLayout(zoomBtns, BoxLayout.X_AXIS));

    zoomBtns.add(zoomPlusBtn);
    zoomBtns.add(zoomMinusBtn);
    horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
    
    JPanel scrollBarAndZoomBtns = new JPanel();
    scrollBarAndZoomBtns.setLayout(new BorderLayout());
    scrollBarAndZoomBtns.add(zoomBtns, BorderLayout.WEST);
    scrollBarAndZoomBtns.add(horizontalScrollBar, BorderLayout.CENTER);
   
    JPanel scrollPaneAndScrollBar = new JPanel();
    scrollPaneAndScrollBar.setLayout(new BorderLayout());

    scrollPaneAndScrollBar.add(scrollBarAndZoomBtns, BorderLayout.SOUTH);
    scrollPaneAndScrollBar.add(scrollPane, BorderLayout.CENTER);
    frame.add(scrollPaneAndScrollBar, BorderLayout.CENTER);
    horizontalScrollBar.addAdjustmentListener(new AdjustmentListener() {

      @Override
      public void adjustmentValueChanged(AdjustmentEvent event) {
        int value = event.getValue();
        for (ITrack each : tracks) {
          each.setHorizontalScroll((float) value / 100);
        }
      }

    });
    // Setup the event handler for closing the window.
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    frame.setTitle("Parameters");
    frame.pack();
    frame.setVisible(true);
  }

  private JScrollBar horizontalScrollBar;


  private void setupScrollPane() {
    lanesPanel = new JPanel();

    locatorHeaderPanel = new JPanel();
    rowHeaderPanel = new JPanel();

    lanesPanel.setLayout(new BoxLayout(lanesPanel, BoxLayout.Y_AXIS));
    lanesPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    ResizeMouseListener mouseListener = new ResizeMouseListener(tracks);
    lanesPanel.addMouseListener(mouseListener);
    lanesPanel.addMouseMotionListener(mouseListener);

    locatorHeaderPanel.add(new JLabel("locatorHeaderPanel"));
    locatorHeaderPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    rowHeaderPanel.setLayout(new BoxLayout(rowHeaderPanel, BoxLayout.Y_AXIS));
    rowHeaderPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    rowHeaderPanel.addMouseListener(mouseListener);
    rowHeaderPanel.addMouseMotionListener(mouseListener);

    scrollPane = new JScrollPane(lanesPanel);
    scrollPane.setColumnHeaderView(locatorHeaderPanel);
    scrollPane.setRowHeaderView(rowHeaderPanel);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(800, 400));
  }


  /**
   * Adds a parameter to the parametersManager
   * 
   * @param p the parameter to add.
   */
  public void addParameter(ParameterManager p) {
    parameters.add(p);
    ParameterUi pUi = p.getParameterUi();
    // add the UI and attach it to the parameter.
    // parameterUis.add(pUi);
    ITrack newTrack = new ParameterTrack(p);
    tracks.add(newTrack);
    rowHeaderPanel.add(newTrack.getPropertyPanel());
    // rowHeaderPanel.add(newTrack.getResizeBar());

    lanesPanel.add(newTrack.getLanePanel());
    // lanesPanel.add(newTrack.getResizeBar());

    frame.pack();
  }


  /**
   * Removes a parameter from the parametersManager
   * 
   * @param p the parameter to remove.
   */
  public void removeParameter(ParameterManager p) {
    parameters.remove(p);

    // Remove the associated parameterUI
    // Iterate through the list of parameterUis and remove any that
    // match the parameter we are removing.
    for (ListIterator<ParameterUi> i = parameterUis.listIterator(); i.hasNext();) {
      ParameterUi pUI = i.next();
      if (pUI.isUIForParameter(p)) {
        pUI.destruct();
        parameterUis.remove(pUI);
        // parametersPanel.remove(pUI.getUIComponent());
        frame.pack();
      }
    }
  }


  /**
   * @param dirPath
   * @see floatingpointdev.parameters.ParametersUI.MainMenu#setFileChooserDefaultDirectory(java.lang.String)
   */
  public void setFileChooserDefaultDirectory(String dirPath) {
    mainMenu.setFileChooserDefaultDirectory(dirPath);
  }

  private class ResizeMouseListener
      implements
        MouseMotionListener,
        MouseListener {
    private ArrayList<ITrack> tracks;
    private ITrack            draggingTrack;
    private boolean           isDragging;
    private int               dragStartY;


    ResizeMouseListener(ArrayList<ITrack> tracks) {
      this.tracks = tracks;
    }


    /** @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) */
    @Override
    public void mouseDragged(MouseEvent event) {
      if (event.getButton() == MouseEvent.BUTTON1) {
        int y = event.getY();

        if (draggingTrack != null) {
          int newHeight;

          newHeight = draggingTrack.getHeight() + (y - dragStartY);
          draggingTrack.setHeight(newHeight);
          dragStartY = y;
        }
      }
    }


    /** @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) */
    @Override
    public void mouseMoved(MouseEvent arg0) {
    }


    /** @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent) */
    @Override
    public void mouseClicked(MouseEvent arg0) {
    }


    /** @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent) */
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }


    /** @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent) */
    @Override
    public void mouseExited(MouseEvent arg0) {
    }


    /** @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) */
    @Override
    public void mousePressed(MouseEvent event) {
      int y = event.getY();

      // figure out which track we are resizing
      int trackHeightSum = 0;
      for (ITrack each : tracks) {
        trackHeightSum += each.getHeight();
        if (((y - trackHeightSum) <= 0)
            && ((y - trackHeightSum) > -INTERTRACK_RESIZE_BAR_SIZE)) {
          draggingTrack = each;
          isDragging = true;
          break;
        }
      }
      if (draggingTrack != null) {
        dragStartY = y;
      }
    }


    /** @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent) */
    @Override
    public void mouseReleased(MouseEvent event) {
      if (isDragging) {
        isDragging = false;
        draggingTrack = null;
      }
    }
  }
  
  
  private class Track implements ITrack {
    private JPanel propertyPanel;
    private JPanel lanePanel;
    private ILane  lane;
    private int    height = 10;


    /**
     * do not directly use Track.
     * 
     * @param propertyPanel
     * @param auxPanel
     * @param lane
     */
    public Track(JPanel propertyPanel, ILane lane) {
      super();

      this.propertyPanel = propertyPanel;
      this.propertyPanel = new JPanel();
      this.propertyPanel.setLayout(new BorderLayout());
      this.propertyPanel.add(Box.createVerticalStrut(INTERTRACK_RESIZE_BAR_SIZE),
                             BorderLayout.SOUTH);
      this.propertyPanel.add(propertyPanel, BorderLayout.CENTER);

      this.lanePanel = new JPanel();
      lanePanel.setLayout(new BorderLayout());
      lanePanel.add(lane.getPanel(), BorderLayout.CENTER);
      lanePanel.add(Box.createVerticalStrut(INTERTRACK_RESIZE_BAR_SIZE),
                    BorderLayout.SOUTH);
      this.lane = lane;
      this.setHeight(120);
    }


    /** @return the propertyPanel */
    public JPanel getPropertyPanel() {
      return propertyPanel;
    }


    /** @return the lane */
    public JPanel getLanePanel() {
      return lanePanel;
    }


    public void setHeight(int height) {

      if (height < TRACK_MINIMUM_HEIGHT) {
        height = TRACK_MINIMUM_HEIGHT;
      }
      this.height = height;
      Dimension d = propertyPanel.getPreferredSize();
      d.setSize(d.getWidth(), height);
      propertyPanel.setMaximumSize(d);
      // propertyPanel.setMinimumSize(d);
      propertyPanel.setPreferredSize(d);

      d = lane.getPanel().getPreferredSize();
      // d.setSize(Integer.MAX_VALUE, height);
      d.setSize(d.getWidth(), height);
      lane.getPanel().setMaximumSize(d);
      // lane.setMinimumSize(d);
      lane.getPanel().setPreferredSize(d);

      lane.getPanel().revalidate();
      propertyPanel.revalidate();
    }


    /**
     * @return the height in pixels of this track.
     */
    public int getHeight() {
      return height;
    }


    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#setHorizontalScroll(float) */
    @Override
    public void setHorizontalScroll(float percent) {
      lane.setHorizontalScroll(percent);
    }


    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#getHorizontalZoom()*/
    @Override
    public long getHorizontalZoom() {
      return lane.getHorizontalZoom();
    }


    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#setHorizontalZoom(long)*/
    @Override
    public void setHorizontalZoom(long viewMs) {
      lane.setHorizontalZoom(viewMs);
    }
  }
  
  private interface ITrack {
    public JPanel getPropertyPanel();
    public long getHorizontalZoom();
    public JPanel getLanePanel();
    public int getHeight();
    public void setHeight(int height);
    public void setHorizontalScroll(float percent);
    public void setHorizontalZoom(long viewMs);
  }

  private class ParameterTrack implements ITrack {
    // private ParameterManager parameterManager;
    private Track track;


    public ParameterTrack(ParameterManager p) {
      super();

      // playableSequenceUiGraph = new PlayableSequenceUiGraph();
      JPanel propertyAndAux = new JPanel();
      propertyAndAux.setLayout(new BorderLayout());
      propertyAndAux.add(p.getParameterUi().getUiPanel(), BorderLayout.WEST);
      //PlayableSequence s = p.getAutomationController().getSequence();
      //JPanel auxPanel = new PlayableSequenceAuxPanel(s);
      //propertyAndAux.add(auxPanel, BorderLayout.EAST);
      propertyAndAux.setBorder(BorderFactory.createLineBorder(new Color(50,
                                                                        0,
                                                                        0), 2));
      //Lane lane = null;
      //Lane lane = new Lane(p.getAutomationController().getSequence(),
      //                     p.getAutomationController().getAutomationMaster());
      //track = new Track(propertyAndAux, lane);
      //track = new Track(propertyAndAux, lane);
    }


    /**
     * @return
     * @see floatingpointdev.parameters.ParametersUI.SequencerUi.Track#getHeight()
     */
    public int getHeight() {
      return track.getHeight();
    }


    /**
     * @return
     * @see floatingpointdev.parameters.ParametersUI.SequencerUi.Track#getLanePanel()
     */
    public JPanel getLanePanel() {
      return track.getLanePanel();
    }


    /**
     * @return
     * @see floatingpointdev.parameters.ParametersUI.SequencerUi.Track#getPropertyPanel()
     */
    public JPanel getPropertyPanel() {
      if(track != null){
        return track.getPropertyPanel();
      }
      return null;
    }


    /**
     * @param height
     * @see floatingpointdev.parameters.ParametersUI.SequencerUi.Track#setHeight(int)
     */
    public void setHeight(int height) {
      track.setHeight(height);
    }


    // private PlayableSequenceUiGraph playableSequenceUiGraph;
    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#setHorizontalScroll(float) */
    @Override
    public void setHorizontalScroll(float percent) {
      track.setHorizontalScroll(percent);
    }


    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#getHorizontalZoom()*/
    @Override
    public long getHorizontalZoom() {
      return track.getHorizontalZoom();
    }


    /** @see floatingpointdev.parameters.ParametersUI.SequencerUi.ITrack#setHorizontalZoom(long)*/
    @Override
    public void setHorizontalZoom(long viewMs) {
      track.setHorizontalZoom(viewMs);
    }
  }
}
