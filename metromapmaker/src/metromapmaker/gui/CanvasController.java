
package metromapmaker.gui;

/**
 * This class serves as a controller for interactions with the canvas.
 * @author seanfang
 */
import javafx.scene.Cursor;
import javafx.scene.shape.Shape;
import metromapmaker.data.MetroData;
import djf.AppTemplate;
import java.util.ArrayList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroLandMarkImage;
import metromapmaker.data.MetroLandMarkText;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroMap;
import metromapmaker.data.MetroObject;
import metromapmaker.data.MetroState;
import metromapmaker.data.MetroStation;

public class CanvasController {
    AppTemplate app;
    int originalX;
    int originalY;
    boolean stationSelected;
    boolean endLineSelected;
    
    
    public CanvasController(AppTemplate initApp){
        app = initApp;
    }
    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     * @param x
     * @param y
     * @return void
     */
    public void processMousePressed(int x, int y){
        MetroData data = (MetroData) app.getDataComponent();
        MetroMap currentMap = data.getCurrentMap();
        MetroObject station = data.selectTopStation(x, y);
        Scene scene = app.getGUI().getPrimaryScene();
        if(!data.isState(MetroState.ADDING_STATION)&& !data.isState(MetroState.DELETING_STATION) ){
        if(station!=null){
            scene.setCursor(Cursor.CROSSHAIR);
            if(station instanceof MetroStation){
            this.originalX = x;
            this.originalY = y;
            data.setState(MetroState.SELECTING_STATION);
            data.fixStationButton();
            data.loadTextSettings(station);

            }
            else if(station instanceof MetroLine){
                this.originalX = x;
                this.originalY = y;
                data.setState(MetroState.SELECTING_ENDLINE);
                data.fixEditLineButton();
                data.loadTextSettings(station);

            }
            else if(station instanceof MetroLandMarkText){
                this.originalX = x;
                this.originalY = y;
                data.setState(MetroState.SELECTING_LABEL);
                data.loadTextSettings(station);
            }
            else if(station instanceof MetroLandMarkImage){
                this.originalX = x;
                this.originalY = y;
                data.setState(MetroState.SELECTING_IMAGE);
            }
        }
        else{
            
            scene.setCursor(Cursor.DEFAULT);
            data.setState(MetroState.DO_NOTHING);
           
            
            
        }
        //System.out.println("yes");
        }
        else{
            if(data.isState(MetroState.ADDING_STATION)){
                MetroObject potentialAdd = station;
                if(potentialAdd ==null){
                    data.setState(MetroState.DO_NOTHING);
                    scene.setCursor(Cursor.DEFAULT);
                    return;
                }
                MetroLine line = data.getLineFromLineComboBox();
                if(potentialAdd instanceof MetroStation){
                    MetroStation cast = (MetroStation) potentialAdd;
                    ////////
                    data.getJTPS().addTransaction(new addstationtolinetransaction(cast,line));
                }
                else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error: Not Station error dialog");
                    alert.setHeaderText("The selected Map element is not a station!");
                    alert.showAndWait();
                }
                
            }
            else if(data.isState(MetroState.DELETING_STATION)){
                MetroObject potentialDelete = station;
                if(potentialDelete == null){
                    data.setState(MetroState.DO_NOTHING);
                    scene.setCursor(Cursor.DEFAULT);
                    return;
                }
                MetroLine line = data.getLineFromLineComboBox();
                if(potentialDelete instanceof MetroStation){
                    MetroStation cast = (MetroStation) potentialDelete;
                    data.getJTPS().addTransaction(new removestationfromlinetransaction(cast,line));
                }
                else{
                Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error: Not Station error dialog");
                    alert.setHeaderText("The selected Map element is not a station!");
                    alert.showAndWait();
            }
                        
            }
            else if(data.isState(MetroState.SELECTING_STATION)){
                System.out.println("dragging");
            }
        }
    }
    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     * @param x
     * @param y
     * @return void
     */
    public void processMouseDragged(int x, int y){
        MetroData data = (MetroData) app.getDataComponent();
        MetroMap currentMap = data.getCurrentMap();
        Scene scene = app.getGUI().getPrimaryScene();
        if(data.isState(MetroState.SELECTING_STATION)){
            MetroStation dragStation =(MetroStation) data.getSelectedObject();
            //data.getJTPS().addTransaction(new changeStationLocTransaction (dragStation,x,y));
            dragStation.drag(x, y);
            data.redrawLines();
        }
        else if(data.isState(MetroState.SELECTING_ENDLINE)){
            MetroLine dragLine = (MetroLine) data.getSelectedObject();
            dragLine.drag(x, y);
            //data.removeleft(dragLine);
            //dragLine.setLeft();
        }
        else if(data.isState(MetroState.SELECTING_LABEL)){
            MetroLandMarkText dragLine = (MetroLandMarkText) data.getSelectedObject();
            dragLine.drag(x, y);
        }
        else if(data.isState(MetroState.SELECTING_IMAGE)){
            MetroLandMarkImage dragImage = (MetroLandMarkImage) data.getSelectedObject();
            dragImage.drag(x, y);
        }
    }
    /**
     * Respond to mouse button release on the rendering surface, which we call canvas,
     * but is actually a Pane.
     * @param x
     * @param y
     * @return void
     */
    public void processMouseReleased(int x, int y){
        MetroData data = (MetroData) app.getDataComponent();
        MetroMap currentMap = data.getCurrentMap();
        Scene scene = app.getGUI().getPrimaryScene();
        if(data.isState(MetroState.SELECTING_ENDLINE)){
            MetroLine dragLine = (MetroLine) data.getSelectedObject();
            if(dragLine.getLeft()){
            data.removeleft(dragLine);
            dragLine.setLeft();
            dragLine.getLeftEdgeLine().setEffect(null);
            dragLine.getRightEdgeLine().setEffect(null);
            data.setState(MetroState.DO_NOTHING);
            data.setSelectedObject();
            data.getJTPS().addTransaction(new changeLineEndTransaction(dragLine,x,y,true));
            }
            else if(dragLine.getRight()){
                data.removeright(dragLine);
                dragLine.setRight();
                data.setState(MetroState.DO_NOTHING);
                dragLine.getLeftEdgeLine().setEffect(null);
            dragLine.getRightEdgeLine().setEffect(null); 
            data.setSelectedObject();
            data.getJTPS().addTransaction(new changeLineEndTransaction(dragLine,x,y,false));

            }
        }
        else if(data.isState(MetroState.SELECTING_STATION)){
            MetroStation dragStation =(MetroStation) data.getSelectedObject();
            data.getJTPS().addTransaction(new changeStationLocTransaction (dragStation,x,y));
        }
        else if(data.isState(MetroState.SELECTING_LABEL)){
            MetroLandMarkText text = (MetroLandMarkText) data.getSelectedObject();
            data.getJTPS().addTransaction(new changeLandMarkTextTransaction(text,x,y));
        }
        else if(data.isState(MetroState.SELECTING_IMAGE)){
            MetroLandMarkImage image = (MetroLandMarkImage) data.getSelectedObject();
            data.getJTPS().addTransaction(new changeImageTransaction(image,x,y));
        }
    }
    
    private class changeImageTransaction implements jTPS_Transaction{
        double initX;
        double initY;
        double newX;
        double newY;
        MetroLandMarkImage image;
        
        public changeImageTransaction(MetroLandMarkImage image, int x, int y){
            this.initX = originalX;
            this.initY = originalY;
            this.newX = x;
            this.newY = y;
            this.image = image;
        }
        @Override
        public void doTransaction() {
            image.drag((int) newX, (int) newY);
        }

        @Override
        public void undoTransaction() {
            image.drag((int) initX, (int) initY);

        }
        
    }

    private  class changeLandMarkTextTransaction implements jTPS_Transaction {
        double initX;
        double initY;
        double newX;
        double newY;
        MetroLandMarkText text;
        public changeLandMarkTextTransaction(MetroLandMarkText text, int x, int y) {
            this.initX = originalX;
            this.initY = originalY;
            this.newX = x;
            this.newY = y;
            this.text = text;
        }

        @Override
        public void doTransaction() {
            text.drag((int) newX, (int) newY);
        }

        @Override
        public void undoTransaction() {
            text.drag((int) initX, (int) initY);

        }
    }
    
    private class changeStationLocTransaction implements jTPS_Transaction{
        double initX;
        double initY;
        double newX;
        double newY;
        MetroStation station;
        
        
        public changeStationLocTransaction(MetroStation station, int x, int y){
            this.initX = originalX;
            this.initY = originalY;
            this.newX = x;
            this.newY = y;
            this.station = station;
            
        }
        
        @Override
        public void doTransaction() {
            station.drag((int)newX, (int)newY);
            MetroData data = (MetroData) app.getDataComponent();
            data.redrawLines();
        }

        @Override
        public void undoTransaction() {
             station.drag((int)initX, (int)initY);
            MetroData data = (MetroData) app.getDataComponent();
            data.redrawLines();
        }
    
    }
    
    private class changeLineEndTransaction implements jTPS_Transaction{
        double initX;
        double initY;
        double newX;
        double newY;
        MetroLine line;
        boolean isLeft;
        
        public changeLineEndTransaction(MetroLine line, int x, int y, boolean isLeft){
            this.initX = originalX;
            this.initY = originalY;
            this.newX = x;
            this.newY = y;
            this.line = line;
            this.isLeft =  isLeft;

        }
        
        @Override
        public void doTransaction() {
            line.drag2((int)newX, (int)newY,isLeft);
        }

        @Override
        public void undoTransaction() {
            line.drag2((int)originalX, (int)originalY,isLeft);
        }
        
    }
    
    private class addstationtolinetransaction implements jTPS_Transaction{
        MetroStation station;
        MetroLine line;
        public addstationtolinetransaction(MetroStation station, MetroLine line){
            this.station = station;
            this.line = line;
        }
        
        @Override
        public void doTransaction() {
            MetroData data = (MetroData) app.getDataComponent();
            data.addStationsToLine(station, line);
                       
        }

        @Override
        public void undoTransaction() {
            MetroData data = (MetroData) app.getDataComponent();
            data.removeStationsFromLine(station, line);
        }
        
    }
    private class removestationfromlinetransaction implements jTPS_Transaction{
        MetroStation station;
        MetroLine line;
        public removestationfromlinetransaction(MetroStation station, MetroLine line){
            this.station = station;
            this.line = line;
        }
        @Override
        public void doTransaction() {
            MetroData data = (MetroData) app.getDataComponent();
            data.removeStationsFromLine(station, line);
            
        }

        @Override
        public void undoTransaction() {
             MetroData data = (MetroData) app.getDataComponent();
            data.addStationsToLine(station, line);
        }
        
    }
    
    
}
