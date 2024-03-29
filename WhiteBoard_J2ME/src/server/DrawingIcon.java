
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/* 
   This class implements an abstract DrawingIcon for you. 
   You should subclass the DrawingIcon class to create 
   drawing icons of your own, so that you can have drawing
   icons for circle, rectangles, etc.  We provide you with
   an example implementation of a drawing icon in the
   RectangleDrawingIcon class.  Your subclasses of DrawingIcon
   should have a constructor and implementations of the
   createShape and getCommand methods.

   Your implementation of createShape should return an instance
   of a subclass of a Shape object.  

   
   Your implementation of getCommand should return a String
   that returns the name of the shape that a drawing icon
   can be used to create.

   DrawingIcons can have "action" listeners.  The addActionListener and
   removeActionListener methods can be used to add and remove listeners to
   or from this drawing icon.   When a drawing icon is clicked on and
   selected, the drawing icon will generate a DrawingIconSelectionEvent
   and broadcast that event to all of its listeners.  The actionPerformed
   method of all of the listeners of the drawing icon will be invoked.

   In the whiteboard project, the PaletteController is an action listener
   of all of the drawing icons, and the PaletteController's
   actionPerformed method is invoked whenever a drawing icon is clicked on.
*/


public abstract class DrawingIcon 
  extends Container {

  private boolean dOn = false;
  private int dWidth = 50, dHeight = 50;
  private int dMargin = 10;
  
  private Vector dMyListeners = new Vector ();

  private Component dShape;

  public DrawingIcon () {
    super();
    if (GlobalDebug.isOn) 
      System.out.println ("DrawingIcon.DrawingIcon(): entered");

    setSize (dWidth,dHeight);
    setBackground(Color.lightGray);

    dShape = createShape (dMargin,dMargin,dWidth-dMargin,dHeight-dMargin);
    dShape.setLocation (dMargin,dMargin);
    add (dShape);

    DrawingIconMouseListener diml = new DrawingIconMouseListener (this);
    addMouseListener (diml);
    dShape.addMouseListener (diml);

    if (GlobalDebug.isOn) 
      System.out.println ("DrawingIcon.DrawingIcon(): exit");
  }

  public abstract Shape createShape (int x1, int y1, int x2, int y2);
  
  public abstract String getCommand ();
  
  public boolean toggleState () {
    if (GlobalDebug.isOn) 
      System.out.println ("DrawingIcon.toggleState()");
    dOn = !dOn;
    repaint();
    return dOn;
  }
  
  public void setState (boolean state) {
    if (dOn != state) {
      dOn = state;
      repaint();
    }
  }

  public void paint (Graphics g) {

    Color c1, c2;

    if (GlobalDebug.isOn) 
      System.out.println ("DrawingIcon.paint()");

    c1 = Color.white;
    c2 = Color.black;

    if (dOn) {
      Color temp = c1;
      c1 = c2;
      c2 = temp;
      setBackground(Color.gray);
    }
    else {
      setBackground(Color.lightGray);
    }

    super.paint(g);

    g.setColor (c1);
    g.drawLine (0,0,0,dHeight-1);
    g.drawLine (0,0,dWidth-1,0);
    g.setColor (c2);
    g.drawLine (0,dHeight-1,dWidth-1,dHeight-1);
    g.drawLine (dWidth-1, 0, dWidth-1, dHeight-1);

  }

  public void addActionListener(ActionListener l) {
    dMyListeners.add (l);
  }

  public void removeActionListener(ActionListener l) {
    dMyListeners.remove (l);
  }

  public Enumeration getListeners () {
    return dMyListeners.elements();
  }

  public Dimension getPreferredSize () {
    return new Dimension (dWidth, dHeight);
  }

}

class DrawingIconMouseListener extends MouseAdapter {
  
  private DrawingIcon dMyIcon;
  
  public DrawingIconMouseListener (DrawingIcon di) {
    dMyIcon = di;
  }
  
  public void mouseClicked(MouseEvent me) {
    //    dMyIcon.toggleState();
    
    Enumeration e = dMyIcon.getListeners ();
    while (e.hasMoreElements()) {
      ((ActionListener)e.nextElement()).actionPerformed 
	(new ActionEvent(dMyIcon,ActionEvent.ACTION_PERFORMED,dMyIcon.getCommand()));
    }
  }

}
