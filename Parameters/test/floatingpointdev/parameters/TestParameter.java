package floatingpointdev.parameters;


import static org.junit.Assert.*;

import org.junit.Test;

import floatingpointdev.parameters.Parameter;
import floatingpointdev.toolkit.util.IObserver;

/**
 * 
 */

/**
 * @author floatingpointdev
 *
 */
public class TestParameter {
  private static float FLOAT_EQUALS_DELTA = .000001f;
  /**
   * Test method for {@link floatingpointdev.parameters.Parameter#get()}.
   */
  @Test
  public void testGetVal() {
    Parameter p = new Parameter("test");
    //test initial value
    assertEquals("InitialValue should be 0",0,p.get(), FLOAT_EQUALS_DELTA);
    
    p.set(.5f);
    assertEquals("set then get value should be .5", .5, p.get(), FLOAT_EQUALS_DELTA);
  }

  
  /**
   * Test method for {@link floatingpointdev.parameters.Parameter#set(float)}.
   */
  @Test
  public void testSetVal() {
    Parameter p = new Parameter("test");

    p.set(.5f);
    assertEquals("Val should still be at .5.", .5, p.get(), FLOAT_EQUALS_DELTA);
    
    p.set(p.getMax()+1f);
    assertEquals("setting the value above the max should set the value to max.",
                 p.getMax(), p.get(), FLOAT_EQUALS_DELTA);
    
    p.set(p.getMin()-1f);
    assertEquals("Setting the value below the min should set the value to min.",
                 p.getMin(), p.get(), FLOAT_EQUALS_DELTA);
  }

  
  @Test
  public void testSetValMin(){
    Parameter p = new Parameter("test");
    p.set(.5f);
    //test setting minVal  below val.
    p.setMin(.5f);
    assertEquals("Setting then getting valMin",.5f,p.getMin(), FLOAT_EQUALS_DELTA);
    assertEquals("Changing valMin should scale val.", .75f, p.get(), FLOAT_EQUALS_DELTA);
    
    //test setting minVal above val.
    p.setMin(-1f);
    assertEquals("Changing valMin should scale val.", 0, p.get(), FLOAT_EQUALS_DELTA);
  }
  
  
  @Test
  public void testSetValMinAboveValMax(){
    //test setting minVal above maxVal
    Parameter p = new Parameter("test");
    p.set(.5f);
    p.setMin(2f);
    //assertEquals("Setting then getting valMin", 2f, p.getValMin(), FLOAT_EQUALS_DELTA);
    //assertEquals("Setting valMin above valMax should set val to valMax.", p.getValMax(), p.getVal(), FLOAT_EQUALS_DELTA);
    assertEquals("valMin should be unchanged", 0f, p.getMin(), FLOAT_EQUALS_DELTA);
    assertEquals("valMax should be unchanged", 1f, p.getMax(), FLOAT_EQUALS_DELTA);  
    assertEquals("val should be unchanged", .5f, p.get(), FLOAT_EQUALS_DELTA);
  
  }
  
  
  @Test
  public void testSetValMax(){
    //test setting maxVal above val.
    Parameter p = new Parameter("test");
    p.set(.5f);
    p.setMax(2f);
    assertEquals("Setting then getting valMax", 2f, p.getMax(), FLOAT_EQUALS_DELTA);
    assertEquals("Changing valMax should scale val.", 1f, p.get(), FLOAT_EQUALS_DELTA);

    //test setting maxVal below val.
    p.setMax(.5f);
    assertEquals("Changing valMin should scale val.", .25f, p.get(), FLOAT_EQUALS_DELTA);
  }
  
  
  @Test
  public void testSetValMinAndMax(){
    Parameter p = new Parameter("test");
    p.setNormalized(.5f);
    
    //test setting valmax and valmin below the current valMin.
    p.setMinAndMax(-2f, -1f);
    assertEquals("ValMin should have changed.", -2f, p.getMin(), FLOAT_EQUALS_DELTA);
    assertEquals("ValMax should have changed.", -1f, p.getMax(), FLOAT_EQUALS_DELTA);
    
    //test setting valMax below valMin.
    p.setMinAndMax(2, 1);
    assertEquals("ValMin should not have changed.", -2f, p.getMin(), FLOAT_EQUALS_DELTA);
    assertEquals("ValMax should not have changed.", -1f, p.getMax(), FLOAT_EQUALS_DELTA);
  }
  
  
  @Test
  public void testSetValMaxBelowValMin(){
    //test setting maxVal below maxVal
    Parameter p = new Parameter("test");
    p.set(.5f);
    p.setMax(-1f);
    assertEquals("valMin should be unchanged", 0f, p.getMin(), FLOAT_EQUALS_DELTA);
    assertEquals("valMax should be unchanged", 1f, p.getMax(), FLOAT_EQUALS_DELTA);  
    assertEquals("val should be unchanged", .5f, p.get(), FLOAT_EQUALS_DELTA);
  }
  
  
  @Test
  public void testGetValNormalized(){
    Parameter p = new Parameter("test");
    p.set(.5f);
    assertEquals("Normalized Val should be .5.", .5f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    
    p.setMin(-1f);
    p.setMax(1f);
    
    p.set(-1f);
    assertEquals("Normalized Val should be 0.", 0f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    
    p.set(.5f);
    assertEquals("Normalized Val should be .75.", .75f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    
    p.set(1f);
    assertEquals("Normalized Val should be 1.", 1f, p.getNormalized(), FLOAT_EQUALS_DELTA);
  }
  
  
  @Test
  public void testSetValNormalized(){
    Parameter p = new Parameter("test");
    p.setMin(-1f);
    p.setMax(1f);
    
    p.setNormalized(0f);
    assertEquals("Normalized Val should be 0.", 0f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    assertEquals("Val should be -1.", -1f, p.get(), FLOAT_EQUALS_DELTA);
        
    p.setNormalized(.5f);
    assertEquals("Normalized Val should be .5.", .5f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    assertEquals("Val should be 0.", 0f, p.get(), FLOAT_EQUALS_DELTA);
    
    p.setNormalized(1f);
    assertEquals("Normalized Val should be 1.", 1f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    assertEquals("Val should be 1.", 1f, p.get(), FLOAT_EQUALS_DELTA);
    
    //test settingval normalized above 1.
    p.setNormalized(2f);
    assertEquals("Normalized Val should be set to 1.", 1f, p.getNormalized(), FLOAT_EQUALS_DELTA);
    
    //test settingval normalized below 0.
    p.setNormalized(-1f);
    assertEquals("Normalized Val should be set to 0.", 0f, p.getNormalized(), FLOAT_EQUALS_DELTA);
  }

  /**
   * Test method for {@link floatingpointdev.parameters.Parameter#addIObserver(floatingpointdev.toolkit.util.IObserver)}.
   */
  @Test
  public void testAddIObserver() {
    Parameter p = new Parameter("name");
    MockObserver mockObserver = new MockObserver();
    p.addIObserver(mockObserver);
    
    //test val changed
    p.set(.5f);
    assertEquals("The observer should have received the new val.", .5f, mockObserver.val, FLOAT_EQUALS_DELTA); 
    
    //test valmin changed
    p.setMin(.1f);
    assertEquals("The observer should have received the new valMin.", .1f, mockObserver.valMin, FLOAT_EQUALS_DELTA); 
    
    //test valmax changed
    p.setMax(.9f);
    assertEquals("The observer should have received the new valMax.", .9f, mockObserver.valMax, FLOAT_EQUALS_DELTA);  
  }

  
  private class MockObserver implements IObserver{
    float val = 999;
    float valMin = 999;
    float valMax = 999;
    
    @Override
    public void update(Object theObserved, Object changeCode) {
      String changeCodeStr = (String)changeCode;
      Parameter theObservedParameter = (Parameter)theObserved;
      if(changeCodeStr.compareTo("val") == 0){
        val = theObservedParameter.get();
      } else if(changeCodeStr.compareTo("min") == 0){
        valMin = theObservedParameter.getMin();
      } else if(changeCodeStr.compareTo("max") == 0){
        valMax = theObservedParameter.getMax();
      }
    }
  }
}
