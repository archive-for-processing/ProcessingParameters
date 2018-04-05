import floatingpointdev.parameters.*;
import floatingpointdev.toolkit.midi.IoMidi;
import processing.core.PApplet;

/**
 * @author floatingpointdev
 *
 */
public class ParametersTestApplet extends PApplet {
  ParametersManager parametersManager;
  IoMidi ioMidi;
  Parameter p1;

  Parameter circleWidth;
  Parameter fillColor;
  
  public void setup(){
    size(600,200);
    background(0);
    ioMidi = new IoMidi();
    parametersManager = new ParametersManager(ioMidi);

    p1 = parametersManager.createParameter("circleX");
    p1.setMax(200);
    p1.set(100);
  
    circleWidth = parametersManager.createParameter("circleWidth");
    circleWidth.set(.5f);
    
    fillColor = parametersManager.createParameter("fillColor");
    fillColor.setMax(255);
    fillColor.set(200); 
  }
  
  
  public void draw(){
    background(0);
    this.stroke(1);
    this.fill(fillColor.get());
    this.ellipse(p1.get(), 100, circleWidth.get() *50, 50);
  }
}
