package Whiteboard;
import javax.microedition.lcdui.*;

public class Rectangle extends Shape {

  public Rectangle (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  public Object clone () {      
    Rectangle rect=new Rectangle(x1,y1,x2,y2);
    rect.setSelectState(getSelectState());
    return rect;
  }

  public void paint () {
      int h=Math.abs(y1-y2)-1, w=Math.abs(x1-x2)-1;
      dGraphics.setColor(dShapeColor);
      //int x0=x1>x2?x2:x1, y0=y1>y2?y2:y1;
     // int x0=0, y0=0;
      int xx1=ShapeCanvas.xScaling(x1), yy1=ShapeCanvas.yScaling(y1),
      ww=ShapeCanvas.lengthScaling(w), hh=ShapeCanvas.lengthScaling(h);
      dGraphics.drawRect(xx1,yy1,ww,hh);
  }
  
  public String getName(){
      return "Rectangle";
  }
  
  public int getShapeCode(){
      return ShapeCoding.RECT;
  }
  
}
