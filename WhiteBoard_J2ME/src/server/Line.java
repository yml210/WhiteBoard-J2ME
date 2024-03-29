
import java.awt.*;
import java.awt.event.*;

public class Line extends Shape {

  /** constructs a Line object */

  public Line (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  /** creates a copy of this Line object.  This method creates a new Line,
      sets its locations, sets its select state, and returns the new Line.
  */

  public Object clone () {
    Line l = new Line (x1,y1,x2,y2);
    l.setLocation (getLocation());
    l.setSelectState (getSelectState());
    return l;
  }

  /** this method paints the line.  */

  public void paint (Graphics g) {
    super.paint(g);

    if (GlobalDebug.isOn) 
      System.out.println ("Line.paint()");
    
    g.setColor (Color.black);

    if (y1 <= y2) {
      g.drawLine (0,0,x2-x1-1,y2-y1-1);
    }
    else {
      g.drawLine (0,y1-y2-1,x2-x1-1,0);
    }
  }

 public int getShapeType(){
     return ShapeCoding.LINE;
 }

 public int getShapeCode(){
     return ShapeCoding.LINE;
 }

 public String getName(){
     return "Line";
 }

}
