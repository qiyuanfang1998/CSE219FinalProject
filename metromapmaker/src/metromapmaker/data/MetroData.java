
package metromapmaker.data;

import djf.AppTemplate;
import djf.components.AppDataComponent;
import java.util.ArrayList;
import java.util.Collections;
import metromapmaker.MetroApp;

import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import metromapmaker.gui.MetroWorkspace;

/**
 * This class serves as the AppDataComponent for our application and 
 * is responsible for data management.
 * @author seanfang
 */
public class MetroData implements AppDataComponent {
    AppTemplate app;
    ObservableList<Node> MetroObjects;
    Color backgroundColor;
    Shape selectedShape;
    Color currentFillColor;
    double currentCircleRadius;
    MetroMap currentMap;
    jTPS jtps;
    Effect highlightedEffect;
    MetroWorkspace workspace;
    MetroState state;
    MetroObject selectedMetroObject;
    ArrayList<Line> interLines = new ArrayList();
    String string = new String();
    MetroLine removed;
    /**
     * Constructor for our app Data component.
     * @param app 
     * @return MetroData
     */
    public MetroData(MetroApp initApp) {
        app = initApp;
        //highlighted effect
        DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
        
        jtps = new jTPS();
    }
    
    public MetroMap getMap(){
        return this.currentMap;
    }
    public void setCurrentMap(MetroMap map){
        this.currentMap = map;
    }
    public MetroMap getCurrentMap(){
        return this.currentMap;
    }
    public jTPS getJTPS(){
        return this.jtps;
    }
    
    public void addLine(MetroLine line){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        currentMap.addLine(line);
        //System.out.println(currentMap.lines.size());
        //redrawCanvas();
        Line one = line.leftEdgeLine;
        Line two = line.rightEdgeLine;
        Label leftEdgeLabel = line.leftEdgeLabel;
        Label rightEdgeLabel = line.rightEdgeLabel;
        canvas.getChildren().addAll(one,two,leftEdgeLabel,rightEdgeLabel);
       line.leftEdgeLabel.setOnMousePressed(e->{
           canvas.getChildren().remove(leftEdgeLabel);
           //line.leftEndCircle.setEffect(highlightedEffect);
           canvas.getChildren().add(line.leftEndCircle);
           line.leftEndCircleShowing = true;
           
       });
       
       line.rightEdgeLabel.setOnMousePressed(e->{
           canvas.getChildren().remove(rightEdgeLabel);
                      //line.rightEndCircle.setEffect(highlightedEffect);

           canvas.getChildren().add(line.rightEndCircle);
           line.rightEndCircleShowing = true;
           setState(MetroState.SELECTING_ENDLINE);
       });
        
        workspace.getLineComboBox().getItems().add(line.name);
        //System.out.println(currentMap.getLines().size());
        
    }
    public MetroLine removeLine(String line){
         workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        for(int i=0;i<currentMap.getLines().size();i++){
            MetroLine arrcheck = currentMap.getLines().get(i);
            if(arrcheck.name == line){
                Line one = arrcheck.leftEdgeLine;
                Line two = arrcheck.rightEdgeLine;
                Label leftEdgeLabel = arrcheck.leftEdgeLabel;
                Label rightEdgeCheck = arrcheck.rightEdgeLabel;
                canvas.getChildren().removeAll(one,two,leftEdgeLabel,rightEdgeCheck);
                //currentMap.getLines().remove(i);
                currentMap.removeLine(arrcheck);
                workspace.getLineComboBox().getItems().remove(line);
                this.removed = arrcheck;
                
                
                
                if(workspace.getLineComboBox().getItems().size()!=0){
                    workspace.getLineComboBox().getSelectionModel().selectNext();
                }
                //System.out.println(currentMap.getLines().size());
            }
        }
        workspace.getCanvas().getChildren().removeAll(this.interLines);
                    
                for(int y = 0;y<currentMap.lines.size();y++){
                MetroLine liner = currentMap.lines.get(y);
                if(liner.stationList.size()!=0){
                liner.leftEdgeLine.setEndX(liner.stationList.get(0).CircleposX);
                liner.leftEdgeLine.setEndY(liner.stationList.get(0).CircleposY);
                liner.rightEdgeLine.setStartX(liner.stationList.get(liner.stationList.size()-1).CircleposX);
                liner.rightEdgeLine.setStartY(liner.stationList.get(liner.stationList.size()-1).CircleposY);}
                else{
                    liner.leftEdgeLine.setEndX(liner.leftEdgeLine.getStartX()+100);
                    liner.leftEdgeLine.setEndY(liner.leftEdgeLine.getStartY());
                    liner.rightEdgeLine.setStartX(liner.leftEdgeLine.getEndX());
                    liner.rightEdgeLine.setStartY(liner.leftEdgeLine.getEndY());
                }
                for (int i=0;i<liner.stationList.size()-1;i++){
                        
                        
                        
                        Line interline = new Line();
                        interline.setStrokeWidth(liner.strokewidth);
                        interline.setStroke(liner.lineColor);
                        interline.setStartX(liner.stationList.get(i).CircleposX);
                        interline.setStartY(liner.stationList.get(i).CircleposY);
                        interline.setEndX(liner.stationList.get(i+1).CircleposX);
                        interline.setEndY(liner.stationList.get(i+1).CircleposY);
                        workspace.getCanvas().getChildren().addAll(interline);
                        interLines.add(interline);
                        liner.stationList.get(i).stationCircle.toFront();
                        liner.stationList.get(i).stationLabel.toFront();
                        
                }
                }
                return removed;
    }
    
    public void addNewStation(MetroStation station){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        currentMap.addStation(station);
        setState(MetroState.DO_NOTHING);
        workspace.getStationComboBox().getItems().add(station.getName());
        workspace.getStartStation().getItems().add(station.getName());
        workspace.getEndStation().getItems().add(station.getName());
        //
        
        canvas.getChildren().addAll(station.getCircle(),station.getStationLabel());
        
    }
    
    public void removeStation(MetroStation station){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        //MetroMap clone = currentMap.clone();
        currentMap.removeStation(station);
        setState(MetroState.DO_NOTHING);
        workspace.getStationComboBox().getItems().remove(station.getName());
        workspace.getStartStation().getItems().remove(station.getName());
        workspace.getEndStation().getItems().remove(station.getName());
        canvas.getChildren().removeAll(station.stationCircle,station.stationLabel);
        
        //delete all instances of the station in the existing lines and the interlines, redraw
        for (int i = 0;i<currentMap.lines.size();i++){
            MetroLine line = currentMap.lines.get(i);
            int removed = 0;
            for(int k = 0;k<line.stationList.size();k++){
                if(line.stationList.get(k).equals(station)){
                    
                    removed = k;
                    line.stationList.remove(removed);
                }
            }
            //
            
           
        }
        
        workspace.getCanvas().getChildren().removeAll(this.interLines);
                    
                for(int y = 0;y<currentMap.lines.size();y++){
                MetroLine liner = currentMap.lines.get(y);
                if(liner.stationList.size()!=0){
                liner.leftEdgeLine.setEndX(liner.stationList.get(0).CircleposX);
                liner.leftEdgeLine.setEndY(liner.stationList.get(0).CircleposY);
                liner.rightEdgeLine.setStartX(liner.stationList.get(liner.stationList.size()-1).CircleposX);
                liner.rightEdgeLine.setStartY(liner.stationList.get(liner.stationList.size()-1).CircleposY);}
                else{
                    liner.leftEdgeLine.setEndX(liner.leftEdgeLine.getStartX()+100);
                    liner.leftEdgeLine.setEndY(liner.leftEdgeLine.getStartY());
                    liner.rightEdgeLine.setStartX(liner.leftEdgeLine.getEndX());
                    liner.rightEdgeLine.setStartY(liner.leftEdgeLine.getEndY());
                }
                for (int i=0;i<liner.stationList.size()-1;i++){
                        
                        
                        
                        Line interline = new Line();
                        interline.setStrokeWidth(liner.strokewidth);
                        interline.setStroke(liner.lineColor);
                        interline.setStartX(liner.stationList.get(i).CircleposX);
                        interline.setStartY(liner.stationList.get(i).CircleposY);
                        interline.setEndX(liner.stationList.get(i+1).CircleposX);
                        interline.setEndY(liner.stationList.get(i+1).CircleposY);
                        workspace.getCanvas().getChildren().addAll(interline);
                        interLines.add(interline);
                        station.stationCircle.toFront();
                        station.stationLabel.toFront();
                        liner.stationList.get(i).stationCircle.toFront();
                        liner.stationList.get(i).stationLabel.toFront();
                        
                }
                }
        
        
        
        if(workspace.getStationComboBox().getItems().size()!=0){
            workspace.getStationComboBox().getSelectionModel().selectNext();
        }
        
    }
    
    public void addStationsToLine(MetroStation station, MetroLine line){
        if(line!=null){
            ArrayList <MetroStation> stations = line.stationList;
            for(int i=0;i<stations.size();i++){
                if(stations.get(i).equals(station)){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error: Station Already in line error");
                    alert.setHeaderText("Station is already in line, please select another");
                    alert.showAndWait();
                    return;
                }
            }
            
            double posBeginX = line.leftEdgeLine.getStartX();
            double posBeginY = line.leftEdgeLine.getStartY();
            Point2D pt = new Point2D(posBeginX,posBeginY);
            double compaisonPosX = station.CircleposX;
            double compaisonPosY = station.CircleposY;
            Point2D pt2 = new Point2D(compaisonPosX,compaisonPosY);
            boolean last = true;
            for (int i = 0;i<line.stationList.size();i++){
                MetroStation stationCompare = line.stationList.get(i);
                double cX = stationCompare.CircleposX;
                double cY = stationCompare.CircleposY;
                Point2D pt3 = new Point2D(cX, cY);
                if(pt.distance(pt2)<pt.distance(pt3)){
                    line.stationList.add(i, station);
                    i++;
                    last = false;
                    break;
                }
            }
            if(last){
                line.stationList.add(station);
            }
            
            line.leftEdgeLine.setEndX(line.stationList.get(0).CircleposX);
            line.leftEdgeLine.setEndY(line.stationList.get(0).CircleposY);
            line.rightEdgeLine.setStartX(line.stationList.get(line.stationList.size()-1).CircleposX);
            line.rightEdgeLine.setStartY(line.stationList.get(line.stationList.size()-1).CircleposY);
                
                
                    
                    
                        
                workspace.getCanvas().getChildren().removeAll(this.interLines);
                    
                for(int y = 0;y<currentMap.lines.size();y++){
                MetroLine liner = currentMap.lines.get(y);
                for (int i=0;i<liner.stationList.size()-1;i++){
                        
                        
                        
                        Line interline = new Line();
                        interline.setStrokeWidth(liner.strokewidth);
                        interline.setStroke(liner.lineColor);
                        interline.setStartX(liner.stationList.get(i).CircleposX);
                        interline.setStartY(liner.stationList.get(i).CircleposY);
                        interline.setEndX(liner.stationList.get(i+1).CircleposX);
                        interline.setEndY(liner.stationList.get(i+1).CircleposY);
                        workspace.getCanvas().getChildren().addAll(interline);
                        interLines.add(interline);
                        station.stationCircle.toFront();
                        station.stationLabel.toFront();
                        liner.stationList.get(i).stationCircle.toFront();
                        liner.stationList.get(i).stationLabel.toFront();
                        
                }
                //liner.stationList.get(line.stationList.size()-1).stationCircle.toFront();
                //liner.stationList.get(line.stationList.size()-1).stationLabel.toFront();
                
                }
                
                        
                
            }
        }
    
    
    public void removeStationsFromLine(MetroStation station, MetroLine line){
        if(line !=null){
            ArrayList <MetroStation> stations = line.stationList;
            boolean existsInLine = false;
            for(int i=0;i<stations.size();i++){
                if(stations.get(i).equals(station)){
                    existsInLine = true;
                    line.stationList.remove(i);
                }
            }
            if(!existsInLine){
                Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error: Station not in line error");
                    alert.setHeaderText("Station is not in line, please select another");
                    alert.showAndWait();
            }
            
            workspace.getCanvas().getChildren().removeAll(this.interLines);
                    
                for(int y = 0;y<currentMap.lines.size();y++){
                MetroLine liner = currentMap.lines.get(y);
                if(liner.stationList.size()!=0){
                liner.leftEdgeLine.setEndX(liner.stationList.get(0).CircleposX);
                liner.leftEdgeLine.setEndY(liner.stationList.get(0).CircleposY);
                liner.rightEdgeLine.setStartX(liner.stationList.get(liner.stationList.size()-1).CircleposX);
                liner.rightEdgeLine.setStartY(liner.stationList.get(liner.stationList.size()-1).CircleposY);
                }
                else{
                    liner.leftEdgeLine.setEndX(liner.leftEdgeLine.getStartX()+100);
                    liner.leftEdgeLine.setEndY(liner.leftEdgeLine.getStartY());
                    liner.rightEdgeLine.setStartX(liner.leftEdgeLine.getEndX());
                    liner.rightEdgeLine.setStartY(liner.leftEdgeLine.getEndY());
                    
                }
                for (int i=0;i<liner.stationList.size()-1;i++){
                        
                        
                        
                        Line interline = new Line();
                        interline.setStrokeWidth(liner.strokewidth);
                        interline.setStroke(liner.lineColor);
                        interline.setStartX(liner.stationList.get(i).CircleposX);
                        interline.setStartY(liner.stationList.get(i).CircleposY);
                        interline.setEndX(liner.stationList.get(i+1).CircleposX);
                        interline.setEndY(liner.stationList.get(i+1).CircleposY);
                        workspace.getCanvas().getChildren().addAll(interline);
                        interLines.add(interline);
                        station.stationCircle.toFront();
                        station.stationLabel.toFront();
                        liner.stationList.get(i).stationCircle.toFront();
                        liner.stationList.get(i).stationLabel.toFront();
                        
                }
                }
            
        }
    }
    
    public MetroLine getLineFromLineComboBox(){
        String r =  workspace.getLineComboBox().getValue().toString();
        ArrayList <MetroLine> line = currentMap.getLines();
        for(int i=0;i<line.size();i++){
            if (line.get(i).name.equals(r)){
                return line.get(i);
            }
        }
        return null;
    }
    
    public MetroStation getStationFromComboBox(){
        String r = workspace.getStationComboBox().getValue().toString();
        ArrayList<MetroStation> station = currentMap.getStations();
        for(int i = 0;i<station.size();i++){
            if(station.get(i).getName().equals(r)){
                return station.get(i);
            }
        }
        return null;
    }
    
    public void highLightStation(MetroObject station){
        if(station instanceof MetroStation){
        MetroStation stations =(MetroStation) station;
        stations.getCircle().setEffect(highlightedEffect);
        }
        else if(station instanceof MetroLine){
            MetroLine line = (MetroLine) station;
            line.getLeftEdgeLine().setEffect(highlightedEffect);
            line.getRightEdgeLine().setEffect(highlightedEffect);
        }
        else if(station instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) station;
            text.getLabel().setEffect(highlightedEffect);
        }
        else if(station instanceof MetroLandMarkImage){
            MetroLandMarkImage image = (MetroLandMarkImage) station;
            image.rect.setEffect(highlightedEffect);
        }
        
       
    }
    
    public void unhighlightStation(MetroObject station){
        if(station instanceof MetroStation){
        MetroStation stations =(MetroStation) station;
        stations.getCircle().setEffect(null);
        }
        else if(station instanceof MetroLine){
            MetroLine line = (MetroLine) station;
            line.getLeftEdgeLine().setEffect(null);
            line.getRightEdgeLine().setEffect(null);
        }
        else if(station instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) station;
            text.getLabel().setEffect(null);
        }
        else if(station instanceof MetroLandMarkImage){
            MetroLandMarkImage image = (MetroLandMarkImage) station;
            image.rect.setEffect(null);
        }
        
    }
    
    
    public void setState(MetroState state){
        this.state = state;
    }
    public boolean isState(MetroState state){
        return state == this.state;
    }
    
    public MetroObject selectTopStation(int x, int y){
        MetroObject station = getTopStation(x,y);
        if(selectedMetroObject!=null){
            unhighlightStation(selectedMetroObject);
        }
        if(station !=null){
            highLightStation(station);
            if(station instanceof MetroStation){
            int stationIndex = 0;
            for(int i=0;i<currentMap.stations.size();i++){
                if(station.equals(currentMap.stations.get(i))){
                    break;
                }
                else{
                    stationIndex++;
                }
            }
            //Collections.swap(currentMap.stations, stationIndex, 0);
            for(int i=0;i<currentMap.stations.size();i++){
                if(currentMap.stations.get(i).equals(station)){
                    MetroStation stations = (MetroStation) station;
                    stations.getCircle().toFront();
                    workspace.getStationComboBox().getSelectionModel().select(stations.stationName);
                }
            }
            }
            else if(station instanceof MetroLine){
                int lineIndex = 0;
            for(int i=0;i<currentMap.lines.size();i++){
                if(station.equals(currentMap.lines.get(i))){
                    break;
                }
                else{
                    lineIndex++;
                }
            }
            //Collections.swap(currentMap.lines, lineIndex, 0);
                for(int i=0;i<currentMap.lines.size();i++){
                    if(currentMap.lines.get(i).equals(station)){
                        MetroLine line = (MetroLine) station;
                        workspace.getLineComboBox().getSelectionModel().select(line.name);
                    }
                }
            }
            else if(station instanceof MetroLandMarkText){
                
            }
            else if(station instanceof MetroLandMarkImage){
                
            }
        }
        selectedMetroObject = station;
        
        return station;
    }
    
    public MetroObject getTopStation(int x, int y){
        ArrayList<MetroStation> stations = currentMap.stations;
        for(int i=0;i<stations.size();i++){
            MetroStation checkStation = stations.get(i);
            if(checkStation.stationCircle.contains(x, y)){
                return checkStation;
            }
        }
        ArrayList<MetroLine> lines = currentMap.lines;
        for(int i=0;i<lines.size();i++){
            MetroLine checkLine = lines.get(i);
            if(checkLine.leftEdgeLine.contains(x,y) || checkLine.rightEdgeLine.contains(x, y)||checkLine.leftEndCircle.contains(x, y)||checkLine.rightEndCircle.contains(x, y)){
                return checkLine;
            }
        }
        ArrayList<MetroLandMarkText> text = currentMap.text;
        //System.out.println(text.size());
        for(int i = 0;i<text.size();i++){
            MetroLandMarkText texts = text.get(i);
           // System.out.println(x+" "+y);
            //System.out.println(texts.getPosX()+" "+texts.getPosY());
            //System.out.println(texts.getEndX()+" "+texts.getEndY());
            if((texts.getPosX()<=x)&&(texts.getPosY()>=y)&&(texts.getEndX()>=x)&&(texts.getEndY()<=y)){
                return texts;
            }
        }
        ArrayList<MetroLandMarkImage> images = currentMap.images;
        for(int i = 0;i<images.size();i++){
            MetroLandMarkImage image = images.get(i);
            
            
            
            if(image.rect.contains(x, y)){
                
                return image;
            }
        }
        return null;
    }
    
    //idk if i use lol
    public void redrawLines(){
        workspace.getCanvas().getChildren().removeAll(this.interLines);
                    
                for(int y = 0;y<currentMap.lines.size();y++){
                MetroLine liner = currentMap.lines.get(y);
                liner.leftEdgeLine.setStrokeWidth(liner.getthickness());
                liner.rightEdgeLine.setStrokeWidth(liner.getthickness());

                if(liner.stationList.size()!=0){
                liner.leftEdgeLine.setEndX(liner.stationList.get(0).CircleposX);
                liner.leftEdgeLine.setEndY(liner.stationList.get(0).CircleposY);
                liner.rightEdgeLine.setStartX(liner.stationList.get(liner.stationList.size()-1).CircleposX);
                liner.rightEdgeLine.setStartY(liner.stationList.get(liner.stationList.size()-1).CircleposY);}
                else{
                    liner.leftEdgeLine.setEndX(liner.leftEdgeLine.getStartX()+100);
                    liner.leftEdgeLine.setEndY(liner.leftEdgeLine.getStartY());
                    liner.rightEdgeLine.setStartX(liner.leftEdgeLine.getEndX());
                    liner.rightEdgeLine.setStartY(liner.leftEdgeLine.getEndY());
                }
                for (int i=0;i<liner.stationList.size()-1;i++){
                        
                        
                        
                        Line interline = new Line();
                        interline.setStrokeWidth(liner.getthickness()); //+
                        interline.setStroke(liner.lineColor);
                        interline.setStartX(liner.stationList.get(i).CircleposX);
                        interline.setStartY(liner.stationList.get(i).CircleposY);
                        interline.setEndX(liner.stationList.get(i+1).CircleposX);
                        interline.setEndY(liner.stationList.get(i+1).CircleposY);
                        workspace.getCanvas().getChildren().addAll(interline);
                        interLines.add(interline);
                        liner.stationList.get(i).stationCircle.toFront();
                        liner.stationList.get(i).stationLabel.toFront();
                        
                }
                }
    }
    
    
    
    
    /**
     * Removes the selected line
     * @param void
     * @return void
     */
    public void removeSelectedLine(){
        
    }
    /**
     * Removes the selected station.
     * @param void
     * @return void
     */
    public void removeSelectedStation(){
        
    }
    /**
     * The station/line that is selected is highlighted.
     * @param void
     * @return void
     */
    public void highlightSelected(){
        
    }
    /**
     * The station/line that is selected is unhighlighted.
     * @param void
     * @return void
     */
    public void unhighlightSelected(){
        
    }
    /**
     * Method for reseting the data and clearing canvas for loading new work/
     * creating new work.
     * @param void
     * @return void
     */
    @Override
    public void resetData() {
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         ((MetroWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getLineComboBox().getItems().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getStationComboBox().getItems().clear();
                  ((MetroWorkspace)app.getWorkspaceComponent()).getStartStation().getItems().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getEndStation().getItems().clear();
         canvas.setBackground(Background.EMPTY);
         currentMap = new MetroMap("das");
    }
    public void resetData(String name) {
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         ((MetroWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getLineComboBox().getItems().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getStationComboBox().getItems().clear();
           ((MetroWorkspace)app.getWorkspaceComponent()).getStartStation().getItems().clear();
         ((MetroWorkspace)app.getWorkspaceComponent()).getEndStation().getItems().clear();
         currentMap = new MetroMap(name);
         canvas.setBackground(Background.EMPTY);

         app.getGUI().getWindow().setTitle("MetroMapMaker- "+name);
    }
    
    public MetroObject getSelectedObject(){
        return selectedMetroObject;
    }
    
    
    
    public void removeleft(MetroLine line){
        Pane canvas = workspace.getCanvas();
        canvas.getChildren().remove(line.leftEndCircle);
        canvas.getChildren().add(line.leftEdgeLabel);
    }
    
    public void removeright(MetroLine line){
                Pane canvas = workspace.getCanvas();
                canvas.getChildren().remove(line.rightEndCircle);
                canvas.getChildren().add(line.rightEdgeLabel);
    }
    
    public void setSelectedObject(){
        selectedMetroObject = null;
    }
    public void bombCanvas(){
        ((MetroWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
    }
    
    public void repop(){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        app.getGUI().getWindow().setTitle("MetroMapMaker- "+currentMap.getName());
        //System.out.println(currentMap.getStations().size());
        Pane canvas = workspace.getCanvas();
        for(int i = 0;i<currentMap.getStations().size();i++){
            canvas.getChildren().addAll(currentMap.getStations().get(i).stationCircle,currentMap.getStations().get(i).stationLabel);
            workspace.getStationComboBox().getItems().add(currentMap.getStations().get(i).getName());
            workspace.getStartStation().getItems().add(currentMap.getStations().get(i).getName());
            workspace.getEndStation().getItems().add(currentMap.getStations().get(i).getName());

        }
        
    }
    
    public void repop2(){
        redrawLines();
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        //System.out.println(currentMap.lines.size());
        for (int i = 0;i<currentMap.getLines().size();i++){
            workspace.getLineComboBox().getItems().add(currentMap.getLines().get(i).name);
            currentMap.getLines().get(i).getLeftEdgeLine().setStrokeWidth(currentMap.getLines().get(i).thickness);
            currentMap.getLines().get(i).getLeftEdgeLine().setStartX(currentMap.getLines().get(i).leftStartX);
            currentMap.getLines().get(i).getLeftEdgeLine().setStartY(currentMap.getLines().get(i).leftStartY);

            currentMap.getLines().get(i).leftEdgeLine.setEndX(currentMap.getLines().get(i).stationList.get(0).CircleposX);
            currentMap.getLines().get(i).leftEdgeLine.setEndY(currentMap.getLines().get(i).stationList.get(0).CircleposY);
           // currentMap.getLines().get(i).leftEdgeLine.setStartX(currentMap.getLines().get(i).stationList.get(0).CircleposX-100);
            //currentMap.getLines().get(i).leftEdgeLine.setStartY(currentMap.getLines().get(i).stationList.get(0).CircleposY);
            currentMap.getLines().get(i).leftEdgeLabel.setLayoutX(currentMap.getLines().get(i).leftEdgeLine.getStartX()-100);
            currentMap.getLines().get(i).leftEdgeLabel.setLayoutY(currentMap.getLines().get(i).leftEdgeLine.getStartY());
            currentMap.getLines().get(i).leftEndCircle.setCenterX(currentMap.getLines().get(i).leftEdgeLine.getStartX()-100);
            currentMap.getLines().get(i).leftEndCircle.setCenterY(currentMap.getLines().get(i).leftEdgeLine.getStartY());
            MetroLine linr = currentMap.getLines().get(i);
            Label leff = currentMap.getLines().get(i).leftEdgeLabel;
            Label right = currentMap.getLines().get(i).rightEdgeLabel;
            leff.setOnMousePressed(e->{
           canvas.getChildren().remove(leff);
           //line.leftEndCircle.setEffect(highlightedEffect);
           canvas.getChildren().add(linr.leftEndCircle);
          linr.leftEndCircleShowing = true;
           
       });
            canvas.getChildren().addAll(currentMap.getLines().get(i).leftEdgeLine,currentMap.getLines().get(i).leftEdgeLabel);
            currentMap.getLines().get(i).getRightEdgeLine().setStrokeWidth(currentMap.getLines().get(i).thickness);
            currentMap.getLines().get(i).getRightEdgeLine().setEndX(currentMap.getLines().get(i).rightEndX);
            currentMap.getLines().get(i).getRightEdgeLine().setEndY(currentMap.getLines().get(i).rightEndY);
            currentMap.getLines().get(i).rightEdgeLine.setStartX(currentMap.getLines().get(i).stationList.get(currentMap.getLines().get(i).stationList.size()-1).CircleposX);
            currentMap.getLines().get(i).rightEdgeLine.setStartY(currentMap.getLines().get(i).stationList.get(currentMap.getLines().get(i).stationList.size()-1).CircleposY);
            //currentMap.getLines().get(i).rightEdgeLine.setEndX(currentMap.getLines().get(i).getStartx()+100);
            //currentMap.getLines().get(i).rightEdgeLine.setEndY(currentMap.getLines().get(i).getStartY());
            currentMap.getLines().get(i).rightEdgeLabel.setLayoutX(currentMap.getLines().get(i).rightEdgeLine.getEndX()+35);
            currentMap.getLines().get(i).rightEdgeLabel.setLayoutY(currentMap.getLines().get(i).rightEdgeLine.getEndY());
            currentMap.getLines().get(i).rightEndCircle.setCenterX(currentMap.getLines().get(i).rightEdgeLine.getEndX()+35);
            currentMap.getLines().get(i).rightEndCircle.setCenterY(currentMap.getLines().get(i).rightEdgeLine.getEndY());
            right.setOnMousePressed(e->{
                canvas.getChildren().remove(right);
           //line.leftEndCircle.setEffect(highlightedEffect);
           canvas.getChildren().add(linr.rightEndCircle);
          linr.rightEndCircleShowing = true;
            });
            canvas.getChildren().addAll(currentMap.getLines().get(i).rightEdgeLine,currentMap.getLines().get(i).rightEdgeLabel);
        }
    }
    
    public void fixLineComboBox(String old,String news){
        ComboBox r = workspace.getLineComboBox();
        r.getItems().remove(old);
        r.getItems().add(news);
        r.getSelectionModel().select(news);
    }

    public void processChangeLineThickness(double value) {
        jtps.addTransaction(new changeLineThicknessTransaction(value));
    }

    public void processChangeRadius(double value) {
        jtps.addTransaction(new changeRadiusTransaction(value));
    }

    public void processChangeCanvasColor(Color newColor) {
        BackgroundFill fill = new BackgroundFill(newColor,null,null);
        Background background = new Background(fill);
        workspace.getCanvas().setBackground(background);
    }

    public void addNewLabel(MetroLandMarkText label) {
        ArrayList<MetroLandMarkText> text = currentMap.getText();
        text.add(label);
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        canvas.getChildren().add(label.getLabel());
    }

    public void removeLabel(MetroLandMarkText label) {
        ArrayList<MetroLandMarkText> text = currentMap.getText();
        text.remove(label);
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        canvas.getChildren().remove(label.getLabel());
    }

    public void repop0() {
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        ArrayList<MetroLandMarkImage> images = currentMap.getImage();
        for(int i = 0;i<images.size();i++){
            canvas.getChildren().add(images.get(i).rect);
        }
        
        
        ArrayList<MetroLandMarkText> text = currentMap.getText();
        
        for(int i = 0;i<text.size();i++){
            canvas.getChildren().add(text.get(i).getLabel());
        }
    }

    public void addImage(MetroLandMarkImage image) {
        jtps.addTransaction(new addImageTransaction(image));
    }

    public void loadTextSettings(MetroObject metroObject) {
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        if(metroObject instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) metroObject;
            int fontSize = (int)text.getFont().getSize();
            String fontFamily = text.getFont().getFamily();
            workspace.getFontSizeComboBox().getSelectionModel().select(Integer.toString(fontSize));
            workspace.getFontTypeComboBox().getSelectionModel().select(fontFamily);
            workspace.gettextColorColorPicker().setValue(text.textColor);
        }
        else if(metroObject instanceof MetroStation){
            MetroStation station = (MetroStation) metroObject;
            int fontSize = (int) station.getFont().getSize();
            String fontFamily = station.getFont().getFamily();
            workspace.getFontSizeComboBox().getSelectionModel().select(Integer.toString(fontSize));
            workspace.getFontTypeComboBox().getSelectionModel().select(fontFamily);
            workspace.gettextColorColorPicker().setValue(station.fontColor);
        }
        else if(metroObject instanceof MetroLine){
            MetroLine line = (MetroLine) metroObject;
            int fontSize = (int) line.getFont().getSize();
            String fontFamily = line.getFont().getFamily();
            workspace.getFontSizeComboBox().getSelectionModel().select(Integer.toString(fontSize));
            workspace.getFontTypeComboBox().getSelectionModel().select(fontFamily);
            workspace.gettextColorColorPicker().setValue(line.fontColor);
        }
        else{
            System.out.println("Unexpected fatal error. Your computer is crazy and may blow up!");
        }
        
        
    }

    public void processremoveel() {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkImage || object instanceof MetroLandMarkText){
            jtps.addTransaction(new removeElTransaction(object));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Delete Decor Error");
            alert.setHeaderText("Selected Object is not a decor object");
            alert.showAndWait();
        }
    }

    public void handleFontSizeChange(String value) {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) object;
            jtps.addTransaction(new metroTextSizeTransaction(text,value));
        }
        else if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new metroStationTextSizeTransaction(station,value));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            jtps.addTransaction(new metroLineTextSizeTransaction(line,value));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Change font size error");
            alert.setHeaderText("Selected Object is not applicable for font size change");
            alert.showAndWait();
        }
    }
    
    public void handleFontFamilyChange(String value){
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) object;
            jtps.addTransaction(new metroTextFamilyTransaction(text,value));
        }
        else if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new metroStationFamilyTransaction(station,value));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            jtps.addTransaction(new metroLineFamilyTransaction(line,value));
                    
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Change font size error");
            alert.setHeaderText("Selected Object is not applicable for font family change");
            alert.showAndWait();
        }
    }
    
    public void handleItalics(){
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) object;
            jtps.addTransaction(new metroTextItalicsChange(text));
        }
        else if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new metroStationItalicsChange(station));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            jtps.addTransaction(new metroLineItalicsChange(line));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Change Italics error");
            alert.setHeaderText("Selected Object is not applicable for Italics change");
            alert.showAndWait();
        }
    }

    public void handleColorChange(Color color) {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) object;
            jtps.addTransaction(new metroTextColorChange(text,color));
        }
        else if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new metroStationTextColorChange(station,color));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            jtps.addTransaction(new metroLineTextColorChange(line,color));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Change color error");
            alert.setHeaderText("Selected Object is not applicable for text color change");
            alert.showAndWait();
        }
    }

    public void getridofgrid() {
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        Color backgroundColor = currentMap.getBackgroundColor();
        BackgroundFill fill = new BackgroundFill(backgroundColor,null,null);
        Background ground = new Background(fill);
        canvas.setBackground(ground);
    }

    public void snap() {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new snapStation(station));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            
                jtps.addTransaction(new snapLineEnd(line,true));
            
            
                jtps.addTransaction(new snapLineEnd(line,false));

            
                    }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("snap error");
            alert.setHeaderText("Cannot snap this object to grid");
            alert.showAndWait();
        }
    }

    public void processmovestationlabel() {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new movestationlabelTransaction(station));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Move station label error");
            alert.setHeaderText("Cannot move this object, not a station");
            alert.showAndWait();
        }
    }

    public void processrotatestationlabel() {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new rotatestationlabelTransaction(station));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Rotate station label error");
            alert.setHeaderText("Cannot rotate this object, not a station");
            alert.showAndWait();
        }
    }

    public ArrayList<String> findpath(String startStationString, String endStationString) {
        ArrayList<String> path = new ArrayList<String>();
        
        ArrayList<MetroLine> lines = currentMap.getLines();
        MetroStation startStation = getStation(startStationString);
        MetroStation endStation = getStation(endStationString);
        for(int i = 0;i<lines.size();i++){
            ArrayList<MetroStation> stationsinLine = lines.get(i).getStation();
            if(stationsinLine.contains(startStation) && stationsinLine.contains(endStation)){
                int startStationInt = stationsinLine.indexOf(startStation);
                int endStationInt = stationsinLine.indexOf(endStation);
                int starttoend = startStationInt-endStationInt;
                int endtostart = endStationInt-startStationInt;
                path.add("Get on "+startStation.stationName+" of "+lines.get(i).name);
                if(startStationInt>endStationInt){
                    path.add("Take "+starttoend+" stops");
                }
                else{
                    path.add("Take "+endtostart+" stops");

                }
                path.add("Get off at "+endStation.stationName);
                return path;

            }
            else if(stationsinLine.contains(startStation)){
                int startStationInt = stationsinLine.indexOf(startStation);
                path.add("Get on "+startStation.stationName+" of "+lines.get(i).name);
                for (int j = startStationInt;j<stationsinLine.size();j++){
                    
                }
            }
            else{
                
            }
            
        }
        
        
        
        return path;
    }
    public MetroStation getStation(String name){
         ArrayList<MetroStation> stations = currentMap.getStations();
         for (int i = 0;i<stations.size();i++){
             if(stations.get(i).stationName.equals(name)){
                 return stations.get(i);
             }
         }
         return null;
    }
    private class rotatestationlabelTransaction implements jTPS_Transaction{
        MetroStation station;
        int oldPos;
        int newPos;

        public rotatestationlabelTransaction(MetroStation station){
            this.station = station;
            this.oldPos = station.getstationrotatepos();
            if(oldPos ==0){
                newPos =1;
            }else if(oldPos ==1){
                this.newPos =2;
            }else if(oldPos ==2){
                newPos =3;

            }
            else if(oldPos ==3){
                 newPos =0;

            }
        }
        @Override
        public void doTransaction() {
            station.setstationrotatepos(newPos);
        }

        @Override
        public void undoTransaction() {
            station.setstationrotatepos(oldPos);

        }
        
    }
    
    private class movestationlabelTransaction implements jTPS_Transaction{
        MetroStation station;
        int oldPos;
        int newPos;
        
        public movestationlabelTransaction(MetroStation station){
            this.station = station;
            this.oldPos = station.getstationlabelpos();
            if(oldPos ==0){
                newPos =1;
            }else if(oldPos ==1){
                this.newPos =2;
            }else if(oldPos ==2){
                newPos =3;

            }
            else if(oldPos ==3){
                 newPos =0;

            }
        }
        
        @Override
        public void doTransaction() {
            station.setstationlabelpos(newPos);
            
        }

        @Override
        public void undoTransaction() {
                        station.setstationlabelpos(oldPos);

        }
        
    }
    
    
    private class snapLineEnd implements jTPS_Transaction{
        MetroLine line;
        int oldX;
        int oldY;
        int newX;
        int newY;
        boolean isLeft;
        
        public snapLineEnd(MetroLine line,boolean isLeft){
            this.line = line;
            this.isLeft = isLeft;
            if(this.isLeft){
            this.oldX = (int)line.getLeftEndStartX();
            this.oldY = (int)line.getLeftEndStartY();
            int interX = this.oldX %30;
            int interY = this.oldY % 30;
            this.newX = oldX-interX;
            this.newY = oldY-interY;
            }
            else{
            this.oldX = (int)line.getRightEndEndX();
            this.oldY = (int)line.getRightEndEndY();
            int interX = this.oldX %30;
            int interY = this.oldY % 30;
            this.newX = oldX-interX;
            this.newY = oldY-interY;
            }
        }
        
        @Override
        public void doTransaction() {
            if(isLeft){
            line.drag2(newX,newY,true);
            redrawLines();
            }
            else{
            line.drag2(newX,newY,false);
            redrawLines();
            }

        }

        @Override
        public void undoTransaction() {
            if(isLeft){
            line.drag2(oldX,oldY,true);
            redrawLines();
            }
            else{
            line.drag2(oldX,oldY,false);
            redrawLines();
            }

        }
        
    }
    
    
    private class snapStation implements jTPS_Transaction{
        MetroStation station;
        int oldX;
        int oldY;
        int newX;
        int newY;
        
        public snapStation(MetroStation station){
            this.station = station;
            this.oldX = (int)this.station.CircleposX;
            this.oldY = (int) this.station.CircleposY;
            int interX = oldX%30;
            int interY = oldY%30;
            
            this.newX = oldX - interX;
            this.newY = oldY - interY;
            
            System.out.println(newX+" "+newY);
            
        }
        @Override
        public void doTransaction() {
            station.drag(newX, newY);
            redrawLines();
        }

        @Override
        public void undoTransaction() {
            station.drag(oldX, oldY);
            redrawLines();

        }
        
    }
    
    private class metroStationTextColorChange implements jTPS_Transaction{
        Color oldFill;
        Color newFill;
        MetroStation station;
        
        public metroStationTextColorChange(MetroStation station, Color color){
            this.station = station;
            this.newFill = color;
            this.oldFill = this.station.getColor();
        }
        @Override
        public void doTransaction() {
            station.setColor(newFill);
        }

        @Override
        public void undoTransaction() {
            station.setColor(oldFill);

        }
    }
    
    private class metroLineTextColorChange implements jTPS_Transaction{
        Color oldFill;
        Color newFill;
        MetroLine line;
        
        public metroLineTextColorChange(MetroLine line, Color color){
            this.line = line;
            this.newFill = color;
            this.oldFill = this.line.getColor();
        }
        @Override
        public void doTransaction() {
            line.setColor(newFill);
        }

        @Override
        public void undoTransaction() {
            line.setColor(oldFill);

        }
    }
    
    private class metroTextColorChange implements jTPS_Transaction{
        Color oldFill;
        Color newFill;
        MetroLandMarkText text;
        
        public metroTextColorChange(MetroLandMarkText text, Color color){
            this.text = text;
            this.newFill = color;
            this.oldFill = this.text.getColor();
        }
        @Override
        public void doTransaction() {
            text.setColor(newFill);
        }

        @Override
        public void undoTransaction() {
            text.setColor(oldFill);

        }
        
    }
    
    private class metroStationItalicsChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroStation station;
        public metroStationItalicsChange(MetroStation station){
            this.station = station;
            this.oldFont = this.station.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }
        }
        @Override
        public void doTransaction() {
            station.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            station.setFont(oldFont);
        }
    }
    private class metroLineItalicsChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLine line;
        public metroLineItalicsChange(MetroLine line){
            this.line = line;
            this.oldFont = this.line.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }
        }
        @Override
        public void doTransaction() {
            line.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            line.setFont(oldFont);
        }
    }
    
    private class metroTextItalicsChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLandMarkText text;
        public metroTextItalicsChange(MetroLandMarkText text){
            this.text = text;
            this.oldFont = this.text.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }
        }
        @Override
        public void doTransaction() {
            text.setFont(newFont);
            text.text.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            text.setFont(oldFont);
            text.text.setFont(oldFont);
        }
        
    }
    

    public void handleBold() {
        MetroObject object = getSelectedObject();
        if(object instanceof MetroLandMarkText){
            MetroLandMarkText text = (MetroLandMarkText) object;
            jtps.addTransaction(new metroTextBoldChange(text));
        }
        else if(object instanceof MetroStation){
            MetroStation station = (MetroStation) object;
            jtps.addTransaction(new metroStationBoldChange(station));
        }
        else if(object instanceof MetroLine){
            MetroLine line = (MetroLine) object;
            jtps.addTransaction(new metroLineBoldChange(line));
        }
        else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Change bold error");
            alert.setHeaderText("Selected Object is not applicable for bold change");
            alert.showAndWait();
        }
    }
    
    private class metroStationBoldChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroStation station;
        
        public metroStationBoldChange(MetroStation station){
            this.station = station;
            this.oldFont = this.station.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }
        }
        
        @Override
        public void doTransaction() {
            station.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            station.setFont(oldFont);
        }
    }
    
    private class metroLineBoldChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLine line;
        
        public metroLineBoldChange(MetroLine line){
            this.line = line;
            this.oldFont = this.line.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }
        }
        
        @Override
        public void doTransaction() {
            line.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            line.setFont(oldFont);
        }
    }
    
    private class metroTextBoldChange implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLandMarkText text;
        public metroTextBoldChange(MetroLandMarkText text){
            this.text = text;
            this.oldFont = this.text.getFont();
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }
        }
        @Override
        public void doTransaction() {
            text.setFont(newFont);
            text.text.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            text.setFont(oldFont);
            text.text.setFont(oldFont);
        }
    }
    
    private class metroStationFamilyTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroStation station;
        
        public metroStationFamilyTransaction(MetroStation station,String value){
            this.station = station;
            this.oldFont = station.getFont();
            String values = value;
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }
        }

        @Override
        public void doTransaction() {
            station.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            station.setFont(oldFont);
        }
        
    }
    
    private class metroLineFamilyTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLine line;
        
        public metroLineFamilyTransaction(MetroLine line, String value){
            this.line = line;
            this.oldFont = line.getFont();
            String values = value;
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }
        }

        @Override
        public void doTransaction() {
            line.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
                        line.setFont(oldFont);

        }
        
    }
    
    private class metroTextFamilyTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLandMarkText text;
        
        public metroTextFamilyTransaction(MetroLandMarkText text, String value){
            this.text = text;
            this.oldFont = this.text.getFont();
            String values = value;
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.REGULAR,oldFont.getSize());
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(values,FontWeight.NORMAL,FontPosture.ITALIC,oldFont.getSize());

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.REGULAR,oldFont.getSize());

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(values,FontWeight.BOLD,FontPosture.ITALIC,oldFont.getSize());

            }
        }

        @Override
        public void doTransaction() {
            text.setFont(newFont);
            text.text.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            text.setFont(oldFont);
            text.text.setFont(oldFont);
        }
    }
    
    private class metroStationTextSizeTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroStation station;
        
        public metroStationTextSizeTransaction(MetroStation station, String value){
            this.station = station;
            this.oldFont = this.station.getFont();
            double size = Double.parseDouble(value);
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,size);
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,size);

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,size);

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,size);

            }
        }

        @Override
        public void doTransaction() {
            station.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            station.setFont(oldFont);
        }
        
    }
    private class metroLineTextSizeTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLine line;
        
        public metroLineTextSizeTransaction (MetroLine line,String value){
            this.line = line;
            this.oldFont = line.getFont();
            double size = Double.parseDouble(value);
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,size);
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,size);

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,size);

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,size);

            }
        }
        
        @Override
        public void doTransaction() {
            line.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            line.setFont(oldFont);

        }
        
    }
    
    private class metroTextSizeTransaction implements jTPS_Transaction{
        Font oldFont;
        Font newFont;
        MetroLandMarkText text;
        
        public metroTextSizeTransaction(MetroLandMarkText text, String value){
            this.text = text;
            this.oldFont = this.text.getFont();
            double size = Double.parseDouble(value);
            String fontStyle = oldFont.getStyle();
            if(fontStyle.equals("Regular")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.REGULAR,size);
            }else if(fontStyle.equals("Italic")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.NORMAL,FontPosture.ITALIC,size);

            }else if(fontStyle.equals("Bold")){
                this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.REGULAR,size);

            }else if(fontStyle.equals("Bold Italic")){
               this.newFont = Font.font(oldFont.getFamily(),FontWeight.BOLD,FontPosture.ITALIC,size);

            }
        }
        
        @Override
        public void doTransaction() {
            text.setFont(newFont);
            text.text.setFont(newFont);
        }

        @Override
        public void undoTransaction() {
            text.setFont(oldFont);
            text.text.setFont(oldFont);
        }
        
    }
    
    
    private class removeElTransaction implements jTPS_Transaction{
        MetroObject deletedObject;
        public removeElTransaction(MetroObject object){
            this.deletedObject = object;
        }
        @Override
        public void doTransaction() {
            if(deletedObject instanceof MetroLandMarkText){
                MetroLandMarkText delete = (MetroLandMarkText) deletedObject;
                currentMap.text.remove(delete);
                workspace = (MetroWorkspace) app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                canvas.getChildren().remove(delete.text);
            }
            else if(deletedObject instanceof MetroLandMarkImage){
                MetroLandMarkImage delete = (MetroLandMarkImage) deletedObject;
                currentMap.images.remove(delete);
                workspace = (MetroWorkspace) app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                canvas.getChildren().remove(delete.rect);
            }
        }

        @Override
        public void undoTransaction() {
            if(deletedObject instanceof MetroLandMarkText){
                MetroLandMarkText add = (MetroLandMarkText) deletedObject;
                currentMap.text.add(add);
                workspace = (MetroWorkspace) app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                canvas.getChildren().add(add.text);
            }
            else if(deletedObject instanceof MetroLandMarkImage){
                MetroLandMarkImage add = (MetroLandMarkImage) deletedObject;
                currentMap.images.add(add);
                workspace = (MetroWorkspace) app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                canvas.getChildren().add(add.rect);
            }
        }
        
    }
    private class addImageTransaction implements jTPS_Transaction{
        MetroLandMarkImage image;
        
        private addImageTransaction(MetroLandMarkImage image) {
            this.image = image;
        }
        
        
        @Override
        public void doTransaction() {
            currentMap.images.add(image);
            workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            canvas.getChildren().add(image.rect);
            System.out.println(currentMap.images.size());
        }

        @Override
        public void undoTransaction() {
            currentMap.images.remove(image);
            workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            canvas.getChildren().remove(image.rect);
                        System.out.println("hmm");

        }
        
    }
    
    private class changeRadiusTransaction implements jTPS_Transaction{
        MetroStation change;
        double oldThickness;
        double newThickness;
        
        public changeRadiusTransaction(double value){
            MetroStation station = getStationFromComboBox();
            this.oldThickness = station.getStationRadius();
            this.newThickness = value;
            this.change = station;
        }

        @Override
        public void doTransaction() {
            change.getCircle().setRadius(newThickness);
            change.setStationRadius(newThickness);
            change.setstationlabelpos(change.getstationlabelpos());
        }

        @Override
        public void undoTransaction() {
            change.getCircle().setRadius(oldThickness);
            change.setStationRadius(oldThickness);
                        change.setstationlabelpos(change.getstationlabelpos());


        }
        
    }
    
    private class changeLineThicknessTransaction implements jTPS_Transaction{
        MetroLine change;
        double oldThickness;
        double newThickness;
        
        private changeLineThicknessTransaction(double value) {
            MetroLine line = getLineFromLineComboBox();
            this.oldThickness = line.getthickness();
            this.newThickness = value;
            this.change = line;
        }

        
        public void doTransaction() {
            change.setthickness(newThickness);
            redrawLines();
            
        }

        @Override
        public void undoTransaction() {
            change.setthickness(oldThickness);
            redrawLines();
        }
        
    }
    
    public void fixEditLineButton(){
        BackgroundFill fill = new BackgroundFill(getLineFromLineComboBox().lineColor,null,null);
        Background background = new Background(fill);
        workspace.getLineColorPicker().setBackground(background);
    }
    
    public void fixStationButton(){
                BackgroundFill fill = new BackgroundFill(getStationFromComboBox().stationCircleColor,null,null);
                        Background background = new Background(fill);
                workspace.getStationColorPicker().setBackground(background);


    }
    
    public void setBackgroundImage(Image image,String absolutePath){
        jtps.addTransaction(new setBackgroundImage(image,absolutePath));
    }
    
    private class setBackgroundImage implements jTPS_Transaction{
        String oldPath;
        ImageView oldImage;
        String newPath;
        ImageView image;
        
        public setBackgroundImage(Image image, String absolutePath){
            workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            this.oldPath = currentMap.getAbsolutePath();
            this.oldImage = currentMap.getBackgroundImage();
            this.image = new ImageView(image);
            this.newPath = absolutePath;
        }

        
        @Override
        public void doTransaction() {
            currentMap.setAbsolutePath(newPath);
            currentMap.setBackgroundImage(image);
            workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            canvas.getChildren().remove(oldImage);
            canvas.getChildren().add(image);
            image.toBack();
            
            
        }

        @Override
        public void undoTransaction() {
            currentMap.setAbsolutePath(oldPath);
            currentMap.setBackgroundImage(oldImage);
            workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            canvas.getChildren().remove(image);
            if(oldImage!=null){
            canvas.getChildren().add(oldImage);
            oldImage.toBack();
            }
            
        }
        
    }
    public void repopbackgroundImage(ImageView image){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            canvas.getChildren().add(image);
            image.toBack();
    }
    
    public Stage getStage(){
        return app.getGUI().getWindow();
    }
    public Pane getCanvas(){
        workspace = (MetroWorkspace) app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
            return canvas;
    }
    
}
