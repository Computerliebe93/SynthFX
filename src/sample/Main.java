package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static GranularModel Grandad = new GranularModel();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller GSPController = new Controller(Grandad);
        View view = new View(Grandad, GSPController);
        primaryStage.setTitle("Grandaddy");
        primaryStage.setScene(new Scene(view.asParent(), 800, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
