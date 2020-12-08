package sample;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.stage.Window;

import java.io.File;


public class Controller {
    Synth model;


    public Controller(Synth model) {
        this.model = model;
    }


    public void setPitchValue(Label label){
        model.setKnobValue(1, Integer.parseInt(label.getText()));

    }

    public  void setView(View view) {
        view.exitBtn.setOnAction(e -> Platform.exit());
        view.exitBtn.setOnAction(e -> System.exit(0));

        // Pitch
        view.updatePlease.setOnAction(e ->{
            view.pitchValue.setText(String.valueOf(model.getKnobValue(1)));
        });

        view.printPitch.setOnAction(e -> {
            System.out.println("Synth pitch is set to: " + model.getKnobValue(1));
            System.out.println(model.getSample());

        });


        view.pitchBtn.setOnAction(e -> {

            view.pitchValue.setText(view.pitchInput.getValue().toString());
            setPitchValue(view.pitchValue);
        });

        // Sample
        view.sampleLoadbtn.setOnAction( e ->{
            model.getSample();
            Window primaryStage = null;
            File selectedFile = model.loadSample().showOpenDialog(primaryStage);
            model.setSample(selectedFile);
        });

        view.grainSizeBtn.setOnAction(e -> view.grainSizeValue.setText(view.grainSizeInput.getText()));
        view.grainIntervalBtn.setOnAction(e -> view.grainIntervalValue.setText(view.grainIntervalInput.getText()));
        view.randomnessBtn.setOnAction(e -> view.randomnessValue.setText(view.randomnessInput.getText()));
        view.startBtn.setOnAction(e -> view.startValue.setText(view.startInput.getText()));
        view.endBtn.setOnAction(e -> view.endValue.setText(view.endInput.getText()));


        view.pitchValue.setText(String.valueOf(model.getKnobValue(1)));


        // view.sampleName.appendText("hej");


    }
}