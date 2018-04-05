package floatingpointdev.parameters;

import floatingpointdev.toolkit.midi.IoMidi;

/**
 * 
 */


/**
 * @author floatingpoint
 * 
 */
public class Globals {
  /** The reference to the singleton instance of this class. */
  public static Globals singletonRef;

  public IoMidi     ioMidi;
//  public IoOsc      ioOsc;

  private Globals() {
    ioMidi = new IoMidi();
  }
  
  /**
   * Get the singleton Globals instance. If the singleton Globals does not exist
   * yet then it is created.
   * 
   * @return the global instance of ParameterManager.
   */
  public static Globals getInstance() {
    if (singletonRef == null) {
      singletonRef = new Globals();
    }
    return singletonRef;
  }

  /**
   * The object.clone() method is overridden because cloning doesn't make sense
   * for a singleton object.
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }

}
