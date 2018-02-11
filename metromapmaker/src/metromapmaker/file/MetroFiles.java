
package metromapmaker.file;

import metromapmaker.data.MetroData;
import metromapmaker.data.MetroLandMarkImage;
import metromapmaker.data.MetroLandMarkText;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroStation;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import metromapmaker.data.MetroMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.String.format;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javax.imageio.ImageIO;
import javax.json.JsonString;


/**
 * This class serves as the file management component for this application providing
 * I/O services
 * @author seanfang
 */
public class MetroFiles implements AppFileComponent {
    String JSON_BG_COLOR;
    String JSON_RED;
    String JSON_ALPHA;
    String JSON_STATIONS;
    String JSON_LINES;
    String JSON_TYPE;
    String JSON_X;
    String JSON_Y;
    String JSON_FILL_COLOR;
    String JSON_THICKNESS;
    String JSON_TEXT;
    String JSON_FONT;
    private String JSON_GREEN;
    private String JSON_BLUE;

    
    /**
     * This method is for saving user work, which is maps and its contained
     * members in this case.
     * @param data
     * @param filePath 
     * @throws IOException Thrown if there is an error writing out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath ) throws IOException{
       MetroData dataManager = (MetroData) data;
       MetroMap currentMap = dataManager.getCurrentMap();
       //our JSON array builder
       JsonArrayBuilder linearrayBuilder = Json.createArrayBuilder();
       JsonArrayBuilder linestationarrayBuilder = Json.createArrayBuilder();
       JsonArrayBuilder stationarrayBuilder = Json.createArrayBuilder();
       JsonArrayBuilder textArrayBuilder = Json.createArrayBuilder();

       JsonArrayBuilder imagesArrayBuilder = Json.createArrayBuilder();
       //Oth lets save the name of the map.
       String mapName = currentMap.getName();
       //map background  color
       JsonObject mapColor = makeJsonColorObject(currentMap.getBackgroundColor());
       
       //map background image url if there is one
       String backgroundImagePath = currentMap.getAbsolutePath();
       
       
       //Now lets save the lines
       for (int i=0;i<currentMap.getLines().size();i++){
           MetroLine curLine = currentMap.getLines().get(i);
           String lineName = curLine.getLineName();
           boolean circular = false;
           JsonObject lineColorJson = makeJsonColorObject(curLine.getLineColor());
           JsonObject lineTextColorJson = makeJsonColorObject(curLine.getColor());
           Font textFont = curLine.getFont();
           String fontfamily = textFont.getFamily();
           String fontstyle = textFont.getStyle();
           double fontSize = textFont.getSize();
           double thickness = curLine.getthickness();
           double leftStartX = curLine.getLeftEndStartX();
           double leftStartY = curLine.getLeftEndStartY();
           double rightEndX = curLine.getRightEndEndX();
           double rightEndY = curLine.getRightEndEndY();
           ArrayList <MetroStation> stations = curLine.getStation();
           linestationarrayBuilder = Json.createArrayBuilder();
           for(int j=0;j<stations.size();j++){
               MetroStation stationInLine = stations.get(j);
               String stationName = stationInLine.getName();
               JsonObject stationInLines = Json.createObjectBuilder()
                       .add("name",stationName).build();
               linestationarrayBuilder.add(stationInLines);
               
                       }
           JsonArray stationsArray = linestationarrayBuilder.build();
           JsonObject object  = Json.createObjectBuilder()
                       .add("name",lineName)
                       .add("color:", lineColorJson)
                       .add("thickness", thickness)
                       .add("leftStartX", leftStartX)
                       .add("leftStartY", leftStartY)
                       .add("rightEndX", rightEndX)
                       .add("rightEndY", rightEndY)
                       .add("linetextcolor",lineTextColorJson)
                       .add("font-family",fontfamily)
                       .add("font-style",fontstyle)
                       .add("font-size",fontSize)
                       .add("station_names", stationsArray).build();
          linearrayBuilder.add(object);
       }
       JsonArray lineArray = linearrayBuilder.build();
       //Now lets save the stations
       
       ArrayList<MetroStation> stationList = currentMap.getStations();
       for (int j=0;j<stationList.size();j++){
           MetroStation curStation = stationList.get(j);
           String stationName = curStation.getName();
           Color stationColor = curStation.getStationColor();
           JsonObject stationColorObject = makeJsonColorObject(stationColor);
           JsonObject stationTextColorObject = makeJsonColorObject(curStation.getColor());
           Font textFont = curStation.getFont();
           String fontfamily = textFont.getFamily();
           String fontstyle = textFont.getStyle();
           double fontSize = textFont.getSize();
           int stationlabelpos = curStation.getstationlabelpos();
           int stationrotationpos = curStation.getstationrotatepos();
           double stationRadius = curStation.getStationRadius();
           double posX = curStation.getCircle().getCenterX();
           double posY = curStation.getCircle().getCenterY();
           JsonObject tobeaddedS = Json.createObjectBuilder()
                   .add("name",stationName)
                   .add("color",stationColorObject)
                   .add("radius",stationRadius)
                   .add("labelpos",stationlabelpos)
                   .add("rotationpos",stationrotationpos)
                   .add("stationtextcolor",stationTextColorObject)
                   .add("font-family",fontfamily)
                   .add("font-style",fontstyle)
                   .add("font-size",fontSize)
                   .add("x", posX)
                   .add("y",posY).build();
           stationarrayBuilder.add(tobeaddedS);
       };
       JsonArray sttt = stationarrayBuilder.build();
       
       //Now lets save our wonderful text
       ArrayList<MetroLandMarkText> textList = currentMap.getText();
       for(int q = 0;q<textList.size();q++){
           MetroLandMarkText text = textList.get(q);
           String textName = text.getName();
           Color textColor = text.getColor();
           JsonObject textColorObject = makeJsonColorObject(textColor);
           Font textFont = text.getFont();
           String fontfamily = textFont.getFamily();
           String isBold = textFont.getStyle();
           double fontSize = textFont.getSize();
           double startX = text.getPosX();
           double startY = text.getPosY();
           double endX = text.getEndX();
           double endY = text.getEndY();
           
           
           JsonObject tobeaddedT = Json.createObjectBuilder()
                   .add("text",textName)
                   .add("text-color",textColorObject)
                   .add("font-family",fontfamily)
                   .add("fontstyle",isBold)
                   .add("fontSize",fontSize)
                   .add("startX",startX)
                   .add("startY",startY)
                   .add("endX",endX)
                   .add("endY",endY).build();
           
           textArrayBuilder.add(tobeaddedT);
       };
       JsonArray textArr = textArrayBuilder.build();
       
       //Now lets finally save our images
       ArrayList<MetroLandMarkImage> images = currentMap.getImage();
       for(int i = 0;i<images.size();i++){
           MetroLandMarkImage image = images.get(i);
           String imagePath = image.path();
           double posX = image.posX();
           double posY = image.posY();
           
           JsonObject tobeaddedI = Json.createObjectBuilder()
                   .add("imagePath", imagePath)
                   .add("posX",posX)
                   .add("posY",posY).build();
           imagesArrayBuilder.add(tobeaddedI);
       };
       JsonArray imageArr = imagesArrayBuilder.build();
       
       
       //make the JSON Object
       JsonObject dataManagerJSO = Json.createObjectBuilder()
               .add("name", mapName)
               .add("background-color",mapColor)
               .add("background-image",backgroundImagePath)
               .add("text",textArr)
               .add("image",imageArr)
               .add("lines",lineArray)
               .add("stations",sttt).build();
       
       //Write it out to file
       Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath+".json");
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath+".json");
	pw.write(prettyPrinted);
	pw.close();
        
        
       
    }
    /**
     * This method is for exporting the user's work as an image to the filepath
     * specified.
     * @param data
     * @param filePath 
     * @return void
     * @throws IOException Thrown if there is an error writing out data to the file.
     */
    public void exportData(AppDataComponent data, String filePath) throws IOException{
       String filePath2 = filePath.concat("/image.png");
       String filePath3 = filePath.concat("/jsonfile.json");
       File dir = new File(filePath);
       dir.mkdir();
       Path dirpath = Paths.get(filePath2);
       Path dirpath2 = Paths.get(filePath3);
       MetroData dataManager = (MetroData) data;
       //lets write out the image first
       WritableImage snapShot = dataManager.getCanvas().snapshot(null, null);
       BufferedImage bImage = SwingFXUtils.fromFXImage(snapShot, null);
       File file = new File(filePath+".png");
       ImageIO.write(bImage,"png" , file);
       Path imgPath = file.toPath();
       Files.move(imgPath, dirpath, StandardCopyOption.REPLACE_EXISTING);

       
       //Now lets write our Export specific JSON
       MetroMap currentMap = dataManager.getCurrentMap();
       JsonArrayBuilder linearrayBuilder = Json.createArrayBuilder();
       JsonArrayBuilder linestationarrayBuilder = Json.createArrayBuilder();
       JsonArrayBuilder stationarrayBuilder = Json.createArrayBuilder();
       
       //first lets get the name of the map
       String mapName = currentMap.getName();
       //background color
       JsonObject mapColor = makeJsonColorObject(currentMap.getBackgroundColor());
       //Now lets save the lines
       for(int i = 0;i<currentMap.getLines().size();i++){
           MetroLine curLine = currentMap.getLines().get(i);
           String lineName = curLine.getLineName();
           boolean circular = false;
           JsonObject lineColorJson = makeJsonColorObject(curLine.getLineColor());
           ArrayList <MetroStation> stations = curLine.getStation();
           linestationarrayBuilder = Json.createArrayBuilder();
           for(int j = 0;j<stations.size();j++){
               linestationarrayBuilder.add(stations.get(j).getName());
           }
           JsonArray stationsArray = linestationarrayBuilder.build();
           JsonObject object  = Json.createObjectBuilder()
                   .add("name", lineName)
                   .add("circular",circular)
                   .add("color",lineColorJson)
                   .add("station_names",stationsArray).build();
           linearrayBuilder.add(object);
       }
       JsonArray lineArray = linearrayBuilder.build();
       
       ArrayList<MetroStation> stationList = currentMap.getStations();
       for(int i = 0;i<stationList.size();i++){
           MetroStation curStation = stationList.get(i);
           String stationName = curStation.getName();
           double posX = curStation.getCircle().getCenterX();
           double posY = curStation.getCircle().getCenterY();
           JsonObject tobeaddedS = Json.createObjectBuilder()
                   .add("name", stationName)
                   .add("x",posX)
                   .add("y",posY).build();
           stationarrayBuilder.add(tobeaddedS);
       }
       JsonArray sttt = stationarrayBuilder.build();
       
       JsonObject dataManagerJSO = Json.createObjectBuilder()
               .add("name", mapName)
               .add("lines", lineArray)
               .add("stations",sttt).build();
       
        Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
        
        OutputStream os = new FileOutputStream(filePath+".json");
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath+".json");
	pw.write(prettyPrinted);
	pw.close();
       Path jsonPath = Paths.get(filePath+".json");
       Files.move(jsonPath, dirpath2, StandardCopyOption.REPLACE_EXISTING);
        
    }
    private JsonObject makeJsonColorObject(Color color) {
	JsonObject colorJson = Json.createObjectBuilder()
		.add("red", color.getRed())
		.add("green", color.getGreen())
		.add("blue", color.getBlue())
		.add("alpha", color.getOpacity()).build();
	return colorJson;
    }
    /**
     * This method loads data from a JSON formatted file into the data management
     * component and then forces updating of the workspace such that the user
     * may edit the data.
     * @param data
     * @param filePath 
     * @return void
     * @throws IOException Thrown if there is an error loading data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath ) throws IOException {
        
        MetroData datas = (MetroData) data;
        //bomb the canvas!!!
        datas.setCurrentMap(null);
        
       
        JsonObject json = loadJSONFile(filePath);
        String mapName = getDataAsString(json,"name");
        Color mapBackgroundColor = loadColor(json,"background-color");
        String mapBackgroundPath = getDataAsString(json,"background-image");
        datas.setCurrentMap(new MetroMap(mapName));
        datas.getCurrentMap().setBackgroundColor(mapBackgroundColor);
        datas.processChangeCanvasColor(datas.getCurrentMap().getBackgroundColor());
        // BOMB THAT CANVAS!!!
        datas.bombCanvas();
        
        if(!mapBackgroundPath.isEmpty()){
            datas.getCurrentMap().setAbsolutePath(mapBackgroundPath);
            File copybackgroundImage = new File(mapBackgroundPath);

            Image image = new Image(copybackgroundImage.toURI().toURL().toExternalForm());
            ImageView imageView = new ImageView(image);
            datas.getCurrentMap().setBackgroundImage(imageView);
            datas.repopbackgroundImage(imageView);
        }
        
        //lets neg 1 our loading by loading back the fucking images.
        JsonArray jsonImageArray = json.getJsonArray("image");
        for(int i = 0;i<jsonImageArray.size();i++){
            JsonObject imageJsonObject = jsonImageArray.getJsonObject(i);
            String path = getDataAsString(imageJsonObject,"imagePath");
            double posX = getDataAsDouble(imageJsonObject,"posX");
            double posY = getDataAsDouble(imageJsonObject,"posY");
            
            File file = new File(path);
            Image image = new Image(file.toURI().toURL().toExternalForm());
            MetroLandMarkImage images = new MetroLandMarkImage(image,path);
            images.setPosX(posX);
            images.setPosY(posY);
            datas.getCurrentMap().getImage().add(images);
        }
        
        
        // Lets zero our loading by loading back the fucking text.
        
        JsonArray jsonTextArray = json.getJsonArray("text");
        for(int i = 0;i<jsonTextArray.size();i++){
            JsonObject textJsonObject = jsonTextArray.getJsonObject(i);
            String textString = getDataAsString(textJsonObject,"text");
            Color textColor = loadColor(textJsonObject,"text-color");
            String fontfamily = getDataAsString(textJsonObject,"font-family");
            String fontstyle = getDataAsString(textJsonObject, "fontstyle");
            double fontSize = getDataAsDouble(textJsonObject,"fontSize");
            double startX = getDataAsDouble(textJsonObject,"startX");
            double startY = getDataAsDouble(textJsonObject,"startY");
            double endX = getDataAsDouble(textJsonObject,"endX");
            double endY = getDataAsDouble(textJsonObject,"endY");
            
            MetroLandMarkText text = new MetroLandMarkText(textString);
            text.setColor(textColor);
            text.setPosX(startX);
            text.setPoxY(startY);
            text.setEndX(endX);
            text.setEndY(endY);
            text.getLabel().setLayoutX(text.getPosX());
            text.getLabel().setLayoutY(text.getPosY());
            if(fontstyle.equals("Regular")){
                text.getLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.REGULAR,fontSize));
                text.setFont(text.getLabel().getFont());
            }
            else if(fontstyle.equals("Italic")){
                text.getLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.ITALIC,fontSize));
                text.setFont(text.getLabel().getFont());
            }
            else if(fontstyle.equals("Bold")){
                text.getLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.REGULAR,fontSize));
                text.setFont(text.getLabel().getFont());
            }
            else if(fontstyle.equals("Bold Italic")){
                text.getLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.ITALIC,fontSize));
                text.setFont(text.getLabel().getFont());
            }
            
            datas.getCurrentMap().getText().add(text);
            
            
        }
        datas.repop0();
        
        
        
        
        //Lets start our wonderful journey by reloading the stations
        JsonArray jsonStationArray = json.getJsonArray("stations");
        for (int i = 0;i<jsonStationArray.size();i++){
            JsonObject stationJsonObject = jsonStationArray.getJsonObject(i);
            String stationName = getDataAsString(stationJsonObject,"name");
            Color stationColor = loadColor(stationJsonObject,"color");
            double labelPos =  getDataAsDouble(stationJsonObject,"labelpos");
            double rotationpos = getDataAsDouble(stationJsonObject,"rotationpos");
            int lp = (int) labelPos;
            int rp = (int) rotationpos;
            double stationRadius = getDataAsDouble(stationJsonObject,"radius");
            double posX =  getDataAsDouble(stationJsonObject,"x");
            double posY = getDataAsDouble(stationJsonObject,"y");
            Color textColor = loadColor(stationJsonObject,"stationtextcolor");
            String fontfamily = getDataAsString(stationJsonObject,"font-family");
            String fontstyle = getDataAsString(stationJsonObject,"font-style");
            double fontSize = getDataAsDouble(stationJsonObject,"font-size");
            MetroStation newStation = new MetroStation(stationName);
            newStation.setStationColor(stationColor);
            newStation.setStationRadius(stationRadius);
            newStation.getCircle().setRadius(newStation.getStationRadius());
            newStation.getCircle().setFill(newStation.getStationColor());
            newStation.drag((int)posX, (int)posY);
            newStation.setstationlabelpos(lp);
            newStation.setstationrotatepos(rp);
            
            if(fontstyle.equals("Regular")){
                newStation.getStationLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.REGULAR,fontSize));
                newStation.setFont(newStation.getStationLabel().getFont());
            }
            else if(fontstyle.equals("Italic")){
                newStation.getStationLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.ITALIC,fontSize));
                newStation.setFont(newStation.getStationLabel().getFont());
            }
            else if(fontstyle.equals("Bold")){
                newStation.getStationLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.REGULAR,fontSize));
                newStation.setFont(newStation.getStationLabel().getFont());
            }
            else if(fontstyle.equals("Bold Italic")){
                newStation.getStationLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.ITALIC,fontSize));
                newStation.setFont(newStation.getStationLabel().getFont());
            }
            newStation.setColor(textColor);
            
            datas.getCurrentMap().addStation(newStation);
            
        }
       // lets add all the stations back on screen first
       datas.repop();
        
        // Next lets continue our wonderful journey by loading in the lines
        
        JsonArray jsonLineArray = json.getJsonArray("lines");
        for (int i = 0;i<jsonLineArray.size();i++){
            JsonObject lineJsonObject = jsonLineArray.getJsonObject(i);
            String lineName = getDataAsString(lineJsonObject,"name");
            double thickness = getDataAsDouble(lineJsonObject,"thickness");
            double leftStartX = getDataAsDouble(lineJsonObject, "leftStartX");
            double leftStartY = getDataAsDouble(lineJsonObject,"leftStartY");
            double rightEndX = getDataAsDouble(lineJsonObject,"rightEndX");
            double rightEndY = getDataAsDouble(lineJsonObject,"rightEndY");
            Color lineColor = loadColor(lineJsonObject, "color:");
            Color textColor = loadColor(lineJsonObject,"linetextcolor");
            String fontfamily = getDataAsString(lineJsonObject,"font-family");
            String fontstyle = getDataAsString(lineJsonObject,"font-style");
            double fontSize = getDataAsDouble(lineJsonObject,"font-size");
            
            
            MetroLine line = new MetroLine(lineName,lineColor);
            line.setLeftStartX(leftStartX);
            line.setLeftStartY(leftStartY);
            line.setRightEndX(rightEndX);
            line.setRightEndY(rightEndY);
            line.setthickness(thickness);
            
            if(fontstyle.equals("Regular")){
                line.getLeftEdgeLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.REGULAR,fontSize));
                line.setFont(line.getLeftEdgeLabel().getFont());
            }
            else if(fontstyle.equals("Italic")){
                line.getLeftEdgeLabel().setFont(Font.font(fontfamily,FontWeight.NORMAL,FontPosture.ITALIC,fontSize));
                line.setFont(line.getLeftEdgeLabel().getFont());
            }
            else if(fontstyle.equals("Bold")){
                line.getLeftEdgeLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.REGULAR,fontSize));
                line.setFont(line.getLeftEdgeLabel().getFont());
            }
            else if(fontstyle.equals("Bold Italic")){
                line.getLeftEdgeLabel().setFont(Font.font(fontfamily,FontWeight.BOLD,FontPosture.ITALIC,fontSize));
                line.setFont(line.getLeftEdgeLabel().getFont());
            }
            line.setColor(textColor);
            
            
            JsonArray jsonLineStationArray = lineJsonObject.getJsonArray("station_names");
            for(int j=0;j<jsonLineStationArray.size();j++){
                JsonObject stationInLine = jsonLineStationArray.getJsonObject(j);
                String stationName = getDataAsString(stationInLine,"name");
                ArrayList<MetroStation> stations = datas.getCurrentMap().getStations();
                for(int k = 0;k<stations.size();k++){
                    if(stations.get(k).getName().equals(stationName)){
                        line.addStationToLine(stations.get(k));
                    }
                }
                
            }
            datas.getCurrentMap().addLine(line);
            
        }
        datas.repop2();
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, "red");
	double green = getDataAsDouble(jsonColor, "green");
	double blue = getDataAsDouble(jsonColor, "blue");
	double alpha = getDataAsDouble(jsonColor, "alpha");
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }

    
    private Double getDataAsDouble(JsonObject json, String dataName){
        JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private String getDataAsString(JsonObject json, String dataName){
        JsonValue value = json.get(dataName);
        JsonString string = (JsonString) value;
        String getString = string.getString();
        return string.getString();
    }
    
    private MetroLine loadLine(JsonObject json){
        return null;
    }
    
    private MetroStation loadStation(JsonObject json){
        return null;
    }
    
    private MetroLandMarkImage loadImage(JsonObject json){
        return null;
    }
    
    private MetroLandMarkText loadText(JsonObject json){
        return null;
    }
    
    private MetroMap loadMap(JsonObject json){
        return null;
    }
    
    
    
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	FileInputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    /**
     * Not used.
     * @param data
     * @param filePath
     * @throws IOException 
     * @return void
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
