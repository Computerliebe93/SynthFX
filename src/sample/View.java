package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.GranularSamplePlayer;

import java.io.IOException;

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
    Button playBtn = new Button("Play");
    // Exit
    Button exitBtn = new Button("Exit");
    // Pitch
    Label pitchNameLbl = new Label("Pitch");
    Label pitchValueLbl = new Label("0");
    ToggleButton pitchOnBtn = new ToggleButton("Pitch On");
    Button pitchBtn = new Button("Set Pitch");
    Spinner pitchInput = new Spinner(0, 127, 0);
    // GrainSize
    Label grainSizeNameLbl = new Label("GrainSize");
    Label grainSizeValueLbl = new Label("0");
    ToggleButton grainSizeOnBtn = new ToggleButton("Grain Size On");
    Button grainSizeBtn = new Button("Set Grain");
    Spinner grainSizeInput = new Spinner(0, 127, 0);
    // GrainInterval
    Label grainIntervalNameLbl = new Label("GrainInterval");
    Label grainIntervalValueLbl = new Label("0");
    ToggleButton grainIntOnBtn = new ToggleButton("Grain Interval On");
    Button grainIntervalBtn = new Button("Set Grain Interval");
    Spinner<Integer> grainIntervalInput = new Spinner();
    // Randomness
    Label randomnessNameLbl = new Label("Randomness");
    Label randomnessValueLbl = new Label("0");
    ToggleButton randomOnBtn = new ToggleButton("Random On");
    Button randomnessBtn = new Button("Set Randomness");
    Spinner<Integer> randomnessInput = new Spinner();
    // Start point
    Label startNameLbl = new Label("Start");
    Label startValueLbl = new Label("0");
    ToggleButton startPointOnBtn = new ToggleButton("Start point On");
    Button startBtn = new Button("Set Start");
    Spinner<Integer> startInput = new Spinner();
    // End point
    Label endNameLbl = new Label("End");
    Label endValueLbl = new Label("0");
    ToggleButton endPointOnBtn = new ToggleButton("End point On");
    Button endBtn = new Button("Set End");
    Spinner<Integer> endInput = new Spinner();
    // Spray
    Label sprayNameLbl = new Label("Spray");
    Label sprayValueLbl = new Label("0");
    ToggleButton sprayOnBtn = new ToggleButton("Spray On");
    Button sprayBtn = new Button("Set Spray");
    Spinner<Integer> sprayInput = new Spinner();

    // Unedited buttons
    String loopTypes [] = {"Forwards", "Backwards", "Alternating", "Reset"};
    ComboBox<String> selectLoopComb = new ComboBox(FXCollections.observableArrayList(loopTypes));


    private void createAndConfigure(){
        Header = new HBox();
        Grid = new GridPane();
        StartView = new BorderPane();
        StartView.setTop(Header);
        StartView.setCenter(Grid);
        Grid.setHgap(5);
        Grid.setVgap(5);


       /* StartView.setMinSize(00, 200);
        StartView.setPadding(new Insets(10, 10, 10, 10));
        StartView.setVgap(3);
        StartView.setHgap(10);*/
        // Sample
        Header.getChildren().addAll(sampleLbl, sampleLoadbtn, samplePath, playBtn);
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
        pitchInput.setMaxSize(60, 10);
        pitchInput.setEditable(true);
        Grid.add(pitchInput, 2, 6);
        Grid.add(pitchOnBtn,1, 6);
        Grid.add(pitchBtn, 2, 7);
        pitchValueLbl.setMinWidth(40);
        pitchValueLbl.setMaxWidth(40);
        Grid.add(pitchValueLbl, 3, 6);
        // GrainSize
        Grid.add(grainSizeNameLbl, 2, 8);
        grainSizeInput.setMaxSize(60, 20);
        grainSizeInput.setEditable(true);
        Grid.add(grainSizeInput, 2, 9);
        Grid.add(grainSizeOnBtn,1,9);
        Grid.add(grainSizeBtn, 2, 10);
        Grid.add(grainSizeValueLbl, 3, 9);
        grainSizeValueLbl.setMinWidth(40);
        grainSizeValueLbl.setMaxWidth(40);
        // GrainSize
        Grid.add(grainIntervalNameLbl, 2, 11);
        grainIntervalInput.setMaxSize(60, 20);
        grainIntervalInput.setEditable(true);
        Grid.add(grainIntervalInput, 2, 12);
        Grid.add(grainIntOnBtn, 1, 12);
        Grid.add(grainIntervalBtn, 2, 13);
        grainIntervalValueLbl.setMinWidth(40);
        grainIntervalValueLbl.setMaxWidth(40);
        Grid.add(grainIntervalValueLbl, 3, 12);
        // Randomness
        Grid.add(randomnessNameLbl, 2, 14);
        randomnessInput.setMaxSize(60, 20);
        randomnessInput.setEditable(true);
        Grid.add(randomnessInput, 2, 15);
        Grid.add(randomOnBtn,1,15);
        Grid.add(randomnessBtn, 2, 16);
        randomnessValueLbl.setMinWidth(40);
        randomnessValueLbl.setMaxWidth(40);
        Grid.add(randomnessValueLbl, 3, 15);
        // Start
        Grid.add(startNameLbl, 5, 5);
        startInput.setMaxSize(60, 20);
        startInput.setEditable(true);
        Grid.add(startInput, 5, 6);
        Grid.add(startPointOnBtn,4,6);
        Grid.add(startBtn, 5, 7);
        startValueLbl.setMinWidth(40);
        startValueLbl.setMaxWidth(40);
        Grid.add(startValueLbl, 6, 6);
        // End
        Grid.add(endNameLbl, 5, 8);
        endInput.setMaxSize(60, 20);
        endInput.setEditable(true);
        Grid.add(endInput, 5, 9);
        Grid.add(endPointOnBtn, 4,9);
        Grid.add(endBtn, 5, 10);
        endValueLbl.setMinWidth(40);
        endValueLbl.setMaxWidth(40);
        Grid.add(endValueLbl, 6, 9);
        // Spray
        Grid.add(sprayNameLbl, 5, 11);
        sprayInput.setMaxSize(60,20);
        sprayInput.setEditable(true);
        Grid.add(sprayInput,5,12);
        Grid.add(sprayOnBtn, 4,12);
        Grid.add(sprayBtn,5,13);
        Grid.add(sprayValueLbl, 6,12);

        // Weird stuff
        Grid.add(exitBtn, 21, 20);

    }
    public Parent asParent() {
        return StartView;
    }
}