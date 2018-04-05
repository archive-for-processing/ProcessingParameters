/**
 * 
 */
package floatingpointdev.toolkit.midi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

import floatingpointdev.toolkit.util.IReceiver;

/**
 * receives midi messages and sends them out an attached midi device.
 * midi output port.
 * @author floatingpointdev
 *
 */
public class OutputPort implements IReceiver {
  private MidiDevice midiDevice;
  private Receiver midiDeviceReceiver;
  /**
   * @param midiDevice
   */
  public OutputPort(MidiDevice midiDevice) {
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
    try{midiDeviceReceiver = midiDevice.getReceiver();}
    catch(MidiUnavailableException e){
      //error, should not get here.
      //DEBUG_ERROR
    }
  }

  public String getName(){
    return midiDevice.getDeviceInfo().getName();
  }
  
  /** @see floatingpointdev.toolkit.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
  @Override
  public void processMessage(Object transmitter, Object message) {
    //send the message to the attached midi device.


    if(message instanceof FpdMidiMessageShort){  
      midiDeviceReceiver.send((MidiMessage)message, (long)-1);
    } else if(message instanceof SysexMessage){
      SysexMessage sysexMessage = (SysexMessage)message;
      midiDeviceReceiver.send(sysexMessage, (long)-1);
    } else {
      midiDeviceReceiver.send((MidiMessage)message, (long)-1);
    }
  }

}
