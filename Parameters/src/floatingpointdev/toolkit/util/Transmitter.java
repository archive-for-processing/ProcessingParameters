/**
 * 
 */
package floatingpointdev.toolkit.util;

/**
 * An object that implements the ITransmitter interface.
 * @author floatingpointdev
 *
 */
public class Transmitter implements ITransmitter {

  public Transmitter(){
    transmitterComponent = new TransmitterComponent();
  }
  
  // ------- Interface Stuff ----------
  //ITransmitter interface
  private TransmitterComponent transmitterComponent;

  /** @see
   * floatingpointdev.toolkit.util.ITransmitter#addReceiver(floatingpointdev.toolkit.util.IReceiver)*/
  @Override
  public void addReceiver(IReceiver receiver) {
    transmitterComponent.addReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.util.ITransmitter#deleteMidiReceiver(floatingpointdev.toolkit.util.IReceiver)*/
  @Override
  public void deleteReceiver(IReceiver receiver) {
    transmitterComponent.deleteReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.util.ITransmitter#deleteMidiReceivers()*/
  @Override
  public void deleteReceivers() {
    transmitterComponent.deleteReceivers();
  }

  /** @see floatingpointdev.toolkit.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)*/
  @Override
  public void transmitToReceivers(Object transmitter, Object message) {
    transmitterComponent.transmitToReceivers(transmitter, message);
  }
  


}
