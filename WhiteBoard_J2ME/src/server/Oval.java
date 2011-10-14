
import java.awt.*;
import java.awt.event.*;

public class Oval extends Shape {

  public Oval (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  public Object clone () {
    Oval o = new Oval (x1,y1,x2,y2);
    o.setLocation (getLocation());
    o.setSelectState (getSelectState());
    return o;
  }

  public void paint (Graphics g) {
    super.paint(g);
    g.drawOval (0,0,x2-x1-1,Math.abs(y2-y1)-1);
  }
  
  public int getShapeCode(){
      return ShapeCoding.OVAL;
  }

  public String getName(){
      return "Oval";
  }

}
