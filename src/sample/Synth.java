package sample;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Synth implements Runnable{
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
    Thread thread = new Thread(this::run);
    public String samplePath = null;
    AudioContext ac = new AudioContext();
    MidiKeyboard midiKeyboard;
    Controller controller;
    View view;
    boolean sampleReady = false;
    GranularSamplePlayer gsp;


    // Knob offsets
    final double pitchOffset = 0.1 / 6.35;
    final double sizeOffset = 0.7;
    final double intervalOffset = 4;
    double spray;
    final double sprayOffset = 10000;
    final double loopOffset = 100;
    final int padValueDummy = 10;


    public Synth() {


        // load the source sample from a file
        Sample sourceSample = null;
        try
        {
            sourceSample = new Sample("Ring02.wav");
        }
        catch(Exception e)
        {
            /*
             * If the program exits with an error message,
             * then it most likely can't find the file
             * or can't open it. Make sure it is in the
             * root folder of your project in Eclipse.
             * Also make sure that it is a 16-bit,
             * 44.1kHz audio file. These can be created
             * using Audacity.
             */
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // instantiate a GranularSamplePlayer
        gsp = new GranularSamplePlayer(ac, sourceSample);

        // tell gsp to loop the file
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);


        // connect gsp to ac
        ac.out.addInput(gsp);

        // begin audio processing
        ac.start();
        System.out.println("Does it reach here*");


    }


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

        if (a[1] == 1){
            setPitch(a[2]);
        }
        else if (a[1] == 2){
            setGrainSize(a[2]);
        }
        else if (a[1] == 3){
            setGrainInterval(a[2]);
        }
        else if (a[1] == 4){
            setRandomness(a[2]);
        }
        else if (a[1] == 5){
            setStart(a[2]);
        }
        else if (a[1] == 6){
            setEnd(a[2]);
        }
        else if (a[1] == 7){
            setGrainSize(a[2]);
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

    // Pitch
    public void setPitch(float f){
        gsp.setPitch( new Static((float)((f) * (pitchOffset))));
        if(f == 60){

        }
        else{
            gsp.getPitchUGen().start();
        }
    }

    // Grain Size
    public void setGrainSize(float f){
        gsp.setGrainSize( new Static(f));

    }

    // Grain Interval
    public void setGrainInterval(float f){
        gsp.setGrainInterval( new Static(f));
    }

    // Randomness
    public void setRandomness(float f){
        gsp.setRandomness( new Static(f));
    }

    // Start
    public void setStart(float f){
        gsp.setLoopStart( new Static(f*10));
    }

    // End
    public void setEnd(float f){
        gsp.setLoopEnd( new Static(f*10));
    }

    // Spray
    public void setSpray(float f){
        //gsp.setSpray( new Static(f));
    }

    // UPDATE View
    public void GUIUpdate(Label label, Spinner text, int knob){

        if(text != null){
            try{
                if(((Integer)text.getValue()) >= 0){
                    label.setText(String.valueOf(text.getValue()));
                    setKnobValue(knob, ((Integer) text.getValue()));
                }
            }
            catch (NumberFormatException e){
                System.out.println("Please enter a number");
            }
        }
        else{
            System.out.println("Please enter something valid");
        }
    }
    // SAMPLE
    public FileChooser loadSample(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.acac")
        );
        return fileChooser;
    }
    public String setSample(File file){
        String path = null;
        try {
            path = file.getCanonicalPath();
            samplePath = path;
            sampleReady = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        return path;
    }
    public String getSample(){
        return samplePath;
    }

    private GranularSamplePlayer mountGsp(){

        Sample sourceSample = null;

        try {
            sourceSample = new Sample(getSample());
            System.out.println("Sample was set to: " + getSample());
            sampleReady = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            sampleReady = false;
        }

        GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
        return gsp;

    }

    public void threadStart(){

    }

    @Override
    public void run(){

        System.out.println("RUNNING RUN");

        GranularSamplePlayer gsp  = mountGsp();

        ac.out.addInput(gsp);
        ac.start();

        while (sampleReady) {


            // KNOBS //
            // Pitch (Knob 1)
            if (!pitchToggle) {
                gsp.getPitchUGen().pause(true);
            }
            if (getKeysValue() > 0 && pitchToggle) {
                setKnobValue(1, (int) getKeysValue());
                setKeysValue(0);
            }

            if (getKnobValue(1) > 0 && pitchToggle == true) {
                gsp.setPitch(new Static(ac, (float) (getKnobValue(1) * (pitchOffset))));
            } else if (getKnobValue(1) == 0) {
                gsp.setPitch(new Static(1));
            }

            // Grain size (Knob 2)
            if (getKnobValue(2) > 0) {
                gsp.setGrainSize(new Static(ac, (float) (getKnobValue(2) * (sizeOffset))));
            } else if (getKnobValue(1) == 0) {
                setKnobValue(1, 63);
            }

            // Grain interval (Knob 3)
            if (getKnobValue(3) > 0) {
                gsp.setGrainInterval(new Static(ac, (float) (getKnobValue(3) * (intervalOffset))));
            } else if (getKnobValue(3) == 0) {
                setKnobValue(3, 63);
            }

            // Random (Knob 4)
            if (getKnobValue(4) > 0) {
                gsp.setRandomness(new Static(getKnobValue(4)));
            } else {
                setKnobValue(4, 0);
            }
            // Spray
            if (getKnobValue(7) > 0) {
                Random random = new Random();
                float max = getKnobValue(7) + 1;
                int min = 1;
                spray = random.nextInt((int) ((max - min) * sprayOffset));
            } else {
                spray = loopOffset;
            }
            // Loop start/end
            gsp.setLoopStart(new Static((float) ((getKnobValue(5)) * spray)));
            if (getKnobValue(5) > getKnobValue(6)) {
                setKnobValue(5, (int) getKnobValue(6) - 1);
            }
            gsp.setLoopEnd(new Static((float) ((getKnobValue(6)) * (spray))));

            // PADS
            switch (getPadValue()) {
                case 0:
                    System.out.println("0 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
                    setPadValue(padValueDummy);
                    break;

                case 1:
                    System.out.println("1 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_BACKWARDS);
                    setPadValue(padValueDummy);
                    break;

                case 2:
                    System.out.println("2 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
                    setPadValue(padValueDummy);
                    break;

                case 3:
                    System.out.println("3 has been triggered");
                    gsp.reset();
                    setPadValue(padValueDummy);
                    break;
            }
        }
    }
}