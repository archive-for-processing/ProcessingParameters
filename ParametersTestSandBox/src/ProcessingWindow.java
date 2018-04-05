
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import processing.core.PApplet;

/**
 * @author floatingpointdev
 *
 * Creates a Swing frame containing a PApplet.
 *
 */


public class ProcessingWindow {
  private PApplet pApplet;
  private JFrame frame;
  
  public ProcessingWindow(PApplet pApplet, String windowText){
    this.pApplet = pApplet;
    frame = new JFrame(windowText);

    //setup the PApplet.
    pApplet.init();
    //pApplet.setSize(appletWidth, appletHeight);   

    JPanel appletFrame = new JPanel();
    appletFrame.add(pApplet);
    frame.setContentPane(appletFrame);
    frame.pack();
    frame.setVisible(true);
    //frame.setSize(appletWidth, appletHeight);
    
    //Setup the event handler for closing the window.
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });
  }
  
}
