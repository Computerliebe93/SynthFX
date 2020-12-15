package sample;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.stage.Window;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.Static;

import java.io.File;
import java.io.IOException;

public class Controller {
    Synth model;

    public Controller(Synth model) {
        this.model = model;
    }

    public void setView(View view) {
        view.exitBtn.setOnAction(e -> Platform.exit());
        view.exitBtn.setOnAction(e -> System.exit(0));



        // Play sample
        view.playSampleBtn.setOnAction(e ->{

            if(model.samplePath != null) {
                model.mountGspSample();
            }
            else{
                System.out.println("Please select a sound sample");
            }
        });

        // Pause sample
        view.pauseSampleBtn.setOnAction(e ->{
            model.pauseSample();
        });

        // Start sample
        view.startSampleBtn.setOnAction(e ->{
            model.startSample();
        });


        // Pitch button
        view.pitchBtn.setOnAction(e -> {
            model.GUIUpdate(view.pitchValueLbl, view.pitchInput, 1);
            model.setPitch(Float.valueOf(view.pitchValueLbl.getText()));
        });

        // Grain size button
        view.grainSizeBtn.setOnAction(e -> {
            model.GUIUpdate(view.grainSizeValueLbl, view.grainSizeInput, 2);
            model.setGrainSize(Float.valueOf(view.grainSizeValueLbl.getText()));
        });

        // Grain interval
        view.grainIntervalBtn.setOnAction(e -> {
            model.GUIUpdate(view.grainIntervalValueLbl, view.grainIntervalInput, 3);
            model.setGrainInterval(Float.valueOf(view.grainIntervalValueLbl.getText()));
        });

        // Randomness
        view.randomnessBtn.setOnAction(e -> {
            model.GUIUpdate(view.randomnessValueLbl, view.randomnessInput, 4);
            model.setRandomness(Float.valueOf(view.randomnessValueLbl.getText()));
        });

        // Start point
        view.startBtn.setOnAction(e -> {
            model.GUIUpdate(view.startValueLbl, view.startInput, 5);
            model.setStart(Float.valueOf(view.startValueLbl.getText()));
        });

        // End point
        view.endBtn.setOnAction(e -> {
            model.GUIUpdate(view.endValueLbl, view.endInput, 6);
            model.setEnd(Float.valueOf(view.endValueLbl.getText()));
        });

        //Spray
        view.sprayBtn.setOnAction(e -> {
            model.GUIUpdate(view.sprayValueLbl, view.sprayInput, 7);
            model.setSpray(Float.valueOf(view.sprayValueLbl.getText()));
        });

        // Loop type
        view.selectLoopComb.setOnAction(e -> {
            switch(view.selectLoopComb.getValue()) {
                case "Forwards":
                    model.setLoopForwards();
                    System.out.println("Forwards");
                    break;
                case "Backwards":
                    model.setLoopBackwards();
                    System.out.println("Backwards");
                    break;
                case "Alternating":
                    model.setLoopAlternating();
                    System.out.println("Alternating");
                    break;
                case "Reset":
                    model.setReset();
                    System.out.println("Reset");
                    break;
            }
        });

        // Select sample
        view.sampleLoadbtn.setOnAction( e ->{
            Window primaryStage = null;
            File selectedFile = model.loadSample().showOpenDialog(primaryStage);
            model.setSample(selectedFile);
            view.samplePath.setText(model.getSample());
        });
    }
}