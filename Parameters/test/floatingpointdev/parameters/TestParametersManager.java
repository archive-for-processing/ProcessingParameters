/**
 * 
 */
package floatingpointdev.parameters;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import floatingpointdev.parameters.Parameter;
import floatingpointdev.parameters.ParametersManager;

/**
 * @author floatingpointdev
 *
 */
public class TestParametersManager {

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link floatingpointdev.parameters.ParametersManager#createParameter(java.lang.String)}.
   */
  @Test
  public void testCreateParameter() {
    MockIoMidi ioMidi = new MockIoMidi();
    ParametersManager parametersManager = new ParametersManager(ioMidi);
    Parameter p1 = parametersManager.createParameter("name1");
    assertEquals("Name of created parameter should match.","name1", p1.getParameterName());
    
    Parameter p2 = parametersManager.createParameter("name2");
    assertEquals("Name of created parameter should match.","name2", p2.getParameterName());
    
    //test adding a duplicate.
    Parameter p1a = parametersManager.createParameter("name1");
    assertSame(p1, p1a);
  }

}
