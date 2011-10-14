
import java.awt.*;

public class ShapeContainer extends Container {

  public ShapeContainer () {
   super ();
   setBackground (Color.white); 
  }

  public void paint(Graphics g) {
    g.setColor(getBackground());
    g.fillRect(1, 1, getWidth()-2, getHeight()-2);
    g.setColor (Color.black);
    super.paint(g);
  }

  public Dimension getPreferredSize () {
    return new Dimension (440,400);
  }
 
// MIDP version

  public Object[] getShapes(){

        Object[] objs= new Object[getComponentCount()];

        for(int i=0; i<getComponentCount();i++){
          objs[i]= getComponent(i);
        }

        return objs;
  }

}
