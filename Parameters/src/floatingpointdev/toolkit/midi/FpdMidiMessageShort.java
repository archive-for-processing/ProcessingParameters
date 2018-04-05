/**
 * 
 */
package floatingpointdev.toolkit.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

/**
 * This is a thin wrapper around the javax.sound.midi.ShortMessage.  It just adds 
 * some handy message getting functions and some creation functions for 
 * the most common types of midi messages.
 * @author floatingpointdev
 * @see javax.sound.midi.ShortMessage
 *
 */
public class FpdMidiMessageShort extends ShortMessage {

  
  
  //enum Type
  /** defines the different Types of midi Message*/
  enum MidiMessageType {
    CONTROLCHANGE, PITCHBEND, NOTEON, NOTEOFF, SYSEX, OTHER
  };
  
  /** defines the different Types of information that can exist
   * in a midi message.*/
//  enum  {
//    CONTROLCHANGE, PITCHBEND, NOTE, VELOCITY, AFTERTOUCH, SYSEX
//  };  
//  
  
  //private MidiMessageType messageType;
  //private int messageCommand;
  
  /**
   * @param data
   */
  protected FpdMidiMessageShort(byte[] data) {
    super(data);  
  }

  /** @see javax.sound.midi.MidiMessage#clone()*/
  @Override
  public Object clone() {
    // Nothing special to do here.
    return super.clone();
  }
  
  
  /**
   * @return the midi Channel
   */
  public int getChannel(){
    return data[0] & 0x0F;
  }
  
  /**
   * @return the midi Note number if this midi message has note information.
   * If it does not have note information then -1 is returned.
   */
  public int getNote(){
    int messageCommand = getCommand();
    if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
      return data[1] & 0xFF;
    }
    return -1;
  }

  /**
   * @return the midi Velocity value if this midi message has velocity information.
   * If it does not have velocity information then -1 is returned;
   */
  public int getVelocity(){
    int messageCommand = getCommand();
    if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
      return data[2] & 0xFF;
    }
    return -1;
  }
  /**
   * @return the midi CC number if this midi message is a ControlChange message.
   * If this is not a ControlChange message then -1 is returned.
   */
  public int getCCNumber(){
    int messageCommand = getCommand();
    if(messageCommand == ShortMessage.CONTROL_CHANGE){
      return data[1] & 0xFF;
    }
    return -1;
  }
  
  /**
   * @return the midi CC Value if this midi message is a ControlChange message.
   * If this is not a ControlChange message then -1 is returned.
   */
  public int getCCValue(){
    int messageCommand = getCommand();
    if(messageCommand == ShortMessage.CONTROL_CHANGE){
      return (data[2] & 0xFF);
    }
    return -1;
  }
  
  /**
   * @return the pitchbend value if this is a pitchbebd message.
   * If this is not a pitchbend message then -1 is returned.
   */
  public int getPitchBendValue(){
    int messageCommand = getCommand();
    if(messageCommand == ShortMessage.PITCH_BEND){
      int lsb = this.getData1();
      int msb = this.getData2() << 7;
      return msb+lsb;
    }
    return -1;
  }

  /** 
   *  Create any ShortMessage
   * @param command The javax.midi.ShortMessage defined midi command.
   * @param channel 
   * @param param1 the byte for parameter 1.
   * @param param2 the byte for parameter 2.
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessageShort(int command, int channel, int param1, int param2){
    //cheat a little bit here by using the java ShortMessage to construct the proper 
    //byte array.
    ShortMessage helper = new ShortMessage();
    try{
      helper.setMessage(command,channel, param1, param2);
    } catch(InvalidMidiDataException e){
      //DEBUG_ERROR
      //an invalid midi message was constructed.
      return null;
    }
    return new FpdMidiMessageShort(helper.getMessage()); 
  }
  
  /**
   * Create a new FpdMidiMessageShort from an array of bytes that contain the message.
   * @param rawData the midi message to create.
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessage(byte[] rawData){
    
    FpdMidiMessageShort ret = new FpdMidiMessageShort(rawData);
    return ret;
    
  }
  
  /**
   * Create a new FpdMidiMessageShort control change message.
   * @param channel
   * @param ccNumber 
   * @param value
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessageControlChange(int channel, int ccNumber, int value){
    return createFpdMidiMessageShort(ShortMessage.CONTROL_CHANGE ,channel, ccNumber, value);
  }

  /**
   * Create a new FpdMidiMessageShort pitch bend message.
   * @param channel
   * @param value
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessagePitchBend(int channel, int value){
    return createFpdMidiMessageShort(ShortMessage.PITCH_BEND ,channel, value, 0);  //param2 is unused in pitchbend messages.
  }
  
  /**
   * Create a new FpdMidiMessageShort note on message.
   * @param channel
   * @param noteNumber 
   * @param velocity 
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessageNoteOn(int channel, int noteNumber, int velocity){
    return createFpdMidiMessageShort(ShortMessage.NOTE_ON ,channel, noteNumber, velocity);  
  }

  /**
   * Create a new FpdMidiMessageShort note off message.
   * @param channel
   * @param noteNumber 
   * @param velocity 
   * @return
   */
  static public FpdMidiMessageShort createFpdMidiMessageNoteOff(int channel, int noteNumber, int velocity){
    return createFpdMidiMessageShort(ShortMessage.NOTE_OFF ,channel, noteNumber, velocity);  
  }
  
  /**
   * Create a new FpdMidiMessageShort channel pressure message
   * 
   */
  static public FpdMidiMessageShort createFpdMidiMessageChannelPressure(int channel, int pressure){
    return createFpdMidiMessageShort(ShortMessage.CHANNEL_PRESSURE ,channel, pressure, 0);  //param2 is unused in channel pressure message
  }
  /**
   * Create a new FpdMidiMessageShort poly pressure message
   * 
   */
  static public FpdMidiMessageShort createFpdMidiMessagePolyPressure(int channel, int noteNumber, int velocity){
    return createFpdMidiMessageShort(ShortMessage.POLY_PRESSURE ,channel, noteNumber, velocity);  
  }
  
}
