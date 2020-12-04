package sample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class View {
    Synth model;
    Controller control;

    public View(Synth model, Controller control){
        this.model = model;
        this.control = control;
        createAndConfigure();
    }
    private GridPane StartView;

    // Labels
    Label sampleNameLbl = new Label("Sample name:");

    // Buttons
    Button loadBtn = new Button("Load sample");
    Button exitBtn = new Button("Exit");

    // Textarea
    TextArea sampleName = new TextArea();

    String loopTypes [] = {"Forwards", "Backwards"};
    ComboBox<String> selectLoopComb = new ComboBox(FXCollections.observableArrayList(loopTypes));




    private void createAndConfigure(){
        StartView = new GridPane();
        StartView = new GridPane();
        StartView.setMinSize(00, 200);
        StartView.setPadding(new Insets(10, 10, 10, 10));
        StartView.setVgap(3);
        StartView.setHgap(10);

        // Sample
        StartView.add(loadBtn, 0, 0);
        sampleName.setMaxHeight(2);
        sampleName.setMinWidth(90);
        StartView.add(sampleName,1,0, 2, 1);

        // Loop
        selectLoopComb.setMinWidth(90);
        StartView.add(selectLoopComb, 0, 1);
        selectLoopComb.getSelectionModel().selectFirst();

        // Exit
        StartView.add(exitBtn, 20, 20);

    }
    public Parent asParent() {
        return StartView;
    }
}
