
package metromapmaker.data;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * This class serves as our Metro Map's lines.
 * @author seanfang
 */
public class MetroLine extends MetroObject {
    ArrayList<MetroStation> stationList;
    ArrayList<Line> lineList;
    Color lineColor;
    int stationCount;
    Line leftEdgeLine;
    Label leftEdgeLabel;
    Line rightEdgeLine;
    Label rightEdgeLabel;
    String name;
    int stationAmnt;
    int strokewidth;
    Circle leftEndCircle ;
    Circle rightEndCircle;
    boolean leftEndCircleShowing;
    boolean rightEndCircleShowing;
    double thickness;
    double leftStartX;
    double leftStartY;
    double rightEndX;
    double rightEndY;
    Font font;
    Color fontColor;
    
    public MetroLine(String name, Color color){
        
        leftEndCircleShowing = false;
        rightEndCircleShowing = false;
        
        
        this.stationList = new ArrayList<MetroStation>();
        strokewidth = 15;
        this.name = name;
        this.lineColor = color;
        this.fontColor = Color.BLACK;
        this.stationAmnt = 0;
        
        this.leftEdgeLine = new Line();
        leftEdgeLine.setStartX(200);
        leftEdgeLine.setStartY(0);
        leftEdgeLine.setEndX(leftEdgeLine.getStartX()+100);
        leftEdgeLine.setEndY(leftEdgeLine.getStartY());
        leftEdgeLine.setStrokeWidth(strokewidth);
        leftEdgeLine.setStroke(lineColor);
        
        this.leftEdgeLabel= new Label(this.name);
        leftEdgeLabel.setLayoutX(leftEdgeLine.getStartX()-100);
        leftEdgeLabel.setLayoutY(0);
        leftEdgeLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        
        this.rightEdgeLine = new Line();
        rightEdgeLine.setStartX(leftEdgeLine.getEndX());
        rightEdgeLine.setEndX(leftEdgeLine.getEndX()+100);
        rightEdgeLine.setStartY(0);
        rightEdgeLine.setEndY(0);
        rightEdgeLine.setStrokeWidth(strokewidth);
        rightEdgeLine.setStroke(lineColor);
        
        this.rightEdgeLabel= new Label(this.name);
        rightEdgeLabel.setLayoutX(rightEdgeLine.getEndX()+35);
        rightEdgeLabel.setLayoutY(0);
        rightEdgeLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        this.font = rightEdgeLabel.getFont();
        
        
        leftEndCircle = new Circle();
        leftEndCircle.setRadius(15);
        leftEndCircle.setStrokeWidth(15);
        leftEndCircle.setFill(Color.YELLOW);
        leftEndCircle.setCenterX(leftEdgeLine.getStartX()-100);
        leftEndCircle.setCenterY(leftEdgeLine.getStartY());
        rightEndCircle = new Circle();
        rightEndCircle.setRadius(15);
        rightEndCircle.setStrokeWidth(15);
        rightEndCircle.setFill(Color.YELLOW);
        rightEndCircle.setCenterX(strokewidth);
        rightEndCircle.setCenterX(rightEdgeLine.getEndX()+35);
        rightEndCircle.setCenterY(rightEdgeLine.getEndY());
        
        this.thickness = 15;
        
    }
    public Line getLeftEdgeLine(){
        return leftEdgeLine;
    }
    public Line getRightEdgeLine(){
        return rightEdgeLine;
    }
    public Label getLeftEdgeLabel(){
        return leftEdgeLabel;
    }
    public Label getRightEdgeLabel(){
        return rightEdgeLabel;
    }
    public String getLineName(){
        return this.name;
    }
    public Color getLineColor(){
        return this.lineColor;
    }
    public void addStationToLine(MetroStation station){
        this.stationList.add(station);
        stationAmnt++;
    }
    public void removeStationFromLine(MetroStation station){
        this.stationList.remove(station);
        stationAmnt--;
    }
    
    public void setLineName(String name){
        this.name = name;
    }
    public void setLineColor(Color color){
        this.lineColor = color;
    }
    
    
    
    
    /**
     * This method is intended for the user to be able to drag the line's end points.
     * @param x
     * @param y 
     * @return void
     */
    public void drag(int x, int y){
        if(leftEndCircleShowing){
            double diffX = x- leftEndCircle.getCenterX();
        double diffY = y - leftEndCircle.getCenterY();
        double newX = leftEndCircle.getCenterX()+diffX;
        double newY = leftEndCircle.getCenterY()+diffY;
        this.leftEndCircle.setCenterX(newX);
        this.leftEndCircle.setCenterY(newY);
        this.leftEdgeLabel.setLayoutX(newX);
        this.leftEdgeLabel.setLayoutY(newY);
        this.leftEdgeLine.setStartX(newX+100);
        this.leftEdgeLine.setStartY(newY);
        }
        else if(rightEndCircleShowing){
            double diffX = x- rightEndCircle.getCenterX();
        double diffY = y - rightEndCircle.getCenterY();
        double newX = rightEndCircle.getCenterX()+diffX;
        double newY = rightEndCircle.getCenterY()+diffY;
        this.rightEndCircle.setCenterX(newX);
        this.rightEndCircle.setCenterY(newY);
        this.rightEdgeLabel.setLayoutX(newX);
        this.rightEdgeLabel.setLayoutY(newY);
        this.rightEdgeLine.setEndX(newX-35);
        this.rightEdgeLine.setEndY(newY);
        }
    }
    
    public void drag2(int x, int y, boolean isLeft){
        if(isLeft){
            double diffX = x- leftEndCircle.getCenterX();
        double diffY = y - leftEndCircle.getCenterY();
        double newX = leftEndCircle.getCenterX()+diffX;
        double newY = leftEndCircle.getCenterY()+diffY;
        this.leftEndCircle.setCenterX(newX);
        this.leftEndCircle.setCenterY(newY);
        this.leftEdgeLabel.setLayoutX(newX);
        this.leftEdgeLabel.setLayoutY(newY);
        this.leftEdgeLine.setStartX(newX+100);
        this.leftEdgeLine.setStartY(newY);
        }
        else{
            double diffX = x- rightEndCircle.getCenterX();
        double diffY = y - rightEndCircle.getCenterY();
        double newX = rightEndCircle.getCenterX()+diffX;
        double newY = rightEndCircle.getCenterY()+diffY;
        this.rightEndCircle.setCenterX(newX);
        this.rightEndCircle.setCenterY(newY);
        this.rightEdgeLabel.setLayoutX(newX);
        this.rightEdgeLabel.setLayoutY(newY);
        this.rightEdgeLine.setEndX(newX-35);
        this.rightEdgeLine.setEndY(newY);
        }
    }
    
    
    /**
     * This method is intended for the user to resize the line and change its location.
     * @param x
     * @param y 
     * @return void
     */
    
    public void setLocationAndSize(int x, int y){
        
    }
    /**
     * Helper method for setting the new draggable end lines.
     * @param void
     * @return void
     */
    public void setNewEndLines(){
        
    }
    /**
     * Helper method for redrawing lines after station or line drag.
     * @param void
     * @return void
     */
    public void redrawLines(){
        
    }
    
    public boolean getLeft(){
        return this.leftEndCircleShowing;
    }
    public boolean getRight(){
        return this.rightEndCircleShowing;
    }
    
    public void setLeft(){
        this.leftEndCircleShowing = false;
    }
    
    public void setRight(){
        this.rightEndCircleShowing = false;
    }
    
    public ArrayList<MetroStation> getStation (){
        return this.stationList;
    }
    public double getStartx(){
        return this.rightEdgeLine.getStartX();
    }
    public double getStartY(){
        return this.rightEdgeLine.getStartY();
    }
    
    public double getthickness(){
        return this.thickness;
    }
    
    public void setthickness(double thickness){
        this.thickness = thickness;
    }
    
    public double getLeftEndStartX(){
        return this.leftEdgeLine.getStartX();
    }
    public double getLeftEndStartY(){
        return this.leftEdgeLine.getStartY();
    }
    public double getRightEndEndX(){
        return this.rightEdgeLine.getEndX();
    }
    public double getRightEndEndY(){
        return this.rightEdgeLine.getEndY();
    }
    
    public void setLeftStartX(double value){
        this.leftStartX = value;
    }
    public void setLeftStartY(double value){
        this.leftStartY = value;
    }
    public void setRightEndX(double value){
        this.rightEndX = value;
    }
    public void setRightEndY(double value){
        this.rightEndY = value;
    }
    public Font getFont(){
        return this.font;
    }
    public void setFont(Font font){
        this.font = font;
        this.leftEdgeLabel.setFont(this.font);
        this.rightEdgeLabel.setFont(this.font);
    }
    public Color getColor(){
        return this.fontColor;
    }
    public void setColor(Color color){
        this.fontColor = color;
        this.leftEdgeLabel.setTextFill(this.fontColor);
        this.rightEdgeLabel.setTextFill(this.fontColor);

    }
    
    
    
}
