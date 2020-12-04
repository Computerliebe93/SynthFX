package sample;

import javafx.application.Platform;


public class Controller {
    Synth model;


    public Controller(Synth model) {
        this.model = model;
    }

    public static void setView(View view) {
        view.exitBtn.setOnAction(e -> Platform.exit());
        view.exitBtn.setOnAction(e -> System.exit(0));

        // view.sampleName.appendText("hej");

    }
}

