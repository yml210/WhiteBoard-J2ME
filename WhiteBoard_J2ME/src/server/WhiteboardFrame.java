
import java.awt.*;

public class WhiteboardFrame extends Frame {

  protected MenuBar dMenuBar;
  protected Palette dPalette;
  protected Container dShapeContainer;
  protected MenuItem [] dMenuItems;

  protected void createMenuBar () {
    dMenuBar = new MenuBar ();
    setMenuBar (dMenuBar);

    int i = 0;
    dMenuItems = new MenuItem [8];

    Menu fileMenu = new Menu ("File");
    fileMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.NEW_MENU_ITEM));
    fileMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.IMPORT_MENU_ITEM));
    fileMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.SAVE_AS_MENU_ITEM));
    fileMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.EXIT_MENU_ITEM));
    dMenuBar.add (fileMenu);
    
    Menu editMenu = new Menu ("Edit");
    editMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.CUT_MENU_ITEM));
    editMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.COPY_MENU_ITEM));
    editMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.PASTE_MENU_ITEM));
    editMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.CLEAR_MENU_ITEM));
    dMenuBar.add (editMenu);

/*
    Menu helpMenu = new Menu ("Help");
    helpMenu.add (dMenuItems[i++] = new MenuItem (WhiteboardConstants.ABOUT_MENU_ITEM));
    dMenuBar.add (helpMenu);
*/

    WhiteboardWindowAdapter adapter = new WhiteboardWindowAdapter (this);
    addWindowListener (adapter);

  }

  public MenuItem [] getAllMenuItems () {
    return dMenuItems;
  }

  protected void createPalette () {
    dPalette = new Palette ();
  }


  public Palette getPalette () {
    return dPalette;
  }

  protected void createShapeContainer () {
    dShapeContainer = new ShapeContainer ();
    //    dShapeContainer = new DoubleBufferShapeContainer ();
  }

  public Container getShapeContainer () {
    return dShapeContainer;
  }
  
  public WhiteboardFrame (String title) {
    super (title);
  }
    
  public void createWhiteboardFrame () {
    createMenuBar();
    createPalette();
    createShapeContainer();

    setLayout (new BorderLayout());
    add (dPalette, BorderLayout.WEST);
    add (dShapeContainer, BorderLayout.CENTER);
    
    setResizable (true);
    setBackground (Color.lightGray);
  }
}
