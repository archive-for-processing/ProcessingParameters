/**
 * 
 */
package floatingpointdev.parameters.automationcontroller;

import java.util.UUID;

/**
 * A time value pair.  Value must be between 0 and 1.  Time must be in the range (0, +infinity)
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */

public class Event {

  private long time = 0;
  private float value = 0;
  private UUID uuid;
  
  /**
   * @param time
   * @param value
   */
  public Event(long time, float value) {
    super();
    setTime(time);
    setValue(value);
    uuid = UUID.randomUUID();
  }
  
  /** @return the time */
  public long getTime() {
    return time;
  }
  
  /** @param time the time to set */
  public void setTime(long time) {
    if(time < 0){
      time = 0;
      //throw new IllegalArgumentException("time cannot be negative");
    }
    this.time = time;
  }
  
  /** @return the value */
  public float getValue() {
    return value;
  }
  
  /** @param value the value to set */
  public void setValue(float value) {
    if(value < 0){
      value = 0;
     // throw new IllegalArgumentException("Value must be between 0 and 1");
    }
    if(value > 1){
      value = 1;
     // throw new IllegalArgumentException("Value must be between 0 and 1");
    }
    this.value = value;
  }

  /** @return the uuid */
  public UUID getUuid() {
    return uuid;
  }

  /** @param uuid the uuid to set */
  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  /** @see java.lang.Object#hashCode()*/
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (time ^ (time >>> 32));
    result = prime * result + Float.floatToIntBits(value);
    return result;
  }

  /** @see java.lang.Object#equals(java.lang.Object)*/
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Event other = (Event) obj;
    if (time != other.time)
      return false;
    if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
      return false;
    return true;
  }

  Event(Event copyFrom){
    this.time = copyFrom.time;
    this.value = copyFrom.value;
    this.uuid = copyFrom.uuid;
  }
  
//  public Event previousEvent(){
//    return null;
//  }
  
}
