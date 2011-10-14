
import java.awt.*;
import java.io.*;
import java.net.*;

public class NetworkShapeContainer 
  extends ShapeContainer 
  implements Runnable
{

  private Socket dSocket;
  private ObjectOutputStream dOOS;
  private ObjectInputStream dOIS;

  private Thread dListenerThread;

  //  private ShapeController dShapeController;

  //  public void setShapeController (ShapeController myShapeController) {
  //    dShapeController = myShapeController;
  //  }

  /** causes the NetworkShapeContainer to run in client mode and
      connect to the specified hostname on the specified port. */
  public NetworkShapeContainer (String hostname, int port) {
    super();
    try {
      dSocket = new Socket (hostname,port);
      dOOS = new ObjectOutputStream (dSocket.getOutputStream());
      dOIS = new ObjectInputStream (dSocket.getInputStream());
      dListenerThread = new Thread (this);
      dListenerThread.start();
      dOOS.writeObject (new NetworkWhiteboardCommand
			(NetworkWhiteboardCommand.HELLO));
			 
    }
    catch (Exception e) {
      dSocket = null;
      dOOS = null;
      e.printStackTrace();
    }
  }

  public void run () {

    if (dOIS == null) 
      return;

    System.out.println ("NetworkShapeContainer: ListenerThread running.");
     
    while (true) {
      try {
	//	Thread.currentThread().sleep(100);
	NetworkWhiteboardCommand nwc =
	  (NetworkWhiteboardCommand)dOIS.readObject();
	System.out.println ("NetworkShapeContainer: Got a shape from server!");
	doCommand (nwc);
      }
      catch (Exception e) {
	e.printStackTrace();
      }
    }
  }

  public void doCommand (NetworkWhiteboardCommand nwc) {
    Object [] components = nwc.getObjects();

    if (nwc.getCommand()==NetworkWhiteboardCommand.ADD) {
      for (int i = 0; i < components.length; i++) {
	if (components[i] instanceof Component) {
	  super.add((Component)components[i]);
	  //	  ((Component)components[i]).addMouseListener (dShapeController);
	  //	  ((Component)components[i]).addMouseMotionListener (dShapeController);
	}
	else {
	  System.out.println ("Object was not a component!");
	}
      }
      repaint();
    }

    if (nwc.getCommand()==NetworkWhiteboardCommand.REMOVE) {
      for (int i = 0; i < components.length; i++) {
	if (components[i] instanceof Component) {
	  super.remove((Component)components[i]);
	  //	  ((Component)components[i]).removeMouseListener (dShapeController);
	  //	  ((Component)components[i]).removeMouseMotionListener (dShapeController);
	}
	else 
	  System.out.println ("Object was not a component!");
      }
      repaint();
    }

  }
      

  public Component add (Component c) {
    if (dOOS != null) {
      Component [] componentList = new Component [1];
      componentList[0] = c;
      
      try {
	System.out.println ("NetworkShapeContainer: wrote Object!");
	dOOS.writeObject (new NetworkWhiteboardCommand
			  (NetworkWhiteboardCommand.ADD,
			   componentList));
      }
      catch (Exception e) {
	e.printStackTrace();
      }
    }
    super.add(c);
    return c;
  }

  public void remove (Component c) {
    if (dOOS != null) {
      Component [] componentList = new Component [1];
      componentList[0] = c;

      try {
	dOOS.writeObject (new NetworkWhiteboardCommand
			  (NetworkWhiteboardCommand.REMOVE,
			   componentList));
      }
      catch (Exception e) {
	e.printStackTrace();
      }
    }
    super.remove(c);
  }

}


