/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

import floatingpointdev.parameters.ParametersUI.SequenceListEditor;
import floatingpointdev.parameters.automationcontroller.AutomationController;
import floatingpointdev.toolkit.util.IObserver;


/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class AutomationLane extends JPanel{

  private AutomationController automationController;
  private Lane lane;
  /** */
  private static final long serialVersionUID = 6331629235788711339L;

  public AutomationLane(AutomationController automationController){
    this.automationController = automationController;
    lane = new Lane(automationController.getSequence(), automationController.getAutomationMaster());
    setupUi();
  }
  
  private void setupUi(){
    JPanel laneDebugPanel = new SequenceListEditor(automationController.getSequence());
    this.setBorder(BorderFactory.createLineBorder(new Color(50,0,0),2));
    this.setLayout(new BorderLayout());

    this.add(lane, BorderLayout.CENTER);
    this.add(laneDebugPanel, BorderLayout.EAST);
    
  }

  
}
