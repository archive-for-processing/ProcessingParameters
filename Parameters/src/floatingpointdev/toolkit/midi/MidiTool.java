/**
 * 
 */
package floatingpointdev.toolkit.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

/**
 * @author floatingpointdev
 *  MidiTool provides some handy methods for creating and querying 
 *  javax.sound.midi.ShortMessages.
 */
public class MidiTool {

  /**
   * @param message The ShortMessage to query.
   * @return the midi Channel
   */
  public static int getChannel(ShortMessage message){
    return message.getChannel();
  }
  
  
  /**
   * @param message The ShortMessage to query.
   * @return the midi CC number if this midi message is a ControlChange message.
   * If this is not a ControlChange message then a RunTimeException is thrown.
   */
  public static int getCcNumber(ShortMessage message){
    int messageCommand = message.getCommand();
    if(messageCommand == ShortMessage.CONTROL_CHANGE){
      return message.getData1();
    }
    throw new RuntimeException("Attempting to get ControlChange information from a non ControlChange midi message.");
  }

  
  /**
   * @param message The ShortMessage to query.
   * @return the midi CC Value if this midi message is a ControlChange message.
   * If this is not a ControlChange message then a RunTimeException is thrown.
   */
  public static int getCcValue(ShortMessage message){
    int messageCommand = message.getCommand();
    if(messageCommand == ShortMessage.CONTROL_CHANGE){
      return message.getData2();
    }
    throw new RuntimeException("Attempting to get ControlChange information from a non ControlChange midi message.");
  }
  
  
  /**
   * @param message The ShortMessage to query.
   * @return the midi Note number if this midi message has note information.
   * If it does not have note information then a RunTimeException is thrown.
   */
  public static int getNote(ShortMessage message){
    int messageCommand = message.getCommand();
    if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
      return message.getData1();
    }
    throw new RuntimeException("Attempting to get Note information from a non note midi message.");
  }

  
  /**
   * @param message The ShortMessage to query.
   * @return the midi Velocity value if this midi message has velocity information.
   * If it does not have velocity information then a RunTimeException is thrown.
   */
  public static int getVelocity(ShortMessage message){
    int messageCommand = message.getCommand();
    if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
      return message.getData2();
    }
    throw new RuntimeException("Attempting to get Velocity information from a non note midi message.");
  }
  
  
  /**
   * @param message The ShortMessage to query.
   * @return the pitchbend value if this is a pitchbebd message.
   * If this is not a pitchbend message then a RunTimeException is thrown.
   */
  public static int getPitchBendValue(ShortMessage message) throws RuntimeException{
    int messageCommand = message.getCommand();
    if(messageCommand == ShortMessage.PITCH_BEND){
      int lsb = message.getData1();
      int msb = message.getData2() << 7;
      return msb+lsb;
    }
    throw new RuntimeException("Attempting to get PitchBend information from a non pitchbend midi message.");
  }
  
  
  /**
   * @param message The ShortMessage to query.
   * @return the pressure value if this midi message has channel pressure information.
   * If it does not have channel pressure information then a RunTimeException is thrown.
   */
  public static int getChannelPressure(ShortMessage message){
    int messageCommand = message.getCommand();
    if(messageCommand == ShortMessage.CHANNEL_PRESSURE){
      return message.getData1(); 
    }
    throw new RuntimeException("Attempting to get ChannelPressure information from a non ChannelPressure midi message.");
  }
  
  
  /**
   * @param message The ShortMessage to query.
   * @return the pressure value if this midi message has poly pressure information.
   * If it does not have poly pressure information then a RunTimeException is thrown.
   */
  public static int getPolyPressure(ShortMessage message){
    int messageCommand = message.getCommand();
    if(messageCommand == ShortMessage.POLY_PRESSURE){
      return message.getData2();
    }
    throw new RuntimeException("Attempting to get ChannelPressure information from a non ChannelPressure midi message.");
  }
  
  
  /** 
   *  Create any ShortMessage
   * @param command The javax.midi.ShortMessage defined midi command.
   * @param channel The channel (0-15)
   * @param param1 the byte for parameter 1. (0-127)
   * @param param2 the byte for parameter 2. (0-127)
   * @return
   * @throws InvalidMidiDataException if the values supplied are invalid.
   */
  public static ShortMessage createShortMessage(int command, int channel, int param1, int param2) throws InvalidMidiDataException{
    ShortMessage ret = new ShortMessage();
    try{
      ret.setMessage(command,channel, param1, param2);
    } catch(InvalidMidiDataException e){
      throw e;
      //DEBUG_ERROR
      //an invalid midi message was constructed.
    }
    return ret;
  }
  
  
  /**
   * Create a new control change message.
   * @param channel The channel (0-15)
   * @param ccNumber (0-127)
   * @param value (0-127)
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException if the values supplied are invalid.
   */
  public static ShortMessage createShortMessageControlChange(int channel, int ccNumber, int value) throws InvalidMidiDataException{
    return createShortMessage(ShortMessage.CONTROL_CHANGE ,channel, ccNumber, value);
  }
  
  
  /**
   * Create a new note on message.
   * @param channel The channel (0-15)
   * @param noteNumber (0-127)
   * @param velocity (0-127)
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException if the values supplied are invalid.
   */
  public static ShortMessage createShortMessageNoteOn(int channel, int noteNumber, int velocity) throws InvalidMidiDataException{
    return createShortMessage(ShortMessage.NOTE_ON ,channel, noteNumber, velocity);  
  }

  
  /**
   * Create a new note off message.
   * @param channel The channel (0-15)
   * @param noteNumber (0-127)
   * @param velocity (0-127)
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException if the values supplied are invalid.
   */
  public static ShortMessage createShortMessageNoteOff(int channel, int noteNumber, int velocity) throws InvalidMidiDataException{
    return createShortMessage(ShortMessage.NOTE_OFF ,channel, noteNumber, velocity);  
  }
  
  
  /**
   * Create a new channel pressure message.
   * @param channel The channel (0-15)
   * @param pressure The channel wide pressure value. (0-127)
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException if the values supplied are invalid.
   * 
   */
  public static ShortMessage createShortMessageChannelPressure(int channel, int pressure) throws InvalidMidiDataException{
    return createShortMessage(ShortMessage.CHANNEL_PRESSURE ,channel, pressure, 0);  //param2 is unused in channel pressure message
  }
  
  
  /**
   * Create a new poly pressure message.
   * @param channel The channel (0-15)
   * @param noteNumber (0-127)
   * @param velocity (0-127)
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException if the values supplied are invalid.
   * 
   */
  public static ShortMessage createShortMessagePolyPressure(int channel, int noteNumber, int velocity) throws InvalidMidiDataException{
    return createShortMessage(ShortMessage.POLY_PRESSURE ,channel, noteNumber, velocity);  
  }


  /**
   * @param channel The channel (0-15)
   * @param value The amount of pitchbend from -1 to 1.
   * @return the created ShortMessage.
   * @throws InvalidMidiDataException 
   */
  public static ShortMessage createShortMessagePitchBend(int channel, float value) throws InvalidMidiDataException {
    //max 16383
    //med 8192
    float valueScaled0_1 = (value + 1) / 2;
    
    int valueInt = Math.round(valueScaled0_1 * 16383f); 
    int lsb = valueInt & 0x7F; 
    int msb = valueInt >> 7;

    return createShortMessage(ShortMessage.PITCH_BEND ,channel, lsb, msb);  
  }
}
