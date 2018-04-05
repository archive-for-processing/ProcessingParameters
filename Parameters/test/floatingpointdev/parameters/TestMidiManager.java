/**
 * 
 */
package floatingpointdev.parameters;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import floatingpointdev.parameters.MidiController;
import floatingpointdev.parameters.Parameter;
import floatingpointdev.parameters.ParametersManager;
import floatingpointdev.toolkit.midi.MidiTool;

/**
 * @author floatingpointdev
 *
 */
public class TestMidiManager {
  float FLOAT_EQUALS_DELTA = .000001f;
  private MockIoMidi ioMidi;
  private ParametersManager parametersManager;
  
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    ioMidi = new MockIoMidi();
    parametersManager = new ParametersManager(ioMidi);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#MidiManager(floatingpointdev.parameters.Parameter, floatingpointdev.toolkit.midi.IoMidi)}.
   */
  @Test
  public void testMidiManager() {
    MidiController m = new MidiController(ioMidi);
    assertNotNull(m);
  }

  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#MidiManager(floatingpointdev.parameters.Parameter, floatingpointdev.toolkit.midi.IoMidi)}.
   */
  @Test(expected=IllegalArgumentException.class)
  public void testMidiManagerInvalidArguments() {
    MidiController m;
    m = new MidiController(ioMidi);
    m = new MidiController(null);    
  }
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessageControlChange() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");

    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CONTROL_CHANGE);
    midiController.setMidiInputNumber(1);
    
    //test sending a null message.
    p1.set(0);
    midiController.processMessage(null, null);
    assertEquals("Parameters value should not have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 0));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(5, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different controller number.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 5, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
  }

  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessageNoteOnVelocity() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.NOTE_ON_VELOCITY);
    midiController.setMidiInputNumber(1);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(1, 1, 127));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(1, 1, 0));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(5, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
 }
  
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessageNoteOffVelocity() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.NOTE_OFF_VELOCITY);
    midiController.setMidiInputNumber(1);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOff(1, 1, 127));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOff(1, 1, 0));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOff(5, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
 }
  
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessagePitchBend() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.PITCHBEND);
    midiController.setMidiInputNumber(1);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePitchBend(1, 1));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePitchBend(1, -1));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePitchBend(5, 1));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
 }
  
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessageChannelPressure() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CHANNEL_PRESSURE);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageChannelPressure(1, 127));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageChannelPressure(1, 0));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageChannelPressure(5, 1));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
 }
  
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#processMessage(java.lang.Object, java.lang.Object)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testProcessMessagePolyPressure() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    p1.addController(midiController);
    midiController.setControlled(p1);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.POLY_PRESSURE);
    
    //test midiMessage that sets parameter to max.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePolyPressure(1, 1, 127));
    assertEquals("Parameters value should have been changed.", 1.0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage that sets parameter to min.
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePolyPressure(1, 1, 0));
    assertEquals("Parameters value should have been changed.", 0f, p1.get(), FLOAT_EQUALS_DELTA);
    
    //test midiMessage with different message type
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);

    //test midiMessage with different channel
    p1.set(0);
    midiController.processMessage(null, MidiTool.createShortMessagePolyPressure(5, 1, 1));
    assertEquals("Parameters value should not have been changed.", 0.0f, p1.get(), FLOAT_EQUALS_DELTA);
 }
  
  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#isLearningMidiInput()}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testIsLearningMidiInput() throws InvalidMidiDataException {
    MidiController midiController = new MidiController(ioMidi);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CONTROL_CHANGE);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    assertEquals("isLearningMidiInput should return true.", true, midiController.isLearningMidiInput());
    
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 1, 127));
    assertEquals("isLearningMidiInput should return false.", false, midiController.isLearningMidiInput());
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setLearningMidiInput(boolean)}.
   * @throws InvalidMidiDataException 
   */
  @Test
  public void testSetLearningMidiInput() throws InvalidMidiDataException {
    Parameter p1 = new Parameter("p1");
    MidiController midiController = new MidiController(ioMidi);
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CONTROL_CHANGE);
    midiController.setMidiInputNumber(1);
    p1.set(0);
    
    midiController.setLearningMidiInput(true);
    assertEquals("isLearningMidiInput should return true.", true, midiController.isLearningMidiInput());
    
    //Send a control change message to learn from
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(1, 5, 127));
    assertEquals("isLearningMidiInput should return false.", false, midiController.isLearningMidiInput()); 
    assertEquals("The MidiInputNumber should have changed to match the message that was sent.", 5, midiController.getMidiInputNumber());
    assertEquals("The parameters value should not have changed.", 0, p1.get(), FLOAT_EQUALS_DELTA);
    
    //Send a control change message to learn from with a different 
    //channel and different controller number.
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageControlChange(5, 6, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());
    assertEquals("The MidiInputNumber should have changed to match the message that was sent.", 6, midiController.getMidiInputNumber());

    //Test sending a message of an incorrect type.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CONTROL_CHANGE);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOff(5, 1, 127));
    assertEquals("The MidiInputChannel should not have changed.", 1, midiController.getMidiInputChannel());
    
    
    //Test learning from a pitchbend message
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.PITCHBEND);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessagePitchBend(5, 1));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());
    
    //Test learning from a note on message.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.NOTE);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(5, 1, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());
    
    //Test learning from a note on velocity message.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.NOTE_ON_VELOCITY);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOn(5, 1, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());
    
    //Test learning from a noteoff velocity message.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.NOTE_OFF_VELOCITY);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageNoteOff(5, 1, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());

    //Test learning from a channel pressure message.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.CHANNEL_PRESSURE);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessageChannelPressure(5, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());

    //Test learning from a poly pressure message.
    midiController.setMidiInputChannel(1);
    midiController.setMidiInputType(MidiController.MidiInputTypes.POLY_PRESSURE);
    midiController.setMidiInputNumber(1);
    
    midiController.setLearningMidiInput(true);
    midiController.processMessage(null, MidiTool.createShortMessagePolyPressure(5, 1, 127));
    assertEquals("The MidiInputChannel should have changed to match the message that was sent.", 5, midiController.getMidiInputChannel());
  }
  

  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#transmitMidiMessage(floatingpointdev.toolkit.midi.FpdMidiMessageShort)}.
   */
  @Test
  public void testTransmitMidiMessage() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#transmitMidiMessageValue(float, float, float)}.
   */
  @Test
  public void testTransmitMidiMessageValue() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiInputBus(java.lang.String)}.
   */
  @Test
  public void testSetMidiInputBus() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiInputType(floatingpointdev.parameters.MidiController.MidiInputTypes)}.
   */
  @Test
  public void testSetMidiInputType() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#getMidiInputChannel()}.
   */
  @Test
  public void testGetMidiInputChannel() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiInputChannel(int)}.
   */
  @Test
  public void testSetMidiInputChannel() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#getMidiInputNumber()}.
   */
  @Test
  public void testGetMidiInputNumber() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiOutputBus(java.lang.String)}.
   */
  @Test
  public void testSetMidiOutputBus() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiOutputType(floatingpointdev.parameters.MidiController.MidiOutputTypes)}.
   */
  @Test
  public void testSetMidiOutputType() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiOutputChannel(int)}.
   */
  @Test
  public void testSetMidiOutputChannel() {
    fail("Not yet implemented");
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.MidiController#setMidiOutputNumber(int)}.
   */
  @Test
  public void testSetMidiOutputNumber() {
    fail("Not yet implemented");
  }
  
}
