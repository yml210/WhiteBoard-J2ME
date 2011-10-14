/*
 * ClientShapeCanvas.java
 *
 * Created on August 17, 2001, 3:00 PM
 */

package Whiteboard;

import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

/**
 *
 * @author Yimin Li
 * @version
 */
public class ClientShapeCanvas extends ShapeCanvas{
    private static final int MAX_FIELD_LENGTH=200;
    private static String DEFAULT_URL="socket://cardinal0.stanford.edu:3232";
    
    private StreamConnection dSC;
    private DataOutputStream dDOS;
    private  InputHandler dInputHandler;
 
    private TextField dServerInfo;
    
    public ClientShapeCanvas(){
    }
    
    public void connectToServer(){
        if(dSC!=null) return;
        try{
            
            if(!(dServerInfo.getString()==null)){
             dSC=(StreamConnection)  
               Connector.open("socket://"+dServerInfo.getString(),Connector.READ_WRITE);
            }
            else{ 
              dSC=(StreamConnection)
                Connector.open(DEFAULT_URL,Connector.READ_WRITE);}
            System.out.println(dServerInfo.getString());
            dDOS=dSC.openDataOutputStream();
            helloServer();
            dInputHandler=new InputHandler(dSC.openDataInputStream());
            dInputHandler.start();
        }catch(Exception e){
            e.printStackTrace();
            message("Failed to make connection!");
        }
    }
    
    private void helloServer(){
           ( new NetworkWhiteboardCommand(NetworkWhiteboardCommand.HELLO, 
                getShapes())).writeTo(dDOS);
    }
        
    public void close(){
        try{
            if(dInputHandler !=null) dInputHandler.join();
            if(dSC!=null) dSC.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            dInputHandler=null;
            dSC=null;
            dDOS=null;
        }
    }
        
    public void messageServer(NetworkWhiteboardCommand nc){
        if(dDOS!=null)
            System.out.println("Message to Server: \n"+nc);
            nc.writeTo(dDOS);
    }    
    
    public Form getServerInfoForm(){
        Form f=new Form("Connection");
        String label="Server URL:";
        if(dServerInfo==null)
            dServerInfo=new TextField(label,"",MAX_FIELD_LENGTH,TextField.URL);
        f.append(dServerInfo);
        return f;
    }
    
    private class InputHandler extends Thread  implements ShapeCoding {
        
        private DataInputStream dDIS;
     
        public InputHandler(DataInputStream dis){
            dDIS=dis;
        }
        
        public void run(){
            while(true){ 
                try{
                    NetworkWhiteboardCommand nc=
                        NetworkWhiteboardCommand.readFrom(dDIS);
                    doCommand(nc);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        
        private void doCommand(NetworkWhiteboardCommand nc){
            System.out.println("Got a Command: \n"+nc);
            int command= nc.getCommand();
            Object[] obs=nc.getObjects();
            for(int i=0; i< obs.length; i++){
                switch(command){
                    case NetworkWhiteboardCommand.HELLO:
                        message("Connected");
                    case NetworkWhiteboardCommand.ADD:                                            
                        dShapes.addElement(obs[i]);
                        break;
                    case NetworkWhiteboardCommand.REMOVE:
                        removeShape(obs[i]);
                        break;
                }
            }
            repaint();
        }
            
    }
    
    //Override the addShape() method in super class, so that the server is informed
    public void addShape(int type,Point start,Point end){
        
        if(type!=ShapeCoding.CURSOR ){
            Object[] objs=new Object[1];
            objs[0]=Shape.createShape(type,start,end);
            messageServer(new NetworkWhiteboardCommand(ShapeCoding.ADD,objs));
        }
        super.addShape(type,start,end);
    }
    
    public void removeShapes(){
        int n=dSelectedShapes.size();
        Object[] objs=new Object[n];
        for(int i=0;i<n;i++)
            objs[i]=dSelectedShapes.elementAt(i);
        messageServer(new NetworkWhiteboardCommand(ShapeCoding.REMOVE,objs));
        super.removeShapes();
    }   
    
    public void removeShape(Object obj){
        Shape s=null, s1=null;
            s=(Shape) obj;
            for(int j=0; j< dShapes.size(); j++){
                s1=(Shape) dShapes.elementAt(j);
                if(s.equals(s1)){ 
                    dShapes.removeElementAt(j);
                    break;
                }
            }
            for(int j=0; j< dSelectedShapes.size(); j++){
                s1=(Shape) dSelectedShapes.elementAt(j);
                if(s.equals(s1)){ 
                    dSelectedShapes.removeElementAt(j);
                    break;
                }
            }
    }
}
