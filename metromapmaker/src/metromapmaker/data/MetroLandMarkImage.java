
package metromapmaker.data;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * This serves as the class for decor images
 * @author seanfang
 */
public class MetroLandMarkImage extends MetroObject {
    Rectangle rect;
    ImagePattern image;
    String path;
    double posX;
    double posY;
    /**
     * This method allows the decor image to be dragged.
     * @param x
     * @param y 
     * @return void
     */
    
    public MetroLandMarkImage(Image image,String path){
        this.image = new ImagePattern(image);
        this.path = path;
        this.rect = new Rectangle();
        rect.setFill(this.image);
        this.posX = 300;
        this.posY = 300;
        this.rect.setX(this.posX);
        this.rect.setY(this.posY);
        rect.setWidth(image.getWidth());
        rect.setHeight(image.getHeight());
        

    }
    
    
    public void drag(int x, int y){
        double diffX = x - posX;
        double diffY = y - posY;
        double newX = posX + diffX;
        double newY = posY + diffY;
        this.rect.setX(newX);
        this.rect.setY(newY);
        this.posX = x;
        this.posY = y;
        this.rect.toFront();
    }
    /**
     * This helper method allow sthe decor image's location and size to be set.
     * @param x
     * @param y 
     * @return void
     */
    public void setLocationAndSize(int x, int y){
        
    }
    
    public ImagePattern getImage(){
        return this.image;
    }
    
    public String path(){
        return this.path;
    }
    public double posX(){
    return this.posX;
    }
    public double posY(){
        return this.posY;
    }
    public void setPosX(double posX){
        this.posX = posX;
        this.rect.setX(this.posX);
    }
    public void setPosY(double posY){
        this.posY = posY;
        this.rect.setY(this.posY);

    }
    
    //?
    public void setImage(ImagePattern image){
        this.image = image;
    }
    public void setPath(String path){
        this.path = path;
    }
}
