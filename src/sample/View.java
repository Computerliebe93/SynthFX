package sample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class View {
    Synth model;
    Controller control;

    public View(Synth model, Controller control){
        this.model = model;
        this.control = control;
        createAndConfigure();
    }
    private HBox Header;
    private BorderPane StartView;
    private GridPane Grid;


    // FileChooser
    Label sampleLbl = new Label("Sample: ");
    Button sampleLoadbtn = new Button("Load sample");
    TextArea samplePath = new TextArea();

    // Exit
    Button exitBtn = new Button("Exit");

    // Pitch
    Label pitchName= new Label("Pitch");
    Label pitchValue = new Label("0");
    Button pitchBtn = new Button("Set Pitch");
    Spinner<Integer> pitchInput = new Spinner(0, 127, 0);

    // GrainSize
    Label grainSizeName= new Label("GrainSize");
    Label grainSizeValue = new Label("0");
    Button grainSizeBtn = new Button("Set Grain");
    TextArea grainSizeInput = new TextArea();

    // GrainInterval
    Label grainIntervalName= new Label("GrainInterval");
    Label grainIntervalValue = new Label("0");
    Button grainIntervalBtn = new Button("Set Grain");
    TextArea grainIntervalInput = new TextArea();

    // GrainInterval
    Label randomnessName= new Label("Randomness");
    Label randomnessValue = new Label("0");
    Button randomnessBtn = new Button("Set randomness");
    TextArea randomnessInput = new TextArea();

    // StartLoop
    Label startName= new Label("Start");
    Label startValue = new Label("0");
    Button startBtn = new Button("Set Start");
    TextArea startInput = new TextArea();

    // EndLoop
    Label endName= new Label("End");
    Label endValue = new Label("0");
    Button endBtn = new Button("Set End");
    TextArea endInput = new TextArea();


    Button updatePlease = new Button("UPDATE PLEASE");
    Button printPitch = new Button("Print Pitch");

    String loopTypes [] = {"Forwards", "Backwards"};
    ComboBox<String> selectLoopComb = new ComboBox(FXCollections.observableArrayList(loopTypes));





    private void createAndConfigure(){
        Header = new HBox();
        Grid = new GridPane();
        StartView = new BorderPane();
        StartView.setTop(Header);
        StartView.setCenter(Grid);



       /* StartView.setMinSize(00, 200);
        StartView.setPadding(new Insets(10, 10, 10, 10));
        StartView.setVgap(3);
        StartView.setHgap(10);*/

        // Sample
        Header.getChildren().addAll(sampleLbl, sampleLoadbtn, samplePath);
        //StartView.add(sampleLoadbtn, 1, 1);
        //StartView.add(sampleLbl, 0, 1);
        samplePath.setMaxHeight(2);
        samplePath.setMinWidth(90);
        //StartView.add(samplePath,1,1, 1, 1);

        // Loop
        selectLoopComb.setMinWidth(90);
        Grid.add(selectLoopComb, 0, 3);
        selectLoopComb.getSelectionModel().selectFirst();

        // Pitch
        Grid.add(pitchName, 2, 5);
        pitchInput.setMaxSize(60, 20);
        pitchInput.setEditable(true);
        pitchInput.getStyleClass().clear();
        Grid.add(pitchInput, 2, 6);
        Grid.add(pitchBtn, 2, 7);
        Grid.add(pitchValue, 3, 6);

        // GrainSize
        Grid.add(grainSizeName, 2, 8);
        grainSizeInput.setMaxSize(30, 20);
        Grid.add(grainSizeInput, 2, 9);
        Grid.add(grainSizeBtn, 2, 10);
        Grid.add(grainSizeValue, 3, 9);

        // GrainSize
        Grid.add(grainIntervalName, 2, 11);
        grainIntervalInput.setMaxSize(30, 20);
        Grid.add(grainIntervalInput, 2, 12);
        Grid.add(grainIntervalBtn, 2, 13);
        Grid.add(grainIntervalValue, 3, 12);

        // Randomness
        Grid.add(randomnessName, 2, 14);
        randomnessInput.setMaxSize(30, 20);
        Grid.add(randomnessInput, 2, 15);
        Grid.add(randomnessBtn, 2, 16);
        Grid.add(randomnessValue, 3, 15);

        // Start
        Grid.add(startName, 5, 5);
        startInput.setMaxSize(30, 20);
        Grid.add(startInput, 5, 6);
        Grid.add(startBtn, 5, 7);
        Grid.add(startValue, 6, 6);

        // End
        Grid.add(endName, 5, 8);
        endInput.setMaxSize(30, 20);
        Grid.add(endInput, 5, 9);
        Grid.add(endBtn, 5, 10);
        Grid.add(endValue, 6, 9);

        // Weird stuff
        Grid.add(updatePlease, 20, 20);
        Grid.add(printPitch, 19,20);
        Grid.add(exitBtn, 21, 20);


    }
    public Parent asParent() {
        return StartView;
    }
}