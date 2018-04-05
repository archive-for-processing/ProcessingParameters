/**
 * 
 */
package floatingpointdev.parameters.ParametersUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import floatingpointdev.parameters.ParametersManager;
import floatingpointdev.toolkit.util.FileUtil;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class MainMenu extends JMenuBar implements ActionListener{
  private JFrame parentFrame;
  /** */
  private static final long serialVersionUID = -8668639704682617309L;

  private JMenuItem menuItemSaveToFile;
  private JMenuItem menuItemLoadFromFile;
  private final JFileChooser fileChooser;
  private String defaultFileChooserDirectory = null;
  private ParametersManager parametersManager;
  
  private class SaveFileFilter extends FileFilter{
    /** @see javax.swing.filechooser.FileFilter#accept(java.io.File)*/
    @Override
    public boolean accept(File f) {
      if (f.isDirectory()) {
        return true;
      }

      String extension = FileUtil.getExtension(f);
      if (extension != null) {
        if (extension.equals("xml")) {
          return true;
        }
      }
      return false; 
    }

    /** @see javax.swing.filechooser.FileFilter#getDescription()*/
    @Override
    public String getDescription() {
      return ".xml";
    }
  }
  

  MainMenu(ParametersManager parametersManager, JFrame parentFrame){
    this.parametersManager = parametersManager;
    fileChooser = new JFileChooser(defaultFileChooserDirectory);
    fileChooser.setMultiSelectionEnabled(false);
    fileChooser.addChoosableFileFilter(new SaveFileFilter());
    this.parentFrame = parentFrame;
    setupMenu();
  }
  
  
  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    //
    //Menu Action Save To File.
    //
    if(actionEvent.getSource() == menuItemSaveToFile){
      int returnVal = fileChooser.showSaveDialog(parentFrame);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          
          if(FileUtil.getExtension(file).compareTo("xml") != 0){
            file = new File(file.getAbsolutePath() + ".xml");
          }
          if(file.exists()){
            int choice = JOptionPane.showConfirmDialog(null, "File already exists. Over write it?", "File Exists Warning", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION){  //yes was selected.

            } else {
              return;
            }
          }
          
          if(file.isDirectory()){
            JOptionPane.showMessageDialog(this,
                "Invalid file selection.  Save failed.",
                "Error selecting file.",
                JOptionPane.ERROR_MESSAGE);
          } else {
            parametersManager.saveToFile(file.getAbsolutePath());
          }
      } else {
          //Cancel button selected.  do nothing.
      }


      //
      //Menu action Load from file
      //
    } else if(actionEvent.getSource() == menuItemLoadFromFile){
      File file = null;
      int returnVal = fileChooser.showOpenDialog(parentFrame);
      if(returnVal == JFileChooser.APPROVE_OPTION){
        file = fileChooser.getSelectedFile();
        if((file != null) && (file.isFile())){//if an actual file was selected.
          if(FileUtil.getExtension(file).compareToIgnoreCase("xml") == 0){ //if the file is an xml file.
            parametersManager.readFromFile(file.getAbsolutePath());
            return; // success.
          }
        }
        //An invalid file was selected.
        JOptionPane.showMessageDialog(parentFrame,
            "Invalid file selection.  Load from file failed.",
            "Error selecting file.",
            JOptionPane.ERROR_MESSAGE);
      }
    }
    
  }
  
  public void setFileChooserDefaultDirectory(String dirPath){
    this.defaultFileChooserDirectory = dirPath;
    if(fileChooser != null){
      fileChooser.setCurrentDirectory(new File(dirPath));
    }
  }
 
  /**
   * Construct the menu bar.
   */
  private void setupMenu(){

    JMenu menu, submenu;
    JMenuItem menuItem;

    //Build the first menu.
    menu = new JMenu("File");
    menu.setMnemonic(KeyEvent.VK_ALT);
    menu.getAccessibleContext().setAccessibleDescription(
            "The file menu.");
    this.add(menu);
    
    //The save settings menu item.
    menuItemSaveToFile = new JMenuItem("Save To File", KeyEvent.VK_S);
    menuItemSaveToFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    menuItemSaveToFile.getAccessibleContext().setAccessibleDescription("Save to file.");
    menuItemSaveToFile.addActionListener(this);
    menu.add(menuItemSaveToFile);
    
    //The Load settings menu item.
    menuItemLoadFromFile = new JMenuItem("Load From File", KeyEvent.VK_L);
    menuItemLoadFromFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
    menuItemLoadFromFile.getAccessibleContext().setAccessibleDescription("Load from file.");
    menuItemLoadFromFile.addActionListener(this);
    menu.add(menuItemLoadFromFile);
  }
  

}
