/**
 * 
 */
package floatingpointdev.toolkit.util;

/**
 * Interface for an object that transmits messages
 * to multiple IReceiver Objects.
 * @author floatingpointdev
 *
 */
public interface ITransmitter {
  public void addReceiver(IReceiver receiver);
  public void deleteReceiver(IReceiver receiver);
  public void deleteReceivers();
  public void transmitToReceivers(Object transmitter, Object message);
}


//cut paste and uncomment the following to make implementing ITransmitter a little easier.
/*//ITransmitter interface
TransmitterComponent transmitterComponent = new TransmitterComponent();
*//** @see floatingpointdev.toolkit.util.ITransmitter#addReceiver(floatingpointdev.toolkit.util.IReceiver)*//*
@Override
public void addReceiver(IReceiver receiver) {
  transmitterComponent.addReceiver(receiver);  
}

*//** @see floatingpointdev.toolkit.util.ITransmitter#deleteReceiver(floatingpointdev.toolkit.util.IReceiver)*//*
@Override
public void deleteReceiver(IReceiver receiver) {
  transmitterComponent.deleteReceiver(receiver);
}

*//** @see floatingpointdev.toolkit.util.ITransmitter#deleteReceivers()*//*
@Override
public void deleteReceivers() {
  transmitterComponent.deleteReceivers();
}

*//** @see floatingpointdev.toolkit.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)*//*
@Override
public void transmitToReceivers(Object transmitter, Object message) {
  transmitterComponent.transmitToReceivers(transmitter, message);
}*/