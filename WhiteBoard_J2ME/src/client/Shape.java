package Whiteboard;
import javax.microedition.lcdui.*;

public class Shape
    implements ShapeCoding 
{

  private static int DEFAULT_COLOR=0x000000;
  protected int x1, x2, y1, y2;
  private int cx1, cx2, cy1, cy2;
  protected boolean dSelected = false;
  
  protected Graphics dGraphics;
  protected int dShapeColor=DEFAULT_COLOR;
//  private Thread dBlinkThread;
// private static int dSleepTime=2000;

  // takes point objects

  public Shape (int x1, int y1, int x2, int y2) {
    cx1=x1; cx2=x2; cy1=y1; cy2=y2;
    setCoordinates (x1,y1,x2,y2);
  }
  
  public static Shape createShape(int[] code){
      Point start=new Point(code[1],code[2]);
      Point end=new Point(code[3],code[4]);
      int shapeType=code[0];
      return Shape.createShape(shapeType,start,end);
  }
  
  public static Shape createShape(int shapeType,Point start,Point end){
        Shape shape=null;
//        start=scaling(start); end=scaling(end);
        int xx1=start.x, xx2=end.x, yy1=start.y, yy2=end.y;
        switch(shapeType){
            case ShapeCoding.LINE:
                shape=new Line(xx1,yy1,xx2,yy2);
                break;
            case ShapeCoding.POINT:
 //               shape=new DirectedLine(xx1,yy1,xx2,yy2);
                break;
            case ShapeCoding.RECT:
                shape=new Rectangle(xx1,yy1,xx2,yy2);
                break;
            case ShapeCoding.OVAL:
                shape=new Oval(xx1,yy1,xx2,yy2);
                break;
            case ShapeCoding.CURSOR:
                shape=new Cursor(xx1,yy1,xx2,yy2);
                break;
        }
        return shape;  
  }
      
  public void setColor(int color){
      dShapeColor=color;
  }
/*setCoordinates is modified to set (x1,y1) to be topleft point 
  and (x2,y2) to be the bottomright point of the boundary box.
 */  
  
  public void setCoordinates (int x1, int y1, int x2, int y2) {
    this.x1=Math.min(x1,x2);
    this.x2=Math.max(x1,x2);
    this.y1=Math.min(y1,y2);
    this.y2=Math.max(y1,y2);
  }
  
  public void setGraphics(Graphics g){
      dGraphics=g;
  }
  
  public Point getTopLeft(){
      return new Point(x1,y1);
  }
  
  public Point getBottomRight(){
      return new Point(x2,y2);
  }
  
  public void moveBy(int dx, int dy){
      x1+=dx; y1+=dy; x2+=dx; y2+=dy;
  }

  public Object clone (){
    return null;
  }
  
  public int getShapeCode(){
      return ShapeCoding.LINE;
  }
  
  public boolean equals(Shape aShape){
     return getShapeCode()==aShape.getShapeCode()
            &&cx1==aShape.cx1 && cy1==aShape.cy1
            && cx2==aShape.cx2 && cy2==aShape.cy2;       
  } 

  public void setSelectState (boolean selected) {
        dSelected=selected;  
    /*
      if(dSelected && !selected){
          try{
            dBlinkThread.join();
          }catch(InterruptedException iE){
              iE.printStackTrace();
          }
          dGraphics.setColor(ShapeCanvas.ShapeColor);
          paint();
          dBlinkThread=null;
      }else if(!dSelected&&selected){
          dBlinkThread=new Thread(this);
          dBlinkThread.start();
      }
       
      dSelected=selected;
       */
  }

  public void toggleSelectState () {
    setSelectState(!dSelected);
    dShapeColor=DEFAULT_COLOR;
  }

  public boolean getSelectState () {
    return dSelected;
  }

   public void paint () {
   }

   public boolean overlap(Shape aShape){
       int x=(x1+x2)/2, y=(y1+y2)/2;
       return x<=aShape.x2 && x>=aShape.x1
          && y<=aShape.y2 && y>=aShape.y1;
   }
   
   public static int getCodeLength(){
       return ShapeCoding.CODE_LENGTH;
   }
   
   public int[] getCode(){
       int[] code={getShapeCode(),cx1, cy1, cx2, cy2};
       return code;
   }
   
   public String toString(){
       StringBuffer sb= new StringBuffer();
       sb.append(getName());
       sb.append("( "+cx1+", "+cy1+")");
       sb.append("( "+cx2+", "+cy2+")");
       return sb.toString();
   }
   
   public String getName(){return "LINE";}
  /*
  public void run() {
      while(true){
          int color=ShapeCanvas.ShapeColor;
          dGraphics.setColor(color);
          paint();
          try{
            dBlinkThread.sleep(dSleepTime);
          }catch(InterruptedException iE){
              iE.printStackTrace();
          }
          if(color==ShapeCanvas.ShapeColor)
              color=ShapeCanvas.GroundColor;
          else
              color=ShapeCanvas.ShapeColor;
      }
  }
 */ 
}


