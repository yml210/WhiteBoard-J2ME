package Whiteboard;

import java.io.*;

public class NetworkWhiteboardCommand{

  private int dCommand;
  private Object [] dObjects;

  public static final int ADD = 0;
  public static final int REMOVE =1;
  public static final int HELLO = 2;

  public NetworkWhiteboardCommand (int command, 
				   Object [] objects) {

    dCommand = command;
    dObjects = objects;
  }
  
  public NetworkWhiteboardCommand(int[] code){
      int[] sc=new int[Shape.getCodeLength()];
      dCommand=code[0];
      dObjects=new Shape[code[1]];
      int index=2;
      for( int i=0; i<code[1]; i++){
          for(int j=0; j<Shape.getCodeLength(); j++) 
              sc[j]=code[index++];
          dObjects[i]=Shape.createShape(sc);
      }
  }
          
      

  public NetworkWhiteboardCommand (int command) {
    this (command,new Object [0]);
  }

  
  public static NetworkWhiteboardCommand readFrom(DataInputStream dis){
      try{
      int command=dis.readInt(), n=dis.readInt();
      Object[] objs=new Object[n];
      int[] s=new int[Shape.getCodeLength()];
      for(int i=0; i< n; i++){
          for(int j=0; j<s.length; j++)
              s[j]=dis.readInt();
          objs[i]=Shape.createShape(s);
      }
      
      return new NetworkWhiteboardCommand(command,objs);
      }catch(IOException e){
          dis=null;
          e.printStackTrace();
          return null;
      }
  }

  public void writeTo(DataOutputStream dos){
	try{
	  int[] code=getCode();
	  for(int i=0; i< code.length; i++)
		dos.writeInt(code[i]);
	}catch(Exception e){
		e.printStackTrace();
	}
  }
	     		 
  public int getCommand () {
    return dCommand;
  }

  public Object [] getObjects () {
    return dObjects;
  }
  
  public int[] getCode(){
      int scl=Shape.getCodeLength();
      int no=dObjects.length;
      int[] asc=new int[scl];
      int[] code=new int[2+no*scl];
      code[0]=dCommand;
      code[1]=no;
      int index=2;
      for( int i=0; i< no; i++){
          asc=((Shape) dObjects[i]).getCode();
          for(int j=0; j<scl; j++)
              code[index++]=asc[j];
      }
      return code;
  }

  public String toString () {
    StringBuffer sb = new StringBuffer();
    sb.append ("NetworkWhiteboardCommand: " + dCommand+"\n" );
    sb.append( " number of objects "+dObjects.length + "\n");
    for (int i = 0; i < dObjects.length; i++) {
      Shape s=(Shape) dObjects[i];
      sb.append(s.toString()+"\n");
    }
    return sb.toString();
  }
}
