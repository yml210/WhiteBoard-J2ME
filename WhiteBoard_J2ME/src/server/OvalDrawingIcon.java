
import java.awt.*;

// get rid of all these ridiculous subclasses of Drawing Icon

public class OvalDrawingIcon extends DrawingIcon {

  public OvalDrawingIcon () {
    super();
  }

  public Shape createShape (int x1, int y1, int x2, int y2) {
    return new Oval (x1,y1,x2,y2);
  }

  public String getCommand () {
    return "Oval";
  }
}
