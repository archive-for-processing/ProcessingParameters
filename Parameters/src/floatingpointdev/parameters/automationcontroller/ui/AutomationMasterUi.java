/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import floatingpointdev.parameters.AutomationMaster;
import floatingpointdev.toolkit.util.IObserver;


/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class AutomationMasterUi extends javax.swing.JPanel implements ActionListener, IObserver{
  private AutomationMaster automationMaster;
  
  private JButton playBtn;
  private JButton pauseBtn;
  private JButton stopBtn;
  private JTextField endTimeField;
  private JTextField positionField;

  public AutomationMasterUi(AutomationMaster automationMaster){
    this.automationMaster = automationMaster;
    this.automationMaster.addIObserver(this);
    setupUi(); 
  }
  
  private void setupUi(){
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    playBtn = new JButton("Play");
    playBtn.addActionListener(this);
      
    pauseBtn = new JButton("Pause");
    pauseBtn.addActionListener(this);
    
    stopBtn = new JButton("Stop");
    stopBtn.addActionListener(this);
    
    positionField = new JTextField();
    positionField.setText(Long.toString(automationMaster.getPosition()));
    positionField.addActionListener(new PositionFieldListener());
    
    endTimeField = new JTextField();
    endTimeField.setText(Long.toString(automationMaster.getEndTime()));
    endTimeField.addActionListener(new EndTimeFieldListener());
    
    this.add(playBtn);
    this.add(pauseBtn);
    this.add(stopBtn);
    this.add(positionField);
    this.add(new JLabel("EndTime:"));
    this.add(endTimeField);
  }

  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if(actionEvent.getSource() == playBtn){
      automationMaster.play();
    } else if(actionEvent.getSource() == pauseBtn){
      automationMaster.pause();
    }else if(actionEvent.getSource() == stopBtn){
      automationMaster.pause();
      automationMaster.setPosition(0);
    }
    
  }

  /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
  @Override
  public void update(Object theObserved, Object changeCode) {
    if(changeCode == "position"){
      positionField.setText(Long.toString(automationMaster.getPosition()));
    }
    
  }
  
  private class PositionFieldListener implements ActionListener{

    /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
    @Override
    public void actionPerformed(ActionEvent event) {
      String text = positionField.getText();
      long position = Long.parseLong(text);
      automationMaster.setPosition(position);
    }
  }
  
  private class EndTimeFieldListener implements ActionListener{

    /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
    @Override
    public void actionPerformed(ActionEvent event) {
      String text = endTimeField.getText();
      long position = Long.parseLong(text);
      automationMaster.setEndTime(position);
      
    }
    
  }
}
