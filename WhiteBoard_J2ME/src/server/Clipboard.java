
/** The Clipboard can store an object using the set method and retrieve it
    if the caller calls the get method.  There is only one Clipboard for
    the entire applciation, and a caller may use the getInstance method
    to get the clipboard. */

public class Clipboard {

  private Object o = null;

  private static Clipboard dClipboard = new Clipboard(); 

  private Clipboard () {

  }

  public static Clipboard getInstance () {
    return dClipboard;
  }

  public void set (Object o) {
	this.o = o;
  }

  public Object get () {
    return o;
  }

}
