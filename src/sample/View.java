package sample;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
ª
public class View {
    GranularModel model;
    Controller control;

    private GridPane StartView;

    Label sampleNameLbl = new Label("Sample name:");

    Button loadBtn = new Button("Load sample");


    TextArea sampleName = new TextArea();
    ComboBox<String> selectLoopComb = new ComboBox();


    public View(GranularModel model, Controller control){
        this.model = model;
        this.control = control;
        createAndConfigure();
    }

    private void createAndConfigure(){
        StartView = new GridPane();
        StartView = new GridPane();
        StartView.setMinSize(00, 200);
        StartView.setPadding(new Insets(10, 10, 10, 10));
        StartView.setVgap(3);
        StartView.setHgap(10);

        StartView.add(loadBtn, 0, 0);

        sampleName.setMaxHeight(2);
        sampleName.setMinWidth(90);
        StartView.add(sampleName,1,0, 2, 1);

        selectLoopComb.setMinWidth(90);
        StartView.add(selectLoopComb, 0, 1);


        /*
        loopType.setMaxHeight(10);
        loopType.setMaxWidth(90);
        StartView.add(loopType, 10,20); */
    }



    public Parent asParent() {
        return StartView;
    }
}
