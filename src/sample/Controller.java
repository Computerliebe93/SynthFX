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

    public  void setView(View view) {
        view.exitBtn.setOnAction(e -> Platform.exit());
        view.exitBtn.setOnAction(e -> System.exit(0));
        // Pitch
        view.pitchOnBtn.setOnAction(e ->{
            if(view.pitchOnBtn.isSelected()){
                model.pitchToggle = true;
            }
            else if (!view.pitchOnBtn.isSelected()){
                model.pitchToggle = false;
            }
        });
        view.updatePlease.setOnAction(e ->{
            view.pitchValueLbl.setText(String.valueOf(model.getKnobValue(1)));
        });
        view.printPitch.setOnAction(e -> {
            System.out.println("Synth pitch is set to: " + model.getKnobValue(1));
            System.out.println(model.getSample());
        });
        // Pitch button
        view.pitchBtn.setOnAction(e -> {
            view.pitchValueLbl.setText(view.pitchInput.getValue().toString());
            model.setKnobValue(1, view.pitchInput.getValue());

        });
        // Grain size button
        view.grainSizeBtn.setOnAction(e -> {
            view.grainSizeValueLbl.setText(view.grainSizeInput.getValue().toString());
            model.setKnobValue(2, view.grainSizeInput.getValue());
        });
        // Grain interval
        view.grainIntervalBtn.setOnAction(e -> {
            view.grainIntervalValueLbl.setText(view.grainIntervalInput.getValue().toString());
            model.setKnobValue(3, view.grainSizeInput.getValue());
        });
        // Randomness
        view.randomnessBtn.setOnAction(e -> {
            view.randomnessValueLbl.setText(view.randomnessInput.getValue().toString());
           model.setKnobValue(4, view.randomnessInput.getValue());
        });
        // Start point
        view.startBtn.setOnAction(e -> {
            view.startValueLbl.setText(view.startInput.getValue().toString());
            model.setKnobValue(5, view.startInput.getValue());
        });
        // End point
        view.endBtn.setOnAction(e -> {
            view.endValueLbl.setText(view.endInput.getValue().toString());
            model.setKnobValue(6, view.endInput.getValue());
        });
        //Spray
        view.sprayBtn.setOnAction(e -> {
            view.sprayValueLbl.setText(view.sprayInput.getValue().toString());
            model.setKnobValue(7, view.sprayInput.getValue());
        });
        // Loop type
        view.selectLoopComb.setOnAction(e -> {
            switch(view.selectLoopComb.getValue()) {
                case "Forwards":
                    model.setPadValue(0);
                    System.out.println("Forwards");
                    model.setPadValue(model.padValueDummy);
                    break;
                case "Backwards":
                    model.setPadValue(1);
                    System.out.println("Backwards");
                    model.setPadValue(model.padValueDummy);
                    break;
                case "Alternating":
                    model.setPadValue(2);
                    System.out.println("Alternating");
                    model.setPadValue(model.padValueDummy);
                    break;
                case "Reset":
                    model.setPadValue(3);
                    System.out.println("Reset");
                    model.setPadValue(model.padValueDummy);
                    break;
            }
                });

        // Select sample
        view.sampleLoadbtn.setOnAction( e ->{
            Window primaryStage = null;
            File selectedFile = model.chooseSampleFile().showOpenDialog(primaryStage);
            if(selectedFile != null){
                model.setSample(selectedFile.getPath());
            }
        });
    }
}