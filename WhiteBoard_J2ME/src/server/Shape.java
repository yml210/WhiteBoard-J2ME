
import java.awt.*;
import java.awt.event.*;

public class Shape extends Component implements Cloneable, ShapeCoding {

  private int cx1, cx2, cy1, cy2;
  protected int x1, x2, y1, y2;

  protected boolean dSelected = false;

  private static final int dSelectedBoxSize = 4;

  private Color dXORColor = Color.black;
  private boolean dUseXor = false;

  // takes point objects

  public Shape (int x1, int y1, int x2, int y2) {
    super ();

    cx1=x1; cx2=x2; cy1=y1; cy2=y2;
    
    if (GlobalDebug.isOn) 
      System.out.println ("Shape.Shape()");

    setCoordinates (x1,y1,x2,y2);

    enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    setXOROff ();
  }

  public boolean equals (Component o){
    if (! (o instanceof Shape)) 
      return false;
    else {
      Shape s = (Shape)o;
      return ((this.cx1 == s.cx1) &&
	      (this.cx2 == s.cx2) &&
	      (this.cy1 == s.cy1) &&
	      (this.cy2 == s.cy2) &&
	      (this.getShapeCode()==s.getShapeCode()));
    }
  }


  public boolean equals (Object o) {
    if (! (o instanceof Shape)) 
      return false;
    else {
      Shape s = (Shape)o;
      return ((this.cx1 == s.cx1) &&
	      (this.cx2 == s.cx2) &&
	      (this.cy1 == s.cy1) &&
	      (this.cy2 == s.cy2) &&
	      (this.getShapeCode()==s.getShapeCode()));
    }
  }

  public void setCoordinates (int x1, int y1, int x2, int y2) {
    setSize (Math.abs(x1-x2), Math.abs (y1-y2));

    if (((x1 > x2) && (y1 > y2)) ||
	((x1 > x2) && (y1 <= y2))) {
      // make sure this.x1 < this.x2
      this.x1 = x2; this.y1 = y2; this.x2 = x1; this.y2 = y1;
    }
    else {
      this.x1 = x1; this.x2 = x2; this.y1 = y1; this.y2 = y2;
    }
  }

  public Object clone () {
    System.err.println ("Shape.clone(): Internal Error: This method should not be getting called.  All shapes should override it.");
    return new Shape (x1,y1,x2,y2);
  }

  public void setSelectState (boolean selected) {
    dSelected = selected;
  }

  public void toggleSelectState () {
    dSelected = !dSelected;
  }

  public boolean getSelectState () {
    return dSelected;
  }

  public void setXOROn () {
    dXORColor = Color.white;
    dUseXor = true;
  }

  public void setXOROff () {
    dUseXor = false;
  }

  // drawKnobs();

  // abstract isInside (MyPoint p)

  public void paint (Graphics g) {
    
    g.setColor (Color.black);

    if (dUseXor) 
      g.setXORMode (dXORColor);

    super.paint (g);
    
    if (dSelected) {
      g.fillRect (0,0,dSelectedBoxSize,dSelectedBoxSize);
      g.fillRect (0,getHeight()-1-dSelectedBoxSize,dSelectedBoxSize,dSelectedBoxSize);
      g.fillRect (getWidth()-1-dSelectedBoxSize,0,dSelectedBoxSize,dSelectedBoxSize);
      g.fillRect (getWidth()-1-dSelectedBoxSize,
		  getHeight()-1-dSelectedBoxSize,
		  dSelectedBoxSize,
		  dSelectedBoxSize);
    }
  }

  public Dimension getPreferredSize () {
    return new Dimension (Math.abs(x1-x2), Math.abs (y1-y2));
  }

  public Dimension getMinimumSize () {
    return new Dimension (Math.abs(x1-x2), Math.abs (y1-y2));
  }

  public Dimension getMaximumSize () {
    return new Dimension (Math.abs(x1-x2), Math.abs (y1-y2));
  }

  public  AWTEvent coalesceEvents(AWTEvent existingEvent, AWTEvent
				  newEvent) {
    return super.coalesceEvents (existingEvent,newEvent);
  }

  public  void firePropertyChange(String propertyName, Object oldValue,
				  Object newValue) {
    super.firePropertyChange (propertyName, oldValue, newValue);
  }

  public  String paramString() {
    return super.paramString();
  }

  public void processComponentEvent(ComponentEvent e) {
    super.processComponentEvent (e);
  }
  
  public  void processEvent(AWTEvent e) {
    super.processEvent (e);
  }

  public  void processFocusEvent(FocusEvent e) {
    super.processFocusEvent (e);
  }

  public  void processInputMethodEvent(InputMethodEvent e) {
    super.processInputMethodEvent(e);
  }

  public  void processKeyEvent(KeyEvent e) {
    super.processKeyEvent (e);
  }

  public void processMouseEvent(MouseEvent e) {
    super.processMouseEvent (e);
  }

  public  void processMouseMotionEvent(MouseEvent e) {
    super.processMouseMotionEvent(e);
  }
// Code for MIDP version
  public static Shape createShape(int[] code){
      MyPoint start=new MyPoint(code[1],code[2]);
      MyPoint end=new MyPoint(code[3],code[4]);
      int shapeType=code[0];
      return Shape.createShape(shapeType,start,end);
  }


  public static Shape createShape(int shapeType, MyPoint start, MyPoint end){
    Shape shape=null;
    int xx1=start.x, xx2=end.x, yy1=start.y, yy2=end.y;
    switch(shapeType){
	case ShapeCoding.LINE:
	  shape=new Line(xx1,yy1,xx2,yy2);
	  break;  
        case ShapeCoding.POINT:
 //       shape=new DirectedLine(xx1,yy1,xx2,yy2);
          break;
        case ShapeCoding.RECT:
          shape=new Rectangle(xx1,yy1,xx2,yy2);
          break;
        case ShapeCoding.OVAL:
          shape=new Oval(xx1,yy1,xx2,yy2);
          break;
//        case ShapeCoding.CURSOR:
//          shape=new Cursor(xx1,yy1,xx2,yy2);
//          break;
    }
    shape.setLocation(Math.min(xx1,xx2),Math.min(yy1,yy2));
    shape.setXOROff();
    return shape;
  }

  public int getShapeCode(){
      return ShapeCoding.LINE;
  }

   public static int getCodeLength(){
       return ShapeCoding.CODE_LENGTH;
   }

   public int[] getCode(){
       int[] code={getShapeCode(),cx1, cy1, cx2, cy2};
       return code;
   }

   public String toString(){
	StringBuffer sb=new StringBuffer();
	sb.append(getName());
	sb.append("("+ cx1+", "+cy1+")");
	sb.append("("+ cx2+", "+cy2+")");
	return sb.toString();
   }
 
   public String getName(){
	return "LINE";
   }
}

