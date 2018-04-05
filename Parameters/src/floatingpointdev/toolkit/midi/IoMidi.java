/**
 * 
 */
package floatingpointdev.toolkit.midi;

import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.*;
import javax.sound.midi.MidiDevice.Info;

import floatingpointdev.toolkit.util.IReceiver;
import floatingpointdev.toolkit.util.ITransmitter;
/**
 * IoMidi manages the sending and receiving of midi messages
 * in and out of the program.  It maintains a list of MidiInputBuses
 * and MidiOutputBusses and manages where these busses send and
 * receive midi messages to and from.
 * 
 * @author floatingpointdev
 *
 */
public class IoMidi {
  protected ArrayList<MidiBuss> inputBusses;
  protected ArrayList<MidiBuss> outputBusses;
  
  protected ArrayList<InputPort> inputPorts;
  protected ArrayList<OutputPort> outputPorts;
  
  //Definitions for the Names of the default Busses "None" and "All Inputs."
  static public String BUSS_SELECTION_NONE = new String("None");
  static public String BUSS_SELECTION_ALLINPUTS = new String("All Inputs");
  
  public IoMidi(){
    inputPorts = new ArrayList<InputPort>();
    outputPorts = new ArrayList<OutputPort>();


    //get information about all the midi Devices available
    //to the system.
    Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
    
    //iterate through the available devices and and create
    //an input or output port for each one.
    for(int i = 0; i < deviceInfo.length; ++i){
      MidiDevice midiDevice;
      try{
        midiDevice = MidiSystem.getMidiDevice(deviceInfo[i]);
      
        //create an InputPort for every available midi input Device.
        if(midiDevice.getMaxTransmitters() == -1){  // -1 means unlimited.
          //this is a midi input port so add it to our list.
          inputPorts.add(new InputPort(midiDevice));
        }
        
        //create an OutputPort for every available midi output device.
        if(midiDevice.getMaxReceivers() == -1){  // -1 means unlimited.
          //this is a midi input port so add it to our list.
          outputPorts.add(new OutputPort(midiDevice));
        }
      }
      catch(MidiUnavailableException e) {
        //should never get here.
        //DEBUG_ERROR
      }
    }
    
    //Setup the input busses.
    inputBusses = new ArrayList<MidiBuss>();
    //create the default 'None' input buss.
    inputBusses.add(new MidiBuss(BUSS_SELECTION_NONE));
    //create the default allInputsBuss input buss.
    
    //Attach each inputPort to the all inputsBuss.
    MidiBuss allInputsBuss = new MidiBuss(BUSS_SELECTION_ALLINPUTS);
    for(int i = 0; i < inputPorts.size(); ++i){
      inputPorts.get(i).addReceiver(allInputsBuss);
    }
    inputBusses.add(allInputsBuss);
   
    
    
    //Setup the output busses.
    outputBusses = new ArrayList<MidiBuss>();
    //create the default 'None' output buss.
    outputBusses.add(new MidiBuss("None"));
   
  }
  
  
  public String[] getInputBussList(){
    String[] ret = new String[inputBusses.size()];
    //iterate through the Busses and return all of their names.
    for(int i = 0; i < inputBusses.size(); ++i){
      ret[i] = inputBusses.get(i).getName();
    }
    return ret;
  }
  
  /** returns a list of all available midi sources.  Right now this means the list
   * of input busses and the list of input ports.
   * @return the list of Names of inputBusses and inputPorts.
   */
  public String[] getInputBussAndPortList(){
    //get the input Buss List.
    String[] bussList = getInputBussList();

    //add the busses to the list
    String[] ret = new String[bussList.length + inputPorts.size()];
    for(int i = 0; i < bussList.length; ++i){
      ret[i] = bussList[i];
    }

    //add the ports to the list.
    for(int i = 0; i < inputPorts.size(); ++i){
      ret[bussList.length + i] = inputPorts.get(i).getMidiDevice().getDeviceInfo().getName();
    }
    
    return ret;
  }
  
  /** returns a list of all available midi output destinations.  Right now this means the list
   * of output busses and the list of output ports.
   * @return the list of names of output busses and output ports
   */
  public String[] getOutputBussAndPortList(){
    //get the input Buss List.
    String[] bussList = getOutputBussList();

    //add the busses to the list
    String[] ret = new String[bussList.length + outputPorts.size()];
    for(int i = 0; i < bussList.length; ++i){
      ret[i] = bussList[i];
    }

    //add the ports to the list.
    for(int i = 0; i < outputPorts.size(); ++i){
      ret[bussList.length + i] = outputPorts.get(i).getMidiDevice().getDeviceInfo().getName();
    }
    
    return ret;
  }
  
  public String[] getOutputBussList(){
    String[] ret = new String[outputBusses.size()];
    //iterate through the Busses and return all of their names.
    for(int i = 0; i < outputBusses.size(); ++i){
      ret[i] = outputBusses.get(i).getName();
    }
    return ret;
  }
  
  private void addInputBus(MidiBuss inputBus){
    inputBusses.add(inputBus);
  }
  
  private void addOutputBus(MidiBuss outputBus){
    outputBusses.add(outputBus);
  }

  private MidiBuss getInputBuss(String name){
    Iterator<MidiBuss> i = inputBusses.iterator();
    
    while(i.hasNext()){
      MidiBuss buss = i.next();
      if(buss.getName().compareToIgnoreCase(name) == 0){
        return buss;
      }
    }
    return null;
  }
  
  private MidiBuss getOutputBuss(String name){
    Iterator<MidiBuss> i = outputBusses.iterator();
    
    while(i.hasNext()){
      MidiBuss buss = i.next();
      if(buss.getName().compareToIgnoreCase(name) == 0){
        return buss;
      }
    }
    
    return null;
  }
  
  
  /**
   * Return the input port with the specified name.
   * @param name
   * @return the input port with the specified name.
   */
  private InputPort getInputPort(String name){
    Iterator<InputPort> i = inputPorts.iterator();
    
    while(i.hasNext()){
      InputPort port = i.next();
      if(port.getName().compareToIgnoreCase(name) == 0){
        return port;
      }
    }
    
    return null;
  }
  
  /**
   * Return the output port with the specified name.
   * @param name the name of the output port to get.
   * @return the output port with the specified name.
   */
  public OutputPort getOutputPort(String name){
    Iterator<OutputPort> i = outputPorts.iterator();
    
    while(i.hasNext()){
      OutputPort port = i.next();
      if(port.getName().compareToIgnoreCase(name) == 0){
        return port;
      }
    }
    
    return null;
  }
  
  
  
  
  /**
   * Attach a receiver to a source within IoMidi.
   * @param receiver The object that should receive midi messages.
   * @param midiSource The name of the midi source to receive messages from. 
   * This can be the name of either an input bus or an input port.
   * @return the ITransmitter that the receiver got attached to.
   *  If the specified midiSource is not found then null is returned.
   */
  public ITransmitter addMidiReceiver(IReceiver receiver, String midiSource){
    
    //try the input busses
    MidiBuss buss = getInputBuss(midiSource);
    if(buss != null){
      buss.addReceiver(receiver);
      return buss;
    }
    
    //try the input ports.
    InputPort port = getInputPort(midiSource);
    if(port != null){
      port.addReceiver(receiver);
      return port;
    }
    
    return null;
    
  }


  /**
   * Detach a midi receiver from IoMidi
   * @param receiver the receiver to detach.
   * @param midiSource the midi source to detach from
   */
  public void removeMidiReceiver(IReceiver receiver, String midiSource) {
    //try the input busses
    MidiBuss buss = getInputBuss(midiSource);
    if(buss != null){
      buss.deleteReceiver(receiver);
      return;
    }
    
    //try the input ports.
    InputPort port = getInputPort(midiSource);
    if(port != null){
      port.deleteReceiver(receiver);
      return;
    } 
    
  }
  
  /**
   * Attach a receiver to a source within IoMidi.
   * @param transmitter The object that will be transmitting midi messages.
   * @param midiDest The name of the midi Receiver to send messages to. 
   * @return the IReceiver that the transmitter got attached to.
   *  If the specified midiDest is not found then null is returned.
   */
  public IReceiver addMidiTransmitter(ITransmitter transmitter, String midiDest){
    //try the output busses.
    MidiBuss buss = getOutputBuss(midiDest);
    if(buss != null){
      transmitter.addReceiver(buss);
      return buss;
    }
    
    //try the output ports.
    OutputPort port = getOutputPort(midiDest);
    if(port != null){
      transmitter.addReceiver(port);
      return port;
    } 
    
    return null;
  }


  /**
   * Detach a midi transmitter from IoMidi
   * @param transmitter the transmitter to remove IoMidi from.
   * @param midiDest the midi destination to remove from transmitter
   */
  public void removeMidiTransmitter(ITransmitter transmitter, String midiDest) {
    //try the output busses
    MidiBuss buss = getOutputBuss(midiDest);
    if(buss != null){
      transmitter.deleteReceiver(buss);
      return;
    }
    
    //try the input ports.
    OutputPort port = getOutputPort(midiDest);
    if(port != null){
      transmitter.deleteReceiver(port);
      return;
    }
    
  }
}
