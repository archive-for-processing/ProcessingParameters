/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;


import floatingpointdev.parameters.MidiController;
import floatingpointdev.parameters.MidiController.MidiInputTypes;
import floatingpointdev.parameters.MidiController.MidiOutputTypes;
import floatingpointdev.toolkit.UI.MinimalButton;
import floatingpointdev.toolkit.UI.WholeNumberField;
import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.util.FpdMap;
import floatingpointdev.toolkit.util.IObserver;

/**
 * @author floatingpointdev
 *
 */
class MidiManagerUI extends JPanel implements IObserver, ActionListener{

  /** */
  private static final long serialVersionUID = -6530301972677427656L;

  private MidiController midiController;
  private IoMidi ioMidi;
  
  //Components
  JComboBox inputBus;
  JComboBox inputRemoteOutputBus;
  JComboBox inputType;
  WholeNumberField inputChannel;
  WholeNumberField inputNumber;
  JLabel inputNumberLabel = new JLabel("Number");
  MinimalButton inputLearn;
  
  JComboBox outputBus;
  JComboBox outputType;
  WholeNumberField outputChannel;
  WholeNumberField outputNumber;
  JLabel outputNumberLabel = new JLabel("Number");
  
  //the default size for gaps between components in pixels.
  Dimension verticalGap = new Dimension(0,4);
  Dimension horizontalGap = new Dimension(4,0); 
  
  /** map for translating MidiInputTypes into their UI string representation.*/
  private FpdMap<MidiInputTypes, String> midiInputTypeStringMap;
  /** map for translating MidiOutputTypes into their UI string representation.*/
  private FpdMap<MidiOutputTypes, String> midiOutputTypeStringMap;

  
  /**
   * @param midiController the ParameterSettings object that this UI observes.
   */
  public MidiManagerUI(MidiController midiController) {
    super();
    if(midiController == null){
      throw new IllegalArgumentException("Argument was null.");
    }

    this.ioMidi = midiController.getIoMidi();
    this.midiController = midiController;
    midiController.addIObserver(this);
    
    midiInputTypeStringMap = new FpdMap<MidiInputTypes,String>();
    midiInputTypeStringMap.put(MidiInputTypes.NONE, "None");  
    midiInputTypeStringMap.put(MidiInputTypes.CONTROL_CHANGE, "Control Change");
    midiInputTypeStringMap.put(MidiInputTypes.PITCHBEND, "Pitchbend");
    midiInputTypeStringMap.put(MidiInputTypes.NOTE, "Note");
    midiInputTypeStringMap.put(MidiInputTypes.VELOCITY, "Velocity");   
    midiInputTypeStringMap.put(MidiInputTypes.NOTE_ON_VELOCITY, "Note On Velocity");   
    midiInputTypeStringMap.put(MidiInputTypes.NOTE_OFF_VELOCITY, "Note Off Velocity");  
    midiInputTypeStringMap.put(MidiInputTypes.CHANNEL_PRESSURE, "Channel Pressure");      
    midiInputTypeStringMap.put(MidiInputTypes.POLY_PRESSURE, "Poly Pressure");  
    
    midiOutputTypeStringMap = new FpdMap<MidiOutputTypes,String>();
    midiOutputTypeStringMap.put(MidiOutputTypes.NONE, "None");  
    midiOutputTypeStringMap.put(MidiOutputTypes.CONTROL_CHANGE, "Control Change");  
    midiOutputTypeStringMap.put(MidiOutputTypes.PITCHBEND, "Pitchbend");  
    midiOutputTypeStringMap.put(MidiOutputTypes.CHANNEL_PRESSURE, "Channel Pressure");  
    midiOutputTypeStringMap.put(MidiOutputTypes.POLY_PRESSURE, "Poly Pressure");  
    
    setupUI();
  }

  /**
   * Build the Midi settings Panel.
   */
  private void setupUI() {

    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents, BoxLayout.X_AXIS));
    contents.add(setupInputPanel());
    contents.add(setupOutputPanel());

    this.add(contents);
  }
  
  JPanel setupInputPanel(){

    
    inputBus = new JComboBox();
    //inputBus.addItem(new String("booyah!"));
    
    //add the available input sources to the list.
    String[] inputSourceList = ioMidi.getInputBussAndPortList();
    for(int i = 0; i < inputSourceList.length; ++i){
      inputBus.addItem(inputSourceList[i]);
    }
    inputBus.setSelectedItem(midiController.getMidiInputBus());
    
    JPanel inputBusPanel = new JPanel();
    inputBusPanel.setLayout(new BoxLayout(inputBusPanel, BoxLayout.X_AXIS));
    inputBusPanel.add(new JLabel("Bus:"));
    inputBusPanel.add(inputBus);
    
    

    inputRemoteOutputBus = new JComboBox();
    inputType = new JComboBox();
    inputChannel = new WholeNumberField(midiController.getMidiInputChannel() + 1,2);
    //inputChannel.setMaximumSize(inputChannel.getPreferredSize());
    inputNumber = new WholeNumberField(midiController.getMidiInputNumber(),3);
    inputLearn = new MinimalButton("Learn");
    
    //Channel Number Learn Panel
    JPanel channelNumberLearn = new JPanel();
    channelNumberLearn.setBorder(BorderFactory.createEtchedBorder());
    channelNumberLearn.setLayout(new BorderLayout());
    channelNumberLearn.add(inputLearn, BorderLayout.CENTER);
      //channel number
      JPanel channelNumber = new JPanel();
      channelNumber.setLayout(new GridLayout(2,2));
      channelNumber.add(new JLabel("Channel:"));
      channelNumber.add(inputChannel);
      channelNumber.add(inputNumberLabel);
      channelNumber.add(inputNumber);
    channelNumberLearn.add(channelNumber,BorderLayout.WEST);
    
    //Type
    JPanel typePanel = new JPanel();  
    inputType = new JComboBox();
    //Fill the list with all possible types.
    for(int i = 0; i < midiInputTypeStringMap.size(); ++i){
      inputType.addItem(midiInputTypeStringMap.getValueAt(i));
    }
    inputType.setSelectedItem(midiInputTypeStringMap.getValueFromKey(midiController.getMidiInputType()));
    
    
    typePanel.setLayout(new BorderLayout());
    typePanel.add(new JLabel("Type:"), BorderLayout.WEST);
    typePanel.add(inputType, BorderLayout.CENTER);
    
    // Remote Surface Output Panel
//    JPanel remoteSurfaceOutput = new JPanel();
//    remoteSurfaceOutput.setBorder(BorderFactory.createTitledBorder("Remote Surface Output"));
//    remoteSurfaceOutput.add(new JLabel("Bus:"));
//    remoteSurfaceOutput.add(inputRemoteOutputBus);
    
    //Input Panel
    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
    contents.add(inputBusPanel);
    contents.add(Box.createRigidArea(verticalGap));
    contents.add(typePanel);
    contents.add(Box.createRigidArea(verticalGap));
    contents.add(channelNumberLearn);
    contents.add(Box.createRigidArea(verticalGap));
   // contents.add(remoteSurfaceOutput);

    JPanel inputPanel = new JPanel();
    inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Midi Input"));
    inputPanel.add(contents);
    

    
    FocusListener inputChannelFocusListener = new FocusListener(){
      @Override
      public void focusGained(FocusEvent arg0) {
      }
      @Override
      public void focusLost(FocusEvent arg0) {
        midiController.setMidiInputChannel(inputChannel.getValue() - 1);//internally channel is (0-15). on the UI it is (1-16).
      }
    };
    FocusListener inputNumberFocusListener = new FocusListener(){
      @Override
      public void focusGained(FocusEvent arg0) {
      }
      @Override
      public void focusLost(FocusEvent arg0) {
        midiController.setMidiInputNumber(inputNumber.getValue());
      }
    };
    
    //attach the Listeners.
    inputBus.addActionListener(this);
    //inputRemoteOutputBus.addActionListener(this);
    inputType.addActionListener(this);
    inputChannel.addActionListener(this);
    inputChannel.addFocusListener(inputChannelFocusListener);
    inputNumber.addActionListener(this);
    inputNumber.addFocusListener(inputNumberFocusListener);
    inputLearn.addActionListener(this);
    
    return inputPanel;
  }
  
  private JPanel setupOutputPanel(){


    outputBus = new JComboBox();
    //outputBus.addItem(new String("booyah!"));
    
    //add the available output sources to the list.
    String[] outputSourceList = ioMidi.getOutputBussAndPortList();
    for(int i = 0; i < outputSourceList.length; ++i){
      outputBus.addItem(outputSourceList[i]);
    }
    outputBus.setSelectedItem(midiController.getMidiOutputBus());
    
    JPanel outputBusPanel = new JPanel();
    outputBusPanel.setLayout(new BoxLayout(outputBusPanel, BoxLayout.X_AXIS));
    outputBusPanel.add(new JLabel("Bus:"));
    outputBusPanel.add(outputBus);
    

    //Type
    JPanel outputTypePanel = new JPanel();  
    outputType = new JComboBox();
    //Fill the list with all possible types.
    for(int i = 0; i < midiOutputTypeStringMap.size(); ++i){
      outputType.addItem(midiOutputTypeStringMap.getValueAt(i));
    }
    outputType.setSelectedItem(midiOutputTypeStringMap.getValueFromKey(midiController.getMidiOutputType()));
    
    outputTypePanel.setLayout(new BorderLayout());
    outputTypePanel.add(new JLabel("Type:"), BorderLayout.WEST);
    //outputType.setAlignmentX(LEFT_ALIGNMENT);
    outputTypePanel.add(outputType, BorderLayout.CENTER);



    
    //Channel Number Learn Panel
    JPanel cnlBorder = new JPanel();
    cnlBorder.setLayout(new BorderLayout());
    JPanel cnlPanel = new JPanel();
    cnlPanel.setLayout(new BorderLayout());
    cnlBorder.setBorder(BorderFactory.createEtchedBorder());
      //channel, number
      outputChannel = new WholeNumberField(midiController.getMidiOutputChannel() + 1,2);//internally channel is (0-15). on the UI it is (1-16).
      outputNumber = new WholeNumberField(midiController.getMidiInputNumber(),3);
      JPanel channelNumber = new JPanel();
      channelNumber.setLayout(new GridLayout(2,2));
      channelNumber.add(new JLabel("Channel:"));
      channelNumber.add(outputChannel);
      channelNumber.add(outputNumberLabel);
      channelNumber.add(outputNumber);
    cnlBorder.add(channelNumber,BorderLayout.CENTER);
    cnlPanel.add(cnlBorder,BorderLayout.WEST);
    
    //Output Panel
    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
    
    contents.add(outputBusPanel);  
    contents.add(Box.createRigidArea(verticalGap));
    contents.add(outputTypePanel);
    contents.add(Box.createRigidArea(verticalGap));
    contents.add(cnlPanel);

    JPanel outputPanel = new JPanel();
    outputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Midi Output"));
    outputPanel.add(contents);

    
    FocusListener outputChannelFocusListener = new FocusListener(){
      @Override
      public void focusGained(FocusEvent arg0) {
      }
      @Override
      public void focusLost(FocusEvent arg0) {
        midiController.setMidiOutputChannel(outputChannel.getValue() - 1);
      }
    };
    FocusListener outputNumberFocusListener = new FocusListener(){
      @Override
      public void focusGained(FocusEvent arg0) {
      }
      @Override
      public void focusLost(FocusEvent arg0) {
        midiController.setMidiOutputNumber(outputNumber.getValue());
      }
    };
    //attach the Listeners.
    outputBus.addActionListener(this);
    outputType.addActionListener(this);
    outputChannel.addActionListener(this);
    outputChannel.addFocusListener(outputChannelFocusListener);
    outputNumber.addActionListener(this);
    outputNumber.addFocusListener(outputNumberFocusListener);
    return outputPanel;
  }
  
  
  
  //*************** Interface stuff ***************
  /**
   * @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)
   */
  @Override
  public void update(Object theObserved, Object changeCode) {
    //make sure theObserved is an instance of MidiController
     if(!(theObserved instanceof MidiController)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    MidiController theObservedTyped = (MidiController)theObserved;
    
    //make sure the change code is a string.
    if(!(changeCode instanceof String)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    String strChangeCode = (String)changeCode;
    
    
    //check the change code to see which element of the parameter has changed.
    //midi input
    if(strChangeCode.equalsIgnoreCase("midiInputBus")){
      String val = theObservedTyped.getMidiInputBus();
      inputBus.setSelectedItem(val);

    } else if(strChangeCode.equalsIgnoreCase("midiInputType")){
      inputType.setSelectedItem(midiInputTypeStringMap.getValueFromKey(midiController.getMidiInputType()));
      
      //disable the number field for everything except control change.
      if(midiController.getMidiInputType() == MidiInputTypes.CONTROL_CHANGE){
        inputNumber.setEnabled(true);
        inputNumberLabel.setEnabled(true);
      } else {
        inputNumber.setEnabled(false);
        inputNumberLabel.setEnabled(false);
      }
//    } else if(strChangeCode.equalsIgnoreCase("midiInputRemoteOutputBus")){
//      String val = theObservedTyped.getMidiInputRemoteOutputBus();
//      inputRemoteOutputBus.setSelectedItem(val);

    } else if(strChangeCode.equalsIgnoreCase("midiInputChannel")){
      int val = theObservedTyped.getMidiInputChannel();
      inputChannel.setText(String.valueOf(val + 1)); //internally channel is (0-15). on the UI it is (1-16).

    } else if(strChangeCode.equalsIgnoreCase("midiInputNumber")){
      int val = theObservedTyped.getMidiInputNumber();
      inputNumber.setText(String.valueOf(val));

    } 
    
    //midi output
    else if(strChangeCode.equalsIgnoreCase("midiOutputBus")){
      String val = theObservedTyped.getMidiOutputBus();
      outputBus.setSelectedItem(val);

    } else if(strChangeCode.equalsIgnoreCase("midiOutputType")){
      outputType.setSelectedItem(midiOutputTypeStringMap.getValueFromKey(midiController.getMidiOutputType()));
      //disable the number field for everything except control change.
      if(midiController.getMidiOutputType() == MidiOutputTypes.CONTROL_CHANGE){
        outputNumber.setEnabled(true);
        outputNumberLabel.setEnabled(true);
      } else {
        outputNumber.setEnabled(false);
        outputNumberLabel.setEnabled(false);
      }
      
    } else if(strChangeCode.equalsIgnoreCase("midiOutputChannel")){
      int val = theObservedTyped.getMidiOutputChannel();
      outputChannel.setText(String.valueOf(val + 1));//internally channel is (0-15). on the UI it is (1-16).

    } else if(strChangeCode.equalsIgnoreCase("midiOutputNumber")){
      int val = theObservedTyped.getMidiOutputNumber();
      outputNumber.setText(String.valueOf(val));

    }

  }


  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent e) {
    //Process actions generated by the UI components.
    Object objSource = e.getSource();
    if(objSource == inputBus){
      String str = (String)(inputBus.getSelectedItem());
      midiController.setMidiInputBus(str);
//    }else if(objSource == inputRemoteOutputBus){
//      String str = (String)(inputRemoteOutputBus.getSelectedItem());
//      midiController.setMidiInputRemoteOutputBus(str);
    } else if(objSource == inputType){
      String str = (String)inputType.getSelectedItem();
      MidiInputTypes val = this.midiInputTypeStringMap.getKeyFromValue(str);
      midiController.setMidiInputType(val);
    } else if(objSource == inputChannel){
      midiController.setMidiInputChannel(inputChannel.getValue() - 1);//internally channel is (0-15). on the UI it is (1-16).
    } else if(objSource == inputNumber){
      midiController.setMidiInputNumber(inputNumber.getValue());
    }else if(objSource == inputLearn){
      midiController.setLearningMidiInput(true);
    } 
    
    else if(objSource == outputBus){
      String str = (String)(outputBus.getSelectedItem());
      midiController.setMidiOutputBus(str);
    } else if(objSource == outputType){
      String str = (String)outputType.getSelectedItem();
      MidiOutputTypes val = this.midiOutputTypeStringMap.getKeyFromValue(str);
      midiController.setMidiOutputType(val);
    } else if(objSource == outputChannel){
      midiController.setMidiOutputChannel(outputChannel.getValue()-1);
    } else if(objSource == outputNumber){
      midiController.setMidiOutputNumber(outputNumber.getValue());
    }
  }

}
