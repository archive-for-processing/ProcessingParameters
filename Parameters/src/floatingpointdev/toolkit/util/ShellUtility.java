/**
 * 
 */
package floatingpointdev.toolkit.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author floatingpointdev
 *
 */
public class ShellUtility {

  public static void execShellCommand(String cmd){
    // cmd is the command to execute in the Unix shell
    // create a process for the shell
    ProcessBuilder pb = new ProcessBuilder("bash", "-c", cmd);
    pb.redirectErrorStream(true); // use this to capture messages sent to stderr
    Process shell;
    try{
      shell = pb.start();      

      InputStream shellIn = shell.getInputStream(); // this captures the output from the command
      int shellExitStatus;
      try{
        shellExitStatus = shell.waitFor(); // wait for the shell to finish and get the return code
      } catch (InterruptedException e){
      }
      // at this point you can process the output issued by the command
      // for instance, this reads the output and writes it to System.out:
      int c;
      while ((c = shellIn.read()) != -1) {System.out.write(c);}
      // close the stream
      shellIn.close();
    } catch (IOException e){  
    }
  
  }  
}
