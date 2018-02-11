/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.data;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author seanfang
 */
public class MetroLandMarkText extends MetroObject {
    Text text;
    Font font;
    double labelX;
    double labelY;
    double labelXEnd;
    double labelYEnd;
    Color textColor;
    
    public MetroLandMarkText(String value){
        this.text = new Text();
        text.setText(value);
        text.setFont(Font.font("Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 24));
        this.font = text.getFont();
        
        this.labelX = 200;
        this.labelY = 200;
        this.text.setLayoutX(this.labelX);
        this.text.setLayoutY(this.labelY);
        
       // System.out.println(text.getLayoutX());
       // System.out.println(text.getLayoutY());
        //text.setX(50);
        //text.setY(50);
        this.labelXEnd = this.labelX  +text.getBoundsInLocal().getWidth();
        this.labelYEnd = this.labelY -text.getBoundsInLocal().getHeight();
        this.textColor = Color.BLACK;
        this.text.setFill(textColor);

    }
    
    
    
    /**
     * This method allows the decor text to be dragged.
     * @param x
     * @param y 
     * @return void
     */
    public void drag(int x, int y){
        double diffX = x - labelX;
        double diffY = y - labelY;
        double newX = labelX + diffX;
        double newY = labelY + diffY;
        this.text.setLayoutX(newX);
        this.text.setLayoutY(newY);
        this.labelX = x;
        this.labelY = y;
        this.labelXEnd = this.labelX  +text.getBoundsInLocal().getWidth();
        this.labelYEnd = this.labelY -text.getBoundsInLocal().getHeight();
        this.text.toFront();
    }
    /**
     * This helper method allows the decor text's location and size to be set.
     * @param x
     * @param y 
     * @return void
     */
    public void setLocationAndSize(int x, int y){
        
    }
    
    public String getName(){
        return this.text.getText();
    }
    public void setText(String x){
        this.text.setText(x);
    }
    public Text getLabel(){
        return this.text;
    }
    public double getPosX(){
        return this.labelX;
    }
    public double getPosY(){
        return this.labelY;
    }
    public double getEndX(){
        return this.labelXEnd;
    }
    public double getEndY(){
        return this.labelYEnd;
    }
    public Font getFont(){
        return this.font;
    }
    public void setFont(Font font){
        this.font = font;
        this.text.setFont(this.font);
    }
    public void setPosX(double value){
        this.labelX = value;
    }
    public void setPoxY(double value){
        this.labelY = value;
    }
    public void setEndX(double value){
        this.labelXEnd = value;
    }
    public void setEndY(double value){
        this.labelYEnd = value;
    }
    public Color getColor(){
        return this.textColor;
    }
    public void setColor(Color color){
        this.textColor = color;
        this.text.setFill(this.textColor);
    }
    
}
