
import java.awt.*;

public class SelectionIcon extends DrawingIcon {

  public SelectionIcon () {
    super();
  }

  public Shape createShape (int x1, int y1, int x2, int y2) {
    SelectBox sb = new SelectBox (x1,y1,x2,y2);
    sb.setBoxSize (3);
    return sb;
  }

  public String getCommand () {
    return "Select";
  }

}
