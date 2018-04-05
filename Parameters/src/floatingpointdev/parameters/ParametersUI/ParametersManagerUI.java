package floatingpointdev.parameters.ParametersUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;

import floatingpointdev.parameters.ParameterManager;
import floatingpointdev.parameters.ParametersManager;
import floatingpointdev.parameters.automationcontroller.ui.AutomationMasterUi;
import floatingpointdev.toolkit.util.FileUtil;

public class ParametersManagerUI{// implements ActionListener{
//  private JMenuItem menuItemSaveToFile;
//  private JMenuItem menuItemLoadFromFile;
//  final JFileChooser fileChooser;
  private MainMenu mainMenu;
  JPanel parametersPanel = new JPanel();
//  private class SaveFileFilter extends FileFilter{
//    /** @see javax.swing.filechooser.FileFilter#accept(java.io.File)*/
//    @Override
//    public boolean accept(File f) {
//      if (f.isDirectory()) {
//        return true;
//      }
//
//      String extension = FileUtil.getExtension(f);
//      if (extension != null) {
//        if (extension.equals("xml")) {
//          return true;
//        }
//      }
//      return false; 
//    }
//
//    /** @see javax.swing.filechooser.FileFilter#getDescription()*/
//    @Override
//    public String getDescription() {
//      return ".xml";
//    }
//  }
//  
  
  /** The ParametersManaager that this UI is for.*/
  private ParametersManager parametersManager;
  
  /** The list of parameters this ParameterManager controls.*/
  private ArrayList<ParameterManager> parameters = new ArrayList<ParameterManager>();
  
  /** The list of parameterUI objects this ParameterManager controls.*/
  private ArrayList<ParameterUi> parameterUis = new ArrayList<ParameterUi>();
  
  JFrame frame;
  private String defaultFileChooserDirectory = null;
  
  public ParametersManagerUI(ParametersManager parametersManager) {
    if(parametersManager == null){
      throw new IllegalArgumentException("A parametersManager object must be " +
      		                            "supplied with the constructor");
    }
    this.parametersManager = parametersManager;
    
    try{
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }
    catch(Exception e){}
    

    parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

    frame = new JFrame();
    
    mainMenu = new MainMenu(parametersManager, frame);
    frame.setJMenuBar(mainMenu);
    
    frame.getContentPane().setLayout(new BorderLayout());
    JPanel automationMasterUi = new AutomationMasterUi(parametersManager.getAutomationMaster());
    frame.add(automationMasterUi, BorderLayout.NORTH);
    frame.add(parametersPanel, BorderLayout.CENTER);
    
    //Setup the event handler for closing the window.
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });

    frame.setTitle ("Parameters");
    frame.setSize (200,600);

    frame.pack();
    frame.setVisible(true);
    
//    fileChooser = new JFileChooser(defaultFileChooserDirectory);
//    fileChooser.setMultiSelectionEnabled(false);
//    fileChooser.addChoosableFileFilter(new SaveFileFilter());
  }
  
  
//  /**
//   * Construct the menu bar.
//   */
//  private void setupMenu(){
//    JMenuBar menuBar;
//    JMenu menu, submenu;
//    JMenuItem menuItem;
//
//    //Create the menu bar.
//    menuBar = new JMenuBar();
//
//    //Build the first menu.
//    menu = new JMenu("File");
//    menu.setMnemonic(KeyEvent.VK_ALT);
//    menu.getAccessibleContext().setAccessibleDescription(
//            "The file menu.");
//    menuBar.add(menu);
//    
//    //The save settings menu item.
//    menuItemSaveToFile = new JMenuItem("Save To File", KeyEvent.VK_S);
//    menuItemSaveToFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
//    menuItemSaveToFile.getAccessibleContext().setAccessibleDescription("Save to file.");
//    menuItemSaveToFile.addActionListener(this);
//    menu.add(menuItemSaveToFile);
//    
//    //The Load settings menu item.
//    menuItemLoadFromFile = new JMenuItem("Load From File", KeyEvent.VK_L);
//    menuItemLoadFromFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
//    menuItemLoadFromFile.getAccessibleContext().setAccessibleDescription("Load from file.");
//    menuItemLoadFromFile.addActionListener(this);
//    menu.add(menuItemLoadFromFile);
//    
//    //attach the menuBar to the main frame.
//    frame.setJMenuBar(menuBar);
//  }
//  
  
  /**
   * Adds a parameter to the parametersManager
   * @param p the parameter to add.
   */
  public void addParameter(ParameterManager p){
    parameters.add(p);
    ParameterUi pUi = p.getParameterUi();
    //add the UI and attach it to the parameter.
    parameterUis.add(pUi);
    parametersPanel.add(pUi.getUiPanel());
    frame.pack();
  }
  
  /**
   * Removes a parameter from the parametersManager
   * @param p the parameter to remove.
   */
  public void removeParameter(ParameterManager p){
    parameters.remove(p);
    
    //Remove the associated parameterUI
    //Iterate through the list of parameterUis and remove any that
    //match the parameter we are removing.
    for(ListIterator<ParameterUi> i = parameterUis.listIterator(); i.hasNext(); ){
      ParameterUi pUI = i.next();
      if(pUI.isUIForParameter(p)){
        pUI.destruct();
        parameterUis.remove(pUI);
        parametersPanel.remove(pUI.getUiPanel());
        frame.pack();
      }
    }
  }


  /**
   * @param dirPath
   */
  public void setFileChooserDefaultDirectory(String dirPath) {
    mainMenu.setFileChooserDefaultDirectory(dirPath);
  }

//
//  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
//  @Override
//  public void actionPerformed(ActionEvent actionEvent) {
//    //
//    //Menu Action Save To File.
//    //
//    if(actionEvent.getSource() == menuItemSaveToFile){
//      int returnVal = fileChooser.showSaveDialog(frame);
//      if (returnVal == JFileChooser.APPROVE_OPTION) {
//          File file = fileChooser.getSelectedFile();
//          
//          if(FileUtil.getExtension(file).compareTo("xml") != 0){
//            file = new File(file.getAbsolutePath() + ".xml");
//          }
//          if(file.exists()){
//            int choice = JOptionPane.showConfirmDialog(null, "File already exists. Over write it?", "File Exists Warning", JOptionPane.YES_NO_OPTION);
//            if(choice == JOptionPane.YES_OPTION){  //yes was selected.
//
//            } else {
//              return;
//            }
//          }
//          
//          if(file.isDirectory()){
//            JOptionPane.showMessageDialog(frame,
//                "Invalid file selection.  Save failed.",
//                "Error selecting file.",
//                JOptionPane.ERROR_MESSAGE);
//          } else {
//            parametersManager.saveToFile(file.getAbsolutePath());
//          }
//      } else {
//          //Cancel button selected.  do nothing.
//      }
//
//
//      //
//      //Menu action Load from file
//      //
//    } else if(actionEvent.getSource() == menuItemLoadFromFile){
//      File file = null;
//      int returnVal = fileChooser.showOpenDialog(frame);
//      if(returnVal == JFileChooser.APPROVE_OPTION){
//        file = fileChooser.getSelectedFile();
//        if((file != null) && (file.isFile())){//if an actual file was selected.
//          if(FileUtil.getExtension(file).compareToIgnoreCase("xml") == 0){ //if the file is an xml file.
//            parametersManager.readFromFile(file.getAbsolutePath());
//            return; // success.
//          }
//        }
//        //An invalid file was selected.
//        JOptionPane.showMessageDialog(frame,
//            "Invalid file selection.  Load from file failed.",
//            "Error selecting file.",
//            JOptionPane.ERROR_MESSAGE);
//      }
//    }
//    
//  }
//  
//  public void setFileChooserDefaultDirectory(String dirPath){
//    this.defaultFileChooserDirectory = dirPath;
//    if(fileChooser != null){
//      fileChooser.setCurrentDirectory(new File(dirPath));
//    }
//  }
// 
  

}
