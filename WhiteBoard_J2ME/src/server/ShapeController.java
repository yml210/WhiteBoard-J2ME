
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ShapeController 
  implements DrawingIconSelectionEventListener,
	     MouseListener,
	     MouseMotionListener,
	     ActionListener
					
{

  private Container dShapeContainer;

  private Clipboard dClipboard = Clipboard.getInstance();

  private static final short DRAW_MODE = 0;
  private static final short SELECT_MODE = 1;

  private short dMode = DRAW_MODE;
  private boolean dMoving = false;

  private DrawingIcon dCurrentSelectedIcon;

  private SelectedVector dSelectedShapes = new SelectedVector();

  private WhiteboardFrame dWhiteboardFrame;

  // change to Points
  private int downX = -1, downY = -1, upX = -1, upY = -1;

  public ShapeController (WhiteboardFrame aWhiteboardFrame,
			  Container aShapeContainer,
			  PaletteController aPaletteController) {

    System.out.println ("ShapeController: 1");
    dShapeContainer = aShapeContainer;
    dShapeContainer.addMouseListener (this);
    dShapeContainer.addMouseMotionListener (this);
    
    System.out.println ("ShapeController: 2");
    dWhiteboardFrame = aWhiteboardFrame;
    System.out.println ("ShapeController: 2.1");
    if (dWhiteboardFrame != null) {
      MenuItem [] mis = aWhiteboardFrame.getAllMenuItems();
      System.out.println ("ShapeController: 2.2");
      for (int i = 0; i < mis.length; i++) {
	System.out.println ("ShapeController: 2.3");
	mis[i].addActionListener (this);
      }
    }
    
    System.out.println ("ShapeController: 3");
    aPaletteController.addDrawingIconSelectionListener (this);

    System.out.println ("ShapeController: 4");
    dCurrentSelectedIcon =
      aPaletteController.getInitiallySelectedDrawingIcon();
    dCurrentSelectedIcon.setState(true);
    iconSelected (new DrawingIconSelectionEvent (dCurrentSelectedIcon));
  }

  /* Drawing Icon Selection Event */

  public void iconSelected (DrawingIconSelectionEvent aDISE) {
    dCurrentSelectedIcon = aDISE.getIcon();
    if (dCurrentSelectedIcon instanceof SelectionIcon) {
      dMode = SELECT_MODE;
    }
    else {
      dMode = DRAW_MODE;

      // unselect all shapes when entering drawing mode, and repaint
      dSelectedShapes.removeAllElements();
    }
    System.out.println ("ShapeController: " +
			dCurrentSelectedIcon.getCommand() + " selected!");
  }

  /* Mouse Events */

  public void mouseClicked (MouseEvent me) {
    if (dMode == SELECT_MODE) {
      // if we're in select mode

      if (me.getSource() instanceof Shape) {
	// and someone clicked on a shape, then
	// toggle the selected state of the clicked on object
	// and updated the selected list appropriately
	dSelectedShapes.add((Shape)me.getSource());
      }
      else {
	// someone clicked outside of any shape;
	// clear the entire selected list
	dSelectedShapes.removeAllElements();
      }
    }
  }

  public void mouseEntered (MouseEvent me) {
    // null
  }

  public void mouseExited (MouseEvent me) {
    // null
  }
  
  public void mousePressed (MouseEvent me) {
    //    System.out.println (me);
    //    System.out.println ("(" + me.getX() + ", " + me.getY() + ")");
    if (me.getSource() == dShapeContainer) {
      downX = me.getX();
      downY = me.getY();
    }
    else {
      Point p = ((Component)me.getSource()).getLocation();
      downX = (int)p.getX() + me.getX();
      downY = (int)p.getY() + me.getY();
    }
    if (dMode == DRAW_MODE) {
    }
    else if (dMode == SELECT_MODE) {
      if (me.getSource() instanceof Shape) {
	dMoving = (dSelectedShapes.contains ((Shape)me.getSource()));
      }
    }
  }

  public void mouseReleased (MouseEvent me) {

    //    System.out.println (me);
    //    System.out.println ("(" + me.getX() + ", " + me.getY() + ")");
    if (me.getSource() == dShapeContainer) {
      upX = me.getX();
      upY = me.getY();
    }
    else {
      Point p = ((Component)me.getSource()).getLocation();
      upX = (int)p.getX() + me.getX();
      upY = (int)p.getY() + me.getY();
    }

    if (dMode == DRAW_MODE) {

      if (lastShape != null) {
	lastShape.repaint();
	dShapeContainer.remove(lastShape);
      }

      Shape s = dCurrentSelectedIcon.createShape (downX, downY, upX, upY);
      s.setLocation (Math.min(downX,upX), Math.min(downY,upY));
      s.addMouseListener (this);
      s.addMouseMotionListener (this);
      s.setXOROff();
      dShapeContainer.add (s);
      s.repaint();
      //      dShapeContainer.repaint();

      lastShape = null;
      downX = downY = upX = upY = -1;
    }
    else if (dMode == SELECT_MODE) {
      dMoving = false;
    }
  }

  /* Mouse Motion Events */

  private Shape lastShape = null;
  private int lastX = -1, lastY = -1;

  public void mouseDragged (MouseEvent me) {

    if (me.getSource() == dShapeContainer) {
      lastX = me.getX();
      lastY = me.getY();
    }
    else {
      Point p = ((Component)me.getSource()).getLocation();
      lastX = (int)p.getX() + me.getX();
      lastY = (int)p.getY() + me.getY();
    }

    if (dMode == DRAW_MODE) {
      if (lastShape != null) {
	//	dShapeContainer.remove(lastShape);
	lastShape.repaint();
	lastShape.setCoordinates (downX, downY, lastX, lastY);
	lastShape.setLocation (Math.min(downX,lastX),
			       Math.min(downY,lastY));
	lastShape.repaint();
      }

      if (lastShape == null) {
	lastShape = dCurrentSelectedIcon.createShape(downX, downY, lastX, lastY);
	lastShape.setLocation (Math.min(downX,lastX),
			       Math.min(downY,lastY));
	lastShape.setXOROn();
	dShapeContainer.add (lastShape);
	lastShape.repaint();
	//lastShape.paint (dShapeContainer.getGraphics());
	//dShapeContainer.repaint();
	
      }
      else {

      }


      /*
	lastShape = dCurrentSelectedIcon.createShape(downX, downY, lastX, lastY);
	lastShape.setLocation (Math.min(downX,lastX),
	Math.min(downY,lastY));
	dShapeContainer.add (lastShape);
	//      dShapeContainer.repaint();
	//      lastShape.repaint();
      */
    }
    else if (dMode == SELECT_MODE) {
      if (dMoving) {
	Enumeration e = dSelectedShapes.elements();
	while (e.hasMoreElements()) {
	  Shape s = (Shape)e.nextElement();
	  Point oldLocation = s.getLocation();
	  s.setLocation ((int)oldLocation.getX() + (lastX-downX),
			 (int)oldLocation.getY() + (lastY-downY));
	}

	//tricky!
	downX = lastX;
	downY = lastY;

	dShapeContainer.repaint();
      }
    }
  }

  public void mouseMoved (MouseEvent me) {
    // null
  }

  /* Action Listener (Menu Events) */

  public void actionPerformed (ActionEvent ae) {
    // menu action performed

    // I thought about replacing the following with a dispatch table,
    // but I wasn't quite sure what would be the best way to give the
    // functions referred to in the dispatch table access to local 
    // data members in this class since they would need these to 
    // accomplish their tasks.

    String arg = (String)ae.getActionCommand();

    if (arg.equals(WhiteboardConstants.NEW_MENU_ITEM)) {
      newCommand();
    }
    else if (arg.equals(WhiteboardConstants.IMPORT_MENU_ITEM)) {
      importCommand();
    }
    else if (arg.equals(WhiteboardConstants.SAVE_AS_MENU_ITEM)) {
      saveAs();
    }
    else if (arg.equals(WhiteboardConstants.EXIT_MENU_ITEM)) {
      exit();
    }
    else if (arg.equals(WhiteboardConstants.CUT_MENU_ITEM)) {
      cut();
    }
    else if (arg.equals(WhiteboardConstants.COPY_MENU_ITEM)) {
      copy();
    }
    else if (arg.equals(WhiteboardConstants.PASTE_MENU_ITEM)) {
      paste();
    }
    else if (arg.equals(WhiteboardConstants.CLEAR_MENU_ITEM)) {
      clear();
    }
    else if (arg.equals(WhiteboardConstants.ABOUT_MENU_ITEM)) {
      about();
    }
    
  }

  public void newCommand () {
    dShapeContainer.removeAll();
    dShapeContainer.repaint();
    dSelectedShapes = new SelectedVector();
  }

  public void importCommand () {
    System.out.println ("Import clicked");
    
    fd = new FileDialog (dWhiteboardFrame, "Import Drawing",
				    FileDialog.LOAD);
    fd.show();
    String filename = fd.getDirectory() + fd.getFile();
    System.out.println ("LOAD FILE " + filename);
    ObjectReader objr = ObjectReader.openFileForReading (filename);

    if (objr != null) {
      Object o = objr.readObject ();
      Component [] c = (Component [])o;
      objr.close();

      for (int i = 0; i < c.length; i++) {
	dShapeContainer.add (c[i]);
      }
      dShapeContainer.repaint();
    }
    else {
      System.out.println ("Could not read!");
    }

  }

  private static FileDialog fd;

  public void saveAs () {
    System.out.println ("Save As clicked");
    fd = new FileDialog (dWhiteboardFrame, "Save Drawing",
				    FileDialog.SAVE);
    fd.show();
    String filename = fd.getDirectory() + fd.getFile();
    System.out.println ("SAVE FILE " + filename);
    ObjectWriter ow = ObjectWriter.openFileForWriting (filename);
    if (ow != null) {
      Component [] components = dShapeContainer.getComponents();
      ow.writeObject (components);
      ow.close();
    }
    else {
      System.out.println ("Could not save!");
    }
  }

  public void exit () {
    System.exit(0);
  }
  
					    
  public void cut () {
    // put all selected shapes in clipboard
    dClipboard.set (dSelectedShapes);
    
    // remove all shapes from the shape container and repaint
    Enumeration e = dSelectedShapes.elements();
    while (e.hasMoreElements()) {
      Component c = (Component)e.nextElement();
      dShapeContainer.remove (c);
      c.removeMouseListener(this);
      c.removeMouseMotionListener(this);
    }
    dShapeContainer.repaint();

    // clear the selected shapes vector
    dSelectedShapes = new SelectedVector ();
  }
  
					    
  public void copy () {
    // put all selected shapes in clipboard
    dClipboard.set (dSelectedShapes.clone());

    // unselect all currently selected shapes
    dSelectedShapes.removeAllElements();

    dShapeContainer.repaint();
  }
  
					    
  public void paste () {
    // unselect all currently selected shapes
    dSelectedShapes.removeAllElements();

    // get shapes from clipboard & add them to shape container
    SelectedVector newShapes = (SelectedVector)dClipboard.get();

    // if there were shapes in the clipboard
    if (newShapes != null) {

      // make a new copy of shapes in clipboard
      newShapes = (SelectedVector)newShapes.clone();

      // and add all of the new shapes to the container, and the
      // current selected vector
      dSelectedShapes = newShapes;
      Enumeration e2 = dSelectedShapes.elements();
      while (e2.hasMoreElements()) {
	Component c = (Component) e2.nextElement();
	dShapeContainer.add (c);
	c.addMouseListener (this);
	c.addMouseMotionListener (this);
      }

      // repaint the screen
      dShapeContainer.repaint();
    }
    
  }
					    
  public void clear () {
    System.out.println ("" + dSelectedShapes.size() + " shapes to clear.");

    // unselect and remove all currently selected shapes
    Enumeration e = dSelectedShapes.elements();
    int i = 0;
    while (e.hasMoreElements()) {
      System.out.println ("Removing shape " + (i++));
      Shape s = (Shape)e.nextElement();
      dShapeContainer.remove (s);
      s.removeMouseListener(this);
      s.removeMouseMotionListener(this);
    }
    dSelectedShapes.removeAllElements();

    dShapeContainer.repaint();
  }
  
					    
  public void about () {
    System.out.println ("About clicked");
  }
  
}
