/**
 * 
 */
package floatingpointdev.parameters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import floatingpointdev.parameters.automationcontroller.Event;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class TestEvent {
  private static float FLOAT_EQUALS_DELTA = .000001f;
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }


  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#SequenceEvent(long, float)}.*/
  @Test
  public void testEvent() {
    Event se = new Event(2, .5f);
    assertEquals(se.getTime(), 2);
    assertEquals(se.getValue(), .5, FLOAT_EQUALS_DELTA);
  }
  
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#SequenceEvent(long, float)}.*/
  @Test//(expected = IllegalArgumentException.class)
  public void testEventInvalidValueNegative() {
    Event se = new Event(2, -.5f);
  }

  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#SequenceEvent(long, float)}.*/
  @Test//(expected = IllegalArgumentException.class)
  public void testSequenceEventInvalidValueHigh() {
    Event se = new Event(2, 2f);
  }
  
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#SequenceEvent(long, float)}.*/
  @Test//(expected = IllegalArgumentException.class)
  public void testSequenceEventInvalidTimeNegative() {
    Event se = new Event(-1, .5f);
  }
  
  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#setTime(long)}.*/
  @Test//(expected = IllegalArgumentException.class)
  public void testSetTime() {
    Event se = new Event(0, 0f);
    se.setTime(1);
    assertEquals(1, se.getTime());
    se.setTime(-1);
    assertEquals(0, se.getTime());
  }


  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#setValue(float)}.*/
  @Test//(expected = IllegalArgumentException.class)
  public void testSetValue() {
    Event se = new Event(0, 0f);
    se.setValue(.5f);
    assertEquals(se.getValue(), .5, FLOAT_EQUALS_DELTA);
    se.setValue(-1f);
    assertEquals(se.getValue(), 0, FLOAT_EQUALS_DELTA);
    se.setValue(2f);
    assertEquals(se.getValue(), 1, FLOAT_EQUALS_DELTA);
  }


  /** Test method for {@link floatingpointdev.parameters.automationcontroller.Event#equals(java.lang.Object)}.*/
  @Test
  public void testEqualsObject() {
    Event se1 = new Event(1, .5f);
    Event se2 = new Event(1, .5f);
    Event se3 = new Event(2, .5f);
    assertTrue(se1.equals(se2));
    assertTrue(se1.equals(se1));   
    assertFalse(se1.equals(se3));
  }

}
