
import java.io.*;

/** The ObjectReader allows you to read arbitrary Java objects from a
    file. */

public class ObjectReader {

  private ObjectInputStream ois;

  private ObjectReader (ObjectInputStream ois) {
    this.ois = ois;
  }

  /** use this method to crate an ObjectReader that reads objects from the
      file specified by filename */
  public static ObjectReader openFileForReading (String filename) {
    try {
      ObjectInputStream ois = new ObjectInputStream (new FileInputStream
						     (filename));
      return new ObjectReader (ois);
    }
    catch (Exception e) {
      e.printStackTrace ();
      return null;
    }
  }

  /** reads a single object from the file, and returns it.  If there are
      no more objects in the file (i.e, we have reached the end of file),
      the null is returned. */
  public Object readObject () {
    try {
      return ois.readObject();
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /** closes a file from which objects are being read. */
  
  public void close () {
    try {
      ois.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
    
}
