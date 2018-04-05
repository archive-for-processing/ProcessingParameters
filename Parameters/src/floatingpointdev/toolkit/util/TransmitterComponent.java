/**
 * 
 */
package floatingpointdev.toolkit.util;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Manages a list of Receivers and has a function
 * that sends a message to all of them. This class
 * is meant to work in conjunction with the MidiTransmitter interface.
 * 
 * Typically, an object that wishes to be a transmitter should implement
 * the ITransmitter interface and delegate the methods in the interface
 *  to an instance of this class.
 * @author floatingpointdev
 */
public class TransmitterComponent {

  private ArrayList<IReceiver> receivers = new ArrayList<IReceiver>();
  
  public void addReceiver(IReceiver receiver){
    receivers.add(receiver);
  }
  
  public void deleteReceiver(IReceiver receiver){
    receivers.remove(receiver);
  }

  public void deleteReceivers(){
    receivers.removeAll(receivers);
  }
  
  public void transmitToReceivers(Object transmitter, Object message){
    //send the message to each of the receivers.
    ListIterator<IReceiver> iterator = receivers.listIterator();
    while(iterator.hasNext()){
      IReceiver aReceiver = iterator.next();
      aReceiver.processMessage(transmitter, message);
    }
  }
}