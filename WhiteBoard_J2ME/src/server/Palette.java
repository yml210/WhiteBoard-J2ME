
import java.awt.*;
import java.util.*;

/** A Palette is the visual entity (a Panel)
    that contains all of the drawing
    icons.  A Palette has a Vector of drawing icons. */

public class Palette extends Panel {

  private Vector dDrawingIcons;

  // should be generalized to take an array of strings...

  private Color dBorderColor = Color.black;

  /** this method should create all of the drawing icons that will be
      shown in the palette, and add them to the palette's vector of
      drawing icons. */

  private void createDrawingIcons () {
    // creates the icons, inserts them into dDrawingIcons,
    // and adds them to the palette
    dDrawingIcons = new Vector ();
    dDrawingIcons.add (new LineDrawingIcon());
    dDrawingIcons.add (new RectangleDrawingIcon());
    dDrawingIcons.add (new OvalDrawingIcon());
    dDrawingIcons.add (new SelectionIcon());
    dDrawingIcons.add (new PointerIcon());
  }

  public void paint (Graphics g) {
    super.paint(g);
    g.setColor(dBorderColor);
    g.drawRect(0,0,getWidth()-1,getHeight()-1);
  }
    
  /** returns an enumeration of all of the drawing icons. */

  public Enumeration getAllDrawingIcons () {
    return dDrawingIcons.elements();
  }

  /** the constructor should call createDrawingIcons(), and
      add each of the drawing icons to this panel. */

  public Palette () {
    super (new GridLayout (3,2));

    createDrawingIcons ();

    Enumeration iconEnum = dDrawingIcons.elements();
    while (iconEnum.hasMoreElements()) {
      DrawingIcon di = (DrawingIcon) iconEnum.nextElement();
      add (di);
    }
  }

  /** returns the preferred size of the palette. */
  public Dimension getPreferredSize () {
    return new Dimension (100,200);
  }

  /** returns the minimum size of the palette. */
  public Dimension getMinimumSize () {
    return new Dimension (100,200);
  }

  /** returns the maximum size of the palette. */
  public Dimension getMaximumSize () {
    return new Dimension (100,200);
  }
  
}
