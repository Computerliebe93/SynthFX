package sample;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application  {
    @Override
    public void start(Stage primaryStage) {
        try{
        Synth synth = new Synth();
        MidiKeyboard midiKeyboard = new MidiKeyboard(synth);
        Controller controller = new Controller(synth);
        View view = new View(synth, controller);
        controller.setView(view);
        synth.setController(controller);
        synth.setView(view);
        synth.setMidiKeyboard(midiKeyboard);

        primaryStage.setTitle("Grandaddy");
        primaryStage.setScene(new Scene(view.asParent(), 800, 600));
        primaryStage.show();

            //new  thread for model
            Thread thread = new Thread(synth);
            thread.start();

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public static void main (String[]args) {launch (args);}{
    }
    @Override
    public void stop() {
        System.exit(0);
    }
}