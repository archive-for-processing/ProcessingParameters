package floatingpointdev.toolkit.UI;

import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * 
 */

/**
 * @author Ryan Hieber (floatingpoint@gmail.com)
 */
public class RealNumberField  extends TextField {

  /** */
  private static final long serialVersionUID = -5204190264501934304L;
  /** */

  private NumberFormat formatter;
  //private int valueMin = 0;
  //private int valueMax = 1;
  //private boolean isBounded = false;
  
  /**
   * 
   * @param value The initial value of the text field.
   * @param columns The number of columns for the number.
   */
  public RealNumberField(int value, int columns){
    super(columns);

    formatter = NumberFormat.getNumberInstance(Locale.US);
    setValue(value);
  }
  
//  /**
//   * Constructor for creating a bounded WholeNumberField.
//   * @param value The initial value of the number field.
//   * @param min The minimum value that this number field can be.
//   * @param max The maximum value that this number field can be.
//   */
//  
//  public WholeNumberField(int value, int min, int max){
//    super(Math.max(Math.abs(max / 10 + 1),Math.abs(min / 10 + 1)));
//    
//    valueMin = min;
//    valueMax = max;
//    isBounded = true;
//    
//    integerFormatter = NumberFormat.getNumberInstance(Locale.US);
//    integerFormatter.setParseIntegerOnly(true);
//    setValue(value);
//  }
  
  /** @return The value of number this field contains. */
  public int getValue(){
    int retVal = 0;
    try{
      retVal = formatter.parse(getText()).intValue();
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
  private void setValue(int value) {
    
    /*if(isBounded){
      if(value < valueMin){
        value = valueMin;
      } else if(value > valueMax){
        value = valueMax;
      }
    }*/
    setText(formatter.format(value));
  }

  protected Document createDefaultModel(){
    return new WholeNumberDocument();
  }
  
  protected class WholeNumberDocument extends PlainDocument{
    
    public void insertString(int offs, String str, AttributeSet a)
    throws BadLocationException{
      char[] source = str.toCharArray();
      char[] result = new char[source.length];
      int j = 0;
      
      for(int i = 0; i < result.length; ++i){
        if(Character.isDigit(source[i]) || (source[i] == '-')|| (source[i] == '.')){
          result[j++] = source[i];
        }else {
          Toolkit.getDefaultToolkit().beep();
        }
      }
      super.insertString(offs, new String(result, 0, j), a);
    }
  }
//  /**
//   * Sets this number field as bounded by min and max.
//   * @param min The minimum value this number field can be.
//   * @param max The maximum value this number field can be.
//   */
//  public void setBounded(int min, int max){
//    valueMin = min;
//    valueMax = max;
//    isBounded = true;
//  }
//  /**
//   * Sets this number field as unbounded.
//   */ 
//  public void setUnbounded(){
//    isBounded = false;
//  }
}
