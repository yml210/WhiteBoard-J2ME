
class MyPoint{
    public int x,y;
    
    public MyPoint(int x,int y){
        this.x=x; this.y=y;
    }
    
    public String toString(){
        return "("+x+", "+y+")";
    }
    
    public int getShapeCode(){
        return ShapeCoding.POINT;
    }
}
