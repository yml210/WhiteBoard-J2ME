package Whiteboard;


class Point{
    public int x,y;
    
    public Point(int x,int y){
        this.x=x; this.y=y;
    }
    
    public String toString(){
        return "("+x+", "+y+")";
    }
    
    public int getShapeCode(){
        return ShapeCoding.POINT;
    }
}