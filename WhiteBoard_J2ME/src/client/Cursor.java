/*
 * Cursor.java
 *
 * Created on August 16, 2001, 1:45 AM
 */

package Whiteboard;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 *
 * @author Yimin Li 
 * @version
 */
public class Cursor extends Shape  {
    
 private static int DEFAULT_SIZE=3;
 private int x,y;
 private int size;
  /** constructs a Line object */

  public Cursor (int x1, int y1,int x2,int y2) {
        super(x1,y1,x2,y2);
        x=(x1+x2)/2; y=(y1+y2)/2;
        size=DEFAULT_SIZE;
  }

  public void paint () {
    dGraphics.setColor(ShapeCanvas.ShapeColor);
    int xx=ShapeCanvas.xScaling(x), yy=ShapeCanvas.yScaling(y);
//    System.out.println(xx+" "+yy);
    dGraphics.drawLine(xx-size,yy,xx+size,yy);
    dGraphics.drawLine(xx,yy-size,xx,yy+size);
 }
 
 public Point getPosition(){
     return new Point(x,y);
 }
 
 public void setPosition(Point p){
     x=p.x; y=p.y;
 }
 
 public void setPosition(int x, int y){
     this.x=x; this.y=y;
 }
 
 public int getShapeType(){
     return ShapeCoding.CURSOR;
 }
 
 public void moveBy(int dx, int dy){
     super.moveBy(dx,dy);
     x=(x1+x2)/2; y=(y1+y2)/2;
 }
}
