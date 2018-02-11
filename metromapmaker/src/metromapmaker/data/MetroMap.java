
package metromapmaker.data;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This is the container class for our 'map'. It serves for file I/O purposes
 * and path finding purposes.
 * @author seanfang
 */
public class MetroMap {
    ArrayList<MetroLine> lines;
    ArrayList<MetroStation> stations;
    ArrayList<MetroLandMarkImage> images;
    ArrayList<MetroLandMarkText> text;
    String name;
    String absolutePath;
    boolean isCircular = false;
    Color backgroundColor;
    ImageView backgroundImage;
    /**
     * Constructor
     * @param name 
     * @return MetroMap
     */
    public MetroMap(String name){
        this.name = name;
        this.lines = new ArrayList<MetroLine>();
        this.stations = new ArrayList<MetroStation>();
        this.images = new ArrayList<MetroLandMarkImage>();
        this.text = new ArrayList<MetroLandMarkText>();
        this.backgroundColor = Color.WHITE;
        this.backgroundImage = null;
        this.absolutePath = "";
    }
    
    public String getName(){
        return this.name;
    }
    
    public void addLine(MetroLine line){
        this.lines.add(line); //
    }
    
    public void removeLine(MetroLine line){
        this.lines.remove(line);
    }
    
    public void addStation(MetroStation station){
        this.stations.add(station); 
    }
    
    public void removeStation(MetroStation station){
        this.stations.remove(station); 
        
    }
    
    public void addImage(){
        
    }
    
    public void removeImage(){
        
    }
    
    public void addText(){
        
    }
    
    public void removeText(){
        
    }
    
    public ArrayList<MetroLine> getLines(){
        return this.lines;
    }
    
    public ArrayList<MetroStation> getStations(){
        return this.stations;
    }
    
    public ArrayList<MetroLandMarkImage> getImage(){
        return this.images;
    }
    
    public ArrayList<MetroLandMarkText> getText(){
        return this.text;
    }
    
    public Color getBackgroundColor(){
        return this.backgroundColor;
    }
    public void setBackgroundColor(Color color){
        this.backgroundColor = color;
    }
    
    public ImageView getBackgroundImage(){
        return this.backgroundImage;
    }
    public void setBackgroundImage(ImageView backgroundImage){
        this.backgroundImage = backgroundImage;
    }
    
    public String getAbsolutePath(){
        return this.absolutePath;
    }
    public void setAbsolutePath(String value){
        this.absolutePath = value;
    }
    
    
    
    
    
    // I don't actually know if this works
    public MetroMap clone(){
        MetroMap clone = new MetroMap(this.name);
        clone.lines = new ArrayList<MetroLine>();
        clone.stations = new ArrayList<MetroStation>();
        clone.images = new ArrayList<MetroLandMarkImage>();
        clone.text = new ArrayList<MetroLandMarkText>();
        
        for(int i=0;i<this.lines.size();i++){
            clone.lines.add(this.lines.get(i));
            //System.out.println("dsa");
        }
        for(int i=0;i<this.stations.size();i++){
            clone.stations.add(this.stations.get(i));
        }
        return clone;
    }
    
}
