/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.event.MouseInputAdapter;

import floatingpointdev.parameters.automationcontroller.Event;
import floatingpointdev.parameters.automationcontroller.PlayableSequence;
import floatingpointdev.parameters.automationcontroller.Sequence;
import floatingpointdev.parameters.automationcontroller.SequenceEvent;
import floatingpointdev.parameters.automationcontroller.SequenceIterator;
import floatingpointdev.parameters.automationcontroller.Sequence.SequenceChangeCodes;
import floatingpointdev.toolkit.util.IObserver;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class PlayableSequenceUiGraph extends JComponent
    implements
      IObserver,
      KeyListener {
  /** */
  private static final long        serialVersionUID               = -4183563589312999320L;

  private LaneView                laneView;
  private PlayableSequence         sequence;
  private MouseListener            mouseListener                  = new MouseListener();
  private Rectangle                selectionRect;
  private ArrayList<SequenceEvent> selectedEvents                 = new ArrayList<SequenceEvent>();
  private Sequence                 draggingEvents                 = new Sequence();


  private long                     timeSnap                       = 1000;
  private long                     majorGridTime                  = 5000;

  private static Color             SNAP_GRID_COLOR                = new Color(110,
                                                                              140,
                                                                              170);
  private static Color             MAJOR_GRID_COLOR               = new Color(0,
                                                                              200,
                                                                              200);
  private static Color             BACKGROUND_COLOR               = new Color(40,
                                                                              40,
                                                                              40);
  private static Color             AUTOMATION_LINE_COLOR          = new Color(255,
                                                                              255,
                                                                              255);
  private static Color             HANDLE_COLOR_SELECTED          = new Color(.8f,
                                                                              .8f,
                                                                              .5f);
  private static Color             HANDLE_COLOR                   = new Color(0,
                                                                              200,
                                                                              0);
  private static Color             SELECTION_RECTANGLE_FILL_COLOR = new Color(.5f,
                                                                              .5f,
                                                                              .5f,
                                                                              .5f);

  private static Color             SELECTION_RECTANGLE_COLOR      = new Color(.8f,
                                                                              .8f,
                                                                              .5f,
                                                                              .8f);

  /** the size of the square representing a handle in pixels. */
  static final int                 HANDLE_SIZE                    = 4;

  private enum EditModes {
    SELECT, SELECT_TIME_RANGE, INSERT, DELETE,
  }


  private EditModes                editMode                  = EditModes.SELECT;
  private boolean                  isTimeSnapEnabled         = true;

  private boolean                  isCopyDragging            = false;
  private boolean                  isDraggingEvents          = false;
  private int                      dragLastPositionX;
  private int                      dragLastPositionY;

  private Sequence                 copyDragOriginalEvents = new Sequence();
  private ArrayList<SequenceEvent> copyDragOriginalEventsInSequence;

  private GraphicsStack graphicsStack = new GraphicsStack();


  /**
   * @param automationMaster
   * @param laneView
   * @param sequence
   */
  public PlayableSequenceUiGraph(LaneView laneView, PlayableSequence sequence) {
    super();
    this.laneView = laneView;
    this.sequence = sequence;

    this.addMouseListener(mouseListener);
    this.addMouseMotionListener(mouseListener);
    sequence.addIObserver(this);
    draggingEvents.addIObserver(this);
    this.setBackground(Color.blue);
    this.addKeyListener(new KeyListenerAdapter());
    this.setFocusable(true);

    //this.setHorizontalZoom(Lane.DEFAULT_ZOOM_MS_PER_PIXEL);
  }


  /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    graphicsStack.push(g);

    // paint background and centerLine
    graphicsStack.push(g);
    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.gray);

    laneView.drawLine(0, .5f, 1, .5f, g);
    graphicsStack.pop(g);

    // paint the snapGrid
    drawSnapGrid(g);

    // draw the automation Line
    drawAutomationLine(g);
    drawSelectionRect(g);
    
    graphicsStack.pop(g);
  }


  //this is basically an iterator that iterators over two Sequences simultaneously.  SequenceEvents are returned in time order.
  private class SequenceZipper {
    private Sequence s1, s2;
    private SequenceIterator s1It, s2It;
    private SequenceEvent    s1Current;
    private SequenceEvent    s2Current;


    SequenceZipper(Sequence s1, Sequence s2) {
      this.s1 = s1;
      this.s2 = s2;
      reset();
    }


    SequenceEvent next() {
      SequenceEvent ret = null;

      if ((s1Current == null) && (s2Current == null)) {
        // no more events in either sequence
        return ret;
      }

      if ((s1Current != null) && (s2Current != null)) {
        if (s1Current.getTime() < s2Current.getTime()) {
          ret = s1Current;
          s1Current = s1It.next();
        } else {
          ret = s2Current;
          s2Current = s2It.next();
        }
      } else if ((s1Current != null) && (s2Current == null)) {
        ret = s1Current;
        s1Current = s1It.next();
      } else if ((s1Current == null) && (s2Current != null)) {
        ret = s2Current;
        s2Current = s2It.next();
      }
      return ret;
    }


    public void reset() {
      s1It = s1.iterator();
      s2It = s2.iterator();
      s1Current = s1It.next();
      s2Current = s2It.next();
    }
  }


  private void drawAutomationLine(Graphics g) {
    SequenceZipper zipper = new SequenceZipper(sequence, draggingEvents);
    graphicsStack.push(g);
    g.setColor(AUTOMATION_LINE_COLOR);

    long leftPtX = 0;
    float leftPtY = 0;
    long rightPtX = 0;
    float rightPtY = 0;
    boolean isSelected = false;
    SequenceEvent current = zipper.next();

    if (current == null) {
      // nothing to draw
      return;
    }
    // draw the first segment
    rightPtX = current.getTime();
    rightPtY = current.getValue();
    leftPtX = 0;
    leftPtY = rightPtY;
    laneView.drawLine(laneView.timeToGraphX(leftPtX),
                       leftPtY,
                       laneView.timeToGraphX(rightPtX),
                       rightPtY,
                       g);
    drawHandle(rightPtX, rightPtY, selectedEvents.contains(current), g);
    current = zipper.next();

    // draw the middle segments
    while (current != null) {

      leftPtX = rightPtX;
      leftPtY = rightPtY;
      rightPtX = current.getTime();
      rightPtY = current.getValue();

      laneView.drawLine(laneView.timeToGraphX(leftPtX),
                         leftPtY,
                         laneView.timeToGraphX(rightPtX),
                         rightPtY,
                         g);

      if (selectedEvents.contains(current)) {
        isSelected = true;
      } else if (draggingEvents.contains(current)) {
        isSelected = true;
      }
      isSelected = selectedEvents.contains(current);
      drawHandle(rightPtX, rightPtY, isSelected, g);
      current = zipper.next();
    }
    // draw the end segment
    laneView.drawLine(laneView.timeToGraphX(rightPtX),
                       rightPtY,
                       1,
                       rightPtY,
                       g);

    graphicsStack.pop(g);
  }


  //
  // private void drawAutomationLine(Sequence sequence, Graphics g) {
  // graphicsStack.push(g);
//
//    g.setColor(AUTOMATION_LINE_COLOR);
//    SequenceEvent sequenceEvent;
//    sequenceEvent = sequence.getFirst();
//
//    long leftPtX = 0;
//    float leftPtY = 0;
//    if (sequenceEvent != null) { // empty sequence;
//      leftPtY = sequenceEvent.getValue();
//    }
//
//    long rightPtX = 0;
//    float rightPtY = 0;
////    float x1, x2, y1, y2;
//
////    long firstSelectedEventsSeqTime = selectedEventsSeq.getFirstTime();
////    long lastSelectedEventsSeqTime = selectedEventsSeq.getLastTime();
//
//    // if after selectedEvents
//    for (SequenceEvent each : sequence) {
//      rightPtX = each.getTime();
//      rightPtY = each.getValue();
//
//      laneView.drawLine(laneView.timeToGraphX(leftPtX),
//                         leftPtY,
//                         laneView.timeToGraphX(rightPtX),
//                         rightPtY,
//                         g);
//      boolean isSelected = totalSequenceBufferSelectedEvents.contains(each);
//
//      drawHandle(rightPtX, rightPtY, isSelected, g);
//      leftPtX = rightPtX;
//      leftPtY = rightPtY;
//
//    }
//
//    // draw the last segment from the last event to the end.
//    laneView.drawLine(laneView.timeToGraphX(leftPtX), leftPtY, 1, leftPtY, g);
//
//    graphicsStack.pop(g);
//  }

//
//  private void drawSelectedEventsSeq(Graphics g) {
//    if(draggingEvents == null){
//      //nothing to draw.
//      return;
//    }
//    SequenceEvent sequenceEvent;
//    sequenceEvent = draggingEvents.getFirst();
//    if (sequenceEvent == null) { // empty sequence;
//      //nothing to draw.
//      return;
//    }
//    
//    graphicsStack.push(g);
//
//    g.setColor(Color.red);
//
//    long leftPtTime = 0;float leftPtValue = 0;
//    long rightPtTime = 0;float rightPtValue = 0;
//    
//    SequenceEvent startPt = sequence.getEventBeforeTime(draggingEvents.getFirstTime());
//    
//    if(startPt != null){
//      leftPtTime = startPt.getTime();
//      leftPtValue = startPt.getValue();
//    } else {
//      leftPtTime = 0;
//      leftPtValue = draggingEvents.getFirst().getValue();
//    }
//
//    for (SequenceEvent each : draggingEvents) {
//      rightPtTime = each.getTime();
//      rightPtValue = each.getValue();
//
//      laneView.drawLine(laneView.timeToGraphX(leftPtTime),
//                         leftPtValue,
//                         laneView.timeToGraphX(rightPtTime),
//                         rightPtValue,
//                         g);
//      boolean isSelected = selectedEvents.contains(each);
//
//      drawHandle(rightPtTime, rightPtValue, isSelected, g);
//      leftPtTime = rightPtTime;
//      leftPtValue = rightPtValue;
//    }
//
//
//    SequenceEvent endPt = sequence.getEventAfterTime(draggingEvents.getLastTime());
//
//    if (endPt == null) {
//      // we are drawing the very last segment.
//      // draw the last segment from the last event in the selectedSequence to
//      // the end.
//      // rightPtY should still hold the last Y value in the sequence.
//      laneView.drawLine(laneView.timeToGraphX(leftPtTime),
//                         leftPtValue,
//                         1,
//                         rightPtValue,
//                         g);
//    } else {
//      rightPtTime = endPt.getTime();
//      rightPtValue = endPt.getValue();
//      laneView.drawLine(laneView.timeToGraphX(leftPtTime),
//                         leftPtValue,
//                         laneView.timeToGraphX(rightPtTime),
//                         rightPtValue,
//                         g);
//
//    }
//    graphicsStack.pop(g);
//  }
//

  private void drawSelectionRect(Graphics g) {
    graphicsStack.push(g);

    g.setColor(SELECTION_RECTANGLE_FILL_COLOR);
    // draw the selection rectangle.
    if (selectionRect != null) {
      int x = (int) selectionRect.getX();
      int y = (int) selectionRect.getY();

      int width = (int) selectionRect.getWidth();
      int height = (int) selectionRect.getHeight();
      if (width < 0) {
        x = x + width;
      }
      if (height < 0) {
        y = y + height;
      }

      g.setColor(SELECTION_RECTANGLE_FILL_COLOR);
      g.fillRect(x,
                 y,
                 Math.abs(selectionRect.width),
                 Math.abs(selectionRect.height));
      g.setColor(SELECTION_RECTANGLE_COLOR);
      g.drawRect(x,
                 y,
                 Math.abs(selectionRect.width),
                 Math.abs(selectionRect.height));
      
    }
    graphicsStack.pop(g);
  }

  
  private void drawHandle(long time, float value, boolean selected, Graphics g) {
    graphicsStack.push(g);

    g.setColor(this.HANDLE_COLOR);
    int x = laneView.timeToScreenX(time);
    int y = laneView.toScreenY(value);

    g.drawRect(x - (HANDLE_SIZE / 2),
               y - (HANDLE_SIZE / 2),
               HANDLE_SIZE,
               HANDLE_SIZE);
    if (selected) {
      g.setColor(HANDLE_COLOR_SELECTED);
      g.drawRect(x - ((HANDLE_SIZE + 2) / 2),
                 y - ((HANDLE_SIZE + 2) / 2),
                 HANDLE_SIZE + 2,
                 HANDLE_SIZE + 2);
    }
    graphicsStack.pop(g);

  }


  private void drawSnapGrid(Graphics g) {
    // snapGrid
    g.setColor(SNAP_GRID_COLOR);
    long firstLineTime = laneView.getViewStartTime()
                         - (laneView.getViewStartTime() % timeSnap) + timeSnap;
    for (long i = firstLineTime; i <= laneView.getViewEndTime(); i += timeSnap) {

      g.drawLine(laneView.timeToScreenX(i),
                 laneView.toScreenY(0),
                 laneView.timeToScreenX(i),
                 laneView.toScreenY(1));
    }


    // Major grid
    g.setColor(MAJOR_GRID_COLOR);
    firstLineTime = laneView.getViewStartTime()
                    - (laneView.getViewStartTime() % majorGridTime)
                    + majorGridTime;
    for (long i = firstLineTime; i <= laneView.getViewEndTime(); i += majorGridTime) {

      g.drawLine(laneView.timeToScreenX(i),
                 laneView.toScreenY(0),
                 laneView.timeToScreenX(i),
                 laneView.toScreenY(1));
    }

  }


  private void dropDraggedEvents() {

    // if 0 or 1 selected event we don't need to delete anything.
//    if (draggingEvents.size() > 1) {
//      long firstDraggedEventTime = draggingEvents.get(0).getTime();
//      long lastDraggedEventTime = draggingEvents.get(draggingEvents.size() - 1)
//                                                .getTime();
//      sequence.remove(firstDraggedEventTime, lastDraggedEventTime);
//    }

    selectedEvents.clear();
    
    // add the selectedEventsSeq into sequence.
    for (SequenceEvent each : draggingEvents) {
      selectedEvents.add(sequence.add(each));
    }

    if (isCopyDragging) {
      isCopyDragging = false;
      copyDragOriginalEventsInSequence = null;
    }
    draggingEvents.clear();

  }


  /** @return the editMode */
  public EditModes getEditMode() {
    return editMode;
  }


  private void getEventsInSelectionRect(MouseEvent event) {
    if (selectionRect != null) {
      long startTime = 0;
      long endTime = 0;
      laneView.ScreenXToTime(event.getY());

      if (selectionRect.width >= 0) {
        startTime = laneView.ScreenXToTime(selectionRect.x);
        endTime = laneView.ScreenXToTime(selectionRect.width + selectionRect.x);
      } else {
        startTime = laneView.ScreenXToTime(selectionRect.x
                                            + selectionRect.width);
        endTime = laneView.ScreenXToTime(selectionRect.x);
      }
      float lowValue = 0;
      float highValue = 0;


      if (selectionRect.height >= 0) {
        lowValue = laneView.ScreenYToValue(selectionRect.y
                                            + selectionRect.height);
        highValue = laneView.ScreenYToValue(selectionRect.y);
      } else {
        lowValue = laneView.ScreenYToValue(selectionRect.y);
        highValue = laneView.ScreenYToValue(selectionRect.height
                                             + selectionRect.y);

      }

      ArrayList<SequenceEvent> eventsInTimeRange = sequence.getEventsByTime(startTime,
                                                                            endTime);
      ArrayList<SequenceEvent> eventsInSelection = new ArrayList<SequenceEvent>();
      for (SequenceEvent each : eventsInTimeRange) {
        if ((each.getValue() > lowValue) && (each.getValue() < highValue)) {
          eventsInSelection.add(each);
        }
      }

      if ((event.getModifiers() & event.SHIFT_MASK) == event.SHIFT_MASK) {
        selectedEvents.addAll(eventsInSelection);
      } else {
        selectedEvents = eventsInSelection;
      }
    }
    selectionRect = null;
  }


  private long getNearestTimeSnap(long time) {
    long retTime = 0;
    if (time % timeSnap > timeSnap / 2) {
      // snap up.
      retTime = time + timeSnap - (time % timeSnap);
    } else {
      // snap down.
      retTime = time - (time % timeSnap);
    }
    return retTime;
  }
  long getViewEndTime(){
    return laneView.getViewEndTime();
  }

  long getViewStartTime(){
    return laneView.getViewStartTime();
  }
  
  void setViewEndTime(long time){
    laneView.setViewEndTime(time);
    repaint();
  }
  
  void setViewStartTime(long time){
    laneView.setViewStartTime(time);
    repaint();
  }

  /** @param editMode the editMode to set */
  public void setEditMode(EditModes editMode) {
    this.editMode = editMode;
  }


  private void setIsCopyDragging(boolean isCopyDragging) {
    if(isCopyDragging == true){
      copyDragOriginalEventsInSequence = sequence.addAll(copyDragOriginalEvents, 0);
    }else {
      if(copyDragOriginalEventsInSequence != null){
        sequence.remove(copyDragOriginalEventsInSequence);
        copyDragOriginalEventsInSequence.clear();
      }
    }
    
    this.isCopyDragging = isCopyDragging;
  }


  private void setIsDraggingEvents(boolean isDraggingEvents) {
    this.isDraggingEvents = isDraggingEvents;

    draggingEvents.clear();

    if (isDraggingEvents == true) {
      sequence.pushUndoRestorePoint();
      draggingEvents.addAll(selectedEvents, 0);
      copyDragOriginalEvents.addAll(selectedEvents, 0);

      if (!isCopyDragging) {
        sequence.remove(selectedEvents);
      }
      selectedEvents = draggingEvents.getAllEvents();
      
    } else {
      //if we are no longer dragging events.
      if(copyDragOriginalEvents != null){
        copyDragOriginalEvents.clear(); 
      }
      if(copyDragOriginalEventsInSequence != null){
        copyDragOriginalEventsInSequence.clear();
      }
    }
  }


  public void setMajorGridTime(long time) {
    this.majorGridTime = time;
  }


  public void setTimeSnap(long timeSnap) {
    this.timeSnap = timeSnap;
  }


  /** @param isTimeSnapEnabled the isTimeSnapEnabled to set */
  public void setTimeSnapEnabled(boolean isTimeSnapEnabled) {
    this.isTimeSnapEnabled = isTimeSnapEnabled;
  }

//  private float horizontalMsPerPixel;
//  //private float verticalMsPerPixel;
//  
//  public static float DEFAULT_ZOOM_MS_PER_PIXEL = 100;
  //private static int PROJECT_ENDTIME_MOCK = 20000;
  
//  
//  public void setHorizontalZoom(float viewMs){
//
//    this.horizontalMsPerPixel = viewMs;
//    this.updateSize();
//
//  }
//  
//  public void setVerticalZoom(float zoom){
//    //verticalZoom = zoom;
//    this.updateUI();
//  }

  /** @return the isTimeSnapEnabled */
  public boolean isTimeSnapEnabled() {
    return isTimeSnapEnabled;

  }
  private void transactionAddEvent(long time, float value){
    sequence.pushUndoRestorePoint();
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    SequenceEvent newEvent;
    newEvent = sequence.add(new Event(time, value));
    selectedEvents.add(newEvent);

  }
  
  private void transactionDeleteSelectedEvents(){
    
    sequence.pushUndoRestorePoint();
    ArrayList<SequenceEvent> selectedEventsToDelete = new ArrayList<SequenceEvent>(selectedEvents.size());
    selectedEventsToDelete.addAll(selectedEvents);
    for(SequenceEvent each: selectedEventsToDelete){
      sequence.remove(each);
    }
  }

  private void transactionStartRecording(){
    
  }


  private class MouseListener extends MouseInputAdapter {
    public void mousePressed(MouseEvent event) {
      int x = event.getX();
      int y = event.getY();
      long time = laneView.ScreenXToTime(x);
      float value = laneView.ScreenYToValue(y);

      requestFocus();
      if (event.getButton() == event.BUTTON1) {

        // figure out if we clicked on a handle.
        long timeDifference = laneView.ScreenXToTime(8) - laneView.ScreenXToTime(0);
        float valueDifference = 1 - laneView.ScreenYToValue(8);
        SequenceEvent selectedEvent = sequence.getEventClosestTo(time,
                                                                 value,
                                                                 timeDifference,
                                                                 valueDifference);

        // if we did click on a handle
        if (selectedEvent != null) {
          // Event selectedEvent = sequence.getEvent(selectedEventUuid);
          // double selectedEventScreenX =
          // laneView.timeToScreenX(selectedEvent.getTime());
          // double selectedEventScreenY =
          // laneView.toScreenY(selectedEvent.getValue());
          // double distance = Math.sqrt((x - selectedEventScreenX)
          // * (x - selectedEventScreenX)
          // + (y - selectedEventScreenY)
          // * (y - selectedEventScreenY));

          // if we are less than 6 pixels away then we are actually clicking on
          // it.
          // if (distance < 6) {


          // if we clicked on an already selected event begin dragging.
          if (selectedEvents.contains(selectedEvent)) {
            setIsDraggingEvents(true);
            // dragLastPositionX = x;
            // dragLastPositionY = y;
            dragLastPositionX = laneView.timeToScreenX(selectedEvent.getTime());
            dragLastPositionY = laneView.toScreenY(selectedEvent.getValue());
            if (event.isMetaDown()) {
              setIsCopyDragging(true);
            }
          } else {
            if (!event.isShiftDown()) {
              selectedEvents.clear();
            }
            selectedEvents.add(selectedEvent);
            setIsDraggingEvents(true);


            // dragLastPositionX = x;
            // dragLastPositionY = y;
            dragLastPositionX = laneView.timeToScreenX(selectedEvent.getTime());
            dragLastPositionY = laneView.toScreenY(selectedEvent.getValue());
            return;
          }


          // if we didn't click on a handle
        } else {
          selectionRect = new Rectangle(event.getX(), event.getY(), 0, 0);
          if (!event.isShiftDown()) {
            selectedEvents.clear();
          }


          if (event.isControlDown()) {
            transactionAddEvent(time, value);
            isDraggingEvents = true;
            dragLastPositionX = x;
            dragLastPositionY = y;
          }
        }
      }

      // finally, repaint.
      Component c = event.getComponent();
    }


    public void mouseDragged(MouseEvent event) {
      if (event.getButton() == event.BUTTON1) {
        if (getEditMode() == EditModes.SELECT) {

          int x = event.getX();
          int y = event.getY();


          long nearestSnapTime = 0;
          if (isDraggingEvents) {

            // see if we need to snap to the nearest snap time.
            if (isTimeSnapEnabled()) {
              nearestSnapTime = getNearestTimeSnap(laneView.ScreenXToTime(x));
              // x = laneView.timeToScreenX(nearestSnapTime);

            }

            int dragOffsetX = dragLastPositionX - x;
            int dragOffsetY = dragLastPositionY - y;

            long dragOffsetTime = laneView.ScreenXToTime(0)
                                  - laneView.ScreenXToTime(dragOffsetX);
            float dragOffsetValue = laneView.ScreenYToValue(0)
                                    - laneView.ScreenYToValue(dragOffsetY);

            // figure out the snap offset
            long snapOffset = 0;// getNearestTimeSnap(selectedEventsSeq.get(0).getTime()
                                // +dragOffsetTime) -
                                // selectedEventsSeq.get(0).getTime();


            ArrayList<SequenceEvent> draggingEventsToModify = draggingEvents.getAllEvents();
            for (SequenceEvent each : draggingEventsToModify) {
              long newTime = each.getTime() + dragOffsetTime + snapOffset;
              float newValue = each.getValue() + dragOffsetValue;
              each.setTime(newTime);
              each.setValue(newValue);
            }
            dragLastPositionX = x;// laneView.timeToScreenX(selectedEvents.get(0).getTime());
            dragLastPositionY = y;
          }
        }
        updateSelectionRectSize(event);
        repaint();
      }
    }


    public void mouseReleased(MouseEvent event) {
      if (isDraggingEvents) {
        dropDraggedEvents();
        setIsDraggingEvents(false);
      }
      // If event selection is happening. get all the events that are inside the
      // selectionRect
      if (selectionRect != null) {
        getEventsInSelectionRect(event);
      }
      event.getComponent().repaint();
    }
  }


  private class KeyListenerAdapter extends java.awt.event.KeyAdapter {
    @Override
    public void keyPressed(KeyEvent keyEvent) {
      int keyCode = keyEvent.getKeyCode();
      if (keyCode == KeyEvent.VK_META) {
        if (isDraggingEvents) {
          setIsCopyDragging(true);
        }
      }
      if (keyEvent.isMetaDown() && keyEvent.isShiftDown() && ((keyCode & KeyEvent.VK_Z) == KeyEvent.VK_Z)) {
        sequence.redo();
      } else 
      if ((keyEvent.isMetaDown()) && ((keyCode & KeyEvent.VK_Z) == KeyEvent.VK_Z)) {
        sequence.undo();
      }

      
      if (keyCode == KeyEvent.VK_BACK_SPACE){
        transactionDeleteSelectedEvents();
      }
    }


    @Override
    public void keyReleased(KeyEvent keyEvent) {
      int keyCode = keyEvent.getKeyCode();
      if (keyCode == KeyEvent.VK_META) {
        setIsCopyDragging(false);
      }
    }
  }


  public void updateSelectionRectSize(MouseEvent event) {
    if (selectionRect == null) {
      return;
    }
    int x = event.getX();
    int y = event.getY();

    int originX = (int) selectionRect.getX();
    int originY = (int) selectionRect.getY();
    int newWidth = x - (int) selectionRect.getX();
    int newHeight = y - (int) selectionRect.getY();
    selectionRect.setRect(originX, originY, newWidth, newHeight);
  }


  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object,
   *      java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    // either our main sequence or or draggedEvents sequence has changed.
    if((changeCode == Sequence.SequenceChangeCodes.EVENT_TIME_CHANGED) || (changeCode == Sequence.SequenceChangeCodes.EVENT_ADDED)){
//
//      long lastTimeInSequence = sequence.getLastTime();
////
//      long roughEndTime = lastTimeInSequence + (int)(.1f * lastTimeInSequence);
//      
//      //always show at least 30 seconds
//      if(roughEndTime < 30000){
//        roughEndTime = 30000;
//      }

      //laneView.setViewStartTime(0);
      //laneView.setViewEndTime(roughEndTime);
      //updateSize();
    }

    this.repaint();
  }
//
//  private void updateSize(){
//    int newWidth = (int)((laneView.getViewEndTime()-laneView.getViewStartTime()) / horizontalMsPerPixel);
//    //if(newWidth < 
//    Dimension d = new Dimension(newWidth , this.getHeight());
//    this.setPreferredSize(d);
//    this.setMinimumSize(d);
//    this.revalidate(); 
//    JRootPane parent = this.getRootPane();
//    if(parent != null){
//      parent.revalidate();
//    }
//  }

  /** @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent) */
  @Override
  public void keyPressed(KeyEvent keyEvent) {

  }


  /** @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent) */
  @Override
  public void keyReleased(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }


  /** @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent) */
  @Override
  public void keyTyped(KeyEvent arg0) {
    // TODO Auto-generated method stub

  }
}
