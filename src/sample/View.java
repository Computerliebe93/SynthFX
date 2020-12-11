package sample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.beadsproject.beads.ugens.GranularSamplePlayer;

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
    Label pitchNameLbl = new Label("Pitch");
    Label pitchValueLbl = new Label("0");
    ToggleButton pitchOnBtn = new ToggleButton("Pitch On");
    Button pitchBtn = new Button("Set Pitch");
    Spinner<Integer> pitchInput = new Spinner(0, 127, 0);
    // GrainSize
    Label grainSizeNameLbl = new Label("GrainSize");
    Label grainSizeValueLbl = new Label("0");
    Button grainSizeOnBtn = new Button("Grain Size On");
    Button grainSizeBtn = new Button("Set Grain");
    Spinner<Integer> grainSizeInput = new Spinner(0, 127, 0);
    // GrainInterval
    Label grainIntervalNameLbl = new Label("GrainInterval");
    Label grainIntervalValueLbl = new Label("0");
    Button grainIntOnBtn = new Button("Grain Interval On");
    Button grainIntervalBtn = new Button("Set Grain Interval");
    Spinner<Integer> grainIntervalInput = new Spinner(0, 127, 0);
    // Randomness
    Label randomnessNameLbl = new Label("Randomness");
    Label randomnessValueLbl = new Label("0");
    Button randomOnBtn = new Button("Random On");
    Button randomnessBtn = new Button("Set Randomness");
    Spinner<Integer> randomnessInput = new Spinner(0, 127, 0);
    // Start point
    Label startNameLbl = new Label("Start");
    Label startValueLbl = new Label("0");
    Button starPointOn = new Button("Start point On");
    Button startBtn = new Button("Set Start");
    Spinner<Integer> startInput = new Spinner(0, 127, 0);
    // End point
    Label endNameLbl = new Label("End");
    Label endValueLbl = new Label("0");
    Button endPointOn = new Button("End point On");
    Button endBtn = new Button("Set End");
    Spinner<Integer> endInput = new Spinner(0, 127, 0);
    // Spray
    Label sprayNameLbl = new Label("Spray");
    Label sprayValueLbl = new Label("0");
    Button sprayOnBtn = new Button("Spray On");
    Button sprayBtn = new Button("Set Spray");
    Spinner<Integer> sprayInput = new Spinner(0, 127, 0);

    // L0rt
    Button updatePlease = new Button("UPDATE PLEASE");
    Button printPitch = new Button("Print Pitch");
    String loopTypes [] = {"Forwards", "Backwards", "Alternating", "Reset"};
    ComboBox<String> selectLoopComb = new ComboBox(FXCollections.observableArrayList(loopTypes));
    ToggleButton toggleButton = new ToggleButton("Hej");

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
        Grid.add(pitchNameLbl, 2, 5);
        pitchInput.setMaxSize(60, 20);
        pitchInput.setEditable(true);
        Grid.add(pitchInput, 2, 6);
        Grid.add(pitchOnBtn,1, 6);
        Grid.add(pitchBtn, 2, 7);
        Grid.add(pitchValueLbl, 3, 6);
        // GrainSize
        Grid.add(grainSizeNameLbl, 2, 8);
        grainSizeInput.setMaxSize(60, 20);
        grainSizeInput.setEditable(true);
        Grid.add(grainSizeInput, 2, 9);
        Grid.add(grainSizeBtn, 2, 10);
        Grid.add(grainSizeValueLbl, 3, 9);
        // GrainSize
        Grid.add(grainIntervalNameLbl, 2, 11);
        grainIntervalInput.setMaxSize(60, 20);
        grainIntervalInput.setEditable(true);
        Grid.add(grainIntervalInput, 2, 12);
        Grid.add(grainIntervalBtn, 2, 13);
        Grid.add(grainIntervalValueLbl, 3, 12);
        // Randomness
        Grid.add(randomnessNameLbl, 2, 14);
        randomnessInput.setMaxSize(60, 20);
        randomnessInput.setEditable(true);
        Grid.add(randomnessInput, 2, 15);
        Grid.add(randomnessBtn, 2, 16);
        Grid.add(randomnessValueLbl, 3, 15);
        // Start
        Grid.add(startNameLbl, 5, 5);
        startInput.setMaxSize(60, 20);
        startInput.setEditable(true);
        Grid.add(startInput, 5, 6);
        Grid.add(startBtn, 5, 7);
        Grid.add(startValueLbl, 6, 6);
        // End
        Grid.add(endNameLbl, 5, 8);
        endInput.setMaxSize(60, 20);
        endInput.setEditable(true);
        Grid.add(endInput, 5, 9);
        Grid.add(endBtn, 5, 10);
        Grid.add(endValueLbl, 6, 9);
        // Spray
        Grid.add(sprayNameLbl, 5, 11);
        sprayInput.setMaxSize(60,20);
        sprayInput.setEditable(true);
        Grid.add(sprayInput,5,12);
        Grid.add(sprayBtn,5,13);
        Grid.add(sprayValueLbl, 6,12);

        // Weird stuff
        Grid.add(updatePlease, 20, 20);
        Grid.add(printPitch, 19,20);
        Grid.add(exitBtn, 21, 20);
        Grid.add(toggleButton, 15, 15);

    }
    public Parent asParent() {
        return StartView;
    }
}