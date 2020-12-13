package sample;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Synth{
    boolean pitchToggle = false;
    boolean grainSizeToggle = false;
    boolean grainIntervalToggle = false;
    boolean randomToggle = false;
    boolean startPointToggle = false;
    boolean endPointToggle = false;
    boolean sprayToggle = false;
    private float[] knobValues = new float[9];
    private int[] padValues = new int[] {0};
    private int[] keyValues = new int[] {0};
    private String samplePath;
    MidiKeyboard midiKeyboard;
    Controller controller;
    View view;

    // Knob offsets
    final double pitchOffset = 0.1 / 6.35;
    final double sizeOffset = 0.7;
    final double intervalOffset = 4;
    double spray;
    final double sprayOffset = 10000;
    final double loopOffset = 100;
    final int padValueDummy = 10;

    public void setController(Controller controller){
        this.controller = controller;
    }
    public void setView(View view){
        this.view = view;
    }
    public void setMidiKeyboard(MidiKeyboard midiKeyboard){
        this.midiKeyboard = midiKeyboard;
    }
    // KNOB
    public void receiveKnobMidi(byte[] a) {
        if (a[1] > 0 && a[1] <= knobValues.length) {
            knobValues[a[1]] = a[2];
            System.out.println("Knob " + a[1] + " value is set to " + knobValues[a[1]]);
        } else {
            System.out.println("Something went wrong");
        }
        Platform.runLater(()-> {
            view.pitchValueLbl.setText(String.valueOf(getKnobValue(1)));
            view.grainSizeValueLbl.setText(String.valueOf(getKnobValue(2)));
            view.grainIntervalValueLbl.setText(String.valueOf(getKnobValue(3)));
            view.randomnessValueLbl.setText(String.valueOf(getKnobValue(4)));
            view.startValueLbl.setText(String.valueOf(getKnobValue(5)));
            view.endValueLbl.setText(String.valueOf(getKnobValue(6)));
            view.sprayValueLbl.setText(String.valueOf(getKnobValue(7)));
        });
    }
    public float getKnobValue(int knobTransmitter) {
        if (knobTransmitter > 0 && knobTransmitter <= knobValues.length) {
            return knobValues[knobTransmitter];
        } else {
            return 0;
        }
    }
    public void setKnobValue(int knobTransmitter, int value){
        knobValues[knobTransmitter] = value;
    }
    // PAD
    public void receivePadMidi(byte[] a) {
        if (a[1] >= 0 && a[1] < 8) {
            System.out.println(a[1]);
            padValues[0] = a[1];
            System.out.println("Active pad is " + padValues[0]);
        }
    }

    public int getPadValue() {
        return padValues[0];
    }
    public void setPadValue(int i){
        padValues[0] = i;
    }
    // KEYS
    public void receiveKeysMidi(byte[] a) {
        keyValues[0] = a[1];
        System.out.println("Key value is set to " + a[1]);
    }

    public float getKeysValue() {
        return keyValues[0];
    }
    public void setKeysValue( int value){
        keyValues[0] = value;
    }
    public FileChooser loadSample(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.acac")
        );
        return fileChooser;
    }
    public String setSample(File file){
        String path = null;
        try {
            path = file.getCanonicalPath();
            samplePath = path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        return path;
    }
    public String getSample(){
        return samplePath;
    }
}