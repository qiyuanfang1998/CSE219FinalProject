package metromapmaker;

import java.util.Locale;
import metromapmaker.data.MetroData;
import metromapmaker.file.MetroFiles;
import metromapmaker.gui.MetroWorkspace;
import djf.AppTemplate;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class serves as the application class for our goLogoLoApp program. 
 * Note that much of its behavior is inherited from AppTemplate, as defined in
 * the Desktop Java Framework. This app starts by loading all the app-specific
 * messages like icon files and tooltips and other settings, then the full 
 * User Interface is loaded using those settings. Note that this is a 
 * JavaFX application.
 * 
 * @author Qi Yuan Fang
 * @version 1.0
 */
public class MetroApp extends AppTemplate {
     /**
     * This hook method must initialize all three components in the
     * proper order ensuring proper dependencies are respected, meaning
     * all proper objects are already constructed when they are needed
     * for use, since some may need others for initialization.
     * @param: void
     * @return: void
     */
    
    public boolean isNewWork = false;
    public int workload = 0;
    String initName = "";
    
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL THREE COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, AND THE DATA COMPONENT NEEDS THE
        // FILE COMPONENT SO WE MUST BE CAREFUL OF THE ORDER
        
        fileComponent = new MetroFiles();
        dataComponent = new MetroData(this);
        
        Image logo = new Image("https://preview.ibb.co/jU822b/Screen_Shot_2017_11_12_at_5_03_55_PM.png");
        

        Image center = new Image("https://fcw.com/~/media/GIG/FCWNow/Topics/Networks/transitmap_370.jpg");
        ImageView centerIV = new ImageView(center);
        ImageView logoWelcomeScreen = new ImageView(logo);
        ImageView ss = new ImageView(logo);

        
        BorderPane welcomeBorderPane = new BorderPane();
        VBox leftWelcomeVBox = new VBox(15);
        Label recentWorkLabel = new Label("Recent Work");
        recentWorkLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        

        
        
        Hyperlink recentWork1 = new Hyperlink("Example 1");
        Hyperlink recentWork2 = new Hyperlink("Example 2");
        Hyperlink recentWork3 = new Hyperlink("Example 3");
        Hyperlink recentWork4 = new Hyperlink("Example 4");
        Hyperlink recentWork5 = new Hyperlink("Example 5");
        Hyperlink recentWork6 = new Hyperlink("Example 6");
        leftWelcomeVBox.getChildren().addAll(recentWorkLabel, recentWork1,recentWork2,recentWork3,recentWork4,recentWork5,recentWork6);
        welcomeBorderPane.setLeft(leftWelcomeVBox);
        StackPane stack = new StackPane();
        stack.getChildren().add(centerIV);
        welcomeBorderPane.setCenter(stack);
        ButtonType createNew = new ButtonType("Create New Metro Map", ButtonBar.ButtonData.OK_DONE);
        
        Alert welcomeAlert = new Alert(Alert.AlertType.INFORMATION, "hola", createNew);
        welcomeAlert.getDialogPane().setContent(welcomeBorderPane);
        recentWork1.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 1;});
        recentWork2.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 2;});
        recentWork3.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 3;});
        recentWork4.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 4;});
        recentWork5.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 5;});
        recentWork6.setOnAction(e->{welcomeAlert.close(); isNewWork = true; workload = 6;});
        
        ButtonType cc = new ButtonType("Create Map with This Name", ButtonBar.ButtonData.OK_DONE);
        Alert setNewName = new Alert(Alert.AlertType.INFORMATION,"LOL",cc);
        setNewName.setTitle("Please enter a new name for your map");
        setNewName.setHeaderText("Map Name");
        HBox newNameHBox = new HBox(10);
        TextField textfield = new TextField();
        newNameHBox.getChildren().add(textfield);
        setNewName.setGraphic(ss);
        setNewName.getDialogPane().setContent(newNameHBox);
           
        
        welcomeAlert.setGraphic(logoWelcomeScreen);
        welcomeAlert.setTitle("Welcome to the Metro Map Maker");
        welcomeAlert.setHeaderText("Choose Recent Work or New Map");
        welcomeAlert.showAndWait().ifPresent(response->{if(response == createNew){setNewName.showAndWait().ifPresent(r->{if(r==cc){this.initName = textfield.getText();}}); isNewWork = true;}});
        
        
        workspaceComponent = new MetroWorkspace(this);
        if(isNewWork){
            this.getGUI().getWindow().setTitle("MetroMapMaker- " +initName);
            this.getGUI().getWindow().show();
            this.getGUI().getFileController().handleNewRequest();
            MetroWorkspace r = (MetroWorkspace) this.workspaceComponent;
            r.setCurrentMap(initName);
            MetroData d = (MetroData) this.dataComponent;
            d.setCurrentMap(r.getCurrentMap());
            if(d.getCurrentMap()==null){
                System.out.println("ds");
            }
            if(workload ==1){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 1.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }else if(workload ==2){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 2.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(workload ==3){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 3.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(workload ==4){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 4.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(workload ==5){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 5.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(workload ==6){
                try {
                    this.fileComponent.loadData(dataComponent, "./work/Example 6.json");
                } catch (IOException ex) {
                    Logger.getLogger(MetroApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    /**
     * Getter method
     * @param void
     * @return initName
     */
    public String getInitName(){
        return this.initName;
    }
    
    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     * @param args
     * @return void
     */
    public static void main(String[] args) {
	Locale.setDefault(Locale.US);
	launch(args);
    }
}