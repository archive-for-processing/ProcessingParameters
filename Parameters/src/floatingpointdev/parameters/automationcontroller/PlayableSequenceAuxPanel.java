/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class PlayableSequenceAuxPanel extends JPanel{
  /** */
  private static final long serialVersionUID = -7324014797474041412L;
  PlayableSequence playableSequence;
  
  public PlayableSequenceAuxPanel(PlayableSequence playableSequence){
    this.playableSequence = playableSequence;
    setupUi();
  }
  
  private void setupUi(){
    JToggleButton readEnableBtn = new JToggleButton("Read");
    readEnableBtn.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton)e.getSource();
        if(btn.isSelected()){
          playableSequence.setReadEnabled(true);
        } else {
          playableSequence.setReadEnabled(false);
        }
      }

     });
    JToggleButton writeEnableBtn = new JToggleButton("Write");
    writeEnableBtn.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton)e.getSource();
        if(btn.isSelected()){
          playableSequence.setWriteEnabled(true);
        } else {
          playableSequence.setWriteEnabled(false);
        }
      }});
    
    JButton clearAutomationBtn = new JButton("Clear");
    clearAutomationBtn.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        playableSequence.clear();
      }
    });
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBorder(BorderFactory.createLineBorder(new Color(50,0,0),1));
    this.add(readEnableBtn);
    this.add(writeEnableBtn);
    this.add(clearAutomationBtn); 

  }
}
