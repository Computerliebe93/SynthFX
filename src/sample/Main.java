package sample;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application  {

    @Override
    public void start(Stage primaryStage) {
        Gsp gsp = new Gsp();
        Synth synth = new Synth();
        MidiKeyboard midiKeyboard = new MidiKeyboard(synth);
        Controller controller = new Controller(synth);
        View view = new View(synth, controller);
        synth.setController(controller);
        synth.setView(view);
        gsp.setSynth(synth);
        gsp.setView(view);
        synth.setMidiKeyboard(midiKeyboard);
        primaryStage.setTitle("Grandaddy");
        primaryStage.setScene(new Scene(view.asParent(), 800, 600));
        controller.setView(view);
        primaryStage.show();
        Thread thread = new Thread(gsp);
        thread.start();
    }
    public static void main (String[]args) {launch (args);}{
    }
}