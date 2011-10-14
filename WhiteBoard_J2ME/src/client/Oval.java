package Whiteboard;

import javax.microedition.lcdui.*;
public class Oval extends Shape {

  public Oval (int x1, int y1, int x2, int y2) {
    super (x1,y1,x2,y2);
  }

  public Object clone () {
      Oval o=new Oval(x1,y1,x2,y2);
      o.setSelectState(getSelectState());
      return o;
  }

  public void paint () {
      int h=Math.abs(y1-y2)-1, w=Math.abs(x1-x2)-1;
      //int x0=x1>x2?x2:x1, y0=y1>y2?y2:y1;
      dGraphics.setColor(dShapeColor);
      int xx1=ShapeCanvas.xScaling(x1), yy1=ShapeCanvas.yScaling(y1),
      ww=ShapeCanvas.lengthScaling(w), hh=ShapeCanvas.lengthScaling(h);
      dGraphics.drawArc(xx1,yy1,ww,hh,0,360);  
  }
  
  public int getShapeCode(){
      return ShapeCoding.OVAL;
  }
  
  public String getName(){
      return "Oval";
  }
}
