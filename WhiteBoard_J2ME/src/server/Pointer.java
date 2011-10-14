
import java.awt.*;
import java.awt.event.*;

public class Pointer extends Shape {

  public static final int LEFT_DIRECTION = 0;
  public static final int RIGHT_DIRECTION = 1;

  private int dDirection;

  public Pointer (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
    if (x1 > x2) {
      dDirection = LEFT_DIRECTION;
    } else {
      dDirection = RIGHT_DIRECTION;
    }
  }

  public Object clone () {
    Pointer p = new Pointer (x1,y1,x2,y2);
    p.setLocation (getLocation());
    p.setSelectState(getSelectState());
    return p;
  }

  public void paint (Graphics g) {
    super.paint (g);

    g.setColor (Color.black);

    if (y1 <= y2) {
      g.drawLine (0,0,x2-x1-1,y2-y1-1);
      if (dDirection == LEFT_DIRECTION) {
	g.drawLine (0,0,0,5);
	g.drawLine (0,0,5,0);
      }
      else if (dDirection == RIGHT_DIRECTION) {
	g.drawLine (x2-x1-1,y2-y1-1,x2-x1-1,y2-y1-1-5);
	g.drawLine (x2-x1-1,y2-y1-1,x2-x1-1-5,y2-y1-1);
      }
    }
    else {
      g.drawLine (0,y1-y2-1,x2-x1-1,0);
      if (dDirection == LEFT_DIRECTION) {
	g.drawLine (0,y1-y2-1,0,y1-y2-1-5);
	g.drawLine (0,y1-y2-1,5,y1-y2-1);
      }
      else {
	g.drawLine (x2-x1-1-5,0,x2-x1-1,0);
	g.drawLine (x2-x1-1,0,x2-x1-1,5);
      }
    }
  }

  public static void main (String argv[]) {
    Frame f = new Frame ("Test Frame");
    f.setLayout(new BorderLayout());
    f.add (new Pointer(5,5,95,95), BorderLayout.CENTER);
    //    f.add (new Pointer(5,5,95,95));
    f.setVisible (true);
    f.setLocation (100,100);
    f.pack();
    f.show();
  }
  
}
