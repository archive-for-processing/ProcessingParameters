/**
 * 
 */
package floatingpointdev.toolkit.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import floatingpointdev.toolkit.util.Transmitter;

/**
 * An input Port attaches to a midi input device and transmits
 * all messages coming from this device to all of its receivers.
 * @author floatingpointdev
 *
 */
public class InputPort extends Transmitter implements Receiver {
  private MidiDevice midiDevice;  
  
  /**
   * @param midiDevice The device that this port should receive midi
   * messages from.
   */
  public InputPort(MidiDevice midiDevice) {
    setMidiDevice(midiDevice);
  }

  /** @return the midiDevice */
  public MidiDevice getMidiDevice() {
    return midiDevice;
  }

  /** @param midiDevice the midiDevice to set */
  public void setMidiDevice(MidiDevice midiDevice) {
    //fist close the current device if any.
    if(midiDevice.isOpen()){
      this.midiDevice.close();
    }
    
    this.midiDevice = midiDevice;
    try{midiDevice.getTransmitter().setReceiver(this);}
    catch(MidiUnavailableException e){
      int foo = 0;
      //error, should not get here.
      //DEBUG_ERROR
    }
  }

  public String getName(){
    return midiDevice.getDeviceInfo().getName();
  }

  //---------Interface stuff ----------
  
  //Interface methods for javax.sound.midi.Receiver.

  /** @see javax.sound.midi.Receiver#close()*/
  @Override
  public void close() {
    // close gets called when the midi transmitter that this input port
    //is receiving from gets closed. 
  }

  /** @see javax.sound.midi.Receiver#send(javax.sound.midi.MidiMessage, long)*/
  @Override
  public void send(MidiMessage message, long timeStamp) {
    
    //if message is a ShortMessage

    if((Object)message instanceof ShortMessage){
      //create an fpdMidiMessageShort from the incoming MidiMessage.
      MidiMessage m = (MidiMessage)message;
      FpdMidiMessageShort fpdMidiMessage = FpdMidiMessageShort.createFpdMidiMessage(m.getMessage());
      
//      int channel = fpdMidiMessage.getChannel();
//      int command = fpdMidiMessage.getCommand();
//      int param = fpdMidiMessage.getCCNumber();
//      int value = fpdMidiMessage.getCCValue();
//      int status = fpdMidiMessage.getStatus();
//      int foo = 0;
      
      //pass the fpdMidiMessage along.
      transmitToReceivers(this, fpdMidiMessage);
    } else if((Object)message instanceof SysexMessage){
      transmitToReceivers(this, message);
    } else {
      transmitToReceivers(this, message);
    }
    
    //MidiDebugWindow dbgWindow = new MidiDebugWindow("InputPortSysex");
    
  }

}
