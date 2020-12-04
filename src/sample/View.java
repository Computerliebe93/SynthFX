package sample;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;



public class View {
    Synth model;
    Controller control;

    public View(Synth model, Controller control){
        this.model = model;
        this.control = control;
        createAndConfigure();
    }
    private GridPane StartView;

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
    TextArea pitchInput = new TextArea();

    // Grain
    Label grainName= new Label("Grainh");
    Label grainValue = new Label("0");
    Button grainBtn = new Button("Set Grain");
    TextArea grainInput = new TextArea();


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
        StartView.add(sampleLoadbtn, 0, 0);
        StartView.add(sampleLbl, 0, 1);
        samplePath.setMaxHeight(2);
        samplePath.setMinWidth(90);
        StartView.add(samplePath,1,1, 1, 1);

        // Loop
        selectLoopComb.setMinWidth(90);
        StartView.add(selectLoopComb, 0, 3);
        selectLoopComb.getSelectionModel().selectFirst();

        // Exit
        StartView.add(exitBtn, 20, 20);

        // Pitch
        StartView.add(pitchName, 2, 5);
        pitchInput.setMaxSize(30, 20);
        StartView.add(pitchInput, 2, 6);
        StartView.add(pitchBtn, 2, 7);
        StartView.add(pitchValue, 3, 6);

        //Grain
        StartView.add(grainName, 2, 8);
        grainInput.setMaxSize(30, 20);
        StartView.add(grainInput, 2, 9);
        StartView.add(grainBtn, 2, 10);
        StartView.add(grainValue, 3, 9);





    }
    public Parent asParent() {
        return StartView;
    }
}
