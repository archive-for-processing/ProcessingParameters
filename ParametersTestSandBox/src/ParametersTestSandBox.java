import processing.core.PApplet;

/**
 * 
 */

/**
 * @author floatingpointdev
 *
 */
public class ParametersTestSandBox {
  public static void main(String[] args) {
    PApplet pApplet = new ParametersTestApplet();  
    @SuppressWarnings("unused")
    ProcessingWindow processingWindow = new ProcessingWindow(pApplet, "title");
  }
}
