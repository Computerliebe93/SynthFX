package sample;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    private AudioContext ac = new AudioContext();
    private boolean newSampleSelected = false;
    private GranularSamplePlayer gsp;

    // maxvalue property
    private final DoubleProperty maxValue = new SimpleDoubleProperty(0.0);
    public DoubleProperty maxValueProperty() {
        return maxValue ;
    }
    public final Double getMaxValueProperty() {
        return maxValueProperty().get();
    }
    public final void setMaxValue(Double maxValue) {
        maxValueProperty().set(maxValue);
    }
    
    //currentValue property
    private final DoubleProperty currentValue = new SimpleDoubleProperty(0.0);
    public DoubleProperty currentValueProperty() {return currentValue;}
    public final Double getCurrentValueProperty() {return currentValueProperty().get();}
    public final void setCurrentValue(Double currentValue) {
        currentValueProperty().set(currentValue);
        //TODO: change the value to reflect the slide -> which value should be changed?
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setMidiKeyboard(MidiKeyboard midiKeyboard) {
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

    public FileChooser chooseSampleFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.acac")
        );
        return fileChooser;
    }

    public void setSample(String filePath) {
        String path = null;
        try {
            //check if such a file exists
            File selectedFile = new File(filePath);
            path = selectedFile.getCanonicalPath();
            samplePath = filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path);
    }
    public String getSample(){
        return samplePath;
    }

    private GranularSamplePlayer playSample(){
        ac.stop();
        ac.out.kill();
        ac = new AudioContext();

        Sample sourceSample = null;
        boolean sampleReady = false;
        // instantiate synth and midikeyboard

        try {
            sourceSample = new Sample(this.getSample());
            sampleReady = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            //System.exit(1);
            sampleReady = false;
        }

        //resize slider - value is in seconds;do it in new thread bc
        // https://www.reddit.com/r/javahelp/comments/7qvqau/problem_with_updating_gui_javafx/
        Sample finalSourceSample = sourceSample;
        Platform.runLater(()->{
            this.setMaxValue(finalSourceSample.getLength()/1000);
        });


        // instantiate a GranularSamplePlayer
        GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
        // connect gsp to ac
        ac.out.addInput(gsp);
        ac.start();
        return gsp;
    }

    public void updateAudioContext() {
        newSampleSelected = true;
    }

    @Override
    public void run() {
        System.out.println("OVERRIDE HAPPENED");

        // load the source sample from a file
        this.setSample("C:\\Users\\baker\\Documents\\SourceTree\\SynthFX\\Ring02.wav");
        gsp = this.playSample();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(true) {
                    setCurrentValue(ac.getTime() / 1000);
                    System.out.println("Inside task");
                    System.out.println(ac.getTime() / 1000);
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }
        };
        task.run();


        // while-loop to configure modifiers live
        while (gsp != null) {
            if (newSampleSelected) {
                //if a new sample is selected, load it and clear the flag
                gsp = this.playSample();
                newSampleSelected = false;
            }



            // KNOBS //
            // Pitch (Knob 1)
            if (!pitchToggle) {

            }
            if (getKeysValue() > 0 && pitchToggle == true) {
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