package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application  {
    Synth synth = new Synth();


    @Override
    public void start(Stage primaryStage) {


        Controller Controller = new Controller(synth);
        View view = new View(synth, Controller);

        primaryStage.setTitle("Grandaddy");
        primaryStage.setScene(new Scene(view.asParent(), 800, 400));
        Controller.setView(view);
        primaryStage.show();

        RunnableThread T1 = new RunnableThread("T1");
        T1.start();

    }

    public static void main (String[]args) {launch (args);}{

    }
}



