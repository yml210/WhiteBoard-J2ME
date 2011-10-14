
import java.util.*;

public class SelectedVector implements Cloneable {

  Vector dVector;

  public SelectedVector () {
    dVector = new Vector();
  }

  public synchronized void add (Shape s) {
      dVector.add (s);
      s.setSelectState(true);
      s.repaint();
  }

  public int size () {
    return dVector.size();
  }

  public synchronized void remove (Shape s) {
    dVector.remove (s);
    s.setSelectState(false);
    s.repaint();
  }

  public Object clone () {
    SelectedVector sv = new SelectedVector();
    Enumeration e = elements();
    while (e.hasMoreElements()) {
      Shape s = (Shape)e.nextElement();
      sv.dVector.addElement(s.clone());
    }

    return sv;
  }

  public boolean contains (Shape s) {
    return dVector.contains(s);
  }
  
  public Enumeration elements () {
    return dVector.elements();
  }

  public synchronized void removeAllElements () {
    Enumeration e = dVector.elements();
    while (e.hasMoreElements()) {
      Shape s = (Shape)e.nextElement();
      s.setSelectState(false);
      s.repaint();
    }
    dVector.removeAllElements();
  }

}
