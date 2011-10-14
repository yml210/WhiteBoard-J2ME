package Whiteboard;
import javax.microedition.lcdui.*;

public class Line extends Shape {

  protected int startX, startY, endX, endY;
  /** constructs a Line object */

  public Line (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
    startX=x1-this.x1; startY=y1-this.y1;
    endX=x2-this.x1; endY=y2-this.y1;
  }
  
  public void setCoordinates(int x1,int y1, int x2, int y2){
      super.setCoordinates(x1, y1, x2, y2);
      startX=x1-this.x1;startY=y1-this.y1;
      endX=x2-this.x1; endY=y2-this.y1;
  }

  /** creates a copy of this Line object.  This method creates a new Line,
      sets its locations, sets its select state, and returns the new Line.
  */

  public Object clone () {
    Line l = new Line (startX,startY,endX,endY);
    return l;
  }

  /** this method paints the line.  */
/*  public boolean equals(Shape aShape){
      return  super.equals(aShape) &&
            endX==((Line) aShape).endX && endY==((Line) aShape).endY 
            && ((Line) aShape).startX==startX && startY==((Line) aShape).startY;
  }

 */
  public void paint () {
    dGraphics.setColor(dShapeColor);
    int xx1=ShapeCanvas.xScaling(x1+startX), xx2=ShapeCanvas.xScaling(x1+endX),
    yy1=ShapeCanvas.yScaling(y1+startY), yy2=ShapeCanvas.yScaling(y1+endY);
 //   System.out.println(xx1+" "+yy1+" "+xx2+" "+yy2);
    dGraphics.drawLine(x1+startX,y1+startY,x1+endX,y1+endY);
 //   System.out.println((x1+startX)+" "+(y1+startY)+" "+(x1+endX)+" "+(y1+endY));
    dGraphics.drawLine(xx1,yy1,xx2,yy2);
 }
 
 public int getShapeType(){
     return ShapeCoding.LINE;
 }
 
 public String getName(){
     return "LINE";
 }
 
 public int getShapeCode(){
     return ShapeCoding.LINE;
 }

}
