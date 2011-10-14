/*
 * MIDWhiteboard.java
 *
 * Created on August 14, 2001, 11:37 AM
 */
package Whiteboard;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
/**
 *
 * @author  Yimin Li
 * @version
 */
public class MIDWhiteboard extends MIDlet implements CommandListener{
    
    Display dDisplay;
    private ClientShapeCanvas dShapeCanvas;
    private Form dAbout;
    
    private Command cExit=new Command("Exit",Command.EXIT,20);
    private Command cOk=new Command("OK",Command.OK,10);
    private Command cAddShape=new Command("Add Shape", Command.SCREEN, 21);
    private Command cRemoveShape=new Command("Remove Shape", Command.SCREEN, 22);
    private Command cSelect=new Command("Select", Command.SCREEN, 23);
    private Command cZoom=new Command("Zoom ...", Command.SCREEN, 24);
    private Command cConnect=new Command("Connect to ...",Command.SCREEN,25);
    private Command cAbout=new Command("About",Command.SCREEN,26);
    private Command cZoomDone=new Command("Done",Command.OK,30);
    private Command cDraw=new Command("Draw",Command.OK,40);
    private Command cShapeSelected=new Command("Toggle",Command.OK,40);
    private Command cServerInfoDone=new Command("OK", Command.OK,45);

    private Command[] MainMenu={cExit,cAddShape,cRemoveShape,cSelect,
    cZoom,cConnect,cAbout};
    
    private Command dTmpCommand=cExit;
    
//    private Command[] ZoomMenu={cZoomIn,cZoomOut};
//    private Command[] SetPointMenu={cPointsSelected,cPointsCancel};
//    private Command[] SelectMenu={cShapeSelected,cShapeSelectedCancel};
                
    public MIDWhiteboard(){
        
        dDisplay=Display.getDisplay(this);
        dShapeCanvas=new ClientShapeCanvas();
    }
    
    public void startApp() {    
        addCommands(MainMenu);

        dShapeCanvas.setCommandListener(this);
        dDisplay.setCurrent(dShapeCanvas);          
    }
    
    private void addCommands(Command[] cs){
        for(int i=0; i< cs.length;i++)
            dShapeCanvas.addCommand(cs[i]);
    }
    
    private void removeCommands(Command[] cs){
        for(int i=0;i<cs.length;i++)
            dShapeCanvas.removeCommand(cs[i]);
    }
    
    private void newTmpCommand(Command newCommand){
        if(dTmpCommand!=null) dShapeCanvas.removeCommand(dTmpCommand);
        dShapeCanvas.addCommand(newCommand);
        dTmpCommand=newCommand;
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
        
        dDisplay.setCurrent(null);
        dShapeCanvas.close();
        dShapeCanvas=null;
        notifyDestroyed();
    }
    
    public void commandAction(Command c, Displayable d){
        if(c==cExit){
            System.out.println(" Quit Whiteboard");
            destroyApp(true);

        }else if(d==dShapeCanvas.getShapeList()){
            System.out.println("Draw...");
            dShapeCanvas.removeCommand(cExit);
            newTmpCommand(cDraw);
            dDisplay.setCurrent(dShapeCanvas);
            
        }else if(c==cDraw){
            dShapeCanvas.addShape();
        
        }else if(c==cAddShape&& d==dShapeCanvas){
            System.out.println("Add Shape");
            dShapeCanvas.setMode(ShapeCanvas.DRAW_MODE);
            List shapeList=dShapeCanvas.getShapeList();
            shapeList.setCommandListener(this);
            dDisplay.setCurrent(shapeList);
            
        }else if(c==cRemoveShape){
            System.out.println("Remove Shape");
            dShapeCanvas.removeShapes();
            
        }else if(c==cSelect){
            System.out.println("Select Shape");
            dShapeCanvas.setMode(ShapeCanvas.SELECT_MODE);
            newTmpCommand(cShapeSelected);
            
        }else if(c==cShapeSelected){
            dShapeCanvas.selectOrDeselectShape();

        }else if(c==cZoom){
            System.out.println("Zooming");
            dShapeCanvas.setMode(ShapeCanvas.ZOOM_MODE);
            newTmpCommand(cZoomDone);
        
        }else if(c==cZoomDone){
            
            newTmpCommand(cExit);
            dShapeCanvas.setMode(ShapeCanvas.MAIN_MODE);

        }else if(c==cConnect){
            System.out.println("Connect to a Sever");
            Form f=dShapeCanvas.getServerInfoForm();
            f.addCommand(cServerInfoDone);
            f.setCommandListener(this);
            dDisplay.setCurrent(f);
        }else if(c==cServerInfoDone){
            dShapeCanvas.connectToServer();
            dDisplay.setCurrent(dShapeCanvas);
        }else if(c==cAbout){
            System.out.println("About Whiteboard");
            Form dAbout=getAboutForm();
            dAbout.addCommand(cOk);
            dAbout.setCommandListener(this);
            dDisplay.setCurrent(dAbout);
        }else if(d==dAbout && c==cOk){
            dDisplay.setCurrent(dShapeCanvas);
        }
    }
    
    private Form getAboutForm(){
        if(dAbout==null){
            dAbout=new Form("About");
            dAbout.append("This is a MIDP version of Whiteboard program.\n");
            dAbout.append("By Yimin Li");
        }
        
        return dAbout;
    }
    
}
