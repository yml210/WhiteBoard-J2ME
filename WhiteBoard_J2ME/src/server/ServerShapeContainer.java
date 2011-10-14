
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerShapeContainer 
  extends ShapeContainer 
  implements Runnable
{

  private ServerSocket dServerSocket;

  private Vector dClientSockets; // Vector of client Sockets
  private Vector dClientOutputStreams; 
  
  private Thread dListenerThread;

  //  private ShapeController dShapeController;

  //  public void setShapeController (ShapeController myShapeController) {
  //    dShapeController = myShapeController;
  //  }

  /** causes the ServerShapeContainer to run in server mode and
      listen on the specified port. */
  public ServerShapeContainer (int port) {
    super();

    dClientSockets = new Vector();
    dClientOutputStreams = new Vector();
    try {
      dServerSocket = new ServerSocket (port);
      dListenerThread = new Thread (this);
      dListenerThread.start();
    }
    catch (Exception e) {
      dServerSocket = null;
      e.printStackTrace();
    }
    System.out.println ("ServerShapeConatiner created.");
  }

  public void run () {
    while (true) {
      try {
	System.out.println ("ServerShapeConatiner waiting for clients...");
	Socket newClientSocket = dServerSocket.accept();
	System.out.println ("ServerShapeConatiner got a client...");
	dClientSockets.addElement (newClientSocket);
/*	dClientOutputStreams.addElement (new ObjectOutputStream(newClientSocket.getOutputStream()));
*/
	dClientOutputStreams.addElement (new DataOutputStream(newClientSocket.getOutputStream()));
	Thread csh = new Thread (new ClientSocketHandler (this,newClientSocket));
	csh.start();
	System.out.println ("ServerShapeConatiner started ClientSocketHandler...");
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
	  removeShape((Component) components[i]);
	  //	  ((Component)components[i]).removeMouseListener (dShapeController);
	  //	  ((Component)components[i]).removeMouseMotionListener (dShapeController);
	}
	else {
	  System.out.println ("Object was not a component!");
	}
      }
      repaint();
    }
  }

  public void removeShape(Component c){
	Shape s=(Shape) c;
	Component[] cs=getComponents();
	for(int i=1; i< cs.length; i++){
	  if(s.equals(cs[i])){
		 super.remove(cs[i]);
	         return;
	  }
        }
  }
	  
  public void helloNewClient(Socket newClient){
    try{
	NetworkWhiteboardCommand nwc=new NetworkWhiteboardCommand(
		NetworkWhiteboardCommand.HELLO, getShapes());
	DataOutputStream dos= new DataOutputStream(newClient.getOutputStream());
	nwc.writeTo(dos);
    }
    catch(Exception e){
	e.printStackTrace();
    }
	
  }

  public void cleanup(Socket mySocket){
	Enumeration sockets=dClientSockets.elements();
	int index=0;
	while(sockets.hasMoreElements()){
	  if(mySocket.equals(sockets.nextElement())) break;
	  index++;
	}
	dClientSockets.removeElementAt(index);
	dClientOutputStreams.removeElementAt(index);
  }
	

  public void broadcastCommand (NetworkWhiteboardCommand nwc, 
				Socket exceptionSocket) {
    System.out.println ("ServerShapeConatiner.broadcastCommand()");
    for (int i = 0; i < dClientOutputStreams.size(); i++) {
      if (dClientSockets.elementAt(i) != exceptionSocket) {
	try {
	  DataOutputStream dos =
	    (DataOutputStream)dClientOutputStreams.elementAt(i);
	  System.out.println ("broadCastCommand: " + nwc + " to client " + i);
	  nwc.writeTo (dos);
	}
	catch (Exception e) {
	  // problem-- maybe remove that client socket out of both vectors?
	  e.printStackTrace();
	}
      }
    }
  }
      
}


class ClientSocketHandler implements Runnable {
  
  ServerShapeContainer dSSC;
  Socket dMySocket;

  public ClientSocketHandler (ServerShapeContainer ssc, Socket mySock) {
    dSSC = ssc;
    dMySocket = mySock;
  }

  public void run () {
    System.out.println ("ClientSocketHandler running...");
    try {
      DataInputStream dis = 
	new DataInputStream (dMySocket.getInputStream());
      
      while (true) {
	//	if (ois.available() > 0) {
	  NetworkWhiteboardCommand nwc = 
	    (NetworkWhiteboardCommand)NetworkWhiteboardCommand.readFrom(dis);
	  if(nwc==null) break;
 	  
	  System.out.println ("ClientSocketHandler got a command!\n"+nwc);
	  if(nwc.getCommand()==NetworkWhiteboardCommand.HELLO){
		helloNewClient(nwc);
	  }
	  else{
	  	dSSC.doCommand (nwc);
	  	dSSC.broadcastCommand (nwc,dMySocket);
          }
	  //	  dSSC.broadcastCommand (nwc,null);
	  //	}
      }
      dSSC.cleanup(dMySocket);
      dMySocket.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  private void helloNewClient(NetworkWhiteboardCommand nwc){
        
	Object[] objs=nwc.getObjects();

	//Send add all shape on server to new client
        dSSC.helloNewClient(dMySocket);

        NetworkWhiteboardCommand nc=
	  new NetworkWhiteboardCommand(NetworkWhiteboardCommand.ADD, objs); 

        //add the shapes from the new client to Server whiteboard
	dSSC.doCommand(nc);

        //add the shapes from the new client to other client
	dSSC.broadcastCommand(nc, dMySocket);
  }

}
