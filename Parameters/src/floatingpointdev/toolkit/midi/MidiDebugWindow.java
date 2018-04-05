/**
 * 
 */
package floatingpointdev.toolkit.midi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.sound.midi.SysexMessage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import floatingpointdev.toolkit.UI.TextField;
import floatingpointdev.toolkit.util.IReceiver;

/**
 * @author floatingpointdev
 *
 */
public class MidiDebugWindow extends JFrame implements IReceiver {
   JTextArea display;
   JTextArea header;
   JScrollPane scrollPane;
   JButton btnClear;
   
   String tab = "\t";
   String newline = "\n";
   String headerStr = "Type"+tab+"Status"+tab+"Command"+tab+"Channel"+tab+"Data[1]"+tab+"Data[2]";
   
   public MidiDebugWindow(String title){
     super(title);
     
     setupUI();
     
     this.setVisible(true);
   }
  /* (non-Javadoc)
   * @see floatingpointdev.toolkit.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)
   */
  @Override
  public void processMessage(Object transmitter, Object message) {
    if(message instanceof FpdMidiMessageShort){
      FpdMidiMessageShort m = (FpdMidiMessageShort)message;
      String type= "ShortMessage";
      String status= Integer.toString(m.getStatus());;
      String command = Integer.toString(m.getCommand());
      String channel = Integer.toString(m.getChannel());
      String data1 = Integer.toString(m.getData1());
      String data2 = Integer.toString(m.getData2());
      
      display.append(type+tab+status+tab+command+tab+channel+tab+data1+tab+data2+newline);
    } else if(message instanceof SysexMessage){
      SysexMessage m = (SysexMessage)message;
      
      String type = "Sysex";
      String status = Integer.toString(m.getStatus());
      String length = Integer.toString(m.getLength());
      byte[] data = m.getData();
      //byte[] dataMessage = m.getMessage();
      String strData = new String();
      

      
      for(int i = 0; i< data.length;++i){
        if(data[i] < 10){
          strData += " ";
        }
        if(data[i] < 100){
          strData += " ";
        }
        strData += Byte.toString(data[i]) +", ";
        
        
        //char c = (char)(data[i] + (byte)'a');
       // strData += c +", ";
        
        
      }

      if(m.getLength() == 24){
        //if this is one of the special apc40 live messages
        Date date = new Date();
        long time = date.getTime();
        
        byte byteTime = Long.SIZE;
        
        display.append(strData+newline);
        
        byte[] md5;
        
        MessageDigest messageDigest;
        try {
          messageDigest = MessageDigest.getInstance("MD5");
          
          md5 = messageDigest.digest(m.getData());
          
          display.append("md5: " + 
              Byte.toString(md5[0]) + 
              Byte.toString(md5[1]) + 
              Byte.toString(md5[2]) + 
              Byte.toString(md5[3]) + 
              Byte.toString(md5[4]) + 
              
              
              
              newline);
          
          
          
        } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
        }
        

        
        
        
        
      } else {
        //if this is any other sysex message.
        display.append("Sysex"+tab+length+tab+status+tab+strData+newline);
        
        

      }
      
      
      
//      display.append("decoding:"+newline);
//      String[] strArray = decode(data,-512,-127);
//      
//      for(int i = 0; i<strArray.length; ++i){
//        display.append(tab+strArray[i]+ newline);
//      }
      
      
    }

  }
  
  public String decodeBytes(byte[] data, int charOffset){
    String strData = new String();
    
    for(int i = 0; i< data.length;++i){
      //strData += Byte.toString(data[i]) +", ";
      
      
      char c = (char)(data[i] + charOffset);
      //char a = 'a';
      //byte aByte = (byte)a;
      strData += c +", ";
    }
    return strData;
  }
  
  
  public String[] decode(byte[]data, int offsetLow, int offsetHi){
    String[] stringArray = new String[offsetHi-offsetLow + 1];
    String str = new String();
    for(int offset = offsetLow; offset <= offsetHi; ++offset){
      str = offset +") " + decodeBytes(data, offset);
      stringArray[offset-offsetLow] = str;
    }
    return stringArray;
  }
    
    
   
  
  public void setupUI(){
    header = new JTextArea(1,32);
    header.setText(headerStr);
    display = new JTextArea(24,32);
    scrollPane = new JScrollPane(display);
    btnClear = new JButton("clear");
    btnClear.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e){
        display.setText("");
      }
      
    });
    
    JPanel contents = new JPanel();
    contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
    contents.add(header);
    contents.add(scrollPane);
    contents.add(btnClear);
    
    this.setContentPane(contents);
    
    //this.setTitle("Midi Debug");
    pack();
    
    
  }

}
