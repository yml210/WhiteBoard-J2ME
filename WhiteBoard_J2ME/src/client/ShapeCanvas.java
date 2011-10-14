/*
 * ShapeCanvas.java
 *
 * Created on August 14, 2001, 12:38 PM
 */
package Whiteboard;

import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author  Yimin Li
 * @version 
 */
public class ShapeCanvas extends Canvas {
    
    private static final int MAX_SCALE=6;
    public static int GroundColor=0xffffff;
    public  static int ShapeColor=0x000000;
    private static int dWidth,dHeight,dScale=MAX_SCALE;
    private static int SHIFT_FACTOR=2, FAST_SHIFT_FACTOR=3;
    private static Center dCenter;
    private static final int DELAY_TIME=1000;
    
    private String[] strShapes={"Line","Pointer","Rectangle","Oval"};
    
    public static int   DRAW_MODE=0,
                        SELECT_MODE=1,
                        ZOOM_MODE=2,
                        MAIN_MODE=3;
    
    private List dShapeList;
    protected Vector dShapes;
    protected Vector dSelectedShapes;
    
    private static Cursor dCursor;
    private Point dStart, dEnd;
    
    private BlinkThread dBlinkThread;
    private static int dMode=DRAW_MODE;
    
    private String dMessage="";
    
    
    public ShapeCanvas(){
        
        dShapes=new Vector();
        dWidth=getWidth();
        dHeight=getHeight();        
        dShapeList=new List("Shape to draw", List.IMPLICIT,strShapes,null);
        dSelectedShapes=new Vector();
        addCursor();
        dCenter=new Center(dWidth*dScale/2,dHeight*dScale/2);
        // System.out.println(""+dCursor.getPosition()+ dCenter);
    }
    
    public void addShape(){
        int shapeType=dShapeList.getSelectedIndex();
        if(dStart!=null&&dEnd!=null)
              addShape(shapeType,dStart,dEnd);
        dStart=null; dEnd=null;
    }
    
    private void addCursor(){
        addShape(ShapeCoding.CURSOR,new Point(0,0),new Point(dWidth*dScale,dHeight*dScale));
       
    }
    
    public void removeCursor(){
        dCursor=null;
        Point topLeft=dCursor.getTopLeft(),
            bottomRight=dCursor.getBottomRight();
        repaint(topLeft,bottomRight);
    }
    
    public void changeScaleBy(int i){
        dScale +=((dScale+i-1)<MAX_SCALE && (dScale+i)>0)? i:0;
        dCenter.update();
    }    
    
    public void addShape(int type,Point start,Point end){
        Shape aShape=Shape.createShape(type,start,end);
        Point topLeft=aShape.getTopLeft(),
            bottomRight=aShape.getBottomRight();
        if(type==ShapeCoding.CURSOR)
            dCursor=(Cursor) aShape;
        else 
            dShapes.addElement(aShape);
        repaint();
    }
    
    public void removeShapes(){
        dSelectedShapes.removeAllElements();
    }
    
    public void setMode(int mode){
        dMode=mode;
    }
    
    public static Point scaling(Point p){
        return new Point((p.x-dCenter.x)/dScale+dWidth/2, 
                                (p.y-dCenter.y)/dScale+dHeight/2);
    }
    
    public static int xScaling(int x){
        return (x-dCenter.x)/dScale+dWidth/2;
    }
    
    public static int yScaling(int y){
        return (y-dCenter.y)/dScale+dHeight/2;
    }
    
    public static int lengthScaling(int l){
        return l/dScale;
    }
    
    
    private void setStartPoint(){
    //    System.out.println("set Start Point:"+ dCursor.getPosition());
        dStart=dCursor.getPosition();
    }
    
    private void setEndPoint(){
    //   System.out.println("set End Point:"+ dCursor.getPosition());
        dEnd=dCursor.getPosition();
    }
    
    public void selectOrDeselectShape(){
        Enumeration shapes= dShapes.elements();
        while(shapes.hasMoreElements()){
            Shape aShape=(Shape) shapes.nextElement();
            if(dCursor.overlap(aShape)){
                aShape.toggleSelectState();
                dSelectedShapes.addElement(aShape);
                dShapes.removeElement(aShape);
                if(dBlinkThread==null){
                    dBlinkThread=new BlinkThread();
                    dBlinkThread.start();
                }
  //              System.out.println("Is alive "+ dBlinkThread.isAlive());
                return;
            }
        }
        
        shapes= dSelectedShapes.elements();
        while(shapes.hasMoreElements()){
            Shape aShape=(Shape) shapes.nextElement();
            if(dCursor.overlap(aShape)){
                aShape.toggleSelectState();
                dShapes.addElement(aShape);
                dSelectedShapes.removeElement(aShape);
                repaint();
                return;
            }
        }
    }
    
    public List getShapeList(){
        return dShapeList;
    }
    
    public Object[] getShapes(){
        int s1=dShapes.size(),
            s2=dSelectedShapes.size();
        Object[] objs=new Object[s1+s2];
        int index=0;
        for(int i=0; i< s1; i++){
            objs[index]=dShapes.elementAt(index);
            index++;
        }
         
        for(int i=0; i< s2; i++){
            objs[index]=dSelectedShapes.elementAt(index);
            index++;
        }
        return objs;
    }

    public void message(String m){
        dMessage=m;
        repaint();
        try{
            Thread.currentThread().sleep(DELAY_TIME);
        }catch(Exception e){
            e.printStackTrace();
        }
        dMessage="";
        repaint();
    }
        
    public void close(){};   
    public void repaint(Point topLeft,Point bottomRight){
        repaint(topLeft.x,topLeft.y,bottomRight.x,bottomRight.y);
    }
    /**
     * paint
     */
    public void paint(Graphics g) {
        g.setColor(GroundColor);
        g.fillRect(0,0,dWidth,dHeight);
        g.setColor(ShapeColor);
        g.drawString(dMessage,0,0,Graphics.TOP|Graphics.LEFT);
        drawShapes(dSelectedShapes,g);
        drawShapes(dShapes, g);
        
        if(dCursor!=null){
            dCursor.setGraphics(g);
            dCursor.paint();
        }
    }
    
    private void drawShapes(Vector vShapes, Graphics g){
         Enumeration shapes=vShapes.elements();
        while(shapes.hasMoreElements()){
            Shape aShape=(Shape) shapes.nextElement();
            aShape.setGraphics(g);
            aShape.paint();
        }
    }

    private void doPointSetting(int keyCode){
        switch(keyCode){
                case KEY_NUM1:
                    setStartPoint();
                    break;
                case KEY_NUM2:
                    setEndPoint();
                    break;
            }
    }
    
    private void doZoomingActions(int keyCode, int action){
        //record the cursor position relative to the center
        
        Point cp=dCursor.getPosition();
        int cx=(cp.x-dCenter.x)/dScale, cy=(cp.y-dCenter.y)/dScale;
        
        switch(keyCode){        
            case KEY_STAR:
                changeScaleBy(1);                
                break;                
            case KEY_POUND:            
                changeScaleBy(-1);                
                break;           
        }
                    
        int dx=0, dy=0;
        int SHIFT2=dScale*FAST_SHIFT_FACTOR;
        switch(action){        
            case DOWN:            
                dy=SHIFT2;                
                break;                
            case UP:            
                dy=-SHIFT2;                
                break;                
            case LEFT:           
                dx=-SHIFT2;                
                break;                
            case RIGHT:            
                dx=SHIFT2;                
                break;            
        }        
        dCenter.moveBy(dx, dy);
        
        dCursor.setPosition(dCenter.x+cx*dScale,dCenter.y+cy*dScale);
        
    }
    
    private void doCursorMoving(int action, int step){
        int dx=0, dy=0;       
        int x=dCursor.getPosition().x,        
        y=dCursor.getPosition().y; 
        
        switch(action){        
                                             
            case DOWN:            
                dy=((step+y)>(dCenter.y+dHeight*dScale/2))?0:step;                
                break;                
            case UP:            
                dy=((y-step)<(dCenter.y-dHeight*dScale/2))?0:-step;                
                break;         
            case RIGHT:            
                dx=((x+step)>(dCenter.x+dWidth*dScale/2))?0:step;                
                break;            
            case LEFT:            
                dx=((x-step)<(dCenter.x-dWidth*dScale/2))?0:-step;                
                break;
        }         
        
        dCursor.moveBy(dx,dy);
        repaint();
    }
    /**
     * Called when a key is pressed.
     */
    protected  void keyPressed(int keyCode) {  
        int action=getGameAction(keyCode);
        if(dMode==DRAW_MODE){
            doPointSetting(keyCode);           
        }
        
        if(dMode==ZOOM_MODE){
            doZoomingActions(keyCode,action);
        }                    
       
        if(dMode!=ZOOM_MODE){
            doCursorMoving(action, SHIFT_FACTOR*dScale);
        
        }
    }
    
    /**
     * Called when a key is released.
     */
    protected  void keyReleased(int keyCode) {
        
    }

    /**
     * Seems that this function does not work in the Tool kits version
     * Called when a key is repeated (held down).
     */
    protected  void keyRepeated(int keyCode) {
        System.out.println("Key Repeated");
        doCursorMoving(getGameAction(keyCode), FAST_SHIFT_FACTOR*dScale);
    }
    
    /**
     * Called when the pointer is dragged.
     */
    protected  void pointerDragged(int x, int y) {
    }

    /**
     * Called when the pointer is pressed.
     */
    protected  void pointerPressed(int x, int y) {
    }

    /**
     * Called when the pointer is released.
     */
    protected  void pointerReleased(int x, int y) {
    }


  private class BlinkThread extends Thread{
    
    private int dShapeColor=ShapeCanvas.ShapeColor;
    private static final int dSleepTime=500;
    
    public BlinkThread(){
    }
    
    public void run(){
        Shape s=null;
        int ss=0;
        while(true){
            if((ss=dSelectedShapes.size())==0) break;
            for(int i=0; i< ss;i++){
                s=(Shape) dSelectedShapes.elementAt(i);
                s.setColor(dShapeColor);
            }                                
            repaint();
            try{
              dBlinkThread.sleep(dSleepTime);
            }catch(InterruptedException iE){
              iE.printStackTrace();
            }
            toggleColor();
        }
        dBlinkThread=null;
    }
    
    private void toggleColor(){
        dShapeColor=~dShapeColor;
    }     
  }
  
  private class Center extends Point{
      public Center(int x, int y){
          super(x,y);
      }
      
      public void update(){
        int dx1=x-dWidth*dScale/2, dy1=y-dHeight*dScale/2,
        dx2=dWidth*MAX_SCALE-x-dWidth*dScale/2, 
        dy2=dHeight*MAX_SCALE-y-dHeight*dScale/2;
        if(dx1<0) x-=dx1;
        if(dx2<0) x+=dx2;
        if(dy1<0) y-=dy1;
        if(dy2<0) y+=dy2;
        repaint();
      }
      
      public void moveBy(int dx, int dy){
        x+=dx; y+=dy;
        update();      
      }
  }
      
      
}
