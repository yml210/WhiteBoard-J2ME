
import java.awt.*;
import java.applet.*;

public class Whiteboard extends Applet {

  private WhiteboardFrame dWhiteboardFrame;
  private PaletteController dPaletteController;
  private ShapeController dShapeController;

  public static final String WHITEBOARD_TITLE = "MyWhiteboard";
  public static final int DEFAULT_PORT = 8091;

  public Whiteboard () {
  }

  public Whiteboard (WhiteboardFrame aWhiteboardFrame) {
    dPaletteController = 
      new PaletteController (aWhiteboardFrame.getPalette());
    dShapeController = 
      new ShapeController (aWhiteboardFrame,
			   aWhiteboardFrame.getShapeContainer(),
			   dPaletteController);
  }

  private Palette dPalette;
  private ShapeContainer dShapeContainer;

  public void init () { 
    //    Whiteboard wb = new Whiteboard();
    dPalette = new Palette ();
    dShapeContainer = new ShapeContainer ();
    
    setLayout (new BorderLayout());
    add (dPalette, BorderLayout.WEST);
    add (dShapeContainer, BorderLayout.CENTER);

    setBackground (Color.lightGray);

    dPaletteController = 
      new PaletteController (dPalette);
    dShapeController = 
      new ShapeController (null, dShapeContainer, dPaletteController);
  }

  public void start () {
    //    dWhiteboardFrame.setVisible (true);
  }

  public void stop () {
    //    dWhiteboardFrame.setVisible (false);
  }

  public void paint (Graphics g) {
    g.drawString ("Applet!",10,20);
  }

  public static void main (String argv[]) {

    WhiteboardFrame aWhiteboardFrame = null;

    if (argv.length == 0) {
      System.out.println ("Running standalone.");
      aWhiteboardFrame = new WhiteboardFrame (WHITEBOARD_TITLE);
    }
    else {
      if (argv.length == 1) {
	System.out.println ("Running as server on port " + Integer.parseInt
			  (argv[0]) + ".");
	aWhiteboardFrame = new ServerWhiteboardFrame ( Integer.parseInt(argv[0]),
						       WHITEBOARD_TITLE);
      }
      else if (argv.length == 2) {
	System.out.println ("Running as client: connecting to " + argv[0]
			    + " on port " + Integer.parseInt(argv[1]) + ".");
	aWhiteboardFrame = new NetworkWhiteboardFrame (argv[0],
						       Integer.parseInt (argv[1]),
						       WHITEBOARD_TITLE);
      }
    }

    aWhiteboardFrame.createWhiteboardFrame();

    Whiteboard wb = new Whiteboard(aWhiteboardFrame);

    /*
      if (aWhiteboardFrame instanceof ServerWhiteboardFrame) {
      ((ServerShapeContainer)aWhiteboardFrame.getShapeContainer()).setShapeController (wb.dShapeController);
    }
    if (aWhiteboardFrame instanceof NetworkWhiteboardFrame) {
	((NetworkShapeContainer)aWhiteboardFrame.getShapeContainer()).setShapeController (wb.dShapeController);
    }
    */

    aWhiteboardFrame.setSize (640,480);
    aWhiteboardFrame.setLocation (100,100);
    aWhiteboardFrame.setVisible (true);
    aWhiteboardFrame.pack();
    aWhiteboardFrame.show();
  }

}
