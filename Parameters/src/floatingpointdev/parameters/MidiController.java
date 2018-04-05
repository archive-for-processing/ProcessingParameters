/**
 * 
 */
package floatingpointdev.parameters;


import java.util.ArrayList;

import javax.sound.midi.ShortMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import floatingpointdev.toolkit.midi.FpdMidiMessageShort;
import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.midi.MidiTool;
import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;
import floatingpointdev.toolkit.util.IReceiver;
import floatingpointdev.toolkit.util.Transmitter;
import floatingpointdev.toolkit.util.XmlHelper;



/**
 * Handles all the midi input and output processing for a parameter
 * @author floatingpointdev
 */

public class MidiController extends Transmitter implements IReceiver, IObservable, IController {
  /** The parameter that this MidiController is working for*/
  //private Parameter parameter;
  private boolean isLearningMidiInput = false;
  IoMidi ioMidi;  //Reference to the IoMidi object to send/recieve midi to/from.
  
  
  /**
   * @param parameter The Parameter that this MidiController manages.
   * @param ioMidi The IoMidi object to send/recieve midi to/from.
   */
  public MidiController(IoMidi ioMidi){
    if(ioMidi == null){
      throw new IllegalArgumentException("One of the arguments for the constructor of MidiController was null.");
    }
    
    //this.parameter = parameter;
    //parameter.addIObserver(this);
    this.ioMidi = ioMidi;
    observableComponent = new ObservableComponent();
    this.setMidiInputBus(IoMidi.BUSS_SELECTION_ALLINPUTS);
    this.setMidiOutputBus(IoMidi.BUSS_SELECTION_NONE);
  }
  
  
  /** Process the midi message.
   * @see floatingpointdev.toolkit.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)
   */
  @Override
  public void processMessage(Object transmitter, Object message) {
    if(message == null){
      //DEBUG_ERROR incoming midi message was null.
      return;
    }
    ShortMessage midiMessage = (ShortMessage)message;
    int messageCommand = midiMessage.getCommand();
    int messageChannel = midiMessage.getChannel();
    if(midiInputBus.compareTo(IoMidi.BUSS_SELECTION_NONE) == 0){
      return;
    }
    
    //If we are listening for midi input to learn values:
    if(isLearningMidiInput){
      if(midiInputType == MidiInputTypes.NONE){
        //Do nothing.
      } else if(midiInputType == MidiInputTypes.CONTROL_CHANGE){
        if(messageCommand == ShortMessage.CONTROL_CHANGE){
          setMidiInputChannel(messageChannel);
          setMidiInputNumber(MidiTool.getCcNumber(midiMessage));
        }
      } else if(midiInputType == MidiInputTypes.NOTE){
        if(messageCommand == ShortMessage.NOTE_ON){
          setMidiInputChannel(messageChannel);
        }
        
      } else if(midiInputType == MidiInputTypes.VELOCITY){
        if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
          setMidiInputChannel(messageChannel);
        }
      } else if(midiInputType == MidiInputTypes.NOTE_ON_VELOCITY){
        if(messageCommand == ShortMessage.NOTE_ON){
          setMidiInputChannel(messageChannel);
        }
      } else if(midiInputType == MidiInputTypes.NOTE_OFF_VELOCITY){
        if(messageCommand == ShortMessage.NOTE_OFF){
          setMidiInputChannel(messageChannel);
        }
      } else if(midiInputType == MidiInputTypes.PITCHBEND){
        if(messageCommand == ShortMessage.PITCH_BEND){
          setMidiInputChannel(messageChannel);
        }
      } else if(midiInputType == MidiInputTypes.CHANNEL_PRESSURE){
        if(messageCommand == ShortMessage.CHANNEL_PRESSURE){
          setMidiInputChannel(messageChannel);
        }
      } else if(midiInputType == MidiInputTypes.POLY_PRESSURE){
        if(messageCommand == ShortMessage.POLY_PRESSURE){
          setMidiInputChannel(messageChannel);
        }
      } else {
        //Not a message we are interested in bail out.
        return;
      }
      isLearningMidiInput = false;
      return;  //return since we don't want to continue processing the message after we have
               //used it to learn values.
    }
    
    
    
    //Check if this is the kind of midi message we are interested in. If it
    //is then stick the value of the message in val.
    int val = -1;
    if(midiInputType == MidiInputTypes.NONE){
      //Do nothing.
    } else if(midiInputType == MidiInputTypes.CONTROL_CHANGE){
      if(messageCommand == ShortMessage.CONTROL_CHANGE){
        if(messageChannel == midiInputChannel){
          if(MidiTool.getCcNumber(midiMessage) == midiInputNumber){
            val = MidiTool.getCcValue(midiMessage);
          }
        }
      }
    } else if(midiInputType == MidiInputTypes.NOTE){
      if(messageCommand == ShortMessage.NOTE_ON){
        if(messageChannel == midiInputChannel){
            val = MidiTool.getNote(midiMessage);
        }
      }
      
    } else if(midiInputType == MidiInputTypes.VELOCITY){
      if((messageCommand == ShortMessage.NOTE_ON) || (messageCommand == ShortMessage.NOTE_OFF)){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getVelocity(midiMessage);
        }
      }
    } else if(midiInputType == MidiInputTypes.NOTE_ON_VELOCITY){
      if(messageCommand == ShortMessage.NOTE_ON){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getVelocity(midiMessage);
        }
      }
    } else if(midiInputType == MidiInputTypes.NOTE_OFF_VELOCITY){
      if(messageCommand == ShortMessage.NOTE_OFF){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getVelocity(midiMessage);
        }
      }
    } else if(midiInputType == MidiInputTypes.PITCHBEND){
      if(messageCommand == ShortMessage.PITCH_BEND){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getPitchBendValue(midiMessage);
        }
      }
    }else if(midiInputType == MidiInputTypes.CHANNEL_PRESSURE){
      if(messageCommand == ShortMessage.CHANNEL_PRESSURE){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getChannelPressure(midiMessage);
        }
      }
    }else if(midiInputType == MidiInputTypes.POLY_PRESSURE){
      if(messageCommand == ShortMessage.POLY_PRESSURE){
        if(messageChannel == midiInputChannel){
          val = MidiTool.getPolyPressure(midiMessage);
        }
      }
    }else {
      //Not a message we are interested in. do nothing
    }
    
    //if val is still -1 then the incoming message was not for us.
    // bail out.
    if(val == -1){
      return;
    }
    
    //Val should now hold the value of the message.
    //Scale the midi value to the parameter range.
    float fval = (float)val/127;
    //parameter.setNormalized(fval);
    controllerComponent.set(this, fval);
  }
  
  /** @return the isLearningMidiInput */
  public boolean isLearningMidiInput() {
    return isLearningMidiInput;
  }
  
  /** @param isLearningMidiInput setting this to true will tell the midi manager
   * to learn input values from the next received midi message. */
  public void setLearningMidiInput(boolean isLearningMidiInput) {
    observableComponent.notifyIObservers(this, "isLearningMidiInput");
    this.isLearningMidiInput = isLearningMidiInput;
  }
  
  /** send a midiMessage to the attached receivers.*/
  public void transmitMidiMessage(FpdMidiMessageShort midiMessage){
    this.transmitToReceivers(this, midiMessage);
  }
  
  /** send a midiMessage to the attached receivers that is derived from a 
   * given value and the midiOutput settings.  Value is scaled to the proper
   * midi value range given min and max.  So value should always be between
   * min and max.
   * 
   * @param value the value to convert to midi data and transmit.
   * @param min the lower bound on the value.
   * @param max the upper bound on the value.  
   * */
  private int recentValue = 0;
  public void transmitMidiMessageValue(float value, float min, float max){
    int scaledValue = (int)((value / (max - min)) * 127);
    if(scaledValue == recentValue){
      return; //the value has not changed.  no need to send it.
    } else {
      recentValue = scaledValue;
    }
    FpdMidiMessageShort newMessage;
    if(midiOutputType == MidiOutputTypes.CONTROL_CHANGE){
        newMessage = FpdMidiMessageShort.createFpdMidiMessageControlChange(midiOutputChannel,
                                                                           midiOutputNumber,
                                                                           scaledValue);
    } else if(midiOutputType == MidiOutputTypes.PITCHBEND){
        newMessage = FpdMidiMessageShort.createFpdMidiMessagePitchBend(midiOutputChannel,
                                                                       scaledValue);
    } else if(midiOutputType == MidiOutputTypes.CHANNEL_PRESSURE){
      newMessage = FpdMidiMessageShort.createFpdMidiMessageChannelPressure(midiOutputChannel,
                                                                           scaledValue);
    } else if(midiOutputType == MidiOutputTypes.POLY_PRESSURE){
      newMessage = FpdMidiMessageShort.createFpdMidiMessagePolyPressure(midiOutputChannel,
                                                                        midiOutputNumber,
                                                                        scaledValue);
    } else {
      //no output type is set that we can send with.
      return;
    }
    
    transmitToReceivers(this, newMessage);
  }  
  
  
  // ---------- MIDI settings -----------
  
  //midi input
  private String midiInputBus = new String();
  private MidiInputTypes midiInputType = MidiInputTypes.CONTROL_CHANGE;
  private int midiInputChannel = 0;
  private int midiInputNumber = 1;
  //private String midiInputRemoteOutputBus;
  
  //midi output
  private String midiOutputBus = new String();
  private MidiOutputTypes midiOutputType = MidiOutputTypes.CONTROL_CHANGE;
  private int midiOutputChannel = 0;
  private int midiOutputNumber = 1;
  

  /** defines the types of Midi message to receive. */
  public enum MidiInputTypes {
    CONTROL_CHANGE, NOTE, PITCHBEND, VELOCITY, NOTE_ON_VELOCITY, 
    NOTE_OFF_VELOCITY, CHANNEL_PRESSURE, POLY_PRESSURE, NONE, 
  };
  
  
  /**
   * Converts a MidiInput type into a readable string.
   * @param type The MidiInputType to convert.
   * @return The string name of the type
   */
  private String MidiInputTypesToString(MidiInputTypes type){
    String ret = new String("NONE");
    if(type == MidiInputTypes.CONTROL_CHANGE){
      ret = "CONTROL_CHANGE";
    } else if(type == MidiInputTypes.NOTE){
      ret = "NOTE";
    } else if(type == MidiInputTypes.PITCHBEND){
      ret = "PITCHBEND";
    } else if(type == MidiInputTypes.VELOCITY){
      ret = "VELOCITY";
    } else if(type == MidiInputTypes.NOTE_ON_VELOCITY){
      ret = "NOTE_ON_VELOCITY";
    } else if(type == MidiInputTypes.NOTE_OFF_VELOCITY){
      ret = "NOTE_OFF_VELOCITY";
    } else if(type == MidiInputTypes.CHANNEL_PRESSURE){
      ret = "CHANNEL_PRESSURE";
    } else if(type == MidiInputTypes.POLY_PRESSURE){
      ret = "POLY_PRESSURE";
    } else if(type == MidiInputTypes.NONE){
      ret = "NONE";
    }
    return ret;
  }
  
  
  /**
   * Converts a string representing a MidiInputType into 
   * its corresponding type.
   * @param type The string representation to convert.
   * @return The MidiInputType represented.
   */
  private MidiInputTypes MidiInputTypesFromString(String type){
    MidiInputTypes ret = MidiInputTypes.NONE;
    if(type == null){
      throw new IllegalArgumentException();
    }
    if(type.compareTo("CONTROL_CHANGE") == 0){
      ret = MidiInputTypes.CONTROL_CHANGE;
    } else if(type.compareTo("NOTE") == 0){
      ret = MidiInputTypes.NOTE;
    } else if(type.compareTo("PITCHBEND") == 0){
      ret = MidiInputTypes.PITCHBEND;
    } else if(type.compareTo("VELOCITY") == 0){
      ret = MidiInputTypes.VELOCITY;
    } else if(type.compareTo("NOTE_ON_VELOCITY") == 0){
      ret = MidiInputTypes.NOTE_ON_VELOCITY;
    } else if(type.compareTo("NOTE_OFF_VELOCITY") == 0){
      ret = MidiInputTypes.NOTE_OFF_VELOCITY;
    } else if(type.compareTo("CHANNEL_PRESSURE") == 0){
      ret = MidiInputTypes.CHANNEL_PRESSURE;
    } else if(type.compareTo("POLY_PRESSURE") == 0){
      ret = MidiInputTypes.POLY_PRESSURE;
    } else if(type.compareTo("NONE") == 0){
      ret = MidiInputTypes.NONE;
    }  
    return ret;
  }
  
  
  /** defines the types of Midi message we can output through. */
  public enum MidiOutputTypes {
    CONTROL_CHANGE, PITCHBEND, CHANNEL_PRESSURE, POLY_PRESSURE, NONE, 
  };
  
  
  /**
   * Converts a MidiOutputTypes value to a readable string representation.
   * @param type The type to convert.
   * @return The string representation of type.
   */
  private String MidiOutputTypesToString(MidiOutputTypes type){
    String ret = new String("NONE");
    if(type == MidiOutputTypes.CONTROL_CHANGE){
      ret = "CONTROL_CHANGE";
    } else if(type == MidiOutputTypes.PITCHBEND){
      ret = "PITCHBEND";
    } else if(type == MidiOutputTypes.CHANNEL_PRESSURE){
      ret = "CHANNEL_PRESSURE";
    } else if(type == MidiOutputTypes.POLY_PRESSURE){
      ret = "POLY_PRESSURE";
    } else if(type == MidiOutputTypes.NONE){
      ret = "NONE";
    }
    return ret;
  }
  
  
  /**
   * Converts a string representing a MidiInputType into 
   * its corresponding type.
   * @param type The string representation to convert.
   * @return The MidiInputType represented.
   */
  private MidiOutputTypes MidiOutputTypesFromString(String type){
    MidiOutputTypes ret = MidiOutputTypes.NONE;
    if(type == null){
      throw new IllegalArgumentException();
    }
    if(type.compareTo("CONTROL_CHANGE") == 0){
      ret = MidiOutputTypes.CONTROL_CHANGE;
    } else if(type.compareTo("PITCHBEND") == 0){
      ret = MidiOutputTypes.PITCHBEND;
    } else if(type.compareTo("CHANNEL_PRESSURE") == 0){
      ret = MidiOutputTypes.CHANNEL_PRESSURE;
    } else if(type.compareTo("POLY_PRESSURE") == 0){
      ret = MidiOutputTypes.POLY_PRESSURE;
    } else if(type.compareTo("NONE") == 0){
      ret = MidiOutputTypes.NONE;
    }
    return ret;
  }
  
  
  //getters and setters
  /** @return the midiInputBus */
  public String getMidiInputBus() {
    return midiInputBus;
  }

  
  /**Attempt to set the midiInputBus 
   * @param midiInputBus the midiInputBus to set */
  public void setMidiInputBus(String midiInputBus) {
    if(midiInputBus == null){
      throw new IllegalArgumentException("midiInputBus can not be null");
    }
    // Only notify observers if the Buss has actually changed.
    if(this.midiInputBus.compareToIgnoreCase(midiInputBus) == 0){
      return;  // no change, bail out.
    }
    
    //detach this from the existing midiInputBuss if any.
    ioMidi.removeMidiReceiver(this, this.midiInputBus);
    
    //attach this to the new input buss.
    ioMidi.addMidiReceiver(this, midiInputBus);
    
    this.midiInputBus = midiInputBus;
    observableComponent.notifyIObservers(this, "midiInputBus");
  }

  
  /** @return the midiInputType */
  public MidiInputTypes getMidiInputType() {
    return midiInputType;
  }

  
  /** @param midiInputType the midiInputType to set */
  public void setMidiInputType(MidiInputTypes midiInputType) {
    this.midiInputType = midiInputType;
    observableComponent.notifyIObservers(this, "midiInputType");
  }

//  /** @return the midiInputRemoteOutputBus */
//  public String getMidiInputRemoteOutputBus() {
//    return midiInputRemoteOutputBus;
//  }
//
//  /** @param midiInputRemoteOutputBus the midiInputRemoteOutputBus to set */
//  public void setMidiInputRemoteOutputBus(String midiInputRemoteOutputBus) {
//    this.midiInputRemoteOutputBus = midiInputRemoteOutputBus;
//    observableComponent.notifyIObservers(this, "midiInputRemoteOutputBus");
//  }
  
  /** @return the midiInputChannel */
  public int getMidiInputChannel() {
    return midiInputChannel;
  }

  /** @param midiInputChannel the midiInputChannel to set */
  public void setMidiInputChannel(int midiInputChannel) {
    if((midiInputChannel >= 0) && (midiInputChannel <= 15)){
      this.midiInputChannel = midiInputChannel;
    }
    observableComponent.notifyIObservers(this, "midiInputChannel");
  }

  /** @return the midiInputNumber */
  public int getMidiInputNumber() {
    return midiInputNumber;
  }
  /** @param midiInputNumber the midiInputNumber to set */
  public void setMidiInputNumber(int midiInputNumber) {
    if((midiInputNumber > 0) && (midiInputNumber <= 128)){
        this.midiInputNumber = midiInputNumber;
    }
    observableComponent.notifyIObservers(this, "midiInputNumber");
  }



  /** @return the midiOutputBus */
  public String getMidiOutputBus() {
    return midiOutputBus;
  }

  /** @param midiOutputBus the midiOutputBus to set */
  public void setMidiOutputBus(String midiOutputBus) {
    
    // Only notify observers if the Buss has actually changed.
    if(this.midiOutputBus != null){
      if(this.midiOutputBus.compareToIgnoreCase(midiOutputBus) == 0){
        return;  // no change, bail out.
      }
    }
    
    //detach this from the existing midiInputBuss if any.
    if(this.midiOutputBus != null){
      ioMidi.removeMidiTransmitter(this, this.midiOutputBus);
    }
    
    //attach this to the new output bus
    ioMidi.addMidiTransmitter(this, midiOutputBus);
    
    
    this.midiOutputBus = midiOutputBus;
    observableComponent.notifyIObservers(this, "midiOutputBus");
  }

  /** @return the midiOutputType */
  public MidiOutputTypes getMidiOutputType() {
    return midiOutputType;
  }

  /** @param midiOutputType the midiOutputType to set */
  public void setMidiOutputType(MidiOutputTypes midiOutputType) {
    this.midiOutputType = midiOutputType;
    observableComponent.notifyIObservers(this, "midiOutputType");
  }

  /** @return the midiOutputChannel */
  public int getMidiOutputChannel() {
    return midiOutputChannel;
  }

  /** @param midiOutputChannel the midiOutputChannel to set */
  public void setMidiOutputChannel(int midiOutputChannel) {
    if((midiOutputChannel >= 0) && (midiOutputChannel <= 15)){
      this.midiOutputChannel = midiOutputChannel;
    }
    observableComponent.notifyIObservers(this, "midiOutputChannel");
  }

  /** @return the midiOutputNumber */
  public int getMidiOutputNumber() {
    return midiOutputNumber;
  }

  /** @param midiOutputNumber the midiOutputNumber to set */
  public void setMidiOutputNumber(int midiOutputNumber) {
    if((midiOutputNumber > 0) && (midiOutputNumber <= 128)){
      this.midiOutputNumber = midiOutputNumber;
    }
    observableComponent.notifyIObservers(this, "midiOutputNumber");
  }
  
  
  // ---------- IOBservable methods -----------
  private ObservableComponent observableComponent = new ObservableComponent();
  public void addIObserver(IObserver anIObserver){
    observableComponent.addIObserver(anIObserver);
  }
  
  public void deleteIObserver(IObserver anIObserver){
    observableComponent.deleteIObserver(anIObserver); 
  }
  
  public void deleteIObservers(){
    observableComponent.deleteIObservers();
  }

  
  // ---------- IObserver method --------------
//  /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
//  @Override
//  public void update(Object theObserved, Object changeCode) {
//    if(theObserved == parameter){
//      //If the value of the associated parameter has changed.
//      String changeCodeStr = (String)changeCode;
//      if(changeCodeStr.compareTo("val") == 0){
//        this.transmitMidiMessageValue(parameter.get(), parameter.getMin(), parameter.getMax());
//      }
//    }
//    
//  }


  /**
   * @return The IoMidi object that this parameterManager uses.
   */
  public IoMidi getIoMidi() {
    return ioMidi;
  }


  /**
   * @param parentElement
   */
  public void saveToFile(Element parentElement) {
    Document docOwner = parentElement.getOwnerDocument();
    
    Element elemMidiManager = docOwner.createElement("MidiController");
    XmlHelper.createElementValuePair(elemMidiManager, "MidiInputBus", midiInputBus);
    XmlHelper.createElementValuePair(elemMidiManager, "MidiInputType", MidiInputTypesToString(midiInputType));
    XmlHelper.createElementValuePair(elemMidiManager, "MidiInputChannel", Integer.toString(midiInputChannel));   
    XmlHelper.createElementValuePair(elemMidiManager, "MidiInputNumber", Integer.toString(midiInputNumber));
    XmlHelper.createElementValuePair(elemMidiManager, "MidiOutputBus", midiOutputBus);
    XmlHelper.createElementValuePair(elemMidiManager, "MidiOutputType", MidiOutputTypesToString(midiOutputType));
    XmlHelper.createElementValuePair(elemMidiManager, "MidiOutputChannel", Integer.toString(midiOutputChannel));
    XmlHelper.createElementValuePair(elemMidiManager, "MidiOutputNumber", Integer.toString(midiOutputNumber));
    
    parentElement.appendChild(elemMidiManager); 
  }


  /**
   * Reads the settings for this MidiController from an XML element.
   * @param element the XML element that contains MidiController settings.
   */
  public void readFromFile(Element element) {
    //verify that this is a MidiController node
    if(element.getNodeName().compareTo("MidiController") != 0){
      return;
    }
    
    //get the values
    NodeList nodeLst = element.getChildNodes();
    String nodeName;
    for(int i = 0; i < nodeLst.getLength(); ++i){
      nodeName = nodeLst.item(i).getNodeName();
      if(nodeName.compareTo("MidiInputBus") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiInputBus(str);
        }
      } else if(nodeName.compareTo("MidiInputType") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiInputType(MidiInputTypesFromString(str));
        }
      } else if(nodeName.compareTo("MidiInputChannel") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiInputChannel(Integer.parseInt(str));
        }
      } else if(nodeName.compareTo("MidiInputNumber") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiInputNumber(Integer.parseInt(str));
        }
      } else if(nodeName.compareTo("MidiOutputBus") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiOutputBus(str);
        }
      } else if(nodeName.compareTo("MidiOutputType") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiOutputType(MidiOutputTypesFromString(str));
        }
      } else if(nodeName.compareTo("MidiOutputChannel") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiOutputChannel(Integer.parseInt(str));
        }
      } else if(nodeName.compareTo("MidiOutputNumber") == 0){
        String str = XmlHelper.getTextData((Element)nodeLst.item(i));
        if(str != null){
          this.setMidiOutputNumber(Integer.parseInt(str));
        }
      }
    }
  }


  private ControllerComponent controllerComponent = new ControllerComponent();
  /** @see floatingpointdev.parameters.IController#receiveEvent(float)*/
  @Override
  public void receiveEvent(float value) {
    this.transmitMidiMessageValue(value, 0, 1);
  }


  /** @see floatingpointdev.parameters.IController#setControlled(floatingpointdev.parameters.Parameter)*/
  @Override
  public void setControlled(IControlled controlled) {
    controllerComponent.setControlled(controlled);
  }
}
