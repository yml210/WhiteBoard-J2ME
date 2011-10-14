
import java.awt.*;

public class PointerIcon extends DrawingIcon {

  public PointerIcon () {
    super();
  }

  public Shape createShape (int x1, int y1, int x2, int y2) {
    return new Pointer (x1,y1,x2,y2);
  }

  public String getCommand () {
    return "Pointer";
  }

}
