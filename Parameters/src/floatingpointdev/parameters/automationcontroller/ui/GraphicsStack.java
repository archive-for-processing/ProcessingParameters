/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class GraphicsStack {
  
  private LinkedList<GraphicsSettings> stack = new LinkedList<GraphicsSettings>();
  /**
   * @param g
   */
  public GraphicsStack() {
    // TODO Auto-generated constructor stub
  }

  public void push(Graphics g){
    stack.push(new GraphicsSettings(g));
    
  }
  
  public void pop(Graphics g){
    GraphicsSettings popped = stack.pop();
    popped.setGraphicsWithMySettings(g);
  }
  
  
  private class GraphicsSettings {
    private Color color;
    private Font font;
    
    /**
     * 
     */
    public GraphicsSettings(Graphics g) {
      super();
      this.color = g.getColor();
      this.font = g.getFont();
      
    }

    /** @return the color */
    public Color getColor() {
      return color;
    }


    /** @return the font */
    public Font getFont() {
      return font;
    }

    
    public void setGraphicsWithMySettings(Graphics g){
      g.setColor(getColor());
      g.setFont(getFont());
    }
  }
  
}
