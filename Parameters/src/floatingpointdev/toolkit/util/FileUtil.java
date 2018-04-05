/**
 * 
 */
package floatingpointdev.toolkit.util;

import java.io.File;

/**
 * @author floatingpointdev
 *
 */
public class FileUtil {
  /** Get the extension of a file.
   * @param f The file to get the extension for.
   * @return the extension of the supplied file.
   */  
  public static String getExtension(File f) {
      String ext = "";
      String s = f.getName();
      int i = s.lastIndexOf('.');

      if (i > 0 &&  i < s.length() - 1) {
          ext = s.substring(i+1).toLowerCase();
      }
      return ext;
  }

}
