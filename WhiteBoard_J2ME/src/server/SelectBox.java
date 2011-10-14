
import java.awt.*;
import java.awt.event.*;

public class SelectBox extends Shape {

  private int dBoxSize = 3;

  public SelectBox (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  public void setBoxSize (int boxSize) {
    dBoxSize = boxSize;
  }
  
  public int getBoxSize () {
    return dBoxSize;
  }

  public void paint (Graphics g) {
    super.paint(g);

    g.setColor (Color.black);
    g.fillRect (0,0,dBoxSize,dBoxSize);
    g.fillRect (0,getHeight()-1-dBoxSize,dBoxSize,dBoxSize);
    g.fillRect (getWidth()-1-dBoxSize,0,dBoxSize,dBoxSize);
    g.fillRect (getWidth()-1-dBoxSize, getHeight()-1-dBoxSize, dBoxSize, dBoxSize);
  }

}
