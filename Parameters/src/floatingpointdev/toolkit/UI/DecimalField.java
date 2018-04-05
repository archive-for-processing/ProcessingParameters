/**
 * 
 */
package floatingpointdev.toolkit.UI;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;


import java.awt.Toolkit;

/**
 * A text field that only accepts integers.
 * @author floatingpointdev
 *
 */
public class DecimalField extends TextField {

  private NumberFormat formatter;
  //private int valueMin = 0;
  //private int valueMax = 1;
  //private boolean isBounded = false;
  
  /**
   * 
   * @param value The initial value of the text field.
   * @param columns The number of columns for the number.
   */
  public DecimalField(double value, int columns){
    super(columns);
    formatter = NumberFormat.getNumberInstance(Locale.US);
    formatter.setParseIntegerOnly(false);
    formatter.setMaximumFractionDigits(6);
    setValue(value);
  }
  
  
  /** @return The value of number this field contains. */
  public double getValue(){
    double retVal = 0;
    try{
      retVal = formatter.parse(getText()).doubleValue();
    } catch (ParseException e){
      // This should never happen because insertString allows
      //only properly formatted data to get in the text field.
      //DEBUG_ERROR
    }
    return retVal;
  }
  
  /**
   * @param value The value of the number that this field contains.
   */
  public void setValue(double value) {
    
    setText(formatter.format(value));
  }

  protected Document createDefaultModel(){
    return new DecimalNumberDocument();
  }
  
  protected class DecimalNumberDocument extends PlainDocument{
    public void insertString(int offs,
        String str,
        AttributeSet a)
    throws BadLocationException{
      char[] source = str.toCharArray();
      char[] result = new char[source.length];
      int j = 0;
      
      for(int i = 0; i < result.length; ++i){
        if(Character.isDigit(source[i]) || (source[i] == '-') || (source[i] == '.')){
          result[j++] = source[i];
        }else {
          Toolkit.getDefaultToolkit().beep();
        }
      }
      super.insertString(offs, new String(result, 0, j), a);
    }
  }

}
