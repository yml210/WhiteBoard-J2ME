
public class NetworkWhiteboardFrame extends WhiteboardFrame {

  protected String dHostname;
  protected int dPort;

  public NetworkWhiteboardFrame (String hostname, int port, String title)  {
      super (title);
      dHostname = hostname;
      dPort = port;
  }

  public void createShapeContainer () {
    dShapeContainer = new NetworkShapeContainer (dHostname, dPort);
  }

}
