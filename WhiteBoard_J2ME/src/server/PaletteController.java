
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PaletteController implements ActionListener {

  private Palette dPalette;

  private Vector dDrawingIconSelectionListeners;

  private static final String dInitallySelectedDrawingCommand = "Line";

  /** the icon that is initally selected when the application starts. */
  private DrawingIcon dIntiallySelectedIcon;

  public PaletteController (Palette aPalette) {
    dPalette = aPalette;
    dDrawingIconSelectionListeners = new Vector();

    /** add this as a listener to all DrawingIcons in the Palette. */
    Enumeration e = dPalette.getAllDrawingIcons ();

    while (e.hasMoreElements()) {
      DrawingIcon di = (DrawingIcon)e.nextElement();
      
      if (di.getCommand().equals(dInitallySelectedDrawingCommand)) {
	dIntiallySelectedIcon = di;
      }

      di.addActionListener(this);
    }

    if (dIntiallySelectedIcon == null) {
      Enumeration f = dPalette.getAllDrawingIcons();
      if (f.hasMoreElements()) {
	DrawingIcon di = (DrawingIcon)f.nextElement();
	System.err.println ("PaletteController.PaletteController(): Warning: Line is not available.  Setting initally selected drawing icon to " + di.getCommand() + " icon.");
	dIntiallySelectedIcon = di;
      }
      else {
	System.err.println ("PaletteController.PaletteController(): Warning: Line is not available.  No icons are available.  Initially selected icon is NULL!");
      }
    }
  }

  public DrawingIcon getInitiallySelectedDrawingIcon () {
    return dIntiallySelectedIcon;
  }
    

  public void addDrawingIconSelectionListener
    (DrawingIconSelectionEventListener aDISE) {
    dDrawingIconSelectionListeners.add(aDISE);
  }

  public void removeDrawingIconSelectionListener
      (DrawingIconSelectionEventListener aDISE) {
    dDrawingIconSelectionListeners.remove(aDISE);
  }

  public void actionPerformed (ActionEvent ae) {
    /** Figure out which button went down,
	pop all others up, 
	generate a DrawingIconSelectionEvent, and
	send to all DrawingIconSelectionEventListeners
    */

    DrawingIcon source = (DrawingIcon)ae.getSource();

    Enumeration iconEnum = dPalette.getAllDrawingIcons ();
    while (iconEnum.hasMoreElements()) {
      DrawingIcon di = (DrawingIcon) iconEnum.nextElement();
      if (di != source) {
	di.setState (false);
      }
      else {
	di.setState (true);
      }
    }

    DrawingIconSelectionEvent dise = 
      new DrawingIconSelectionEvent (source);
    Enumeration disEnum = dDrawingIconSelectionListeners.elements();
    while (disEnum.hasMoreElements()) {
      ((DrawingIconSelectionEventListener)disEnum.nextElement()).iconSelected(dise);
    }

  }
}
