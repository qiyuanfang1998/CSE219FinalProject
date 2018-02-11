
package metromapmaker.gui;

import metromapmaker.data.MetroData;
import djf.AppTemplate;
import java.awt.Button;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metromapmaker.data.MetroLandMarkText;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroMap;
import metromapmaker.data.MetroState;
import metromapmaker.data.MetroStation;

/**
 *
 * @author seanfang
 */
public class MapEditorController {
    AppTemplate app;
    MetroData dataManager;
    jTPS jtps;
    FileChooser fileChooser;
    Stack<Object> copyStack;
    Stack<Object> redoStack;
    Stage metroLineEditDialogStage;
    Scene metroLineEditDialogScene;
    GridPane metroLineEditGridPane;
    TextField metroLineEditTextField;
    ColorPicker metroLineEditColorPicker;
    Button metroLineEditOkButton;
    Button metroLineEditCancelButton;
    Stage metroLineStationListingStage;
    Scene metroLineStationListingScene;
    GridPane metroLineStationListingGridPane;
    Label metroLineStationListingLabel;
    ListView metroLineStationListingListView;
    Button metroLineStationListingOkButton;
    Stage metroRouteStage;
    Scene metroRouteScene;
    Label metroRouteLabel;
    ListView metroRouteListView;
    Button metroRouteOkButton;
    //MetroMap currentMap;
    /**
     * This is the constructor for our controller.
     * @param void
     * @return MapEditorController
     */
    public MapEditorController(AppTemplate initApp){
        app = initApp;
        dataManager = (MetroData) app.getDataComponent();
        jtps = new jTPS();
    }
    
    /**
     * This method is the handler for adding a line.
     * @param void
     * @return void
     */
    public void processAddLine(MetroLine line){
        
        
        dataManager.getJTPS().addTransaction(new AddLineTransaction(line));
    
        
    }
    private class AddLineTransaction implements jTPS_Transaction{
        String lineName;
        Color lineColor;
        MetroLine line;
        
        public AddLineTransaction(MetroLine line){
             this.line = line;
             this.lineName = line.getLineName();
             this.lineColor = line.getLineColor();
        }
        
        @Override
        public void doTransaction() {
            dataManager.addLine(line);
        }

        @Override
        public void undoTransaction() {
            dataManager.removeLine(lineName);
        }
        
    }
    /**
     * This method is the handler for removing lines.
     * @param void
     * @return void
     */
    public void processRemoveLine(String delLineName){
        dataManager.getJTPS().addTransaction(new removelineTransaction(delLineName));
    }
    private class removelineTransaction implements jTPS_Transaction{
        MetroLine deletedLine;
        String delLineName;
        public removelineTransaction(String delLineName){
            this.delLineName = delLineName;
        }
        @Override
        public void doTransaction() {
            deletedLine = dataManager.removeLine(delLineName);
        }

        @Override
        public void undoTransaction() {
            dataManager.addLine(deletedLine);
        }
        
    }
    /**
     * This method is the handler for editing lines.
     * @param void
     * @return void
     */
    public void processEditLine(MetroLine line, String newLineName, Color newLineColor){
        dataManager.getJTPS().addTransaction(new editLineTransaction(line,newLineName,newLineColor));
    }
    private class editLineTransaction implements jTPS_Transaction{
        MetroLine line;
        String oldLineName;
        String newLineName;
        Color oldLineColor;
        Color newlineColor;
        
        
        public editLineTransaction(MetroLine line, String newLineName, Color newLineColor){
            this.oldLineName = line.getLineName();
            this.oldLineColor= line.getLineColor();
            this.newLineName = newLineName;
            this.newlineColor = newLineColor;
            this.line = line;
            
        }
        
        @Override
        public void doTransaction() {
            line.setLineName(newLineName);
            line.setLineColor(newlineColor);
            line.getLeftEdgeLabel().setText(newLineName);
            line.getRightEdgeLabel().setText(newLineName);
            line.getLeftEdgeLine().setStroke(newlineColor);
            line.getRightEdgeLine().setStroke(newlineColor);
            dataManager.redrawLines();
            dataManager.fixLineComboBox(oldLineName, newLineName);
            dataManager.fixEditLineButton();
            
        }

        @Override
        public void undoTransaction() {
            line.setLineName(oldLineName);
            line.setLineColor(oldLineColor);
            line.getLeftEdgeLabel().setText(oldLineName);
            line.getRightEdgeLabel().setText(oldLineName);
            line.getLeftEdgeLine().setStroke(oldLineColor);
            line.getRightEdgeLine().setStroke(oldLineColor);
            dataManager.redrawLines();
            dataManager.fixLineComboBox(newLineName, oldLineName);
            dataManager.fixEditLineButton();
        }
        
    }
    
    /**
     * This method is the handler for adding stations to lines.
     * @param void
     * @return void
     */
    public void processAddStationToLine(){
        dataManager.setState(MetroState.ADDING_STATION);
    }
       
    
    private class addStationToLineTransaction implements jTPS_Transaction{
        ArrayList<MetroStation> addedStations;
        
        public addStationToLineTransaction(){
            
        }
        
        
        @Override
        public void doTransaction() {
            
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
   
    /**
     * This method is the handler for removing stations from lines.
     * @param void
     * @return void
     */
    public void processRemoveStationFromLine(){
        
    }
    private class removeStationFromLineTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for changing line thickness.
     * @param void
     * @return void
     */
    public void processChangeLineThickness(){
        
    }
    private class changeLineThicknessTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for adding new stations to map.
     * @param void
     * @return void
     */
    public void processAddNewStation(String stationName){
        dataManager.getJTPS().addTransaction(new addNewStationTransaction(stationName));
    }
    private class addNewStationTransaction implements jTPS_Transaction{
        String stationName;
        MetroStation station;
        public addNewStationTransaction(String stationName){
            this.stationName = stationName;
            station = new MetroStation(stationName);
        }
        @Override
        public void doTransaction() {
            dataManager.addNewStation(station);
        }

        @Override
        public void undoTransaction() {
            dataManager.removeStation(station);
        }
        
    }
    /**
     * This method is the handler for removing stations from map.
     * @param void
     * @return void
     */
    public void processRemoveStation(String toRemoveStationName){
        dataManager.getJTPS().addTransaction(new removeStationTransaction(toRemoveStationName));
    }
    private class removeStationTransaction implements jTPS_Transaction{
        MetroStation removedStation;
        String removeStationName;
        MetroMap clone;
        public removeStationTransaction(String stationName){
            ArrayList<MetroStation> arr = dataManager.getCurrentMap().getStations();
            for(int i=0;i<arr.size();i++){
                if(stationName.equals(arr.get(i).getName())){
                    removedStation = arr.get(i);
                }
            }
            
        }
        @Override
        public void doTransaction() {
            dataManager.removeStation(removedStation);
        }

        @Override
        public void undoTransaction() {
            dataManager.addNewStation(removedStation);
        }
        
    }
    /**
     * This method is the handler for rotating station labels.
     * @param void
     * @return void
     */
    public void processRotateStationLabel(){
        
    }
    private class rotateStationTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for changing station radius.
     * @param void
     * @return void
     */
    public void processChangeStationRadius(){
        
    }
    private class changeStationRadiusTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for processing snap to grid.
     * @param void
     * @return void
     */
    public void processSnapToGrid(){
        
    }
    private class snapTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    public void processToggleGrid(){
        
    }
    /**
     * This method is the handler for processing changing station fill colors.
     * @param void
     * @return void
     */
    public void processChangeStationFillColor(MetroStation station,Color color){
        dataManager.getJTPS().addTransaction(new changeSFCTransaction(station,color));
    }
    private class changeSFCTransaction implements jTPS_Transaction{
        MetroStation station;
        Color oldColor;
        Color newColor;
        
        public changeSFCTransaction(MetroStation station, Color color){
            this.station = station;
            this.oldColor = station.getStationColor();
            this.newColor = color;
        }

        @Override
        public void doTransaction() {
            station.getCircle().setFill(newColor);
            station.setStationColor(newColor);
            dataManager.fixStationButton();
        }

        @Override
        public void undoTransaction() {
            station.getCircle().setFill(oldColor);
            station.setStationColor(oldColor);
            dataManager.fixStationButton();

        }
        
    }
    /**
     * This method is the handler for processing adding image overlay.
     * @param void
     * @return void
     */
    public void processAddImageOverlay(){
        
    }
    private class addImageOverlayTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for processing adding image backgrounds.
     * @param void
     * @return void
     */
    public void processSetImageBackground(){
        
    }
    private class setImageBackgroundTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for moving station labels.
     * @param void
     * @return void
     */
    public void processMoveStationLabel(){
        
    }
    private class moveStationTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for setting background color.
     * @param void
     * @return void
     */
    public void processSetBackgroundColor(Color newColor){
        dataManager.getJTPS().addTransaction(new setBackgroundColorTransaction(newColor));
        
    }
    private class setBackgroundColorTransaction implements jTPS_Transaction{
        
        Color oldColor;
        Color newColor;
        public setBackgroundColorTransaction(Color newColor){
            this.newColor = newColor;
            this.oldColor = dataManager.getCurrentMap().getBackgroundColor();
        }
        @Override
        public void doTransaction() {
            dataManager.processChangeCanvasColor(newColor);
            dataManager.getCurrentMap().setBackgroundColor(newColor);
        }

        @Override
        public void undoTransaction() {
            dataManager.processChangeCanvasColor(oldColor);
            dataManager.getCurrentMap().setBackgroundColor(oldColor);

        }
        
    }
    /**
     * This method is the handler for adding a label decor.
     * @param void
     * @return void
     */
    public void processAddLabel(String labelName){
        dataManager.getJTPS().addTransaction(new addLabelTransaction(labelName));
        
    }
    private class addLabelTransaction implements jTPS_Transaction{
        MetroLandMarkText label;
        
        public addLabelTransaction(String labelName){
            this.label = new MetroLandMarkText(labelName);
        }
        
        @Override
        public void doTransaction() {
            dataManager.addNewLabel(this.label);
        }

        @Override
        public void undoTransaction() {
            dataManager.removeLabel(this.label);
        }
        
    }
    /**
     * This method is the handler for removing a map element.
     * @param void
     * @return void
     *
     */
    public void processRemoveMapElement(){
        
    }
    private class removeMapElTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
}
    /**
     * This method is the handler for moving a map element.
     * @param void 
     * @return void
     */
    public void processMoveMapElement(){
        
    }
    private class moveMapElTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for processing change of text color.
     * @param void
     * @return void
     */
    public void processChangeTextColor(){
        
    }
    private class changeTextColor implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for changing font.
     * @param void
     * @return void
     */
    public void processChangeTextFont(){
        
    }
    private class changeTextFontTransaction implements jTPS_Transaction{

        @Override
        public void doTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void undoTransaction() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    /**
     * This method is the handler for undo.
     * @param void
     * @return void
     */
    public void processUndo(){
        dataManager.getJTPS().undoTransaction();
    }
    /**
     * This method is the handler for redo.
     * @param void
     * @return void
     */
    public void processRedo()
    {
                dataManager.getJTPS().doTransaction();

    }
    /**
     * This method is the handler for finding route.
     * @param void
     * @return void
     */
    public void processFindRoute(){
        
    }
    
    public void processZoomplus(){
        
    }
    
    public void processZoomMinus(){
        
    }
    
    public void processMapZoomup(){
        
    }
    
    public void processMapZoomdown(){
        
    }
    
    public void processNew(){
        ButtonType cc = new ButtonType("Create Map with This Name", ButtonBar.ButtonData.OK_DONE);
        ButtonType dd = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(AlertType.INFORMATION,"D",cc,dd);
        alert.setTitle("New Work Title");
        alert.setHeaderText("Enter title");
        TextField text = new TextField();
        alert.getDialogPane().setContent(text);
        
                alert.showAndWait().ifPresent(r->{if(r==cc){dataManager.resetData(text.getText());}});
    }
}
