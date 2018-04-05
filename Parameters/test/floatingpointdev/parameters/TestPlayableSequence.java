/**
 * 
 */
package floatingpointdev.parameters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import floatingpointdev.parameters.automationcontroller.PlayableSequence;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class TestPlayableSequence {
  private PlayableSequence s1;
  
  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    s1 = new PlayableSequence();
  }


  /*** Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#receiveEvent(float)}.*/
  @Test
  public void testReceiveEvent() {
    s1.receiveEvent(1f);
    assertEquals(0, s1.size());
    s1.receiveEvent(.1f);
    assertEquals(0, s1.size());
  }

  /*** Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#receiveEvent(float)}.*/
  @Test
  public void testReceiveEventWriteEnabled() {
    s1.setWriteEnabled(true);
    s1.play();
    s1.setPosition(1);
    
    s1.receiveEvent(.1f);
    s1.setPosition(2);
    s1.receiveEvent(.2f);
    s1.setPosition(50);
    s1.receiveEvent(.5f);
    
    assertEquals(3, s1.size());
    
    s1.pause();

    
    s1.setPosition(4);
    s1.receiveEvent(.4f);
    
    assertEquals(3, s1.size());
  }

  /*** Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#receiveEvent(float)}.*/
  @Test
  public void testReceiveEventOverWrite() {
    s1.setWriteEnabled(true);
    s1.play();
    s1.setPosition(10);
    
    s1.receiveEvent(.1f);
    s1.setPosition(20);
    s1.receiveEvent(.2f);
    s1.setPosition(50);
    s1.receiveEvent(.9f);
    
    assertEquals(3, s1.size());
    
    s1.pause();

    s1.setPosition(18);
    s1.play();
    
    
    s1.setPosition(22);
    assertEquals(3, s1.size());
    s1.receiveEvent(.99f);
    assertEquals(4, s1.size());

  }
  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#setPosition(long)}.
   */
  @Test
  public void testSetPosition() {
    fail("Not yet implemented");
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#play()}.
   */
  @Test
  public void testPlay() {
    fail("Not yet implemented");
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#pause()}.
   */
  @Test
  public void testPause() {
    fail("Not yet implemented");
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#recordEnable()}.
   */
  @Test
  public void testRecordEnable() {
    fail("Not yet implemented");
  }


  /**
   * Test method for {@link floatingpointdev.parameters.automationcontroller.PlayableSequence#update(java.lang.Object, java.lang.Object)}.
   */
  @Test
  public void testUpdate() {
    fail("Not yet implemented");
  }

}
