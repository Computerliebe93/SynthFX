package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends Application  {
    Synth synth = new Synth();

    @Override
    public void start(Stage primaryStage) {
        RunnableThread T1 = new RunnableThread("T1");
        T1.start();

        Controller Controller = new Controller(synth);
        View view = new View(synth, Controller);
        primaryStage.setTitle("Grandaddy");
        primaryStage.setScene(new Scene(view.asParent(), 800, 400));
        Controller.setView(view);
        primaryStage.show();

        RunnableThread T2 = new RunnableThread("T2");
        T2.start();
    }

    public static void main (String[]args) {launch (args);}{

    }
}



