
import java.awt.*;
import java.awt.event.*;

public class Rectangle extends Shape {

  public Rectangle (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  public Object clone () {
    Rectangle r =  new Rectangle (x1,y1,x2,y2);
    r.setLocation (getLocation());
    r.setSelectState(getSelectState());
    return r;
  }

  public void paint (Graphics g) {
    super.paint(g);
    g.drawLine (0,0,x2-x1-1,0);
    g.drawLine (x2-x1-1,0,x2-x1-1,Math.abs(y2-y1)-1);
    g.drawLine (x2-x1-1,Math.abs(y2-y1)-1,0,Math.abs(y2-y1)-1);
    g.drawLine (0,Math.abs(y2-y1)-1,0,0);
  }
  
  public int getShapeCode(){
      return ShapeCoding.RECT;
  }

  public String getName(){
      return "Rectangle";
  }
	
}
