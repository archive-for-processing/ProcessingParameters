/**
 * 
 */
package floatingpointdev.parameters;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import floatingpointdev.parameters.automationcontroller.Event;
import floatingpointdev.parameters.automationcontroller.Sequence;
import floatingpointdev.parameters.automationcontroller.SequenceEvent;
import floatingpointdev.parameters.automationcontroller.SequenceIterator;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class TestSequence {
  private static float FLOAT_EQUALS_DELTA = .000001f;
  
  private Sequence s;
  private Event e1;
  private Event e2;
  private Event e3;
  private Event e4;
  private Event e5;
  private Event e6;
  
  
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    s = new Sequence();
    e1 = new Event(1, .1f);
    e2 = new Event(2, .2f);
    e3 = new Event(3, .3f);
    e4 = new Event(4, .4f);
    e5 = new Event(5, .5f);
    e6 = new Event(6, .6f);    
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#add(floatingpointdev.parameters.automationcontroller.Event)}.
   */
  @Test
  public void testAdd() {
    s.add(e1);
    assertEquals(1, s.size());
    s.add(e2);
    assertEquals(2, s.size()); 
    s.add(e3);
    assertEquals(3, s.size()); 
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(e1.getTime(), events.get(0).getTime());
    assertEquals(e2.getTime(), events.get(1).getTime());
    assertEquals(e3.getTime(), events.get(2).getTime());
    
    //shouldn't add null.
    Event nullEvent = null;
    s.add(nullEvent);
    assertEquals(3, s.size());
  }

  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#add(floatingpointdev.parameters.automationcontroller.Event)}.*/
  @Test
  public void testAddDuplicates() {
    s.add(e1);
    s.add(e2);
    s.add(e3);
    assertEquals(3, s.size()); 
    
    s.add(e3);
    assertEquals(3, s.size());
    s.add(e2);
    assertEquals(3, s.size());
    s.add(e1);
    assertEquals(3, s.size());

  }
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#add(floatingpointdev.parameters.automationcontroller.Event)}.*/
  @Test
  public void testAddBetweenEvents() {
    s.add(new Event(10, .1f));
    s.add(new Event(20, .2f));
    s.add(new Event(15, .15f));
    
    Iterator<SequenceEvent> it = s.iterator();
    assertEquals(10, it.next().getTime());
    assertEquals(15, it.next().getTime());
    assertEquals(20, it.next().getTime());
  }
  
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#add(floatingpointdev.parameters.automationcontroller.Event)}.*/
  @Test
  public void testAddReverseOrder() {
    s.add(e2);
    s.add(e1);
    assertEquals(2, s.size()); 
    
    Iterator<SequenceEvent> it = s.iterator();
    assertEquals(e1.getTime(), it.next().getTime());
    assertEquals(e2.getTime(), it.next().getTime());
  }

  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#addAll(floatingpointdev.parameters.automationcontroller.Sequence, long)}.
   */
  @Test
  public void testAddAll() {
    Sequence s2 = new Sequence();
    
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    SequenceEvent se4 = s2.add(e4);
    SequenceEvent se5 = s2.add(e5);
    SequenceEvent se6 = s2.add(e6);
    
    s.addAll(s2, 0);
    
    assertEquals(6, s.size());
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(e1.getTime(), events.get(0).getTime());
    assertEquals(e2.getTime(), events.get(1).getTime());
    assertEquals(e3.getTime(), events.get(2).getTime());
    assertEquals(e4.getTime(), events.get(3).getTime());
    assertEquals(e5.getTime(), events.get(4).getTime());
    assertEquals(e6.getTime(), events.get(5).getTime());
  }
  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#addAll(floatingpointdev.parameters.automationcontroller.Sequence, long)}.
   */
  @Test
  public void testAddAllIntermixed() {
    Sequence s2 = new Sequence();
    
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s2.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    SequenceEvent se4 = s2.add(e4);
    SequenceEvent se5 = s.add(e5);
    SequenceEvent se6 = s2.add(e6);
    
    s.addAll(s2, 0);
    
    assertEquals(6, s.size());
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(e1.getTime(), events.get(0).getTime());
    assertEquals(e2.getTime(), events.get(1).getTime());
    assertEquals(e3.getTime(), events.get(2).getTime());
    assertEquals(e4.getTime(), events.get(3).getTime());
    assertEquals(e5.getTime(), events.get(4).getTime());
    assertEquals(e6.getTime(), events.get(5).getTime());
  }
  
  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#addAll(floatingpointdev.parameters.automationcontroller.Sequence, long)}.
   */
  @Test
  public void testAddAllDuplicates() {
    Sequence s2 = new Sequence();
    
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    SequenceEvent se1dupe = s2.add(e1);
    SequenceEvent se2dupe = s2.add(e2);
    SequenceEvent se3dupe = s2.add(e3);
    
    s.addAll(s2, 0);
    
    assertEquals(3, s.size());
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(e1.getTime(), events.get(0).getTime());
    assertEquals(e2.getTime(), events.get(1).getTime());
    assertEquals(e3.getTime(), events.get(2).getTime());

  }

  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#clear()}.
   */
  @Test
  public void testClear() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    assertEquals(3, s.size()); 
    
    s.clear();
    assertEquals(0, s.size()); 
    
    assertNull(s.getFirst());
    assertFalse(s.contains(se1));
    assertFalse(s.contains(se2));
    assertFalse(s.contains(se3));
  }


//  /**
//   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#contains(floatingpointdev.parameters.automationcontroller.SequenceEvent)}.
//   */
//  @Test
//  public void testContains() {
//    s.add(e1);
//    s.add(e2);
//    s.add(e3);
//    fail("Not yet implemented");
//    //assertTrue(s.contains(sequenceEvent)
//  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#getEventsByTime(long, long)}.
   */
  @Test
  public void testGetEventsByTime() {
    s.add(e1);
    s.add(e2);
    s.add(e3);
    s.add(e4);
    s.add(e5);    
    s.add(e6);
    
    ArrayList<SequenceEvent> events = s.getEventsByTime(e1.getTime(), e6.getTime());
    assertEquals(6, events.size());
    assertEquals(e1.getTime(), events.get(0).getTime());
    assertEquals(e5.getTime(), events.get(4).getTime()); 
    
    events = s.getEventsByTime(e1.getTime(), e6.getTime()+1);
    assertEquals(6, events.size());
    assertEquals("Should return all events", e1.getTime(), events.get(0).getTime());
    
    
    events = s.getEventsByTime(e1.getTime(), e1.getTime());
    assertEquals(1, events.size());
    assertEquals("Should return all but the last event.", e1.getTime(), events.get(0).getTime());
    
    
    events = s.getEventsByTime(1000000, 10000001);
    assertEquals("empty selection past our last event should return nothing", 0, events.size());
    
    //start time greater than end time should return nothing.
    events = s.getEventsByTime(e5.getTime(), e1.getTime());
    assertEquals("start time greater than end time should return nothing.", 0, events.size());
    
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#iterator()}.
   */
  @Test
  public void testIterator() {
    s.add(e1);
    s.add(e2);
    s.add(e3);
    s.add(e4);
    s.add(e5);    
    s.add(e6);
    
    Iterator<SequenceEvent> it = s.iterator();
    ArrayList<SequenceEvent> events = s.getAllEvents();
    int i = 0;
    while(it.hasNext()){
      SequenceEvent each = it.next();
      assertEquals(events.get(i), each);
      ++i;
    }
  }

//
//  /**
//   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#remove(long, long)}.
//   */
//  @Test
//  public void testRemoveLongLong() {
//    fail("Not yet implemented");
//  }

  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#remove(long)}.*/
  @Test
  public void testRemove() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    
    assertEquals(2, s.size()); 
    s.remove(se1);

    assertEquals(se2, s.getFirst());
    
    s.remove(se2);
    assertEquals(0, s.size()); 
  }

  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#remove(long)}.*/
  @Test
  public void testRemoveMiddle() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    assertEquals(3, s.size()); 
    s.remove(se2);

    assertEquals(se1, s.getFirst());
    assertEquals(se3, s.getFirst().next());

  }
  
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#remove(long)}.*/
  @Test
  public void testRemoveLast() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    assertEquals(3, s.size()); 
    s.remove(se3);

    assertEquals(se1, s.getFirst());
    assertEquals(se2, s.getFirst().next());

  }



  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#setEventTime(floatingpointdev.parameters.automationcontroller.SequenceEventNode, long)}.
   */
  @Test
  public void testSetEventTime() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    assertEquals(3, s.size()); 
    
    se1.setTime(500);
    assertEquals(se2, s.getFirst());
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(se2.getTime(), s.getFirst().getTime());
    assertEquals(500, events.get(2).getTime());
  }
  
  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.Sequence#setEventTime(floatingpointdev.parameters.automationcontroller.SequenceEventNode, long)}.
   */
  @Test
  public void testSetEventTimeEndToBeginning() {
    SequenceEvent se1 = s.add(e1);
    SequenceEvent se2 = s.add(e2);
    SequenceEvent se3 = s.add(e3);
    
    assertEquals(3, s.size()); 
    
    se3.setTime(0);
    assertEquals(se3, s.getFirst());
    
    ArrayList<SequenceEvent> events = s.getAllEvents();
    assertEquals(se3.getTime(), s.getFirst().getTime());
    assertEquals(se1.getTime(), events.get(1).getTime());
    assertEquals(se2.getTime(), events.get(2).getTime());
  }

  
  @Test
  public void testGetClosestEvent(){
    SequenceEvent se1 = s.add(new Event(10,.1f));
    SequenceEvent se2 = s.add(new Event(20,.2f));
    SequenceEvent se3 = s.add(new Event(30,.3f));
    
    SequenceEvent event;
    
    event = s.getEventClosestTo(10, .1f, 0, 0);
    assertEquals(se1, event);
    event = s.getEventClosestTo(20, .2f, 0, 0);
    assertEquals(se2, event);
    event = s.getEventClosestTo(30, .3f, 0, 0);
    assertEquals(se3, event);
    
    event = s.getEventClosestTo(7, .1f, 7, 0);
    assertEquals(se1, event);
    event = s.getEventClosestTo(24, .2f, 7, 0);
    assertEquals(se2, event);
    event = s.getEventClosestTo(26, .3f, 7, 0);
    assertEquals(se3, event);
    
    event = s.getEventClosestTo(10, .11f, 0, .05f);
    assertEquals(se1, event);
    event = s.getEventClosestTo(20, .22f, 0, .05f);
    assertEquals(se2, event);
    event = s.getEventClosestTo(30, .33f, 0, .05f);
    assertEquals(se3, event);
    
    event = s.getEventClosestTo(9, .11f, 0, 0);
    assertNull(event);
    event = s.getEventClosestTo(19, .22f, 0, 0);
    assertNull(event);
    event = s.getEventClosestTo(29, .33f, 0, 0);
    assertNull(event);
    
    event = s.getEventClosestTo(6, .11f, 2, 0);
    assertNull(event);
    event = s.getEventClosestTo(17, .22f, 2, 0);
    assertNull(event);
    event = s.getEventClosestTo(27, .33f, 2, 0);
    assertNull(event);
  }
}
