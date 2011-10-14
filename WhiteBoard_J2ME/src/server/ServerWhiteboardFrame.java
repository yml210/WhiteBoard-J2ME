
public class ServerWhiteboardFrame extends WhiteboardFrame {

  protected int dPort;

  public ServerWhiteboardFrame (int port, String title)  {
      super (title);
      dPort = port;
  }

  public void createShapeContainer () {
    dShapeContainer = new ServerShapeContainer (dPort);
  }

}
