/**
 * 
 */
package floatingpointdev.parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.midi.ShortMessage;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import floatingpointdev.parameters.ParametersUI.ParametersManagerUI;
import floatingpointdev.parameters.ParametersUI.SequencerUi;
import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;

/**
 * ParametersManager manages a set of parameters.  Once this class is instantiated
 * call its createParameter() method to add parameters.  It is important to 
 * remember that the name of the parameter is its unique identifier.  So if you
 * create a Parameter with the same name as an existing Parameter, then a reference
 * to the existing Parameter is returned. 
 * @author floatingpointdev
 *
 */
public class ParametersManager{
  private ParametersManagerUI parametersManagerUi;
  private SequencerUi sequencerUi;
  private ArrayList<ParameterManager> parameters;
  private IoMidi ioMidi;
  private AutomationMaster automationMaster;
  private String saveFilePath = new String("");
 // private String defaultFileChooserPath = null;
  
  
  /**
   * Default constructor for creating a ParametersManager object.
   */
  public ParametersManager(){
    this.ioMidi = new IoMidi();
    this.automationMaster = new AutomationMaster();
    constructorCommon();
  }
  

  
  /**
   * Constructor for creating a ParametersManager that uses a specific IoMidi
   * object to send and recieve midi from.  This is useful if you have multiple
   * objects that want to use IoMidi (say 2 different ParametersManagers).
   * 
   * @param ioMidi Reference to the IoMidi object to use.
   */
  public ParametersManager(IoMidi ioMidi){
    this.ioMidi = ioMidi;
    constructorCommon();
  }
  
  
  /**
   * common stuff that both consturctors do.
   */
  private void constructorCommon(){
    automationMaster = new AutomationMaster();
    parameters = new ArrayList<ParameterManager>();
    parametersManagerUi = new ParametersManagerUI(this);
    //sequencerUi = new SequencerUi(this);
  }
 
  /**
   * Create a parameter that this ParametersManager will manage and return 
   * a reference to it.
   * @param parameterName The name that you want the created Parameter to have.
   * @return A reference to the newly created Parameter if parameterName 
   * was unique. If the name already exists in this 
   * ParametersManager then a reference to the existing parameter 
   * is returned.
   */
  public Parameter createParameter(String parameterName){
    ParameterManager p = getParameterManagerByName(parameterName);
    if(p != null){
      //if parameterName does exist then return a reference to it. 
      return p.getParameter();
    } else {
      //if parameterName does not already exist. Then create and add the parameter.
      ParameterManager parameterManager = new ParameterManager(parameterName, ioMidi, automationMaster);
      parameters.add(parameterManager);
      parametersManagerUi.addParameter(parameterManager); //add the parameter to the ui.
      //sequencerUi.addParameter(parameterManager); //add the parameter to the ui.
      return parameterManager.getParameter();
    }

  }
  
  
  /**
   * Create a new Parameter with a given name and an initial value.
   * @param parameterName the name that the Parameter should have.
   * @param initialValNormalized the initial value for the parameter
   * in the range(0-1).
   */
  public Parameter createParameter(String parameterName, float initialValNormalized){
    Parameter p = createParameter(parameterName);
    p.setNormalized(initialValNormalized);
    return p;
  }
  
  
  /**
   * Create a new Parameter with a given name, an initial value,
   * a minimum value, and a maximum value.  
   * @param parameterName
   * @param initialVal the initial value for the parameter.  This must be 
   * a value between valMin and valMax.
   * @param valMin The minimum value of the parameter.
   * @param valMax The maximum value of the parameter.
   */
  public Parameter createParameter(String parameterName, float initialVal, float valMin, float valMax){
    Parameter p = createParameter(parameterName);
    p.setMinAndMax(valMin, valMax);
    p.set(initialVal);
    return p;
  }
  
  
  /**
   * @param parameterName the parameter name to search for.
   * @return Returns a reference to the Parameter if found. If
   * not found then null is returned.
   */
  private ParameterManager getParameterManagerByName(String parameterName){
    String name;
    for(Iterator<ParameterManager> i = parameters.iterator(); i.hasNext();){
      ParameterManager p = i.next();
      name = p.getParameterName();
      if(parameterName.compareTo(name) == 0){
        return p;
      }
    }
    return null;
  }
  
  
//  /**
//   * Sets the path that settings will be saved to and read from.
//   * @param filePath the path to the file.
//   */
//  public void setSaveFilePath(String filePath){
//    File file = new File(filePath);
//    int result;
//    if(file.exists()){
//      result = JOptionPane.showConfirmDialog(null, "File already exists. Over write it?");
//      if(result == 0){  //yes was selected.
//        this.saveFilePath = filePath;
//      } else {
//        
//      }
//    }
//
//  }
  
  
  /**
   * Save all Parameters to a savefile. 
   * @param filePath the path of the file to save.
   */
  public void saveToFile(String filePath){
    
    //File file = new File(filePath);
    this.saveFilePath = filePath;
    
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); 
    DocumentBuilder documentBuilder;
    Document document;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
      return;
    }
    
    document = documentBuilder.newDocument();    
    Element rootElement = document.createElement("ParametersManager");    
    rootElement.setAttribute("SchemaVersion", "0.9");
    document.appendChild(rootElement);
      
    //Iterate through all of the parameters and save them to file.
    for(Iterator<ParameterManager> i = parameters.iterator(); i.hasNext();){
      ParameterManager pm = i.next();
      pm.saveToFile(rootElement);
    }
      
     
    TransformerFactory transformerFactory = TransformerFactory.newInstance();        
    Transformer transformer;
    try {
      transformer = transformerFactory.newTransformer();
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
      return;
    }        
    
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

    DOMSource source = new DOMSource(document);     
    
    FileOutputStream fileOs;
    try {
      fileOs = new FileOutputStream(saveFilePath);
    } catch (FileNotFoundException e1) {
      // TODO Error: Invalid file.
      return;
    }
    
    StreamResult result =  new StreamResult(fileOs); 
    
    try {
      transformer.transform(source, result);
    } catch (TransformerException e) {
      e.printStackTrace();
      return;
    }
    
    try {
      fileOs.close();
    } catch (IOException e1) {
       //Error, failed to close the file after saving it.
    }
  }
  
  
  /**
   * Load in all of the parameters that are in a savefile. 
   * @param filePath the path to the file to read from.
   */
  public void readFromFile(String filePath){
    File file = new File(filePath);  
    if(!file.isFile()){
      JOptionPane.showMessageDialog(null,
          "File " + filePath + " does not exist or cannot be read.",
          "Error loading file.",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();  
    DocumentBuilder documentBuilder;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    }  
    
    Document doc;
    try {
      doc = documentBuilder.parse(file);
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    }  
    
    doc.getDocumentElement().normalize();  
    
    //begin processing the nodes.
    Element elementRoot = doc.getDocumentElement();
    if(elementRoot.getNodeName().compareTo("ParametersManager") == 0){
      if(elementRoot.getAttribute("SchemaVersion").compareTo(".09") == 0){
        
      } else {
        //TODO:error the save file was created by a different version. attempting to read anyways.
      }
      NodeList nodeLst = doc.getElementsByTagName("Parameter");  
      if(nodeLst.getLength() > 0){
        Element element;
        String parameterName;
        for(int i = 0; i < nodeLst.getLength(); ++i){
          if(nodeLst.item(i).getNodeType() == Node.ELEMENT_NODE){
            element = (Element)nodeLst.item(i);
            parameterName = element.getAttribute("name");
            if(parameterName != null){
              createParameter(parameterName); //create the parameter if it doesn't already exist.
              getParameterManagerByName(parameterName).readFromFile(element);
            }
          }
        }
      }
    }
  }
  
  
  /**
   * Sets the directory that the save/load from file dialog starts in.
   * @param dirPath the path to the directory that the open and close dialog 
   * should start in.
   */
  public void setFileChooserDefaultDirectory(String dirPath){
    parametersManagerUi.setFileChooserDefaultDirectory(dirPath);
    //sequencerUi.setFileChooserDefaultDirectory(dirPath);
  }


  /**
   * @return the automationMaster for this parameters manager.
   */
  public AutomationMaster getAutomationMaster() {
    return automationMaster;
  }

//
//  private ObservableComponent observableComponent;
//  /** @see floatingpointdev.toolkit.util.IObservable#addIObserver(floatingpointdev.toolkit.util.IObserver)*/
//  @Override
//  public void addIObserver(IObserver anIObserver) {
//    observableComponent.addIObserver(anIObserver);
//  }
//
//  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObserver(floatingpointdev.toolkit.util.IObserver)*/
//  @Override
//  public void deleteIObserver(IObserver anIObserver) {
//    observableComponent.deleteIObserver(anIObserver);
//  }
//
//
//  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObservers()*/
//  @Override
//  public void deleteIObservers() {
//    observableComponent.deleteIObservers();
//  }

}
