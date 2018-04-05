/**
 * 
 */
package floatingpointdev.toolkit.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Set;

/**
 * A 1 to 1 mapping object.  It contains a list of pairs of objects, keys and values.
 * each key must be unique and each value must be unique to maintain the 1 to 1 mapping.
 * 
 * @author floatingpointdev
 *
 */
public class FpdMap<K,V> {

  private ArrayList<K> keys;
  private ArrayList<V> values;

  /**
   * 
   */
  public FpdMap() {
    keys = new ArrayList<K>();
    values = new ArrayList<V>();
  }

  public int size(){
    return keys.size();
  }
  
  public void put(K key, V value){
    //check if key already exists.
    if(keys.contains(key)){
      int index = keys.indexOf(key);
      values.set(index, value);
      return;
    }
    //check if value already exists.
    if(values.contains(value)){
      int index = values.indexOf(value);
      keys.set(index, key);
      return;
    } 
    
    keys.add(key);
    values.add(value);
    
  }
  
  public V getValueFromKey(K key){
    int index = keys.indexOf(key);
    
    if(index == -1){
      return null;
    } 
    
    return values.get(index);
  }
  
  public K getKeyFromValue(V value){
    int index = values.indexOf(value);
    
    if(index == -1){
      return null;
    } 
    
    return keys.get(index);
  } 
  
  public V[] values(){
    return (V[])values.toArray();
  }
  
  public K[] keys(){
    return (K[])keys.toArray();
  }
  
  public K getKeyAt(int index){
    return keys.get(index);
  }
  
  public V getValueAt(int index){
    return values.get(index);
  }
  
  /**Remove all keys and values from this map.*/
  public void clear(){
    keys.clear();
    values.clear();
  }
}
