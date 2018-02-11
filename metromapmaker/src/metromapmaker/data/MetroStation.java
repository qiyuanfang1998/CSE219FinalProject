/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *This class is the class used to represent Metro Stations for our map making
 * application.
 * @author seanfang
 */
public class MetroStation extends MetroObject {
   // MetroData data;
    Circle stationCircle;
    Label stationLabel;
    String stationName;
    double CircleposX;
    double CircleposY;
    double labelPosX;
    double labelPosY;
    Color stationCircleColor;
    double stationRadius;
    int stationlabelpos;
    int stationrotatepos;
    Font font;
    Color fontColor;
    
    public MetroStation(String name) {
        this.stationName = name;
        this.stationLabel = new Label(this.stationName);
        this.stationCircle = new Circle();
        stationCircle.setRadius(15);
        this.stationRadius = 15;
        stationCircle.setStrokeWidth(15);
        stationCircle.setCenterX(200);
        stationCircle.setCenterY(200);
        this.CircleposX = 200;
        this.CircleposY = 200;
        stationLabel.setLayoutX(CircleposX+20);
        stationLabel.setLayoutY(CircleposY-15);
        this.labelPosX = CircleposX+20;
        this.labelPosY = CircleposY-100;
        stationCircleColor = Color.WHITE;
        this.stationCircle.setFill(stationCircleColor);
        this.stationCircle.setStroke(Color.BLACK);
        this.stationCircle.setStrokeWidth(5);
        this.stationCircle.toFront();
        this.stationLabel.toFront();
        this.stationlabelpos = 0;
        this.stationrotatepos = 0;
        this.stationLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
        this.font = stationLabel.getFont();
        this.fontColor = Color.BLACK;
        stationLabel.setTextFill(this.fontColor);
    }
    
    
    /**
     * This method is intended for the user to be able to drag the station.
     * @param x
     * @param y 
     * @return void
     */
    public void drag(int x, int y){
        double diffX = x- CircleposX;
        double diffY = y - CircleposY;
        double newX = CircleposX+diffX;
        double newY = CircleposY+diffY;
        this.stationCircle.setCenterX(newX);
        this.stationCircle.setCenterY(newY);
        this.CircleposX = x;
        this.CircleposY = y;
        if(stationlabelpos ==0){
        stationLabel.setLayoutX(CircleposX+stationRadius);
        stationLabel.setLayoutY(CircleposY-stationRadius);
        this.labelPosX = CircleposX+stationRadius;
        this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==1){
            stationLabel.setLayoutX(CircleposX+stationRadius);
            stationLabel.setLayoutY(CircleposY+stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==2){
            stationLabel.setLayoutX(CircleposX-stationRadius);
            stationLabel.setLayoutY(CircleposY+stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==3){
            stationLabel.setLayoutX(CircleposX-stationRadius);
            stationLabel.setLayoutY(CircleposY-stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
        //System.out.println(this.CircleposX);
        //System.out.println(this.CircleposY);
        this.stationCircle.toFront();
        this.stationLabel.toFront();
        
    }
    /**
     * This method is intended for the user to resize the station and change its location.
     * @param x
     * @param y 
     * @return void
     */
    
    public void setLocationAndSize(int x, int y){
        
    }
    
    public void setName(String x){
        this.stationName = x;
        this.stationLabel = new Label(x);
    }
    
    public String getName(){
        return this.stationName;
    }
    public Circle getCircle(){
        return this.stationCircle;
    }
    public Label getStationLabel(){
        return this.stationLabel;
    }
    
    public Color getStationColor(){
        return this.stationCircleColor;
    }
    public void setStationColor(Color color){
        this.stationCircleColor = color;
    }
    public double getStationRadius(){
        return this.stationRadius;
    }
    public void setStationRadius(double value){
        this.stationRadius = value;
    }
    public void setstationlabelpos(int value){
        this.stationlabelpos = value;
        if(stationlabelpos ==0){
        stationLabel.setLayoutX(CircleposX+stationRadius);
        stationLabel.setLayoutY(CircleposY-stationRadius);
        this.labelPosX = CircleposX+stationRadius;
        this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==1){
            stationLabel.setLayoutX(CircleposX+stationRadius);
            stationLabel.setLayoutY(CircleposY+stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==2){
            stationLabel.setLayoutX(CircleposX-stationRadius);
            stationLabel.setLayoutY(CircleposY+stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
        else if(stationlabelpos ==3){
            stationLabel.setLayoutX(CircleposX-stationRadius);
            stationLabel.setLayoutY(CircleposY-stationRadius);
            this.labelPosX = CircleposX+stationRadius;
            this.labelPosY = CircleposY-stationRadius;
        }
    }
    public int getstationlabelpos(){
        return this.stationlabelpos;
    }
    public void setstationrotatepos(int value){
        this.stationrotatepos = value;
        if(stationrotatepos==0){
            stationLabel.setRotate(0);
        }else if(stationrotatepos ==1){
            stationLabel.setRotate(90);

        }else if(stationrotatepos ==2){
            stationLabel.setRotate(180);

        }else if(stationrotatepos ==3){
            stationLabel.setRotate(270);

        }
        
    }
    public int getstationrotatepos(){
        return this.stationrotatepos;
    }
    
    public Font getFont(){
        return this.font;
    }
    public void setFont(Font font){
        this.font = font;
        this.stationLabel.setFont(this.font);
    }
    public Color getColor(){
        return this.fontColor;
    }
    public void setColor(Color color){
        this.fontColor = color;
        this.stationLabel.setTextFill(this.fontColor);

    }
    
}
