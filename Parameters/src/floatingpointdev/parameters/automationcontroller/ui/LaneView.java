/**
 * 
 */
package floatingpointdev.parameters.automationcontroller.ui;

import java.awt.Graphics;

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class LaneView {
  private int widthPixels;
  private int heightPixels;
  private long viewStartTime;
  private long viewEndTime;
  
  public void drawLine(float x1, float y1, float x2, float y2, Graphics g){
    g.drawLine(toScreenX(x1), toScreenY(y1), toScreenX(x2), toScreenY(y2));
  }

  /** @return the widthPixels */
  public int getWidthPixels() {
    return widthPixels;
  }

  /** @return the heightPixels */
  public int getHeightPixels() {
    return heightPixels;
  }

  /** @return the viewEndTime */
  public long getViewEndTime() {
    return viewEndTime;
  }

  /** @return the viewStartTime */
  public long getViewStartTime() {
    return viewStartTime;
  }

  public void paint(Graphics g){
    
  }

  /**
   * Convert the pixel coordinate x in the view to a time.
   * @param x the x coordinate of a pixel in the view to convert.
   * @return the time that x represents.
   */
  public long ScreenXToTime(int x){
    //long ret =  (x-widthPixels) / (viewEndTime - viewStartTime);
    long ret = (long)(((float)x/(float)widthPixels) *  (float)(viewEndTime - viewStartTime)) + viewStartTime;
    return ret;
  }

  /**
   * Convert the pixel coordinate y in the view to a value.
   * @param y the y coordinate of a pixel in teh view to convert.
   * @return the value that the pixel at y represents.
   */
  public float ScreenYToValue(int y) {
    float ret = 1-(float)(((float)y/(float)heightPixels));
    return ret;
  }

  /** @param heightPixels the heightPixels to set */
  public void setHeightPixels(int heightPixels) {
    this.heightPixels = heightPixels;
  }

  /** @param viewEndTime the viewEndTime to set */
  public void setViewEndTime(long endTime) {
    this.viewEndTime = endTime;
  }

  /** @param viewStartTime the viewStartTime to set */
  public void setViewStartTime(long startTime) {
    this.viewStartTime = startTime;
  }

  /** @param widthPixels the widthPixels to set */
  public synchronized void setWidthPixels(int widthPixels) {
    this.widthPixels = widthPixels;
  }

  /**
   * Convert a time into percentage of the view.  e.g. if view start time is 10s, and view end time is 20s
   * this function would return .5 for a value of 15 and.2 for the time of 12s.
   * 
   * @param time the time to convert.
   * @return a percentage of the view. (0,1)
   */
  public float timeToGraphX(long time){
    float currentX = (float)((time - viewStartTime)) / (float)((viewEndTime-viewStartTime));
    return currentX;
  }

  /**
   * Convert a time into pixel coordinates.
   * @param time the time to convert.
   * @return the x coordinate that corresponds to time.
   */
  public int timeToScreenX(long time){
    return toScreenX(timeToGraphX(time));
  }

  /**
   * get the x pixel coordinate given a percentage of the view x.
   * @param x
   * @return
   */
  public int toScreenX(float x){
    return (int)(x * (float)widthPixels);
  }

  /**
   * get the y pixel coordinate.
   * @param y
   * @return
   */
  public int toScreenY(float y){
    return (int)((1-y) * (float)heightPixels);
  }
}
