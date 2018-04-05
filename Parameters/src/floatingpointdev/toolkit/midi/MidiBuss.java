/**
 * 
 */
package floatingpointdev.toolkit.midi;

import floatingpointdev.toolkit.util.IReceiver;
import floatingpointdev.toolkit.util.Transmitter;

/**
 * maintains a list of midi receivers. Any midi message received via the
 * processMidiMessage() function is passed along to all of the buss's receivers.
 * 
 * @author floatingpointdev
 * 
 */
public class MidiBuss extends Transmitter implements IReceiver{
  /** The displayable name of this Buss. */
  private String name;

  public MidiBuss(String name) {
    this.name = name;
  }


  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set*/
  public void setName(String name) {
    this.name = name;
  }

  //IReceiver interface
  /** process the incoming message. 
   * @see floatingpointdev.toolkit.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
  @Override
  public void processMessage(Object transmitter, Object message) {
    transmitToReceivers(this, message);
  }


}
