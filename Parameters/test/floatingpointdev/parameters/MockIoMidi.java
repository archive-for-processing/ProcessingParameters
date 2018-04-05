/**
 * 
 */
package floatingpointdev.parameters;

import javax.sound.midi.MidiDevice;

import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.midi.MidiBuss;
import floatingpointdev.toolkit.midi.OutputPort;
import floatingpointdev.toolkit.util.IReceiver;
import floatingpointdev.toolkit.util.ITransmitter;

/**
 * @author floatingpointdev
 *
 */
class MockIoMidi extends IoMidi {

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#getInputBussList()
   */
  @Override
  public String[] getInputBussList() {
    String[] strings = new String[1];
    strings[1] = new String("inputBuss1");
    return strings;
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#getInputBussAndPortList()
   */
  @Override
  public String[] getInputBussAndPortList() {
    String[] strings = new String[1];
    strings[0] = new String("inputBussAndPort1");
    return strings;
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#getOutputBussAndPortList()
   */
  @Override
  public String[] getOutputBussAndPortList() {
    String[] strings = new String[1];
    strings[0] = new String("outputBussandPort1");
    return strings;
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#getOutputBussList()
   */
  @Override
  public String[] getOutputBussList() {
    String[] strings = new String[1];
    strings[1] = new String("outputBuss1");
    return strings;
  }


  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#getOutputPort(java.lang.String)
   */
  @Override
  public OutputPort getOutputPort(String name) {
    return new MockOutputPort(null);
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#addMidiReceiver(floatingpointdev.toolkit.util.IReceiver, java.lang.String)
   */
  @Override
  public ITransmitter addMidiReceiver(IReceiver receiver, String midiSource) {
    return new MockTransmitter();
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#removeMidiReceiver(floatingpointdev.toolkit.util.IReceiver, java.lang.String)
   */
  @Override
  public void removeMidiReceiver(IReceiver receiver, String midiSource) {

  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#addMidiTransmitter(floatingpointdev.toolkit.util.ITransmitter, java.lang.String)
   */
  @Override
  public IReceiver addMidiTransmitter(ITransmitter transmitter, String midiDest) {
    return new MockReciever();
  }

  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.midi.IoMidi#removeMidiTransmitter(floatingpointdev.toolkit.util.ITransmitter, java.lang.String)
   */
  @Override
  public void removeMidiTransmitter(ITransmitter transmitter, String midiDest) {

  }

  class MockTransmitter implements ITransmitter{

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.util.ITransmitter#addReceiver(floatingpointdev.toolkit.util.IReceiver)
     */
    @Override
    public void addReceiver(IReceiver receiver) {
      // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.util.ITransmitter#deleteReceiver(floatingpointdev.toolkit.util.IReceiver)
     */
    @Override
    public void deleteReceiver(IReceiver receiver) {
      // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.util.ITransmitter#deleteReceivers()
     */
    @Override
    public void deleteReceivers() {
      // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)
     */
    @Override
    public void transmitToReceivers(Object transmitter, Object message) {
      // TODO Auto-generated method stub
    }
  }
  
  class MockReciever implements IReceiver{

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)
     */
    @Override
    public void processMessage(Object transmitter, Object message) {
      // TODO Auto-generated method stub
      
    }
  }
  
  class MockOutputPort extends OutputPort{

    /**
     * @param midiDevice
     */
    public MockOutputPort(MidiDevice midiDevice) {
      super(midiDevice);
      // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.midi.OutputPort#getMidiDevice()
     */
    @Override
    public MidiDevice getMidiDevice() {
      return null;
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.midi.OutputPort#setMidiDevice(javax.sound.midi.MidiDevice)
     */
    @Override
    public void setMidiDevice(MidiDevice midiDevice) {
      
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.midi.OutputPort#getName()
     */
    @Override
    public String getName() {
      return new String("Name");
    }

    /* (non-Javadoc)
     * @see floatingpointdev.toolkit.midi.OutputPort#processMessage(java.lang.Object, java.lang.Object)
     */
    @Override
    public void processMessage(Object transmitter, Object message) {

    }
    
  }
  
}
