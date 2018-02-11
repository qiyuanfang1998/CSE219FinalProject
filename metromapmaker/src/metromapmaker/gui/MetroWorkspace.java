/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metromapmaker.gui;

/**
 *This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 * @author seanfang
 */

import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.controller.AppFileController;
import static djf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static djf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static djf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static djf.settings.AppPropertyType.WORK_FILE_EXT;
import static djf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static djf.settings.AppStartupConstants.PATH_WORK;
//import djf.ui.AppDialogs;
import djf.ui.AppGUI;
import static djf.ui.AppGUI.CLASS_FILE_BUTTON;
import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import static metromapmaker.css.metrostyle.CLASS_COLOR_CHOOSER_CONTROL;
import static metromapmaker.css.metrostyle.CLASS_EDIT_TOOLBAR;
import static metromapmaker.css.metrostyle.CLASS_EDIT_TOOLBAR_ROW;
import static metromapmaker.metroLanguageProperty.ABOUT_ICON;
import static metromapmaker.metroLanguageProperty.ABOUT_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ADDLINE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ADDLINE_TOOL_ICON;
import static metromapmaker.metroLanguageProperty.ADDSTATIONTOLINE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ADDSTATION_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ARROWIN_ICON;
import static metromapmaker.metroLanguageProperty.ARROWIN_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ARROWOUT_ICON;
import static metromapmaker.metroLanguageProperty.ARROWOUT_TOOLTIP;
import static metromapmaker.metroLanguageProperty.BOLD_ICON;
import static metromapmaker.metroLanguageProperty.BOLD_TOOLTIP;
import static metromapmaker.metroLanguageProperty.DIRECTION_ICON;
import static metromapmaker.metroLanguageProperty.DIRECTION_TOOLTIP;
import static metromapmaker.metroLanguageProperty.EXPORT_ICON;
import static metromapmaker.metroLanguageProperty.EXPORT_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ITALICS_ICON;
import static metromapmaker.metroLanguageProperty.ITALICS_TOOLTIP;
import static metromapmaker.metroLanguageProperty.LIST_ICON;
import static metromapmaker.metroLanguageProperty.LIST_STATIONSINLINE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.MAGMINUS_ICON;
import static metromapmaker.metroLanguageProperty.MAGMINUS_TOOLTIP;
import static metromapmaker.metroLanguageProperty.MAGPLUS_ICON;
import static metromapmaker.metroLanguageProperty.MAGPLUS_TOOLTIP;
import static metromapmaker.metroLanguageProperty.REDO_ICON;
import static metromapmaker.metroLanguageProperty.REDO_TOOLTIP;
import static metromapmaker.metroLanguageProperty.REMOVELINE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.REMOVELINE_TOOL_ICON;
import static metromapmaker.metroLanguageProperty.REMOVESTATION_TOOLTIP;
import static metromapmaker.metroLanguageProperty.REMOVE_ICON;
import static metromapmaker.metroLanguageProperty.REMOVE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.ROTATE_ICON;
import static metromapmaker.metroLanguageProperty.ROTATE_TOOLTIP;
import static metromapmaker.metroLanguageProperty.UNDO_ICON;
import static metromapmaker.metroLanguageProperty.UNDO_TOOLTIP;
import metromapmaker.gui.CanvasController;
import metromapmaker.gui.MapEditorController;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import metromapmaker.data.MetroData;
import metromapmaker.data.MetroLandMarkImage;
import metromapmaker.data.MetroLine;
import metromapmaker.data.MetroMap;
import metromapmaker.data.MetroObject;
import metromapmaker.data.MetroState;
import metromapmaker.data.MetroStation;
import static metromapmaker.metroLanguageProperty.SAVEAS_ICON;
import static metromapmaker.metroLanguageProperty.SAVEAS_TOOLTIP;
import properties_manager.PropertiesManager;

public class MetroWorkspace extends AppWorkspaceComponent {
    //UI Components
    AppTemplate app;
    AppGUI gui;
    Pane canvas;
    ScrollPane scrollCanvas;
    GridPane gridCanvas;
    //Controllers
    CanvasController canvasController;
    MapEditorController mapEditorController;
    //Debug text
    Text debugText;
    //File I/O Toolbar UI
    Button newButton;
    Button loadButton;
    Button saveButton;
    Button saveAsButton;
    Button exportButton;
    Button undoButton;
    Button redoButton;
    Button aboutButton;
    FlowPane fileFlowPane;
    FlowPane undoRedoFlowPane;
    FlowPane aboutFlowPane;
    HBox aggregateTopToolBarPane;
    VBox editToolbar;
    HBox row1Bar;
    GridPane row1Grid;
    Label metroLinesLabel;
    ComboBox linesComboBox;
    Button linesColorPicker;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLineButton;
    Button removeStationFromLineButton;
    Button listStationButton;
    Slider lineThicknessSlider;
    HBox row2Bar;
    GridPane row2Grid;
    Label metroStationLabel;
    ComboBox metroStationComboBox;
    Button metroStationColorPicker;
    Button addStationButton;
    Button removeStationButton;
    Button snapButton;
    Button moveLabelButton;
    Button rotateButton;
    Slider metroStationSlider;
    HBox row3Bar;
    GridPane row3Grid;
    ComboBox startStationComboBox;
    ComboBox endStationComboBox;
    Button findPathButton;
    HBox row4Bar;
    GridPane row4Grid;
    Label decorLabel;
    Button decorColorPicker;
    Button setBackgroundImageButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElButton;
    HBox row5Bar;
    GridPane row5Grid;
    Button boldButton;
    Button italicsButton;
    ColorPicker textColorColorPicker;
    ComboBox fontSizeComboBox;
    ComboBox fontTypeComboBox;
    HBox row6Bar;
    GridPane row6GridPane;
    Button plusZoomButton;
    Button minusZoomButton;
    Button plusMapSizeButton;
    Button minusMapSizeButton;
    CheckBox showGridToggleButton;
    Dialog welcomeDialog;
    BorderPane welcomeDialogPane;
    VBox leftWelcomePane;
    Label recentWorkLabel;
    GridPane centerWorkPane;
    TextFlow centerCreateNewMetroMap;
    GridPane snapGridPane;
    boolean isNewWork = false;
    boolean warned = false;
    
    MetroMap currentMap;
    
    /**
     * Constructor for initializing the workspace. This will fully
     * setup the workspace user interface for use.
     * @param initApp
     * @throws IOException thrown should there be an error loading data for the
     * UI.
     * @return MetroWorkspace
     */
    public MetroWorkspace(AppTemplate initApp){
        app = initApp;
        gui = app.getGUI();
        saveAsButton = gui.initChildButton(gui.getFileToolbar(), SAVEAS_ICON.toString(), SAVEAS_TOOLTIP.toString(), workspaceActivated);
        exportButton =  gui.initChildButton(gui.getFileToolbar(), EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), workspaceActivated);
        undoButton = gui.initChildButton(gui.getUndoRedoToolbar(), UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), workspaceActivated);
        redoButton = gui.initChildButton(gui.getUndoRedoToolbar(), REDO_ICON.toString(), REDO_TOOLTIP.toString(), workspaceActivated);
        aboutButton = gui.initChildButton(gui.getAboutMeToolbar(), ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), workspaceActivated);
        Image logo = new Image("https://preview.ibb.co/jU822b/Screen_Shot_2017_11_12_at_5_03_55_PM.png");
        gui.getWindow().getIcons().add(logo);

        
        
        
        initLayout();
        initControllers();
        initStyle();
        
        
        
        
    }
    public void setCurrentMap(String x){
        this.currentMap = new MetroMap(x);
    }
    
    public MetroMap getCurrentMap(){
        return this.currentMap;
    }
    public Pane getCanvas(){
        return canvas;
    }
    
    
    
    /**
     * Note that this is for displaying text during development.
     * @param text
     * @return void
     */
    public void setDebugText(String text){
        
    }
    /**
     * Helper settup method used in MetroWorkSpace constructor.
     * Purpose: layout of UI elements setup.
     * @param void
     * @return void
     */
    public void initLayout(){
        // left side of workspace
        editToolbar = new VBox();
        
        //Row 1
        row1Bar = new HBox();
        VBox row1VBox = new VBox(10);
        HBox row1_1Bar = new HBox(10);
        HBox row1_2Bar = new HBox(10);
        HBox row1_3Bar = new HBox(10);
        
        metroLinesLabel = new Label("Metro Lines");
        metroLinesLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        linesComboBox = new ComboBox();
        linesColorPicker = new Button("Edit Line");
        addLineButton = gui.initChildButton(row1_2Bar, ADDLINE_TOOL_ICON.toString(), ADDLINE_TOOLTIP.toString(), false);
        removeLineButton = gui.initChildButton(row1_2Bar, REMOVELINE_TOOL_ICON.toString(), REMOVELINE_TOOLTIP.toString(), false);
        addStationToLineButton = new Button("Add\nStation");
        Tooltip addStationtolinebuttontooltip = new Tooltip("Add Station to line");
        addStationToLineButton.setTooltip(addStationtolinebuttontooltip);
        removeStationFromLineButton = new Button("Remove\nStation");
        Tooltip removestationfromlinebutontooltip = new Tooltip("Remove Station from line");
        removeStationFromLineButton.setTooltip(removestationfromlinebutontooltip);
        row1_2Bar.getChildren().addAll(addStationToLineButton, removeStationFromLineButton);
        row1_1Bar.getChildren().addAll(metroLinesLabel, linesComboBox, linesColorPicker);
        
        listStationButton = gui.initChildButton(row1_2Bar, LIST_ICON.toString(), LIST_STATIONSINLINE_TOOLTIP.toString(), false);
        lineThicknessSlider = new Slider();
        row1_3Bar.getChildren().addAll(lineThicknessSlider);
        row1VBox.getChildren().addAll(row1_1Bar,row1_2Bar,row1_3Bar);
        row1Bar.getChildren().add(row1VBox);
         
        // Row2 
        row2Bar = new HBox();
        VBox row2VBox = new VBox(10);
        HBox row2_1Bar = new HBox(10);
        HBox row2_2Bar = new HBox(10);
        HBox row2_3Bar = new HBox(10);
        metroStationLabel = new Label("Metro Stations");
        metroStationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        metroStationComboBox = new ComboBox();
        metroStationColorPicker = new Button("Change Station Color");
        row2_1Bar.getChildren().addAll(metroStationLabel, metroStationComboBox, metroStationColorPicker);
        addStationButton = gui.initChildButton(row2_2Bar, ADDLINE_TOOL_ICON.toString(), ADDSTATION_TOOLTIP.toString(), false);
        removeStationButton = gui.initChildButton(row2_2Bar, REMOVELINE_TOOL_ICON.toString(), REMOVESTATION_TOOLTIP.toString(), false);
        snapButton = new Button("Snap\nStations");
        Tooltip snapButtonToolTip = new Tooltip("Snap stations to grid");
        snapButton.setTooltip(snapButtonToolTip);
        moveLabelButton = new Button("Move\nLabel");
        Tooltip moveLabelButtonLabel = new Tooltip("Move Label");
        moveLabelButton.setTooltip(moveLabelButtonLabel);
        row2_2Bar.getChildren().addAll(snapButton, moveLabelButton);
        rotateButton =gui.initChildButton(row2_2Bar, ROTATE_ICON.toString(), ROTATE_TOOLTIP.toString(), false);
        metroStationSlider = new Slider();
        row2_3Bar.getChildren().add(metroStationSlider);
        row2VBox.getChildren().addAll(row2_1Bar,row2_2Bar,row2_3Bar);
        row2Bar.getChildren().add(row2VBox);
        
        //Row3
         row3Bar = new HBox(10);
         VBox row3_1 = new VBox(10);
         VBox row3_2 = new VBox(10);
         startStationComboBox = new ComboBox();
            endStationComboBox = new ComboBox();
            row3_1.getChildren().addAll(startStationComboBox,endStationComboBox);
            findPathButton =gui.initChildButton(row3_2, DIRECTION_ICON.toString(), DIRECTION_TOOLTIP.toString(), false);
            row3Bar.getChildren().addAll(row3_1,row3_2);
        
        //Row4
        row4Bar = new HBox(10);    
        VBox row4VBox = new VBox(10);
        HBox row4_1 = new HBox(10);  
        HBox row4_2 = new HBox(10);
        
        decorLabel = new Label("Decor");
        decorLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        decorColorPicker = new Button("Set Background Color");
        row4_1.getChildren().addAll(decorLabel, decorColorPicker);
        
        
        
         
         
          setBackgroundImageButton = new Button("Set Image\nBackground");
          Tooltip setBcktt = new Tooltip("Set Image Background");
          setBackgroundImageButton.setTooltip(setBcktt);
        addImageButton = new Button("Add\nImage");
        Tooltip imgBcktt = new Tooltip("Add Image");
        addImageButton.setTooltip(setBcktt);
        addLabelButton = new Button("Add\nLabel");
        addLabelButton.setTooltip(new Tooltip("Add Label"));
        removeElButton = new Button("Remove\nElement");
        removeElButton.setTooltip(new Tooltip("Remove Element"));
        row4_2.getChildren().addAll(setBackgroundImageButton, addImageButton,addLabelButton,removeElButton);
        row4VBox.getChildren().addAll(row4_1,row4_2);
        row4Bar.getChildren().add(row4VBox);
        
      //Row 5
        row5Bar = new HBox(10);
        VBox row5VBox = new VBox(10);
        HBox row5_1 = new HBox(10);
        HBox row5_2 = new HBox(10);
        Label fontLabel = new Label("Font");
        fontLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        textColorColorPicker = new ColorPicker();
        row5_1.getChildren().addAll(fontLabel, textColorColorPicker);
        boldButton = gui.initChildButton(row5_1, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), false);
        italicsButton = gui.initChildButton(row5_1, ITALICS_ICON.toString(), ITALICS_TOOLTIP.toString(), false);
        fontSizeComboBox = new ComboBox();
        fontSizeComboBox.getItems().addAll("8","12","14","16","18","24","48");
        fontTypeComboBox = new ComboBox();
        fontTypeComboBox.getItems().addAll("Times New Roman","Verdana","Arial","Courier");
        row5_2.getChildren().addAll(boldButton, italicsButton, fontSizeComboBox, fontTypeComboBox );
        row5VBox.getChildren().addAll(row5_1,row5_2);
        row5Bar.getChildren().add(row5VBox);
        
        //Row 6
        row6Bar = new HBox(10);
        VBox row6VBox = new VBox(10);
        HBox row6_1 = new HBox(10);
        HBox row6_2 = new HBox(10);
        
        Label navigationLabel = new Label("Navigation");
        navigationLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        showGridToggleButton = new CheckBox("Show Grid");
        row6_1.getChildren().addAll(navigationLabel, showGridToggleButton);
        
        plusZoomButton = gui.initChildButton(row6_2, MAGPLUS_ICON.toString(), MAGPLUS_TOOLTIP.toString(), false);
        minusZoomButton = gui.initChildButton(row6_2, MAGMINUS_ICON.toString(), MAGMINUS_TOOLTIP.toString(), false);
        plusMapSizeButton = gui.initChildButton(row6_2, ARROWIN_ICON.toString(), ARROWIN_TOOLTIP.toString(), false);
        minusMapSizeButton = gui.initChildButton(row6_2, ARROWOUT_ICON.toString(), ARROWOUT_TOOLTIP.toString(), false);
        
       row6VBox.getChildren().addAll(row6_1,row6_2);
       row6Bar.getChildren().add(row6VBox);
        
        
        
      
        // Get rows of ediToolBar
         editToolbar.getChildren().add(row1Bar);
         editToolbar.getChildren().add(row2Bar);
         editToolbar.getChildren().add(row3Bar);
         editToolbar.getChildren().add(row4Bar);
         editToolbar.getChildren().add(row5Bar);
         editToolbar.getChildren().add(row6Bar);
         
         canvas = new Pane();
         canvas.setMinHeight(2000);
         canvas.setMinWidth(2000);
         //gridCanvas = new GridPane();
         //gridCanvas.getChildren().add(canvas);
         //gridCanvas.setGridLinesVisible(true);
         //gridCanvas.toFront();
         
         scrollCanvas = new ScrollPane();
         
         scrollCanvas.setContent(canvas);
         
         
         
        //final settup of workspace
        workspace = new BorderPane();
        ((BorderPane)workspace).setLeft(editToolbar);
	((BorderPane)workspace).setCenter(scrollCanvas);
    }
    /**
     * Helper settup method used in MetroWorkSpace constructor.
     * Purpose: To initialize controls with appropriate controller
     * methods.
     * @param void
     * @return void
     */
    private void initControllers(){
        
        //This is our mapEditorController
        
        mapEditorController = new MapEditorController(app);
        
        //INIT CONTROLLER OF ABOUT BUTTON
        aboutButton.setOnAction(e->{
            Alert aboutAlert = new Alert(AlertType.INFORMATION);
            Image aaimage = new Image("https://preview.ibb.co/jU822b/Screen_Shot_2017_11_12_at_5_03_55_PM.png");
            ImageView aaimageView = new ImageView(aaimage);
            aboutAlert.setGraphic(aaimageView);
            aboutAlert.setTitle("About MetroMapMaker");
            aboutAlert.setHeaderText("MetroMapMaker");
            aboutAlert.setContentText("MetroMapMaker is a GUI application intended for the making of subway maps which can be "
                    + "exported to a web friendly format for use on the web. MetromapMaker allows users to create and modify custom subway"
                    + "lines and stations and find the quickest route between them.\n Author: Qi Yuan Fang - is a second year Computer Science"
                    + " student at Stony Brook University");
            aboutAlert.showAndWait();
            });
        
         Button newButton = app.getGUI().getNew();
         newButton.setOnAction(e->{
             mapEditorController.processNew();
         });
            
            //INIT CONTROLLER UNDO
            undoButton.setOnAction(e->{mapEditorController.processUndo(); });
            // INIT CONTROLLER REDO
            redoButton.setOnAction(e->{mapEditorController.processRedo();});
            //INIT CONTROLLER ADD LINE TO MAP
            addLineButton.setOnAction(e->{
                 String lineName;
                Color lineColor;
                //MetroLine line;
                
                lineName = new String();
        //alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Line Creation Dialog");
        alert.setHeaderText("Create a line with Name and Color");
        BorderPane bbpane1 = new BorderPane();
        TextField lineTxt = new TextField();
        //MetroWorkspace spc = (MetroWorkspace)app.getWorkspaceComponent();
        ColorPicker inneraddLineColorPicker = new ColorPicker();
        bbpane1.setLeft(lineTxt);
        bbpane1.setCenter(inneraddLineColorPicker);
        alert.getDialogPane().setContent(bbpane1);
        
        ButtonType buttonTypeOne = new ButtonType("Create Line");
        ButtonType buttonTypeTwo = new ButtonType("Cancel");
        
       alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            lineName = lineTxt.getText();
            int length = lineName.length();
            if(length == 0){
                 Alert alert2 = new Alert(AlertType.ERROR);
                    alert2.setTitle("Line already exists alert or name is empty");
                    alert2.setHeaderText("Line already exists or name is empty");
                    alert2.showAndWait();
                    return;
            }
            lineColor = inneraddLineColorPicker.getValue();
            MetroWorkspace workspace = (MetroWorkspace) app.getWorkspaceComponent();
            ObservableList list = workspace.getLineComboBox().getItems();
            for (int i=0;i<list.size();i++){
                
                if(list.get(i).toString().equals(lineName)|| lineName.length()==0){
                    Alert alert2 = new Alert(AlertType.ERROR);
                    alert2.setTitle("Line already exists alert or name is empty");
                    alert2.setHeaderText("Line already exists or name is empty");
                    alert2.showAndWait();
                    return;
                }
            }
            MetroLine line = new MetroLine(lineName,lineColor);
            mapEditorController.processAddLine(line);
        } else if (result.get() == buttonTypeTwo) {
            return;
        } 
              
                
                
            });
            //INIT CONTROLLER REMOVE LINE FROM MAP
            removeLineButton.setOnAction(e->{
                if(getLineComboBox().getValue()!=null){
                    Alert alert3 = new Alert(AlertType.CONFIRMATION);
                    alert3.setTitle("Confirm line deletion");
                    alert3.setHeaderText("Are you sure you want to delete the selected line in ComboBox?");
                    ButtonType buttonTypeOne = new ButtonType("Delete Line");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    alert3.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert3.showAndWait();
                    if (result.get() == buttonTypeOne){
                            String tbDeletedLineName = getLineComboBox().getValue().toString();
                            mapEditorController.processRemoveLine(tbDeletedLineName);
                     }
                    else{
                        return;
                    }
                    
                    
                }
                else{
                    Alert alert2 = new Alert(AlertType.ERROR);
                    alert2.setTitle("No line selected for deletion error dialog box");
                    alert2.setHeaderText("No line selected for deletion");
                    alert2.showAndWait();
                }
            });
            
            lineThicknessSlider.setOnMouseReleased(e->{
                if(getLineComboBox().getValue()!=null){
                     MetroData data = (MetroData) app.getDataComponent();
                     data.processChangeLineThickness(lineThicknessSlider.getValue());
                }
                else{
                    return;
                }
            });
            
            metroStationSlider.setOnMouseReleased(e->{
                if(getStationComboBox().getValue()!=null){
                      MetroData data = (MetroData) app.getDataComponent();
                      data.processChangeRadius(metroStationSlider.getValue());

                }
                else{
                    return;
                }
                       
            });

            //INIT CONTROLLER ADD STATION TO LINE
            addStationToLineButton.setOnAction(e->{
                
                Alert alert9 = new Alert(AlertType.INFORMATION);
                alert9.setTitle("How adding stations work");
                alert9.setHeaderText("Stations are added in the order of the proximity to the start point (from start -> end of line)");
                if(!warned){
                    alert9.showAndWait();
                    warned = true;
                }
                
                if(getLineComboBox().getValue()!=null){
                MetroData data = (MetroData) app.getDataComponent();
                data.setState(MetroState.ADDING_STATION);
                Scene scene = app.getGUI().getPrimaryScene();
                scene.setCursor(Cursor.CROSSHAIR);
                }
                else{
                    Alert emptyAlert = new Alert(AlertType.ERROR);
                    emptyAlert.setTitle("Error: No Line selected");
                    emptyAlert.setHeaderText("No line is selected for stations to be added to");
                    emptyAlert.showAndWait();
                }
            });
            //INIT CONTROLLER REMOVE STATION FROM LINE
            removeStationFromLineButton.setOnAction(e->{
                MetroData data = (MetroData) app.getDataComponent();
                data.setState(MetroState.DELETING_STATION);
                Scene scene = app.getGUI().getPrimaryScene();
                scene.setCursor(Cursor.CROSSHAIR);
            });
            
            linesColorPicker.setOnAction(e->{
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Edit Line Dialog");
                alert.setHeaderText("Select the color and input name of currently selected station");
                
                MetroData data = (MetroData) app.getDataComponent();
                MetroObject currSelect = data.getSelectedObject();
                if(currSelect instanceof MetroLine){
                    
                    ButtonType buttonTypeOne = new ButtonType("Edit Line");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    
                    MetroLine cast = (MetroLine) currSelect;
                    String lineName = cast.getLineName();
                    TextField txt = new TextField();
                    txt.setText(lineName);
                    ColorPicker picker = new ColorPicker(cast.getLineColor());
                    HBox hbox = new HBox(15);
                    hbox.getChildren().addAll(txt,picker);
                    alert.getDialogPane().setContent(hbox);
                    alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne){
                            String editLineName = txt.getText();
                            Color newLineColor = picker.getValue();
                            mapEditorController.processEditLine(cast,editLineName,newLineColor);
                     }
                    else{
                        return;
                    }
                }
                else{
                    alert.setContentText("Error selected object is not a line, and cannot be edited");
                    alert.showAndWait();
                }
                
            });
            
            metroStationColorPicker.setOnAction(e->{
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Change Station Fill Color Dialog");
                alert.setHeaderText("Select the new color of selected station");
                MetroData data = (MetroData) app.getDataComponent();
                MetroObject currSelect = data.getSelectedObject();
                if(currSelect instanceof MetroStation){
                    ButtonType buttonTypeOne = new ButtonType("Edit Station Color");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    
                    MetroStation cast = (MetroStation) currSelect;
                    String stationName = cast.getName();
                    ColorPicker picker = new ColorPicker(cast.getStationColor());
                    alert.getDialogPane().setContent(picker);
                    alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne){
                            Color newLineColor = picker.getValue();
                            mapEditorController.processChangeStationFillColor(cast,newLineColor);
                     }
                    else{
                        return;
                    }
                    

                }
                else{
                    alert.setContentText("Error selected object is not a station, and cannot be edited");
                    alert.showAndWait();
                }
            });
            
            decorColorPicker.setOnAction(e->{
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Change Background ColorPicker");
                alert.setHeaderText("Select the new color of the map background");
                MetroData data = (MetroData) app.getDataComponent();
                MetroMap currentMap = data.getCurrentMap();
                ButtonType buttonTypeOne = new ButtonType("Edit Background Color");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                
                ColorPicker picker = new ColorPicker(currentMap.getBackgroundColor());
                    alert.getDialogPane().setContent(picker);
                    alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne){
                            Color newBackgroundColor = picker.getValue();
                            mapEditorController.processSetBackgroundColor(newBackgroundColor);
                     }
                    else{
                        return;
                    }
            });
            
            
            // INIT CONTROLLER ADD STATION TO MAP
            addStationButton.setOnAction(e->{
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Add New Station Dialog");
                alert.setHeaderText("Enter Name of New Station (NEW STATION NAME MUST BE UNIQUE)");
                TextField lineTxt = new TextField();
                alert.getDialogPane().setContent(lineTxt);
                ButtonType buttonTypeOne = new ButtonType("Create Station");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == buttonTypeOne){
                    String stationName = lineTxt.getText();
                    int length = stationName.length();
                    if(length == 0){
                        Alert alert2 = new Alert(AlertType.ERROR);
                        alert2.setTitle("Create Station Error");
                        alert2.setHeaderText("Station Name is empty");
                        alert2.showAndWait();
                        return;
                    }
                    ArrayList<MetroStation> stations = currentMap.getStations();
                    boolean unique = true;
                    for(int i=0;i<stations.size();i++){
                        if(stations.get(i).getName().equals(stationName)){
                            unique = false;
                        }
                    }
                    if(unique){
                    mapEditorController.processAddNewStation(stationName);
                    }
                    else{
                        Alert alert2 = new Alert(AlertType.ERROR);
                        alert2.setTitle("Create Station Error");
                        alert2.setHeaderText("Station Name is not unique");
                        alert2.showAndWait();
                    }
                }else{
                    return;
                }
                
            });
            //INIT CONTROLLER REMOVE STATION FROM MAP
            removeStationButton.setOnAction(e->{
                if(getStationComboBox().getValue()!=null){
                    Alert alert3 = new Alert(AlertType.CONFIRMATION);
                    alert3.setTitle("Confirm Station deletion");
                    alert3.setHeaderText("Are you sure you want to delete the selected station from the map?. It will also delete from all lines it is in.");
                    ButtonType buttonTypeOne = new ButtonType("Delete Station");
                    ButtonType buttonTypeTwo = new ButtonType("Cancel");
                    alert3.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert3.showAndWait();
                    if (result.get() == buttonTypeOne){
                            mapEditorController.processRemoveStation(getStationComboBox().getValue().toString());
                     }
                    else{
                        return;
                    }
                }
                else{
                    Alert alert2 = new Alert(AlertType.ERROR);
                    alert2.setTitle("ERROR: Remove Station From Map Error");
                    alert2.setHeaderText("No Station selected for deletion");
                    alert2.showAndWait();
                }
            });
            listStationButton.setOnAction(e->{
                Alert stationList = new Alert(AlertType.INFORMATION);
                stationList.setTitle("Stations in Selected Line");
                stationList.setHeaderText("List of Stations in line selected in ComboBox");
                String lineName = getLineComboBox().getValue().toString();
                if(lineName!=null ){
                    MetroData data = (MetroData)app.getDataComponent();
                    MetroLine line = data.getLineFromLineComboBox();
                    ListView list = new ListView();
                    ObservableList ob = FXCollections.observableArrayList();
                    for(int i=0;i<line.getStation().size();i++){
                        ob.add(line.getStation().get(i).getName());
                    }
                    list.setItems(ob);
                    stationList.getDialogPane().setContent(list);
                }
                else{
                    stationList.setContentText("No line selected");
                }
                stationList.showAndWait();
                
            });
            
            // INIT CONTROLLER MOVE LABEL 
            moveLabelButton.setOnAction(e->{
                MetroData data = (MetroData)app.getDataComponent();
                data.processmovestationlabel();
            });
            // INIT CONTROLLER ROTATE
            rotateButton.setOnAction(e->{
                MetroData data = (MetroData)app.getDataComponent();
                data.processrotatestationlabel();
            });
            //INIT CONTROLLER FIND ROUTE
            findPathButton.setOnAction(e->{
                
                
                if(!startStationComboBox.getSelectionModel().isEmpty() && !endStationComboBox.getSelectionModel().isEmpty()){
                    String startStationString = startStationComboBox.getValue().toString();
                    String endStationString = endStationComboBox.getValue().toString();
                    if(startStationString.equals(endStationString)){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Find Route error");
                        alert.setHeaderText("Same station selected for start and end");
                        alert.showAndWait();
                    }
                    else{
                        MetroData data = (MetroData)app.getDataComponent();
                        ArrayList <String> path = data.findpath(startStationString,endStationString);
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Find Route");
                        alert.setHeaderText("Route");
                        ListView list = new ListView();
                        for (int i = 0;i<path.size();i++){
                            list.getItems().add(path.get(i));
                        }
                        alert.getDialogPane().setContent(list);
                        alert.showAndWait();
                    }
                }
               
                else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Find Route error");
                    alert.setHeaderText("Must have two stations selected");
                    alert.showAndWait();
                }
                
            });
            
            // INIT CONTROLLER SETIMBACK
            setBackgroundImageButton.setOnAction(e->{
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Change Background Image Dialog");
                alert.setHeaderText("Do you want to change the background Image?");
                ButtonType buttonTypeOne = new ButtonType("Set Background Image");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == buttonTypeOne){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Choose Background Image");
                    File backgroundImage = fileChooser.showOpenDialog(app.getGUI().getWindow());
                    
                    File copybackgroundImage = new File("./images/"+backgroundImage.getName());
                    try{
                    Files.copy(backgroundImage.toPath(), copybackgroundImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }catch(Exception f){
                        f.printStackTrace();
                    }
                    
                    
                    try{
                    Image image = new Image(copybackgroundImage.toURI().toURL().toExternalForm());
                    
                    MetroData data = (MetroData)app.getDataComponent();
                    String abspath = copybackgroundImage.getPath();
                    //System.out.println(abspath);
                    data.setBackgroundImage(image, abspath);
                    }catch(Exception v){
                        ;
                    }
                    
                }
                    else{
                    return;
                }
            });
            //INIT CONTROLLER ADDIMG
            addImageButton.setOnAction(e->{
                FileChooser choose = new FileChooser();
                choose.setTitle("Choose Image");
                File Image = choose.showOpenDialog(app.getGUI().getWindow());
                
                File copyImage = new File("./images/"+Image.getName());
                
                try{
                    Files.copy(Image.toPath(), copyImage.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }catch(Exception f){
                        f.printStackTrace();
                    }
                    
                    
                    try{
                    Image image = new Image(copyImage.toURI().toURL().toExternalForm());
                    
                    MetroData data = (MetroData)app.getDataComponent();
                    String abspath = copyImage.getPath();
                    MetroLandMarkImage imaged = new MetroLandMarkImage(image,abspath);
                    data.addImage(imaged);
                    }catch(Exception v){
                        ;
                    }
                
            });
            // INIT CONTROLLER ADD LABEL
            addLabelButton.setOnAction(e->{//mapEditorController.processAddLabel();
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Add Label dialog");
                alert.setHeaderText("Please enter text for label");
                TextField lineTxt = new TextField();
                alert.getDialogPane().setContent(lineTxt);
                ButtonType buttonTypeOne = new ButtonType("Create Label");
                ButtonType buttonTypeTwo = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == buttonTypeOne){
                    String labelName = lineTxt.getText();
                    int length = labelName.length();
                    if(length == 0){
                        Alert alert2 = new Alert(AlertType.ERROR);
                        alert2.setTitle("Create Label Error");
                        alert2.setHeaderText("Label Name is empty");
                        alert2.showAndWait();
                        return;
                    }
                    else{
                        mapEditorController.processAddLabel(labelName);
                    }
                }
                    else{
                    return;
                }
                
            });
            // INIT CONTROLLER REMOVEEL
            removeElButton.setOnAction(e->{
                MetroData data = (MetroData) app.getDataComponent();
                data.processremoveel();
            });
            
            fontSizeComboBox.setOnAction(e->{
                String value = fontSizeComboBox.getValue().toString();
                MetroData data = (MetroData)app.getDataComponent();
                data.handleFontSizeChange(value);
            });
            fontTypeComboBox.setOnAction(e->{
                String value = fontTypeComboBox.getValue().toString();
                //System.out.println(value);
                MetroData data = (MetroData)app.getDataComponent();
                data.handleFontFamilyChange(value);
            });
            
            textColorColorPicker.setOnAction(e->{
                Color color = textColorColorPicker.getValue();
                MetroData data = (MetroData)app.getDataComponent();
                data.handleColorChange(color);
            });
            
            //INIT CONTROLLER BOLD TEXT
            boldButton.setOnAction(e->{MetroData data = (MetroData)app.getDataComponent();
                data.handleBold();});
            //INIT CONTROLLER ITALICS TEXT
            italicsButton.setOnAction(e->{MetroData data = (MetroData)app.getDataComponent();
                data.handleItalics();});
            // INIT CONTROLLER SHOW GRID
            showGridToggleButton.setOnAction(e->mapEditorController.processToggleGrid());
            //INIT CONTROLLER MAGUP
            plusZoomButton.setOnAction(e->{
                                MetroData data = (MetroData) app.getDataComponent();

                ObservableList <Node> r = canvas.getChildren();
                //canvas.setScaleX(canvas.getScaleX()*1.1);
                //canvas.setScaleY(canvas.getScaleY()*1.1);
                //scrollCanvas.setScaleX(scrollCanvas.getScaleX()*1.1);
                //scrollCanvas.setScaleY(scrollCanvas.getScaleY()*1.1);
                
                for(int i = 0;i<r.size();i++){
                    r.get(i).setScaleX(r.get(i).getScaleX()*1.1);
                    r.get(i).setScaleY(r.get(i).getScaleY()*1.1);
                    canvas.setMinHeight(canvas.getMinHeight()*1.1);
                    canvas.setMinWidth(canvas.getMinWidth()*1.1);
                    data.redrawLines();
                }
                
                
                
            });
            //INIT CONTROLLER MAGDOWN
            minusZoomButton.setOnAction(e->{
                MetroData data = (MetroData) app.getDataComponent();
                ObservableList <Node> r = canvas.getChildren();
                
                
                for(int i = 0;i<r.size();i++){
                    r.get(i).setScaleX(r.get(i).getScaleX()*0.9);
                    r.get(i).setScaleY(r.get(i).getScaleY()*0.9);
                    canvas.setMinHeight(canvas.getMinHeight()*0.9);
                    canvas.setMinWidth(canvas.getMinWidth()*0.9);
                    data.redrawLines();

                }
                
                //canvas.setScaleX(canvas.getScaleX()/1.1);
                //canvas.setScaleY(canvas.getScaleY()/1.1);
                //scrollCanvas.setScaleX(scrollCanvas.getScaleX()/1.1);
                //scrollCanvas.setScaleY(scrollCanvas.getScaleY()/1.1);
            });
            //INIT CONTROLLER MAPUP
            plusMapSizeButton.setOnAction(e->{
                canvas.setMinHeight(canvas.getMinHeight()*1.1);
                canvas.setMinWidth(canvas.getMinWidth()*1.1);

            });
            //INIT CONTROLLER MAPDOWN
        minusMapSizeButton.setOnAction(e-> {
            if(canvas.getMinHeight()>=200 && canvas.getMinWidth()>=200){
            canvas.setMinHeight(canvas.getMinHeight()*0.9);
            canvas.setMinWidth(canvas.getMinWidth()*0.9);
            }
            else{
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error map size at minimum");
                alert.setHeaderText("Cannot make smaller!");
                alert.showAndWait();
            }
        });
        showGridToggleButton.setOnAction(e->{
            if(showGridToggleButton.isSelected()){
                 File plus = new File("./images/grid.png");
                 try{
                 Image image = new Image(plus.toURI().toURL().toExternalForm());
                 canvas.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
                 
                 }catch(Exception r){
                     ;
                 }
            }
            else{
                MetroData data = (MetroData) app.getDataComponent();
                data.getridofgrid();
            }
        });
        
        snapButton.setOnAction(e->{
            MetroData data = (MetroData) app.getDataComponent();
            data.snap();
        });
        
        canvasController = new CanvasController(app);
        canvas.setOnMousePressed(e->{
	    canvasController.processMousePressed((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processMouseReleased((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processMouseDragged((int)e.getX(), (int)e.getY());
	});
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:  scrollCanvas.setVvalue(scrollCanvas.getVvalue()-0.1);
                            break;
                    case A:  scrollCanvas.setHvalue(scrollCanvas.getHvalue()-0.1);
                            break;
                    case D:  scrollCanvas.setHvalue(scrollCanvas.getHvalue()+0.1);
                            break;
                    case S: scrollCanvas.setVvalue(scrollCanvas.getVvalue()+0.1);
                            break;
                }
            }
        });
        
        exportButton.setOnAction(e->{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            FileChooser file = new FileChooser();
            FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
                try {
                    app.getFileComponent().exportData(app.getDataComponent(), selectedFile.getPath());
                                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();

                            dialog.show("Export Completed Dialog","Done with export. .png and .json created");

                } catch (IOException ex) {
                    Logger.getLogger(MetroWorkspace.class.getName()).log(Level.SEVERE, null, ex);
                }
		}
        });
        saveAsButton.setOnAction(e->{
            PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
	    // MAYBE WE ALREADY KNOW THE FILE
	    
	    // OTHERWISE WE NEED TO PROMPT THE USER
	    
		// PROMPT THE USER FOR A FILE NAME
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
		    app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath());
	
	
	
	// TELL THE USER THE FILE HAS BEEN SAVED
	AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	
        dialog.show(props.getProperty(SAVE_COMPLETED_TITLE),props.getProperty(SAVE_COMPLETED_MESSAGE));
		    
	// AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
	// THE APPROPRIATE CONTROLS
	
		}
	    
        } catch (IOException ioe) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
        });
        
       
    

    }
    /**
     * Helper method for constructor for loading settings.
     * @param Object
     * @return void
    */
    public void loadSelectedSettings(Object object){
        
    }
    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle(){
        editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row2Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row3Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row4Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row5Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row6Bar.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        exportButton.getStyleClass().add(CLASS_FILE_BUTTON);
        undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        aboutButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
    
    
    /**
     * Abstract method not implemented in this application.
     * @param void
     * @return void
    */
    public void resetLanguage() {
       
    }
    /**
     * Abstract method for reloading workspace. We will not be using this method in this application.
     * @param dataComponent 
     * @return void
     */
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        
    }
    /**
     * Method for resetting workspace
     * @param void
     * @return void
     */
    @Override
    public void resetWorkspace() {
        
    }
    
    public Button getLineColorPicker(){
        return linesColorPicker;
    }
    
    public Button getStationColorPicker(){
        return metroStationColorPicker;
    }
    
    public ComboBox getLineComboBox(){
        return this.linesComboBox;
    }
    public ComboBox getStationComboBox(){
        return this.metroStationComboBox;
    }
    
    public ComboBox getFontSizeComboBox(){
        return this.fontSizeComboBox;
    }
    public ComboBox getFontTypeComboBox(){
        return this.fontTypeComboBox;
    }
    public ColorPicker gettextColorColorPicker(){
        return this.textColorColorPicker;
    }
    public ComboBox getStartStation(){
        return this.startStationComboBox;
    }
    public ComboBox getEndStation(){
        return this.endStationComboBox;
    }
   
    
}
