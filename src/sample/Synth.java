package sample;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import java.util.concurrent.TimeUnit;

public class Synth implements Runnable {
    private float[] knobValues = new float[9];
    private int[] padValues = new int[]{0};
    private int[] keyValues = new int[]{0};
    public String samplePath;//??null and private/public
    MidiKeyboard midiKeyboard;
    Controller controller;
    View view;
    //boolean sampleReady = false;//??


    // Knob offsets
    final double pitchOffset = 0.1 / 6.35;
    final double sizeOffset = 0.7;
    final double intervalOffset = 4;
    double spray;
    final double sprayOffset = 10000;
    final double loopOffset = 100;
    final int padValueDummy = 10;
    AudioContext ac = new AudioContext();
    private boolean newSampleSelected = false;
    GranularSamplePlayer gsp;//??

    // maxvalue property
    private final DoubleProperty maxValue = new SimpleDoubleProperty(0.0);

    public DoubleProperty maxValueProperty() {
        return maxValue;
    }

    public final Double getMaxValueProperty() {
        return maxValueProperty().get();
    }

    public final void setMaxValue(Double maxValue) {
        maxValueProperty().set(maxValue);
    }

    //currentValue property
    private final DoubleProperty currentValue = new SimpleDoubleProperty(0.0);

    public DoubleProperty currentValueProperty() {
        return currentValue;
    }

    public final Double getCurrentValueProperty() {
        return currentValueProperty().get();
    }

    public final void setCurrentValue(Double currentValue) {
        currentValueProperty().set(currentValue);
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

/*    public Synth() {

        Sample sourceSample = null;

        try {
            sourceSample = new Sample("C:\\Users\\kaese47\\OneDrive\\Dokumenter\\SourceTree\\SynthFX\\Ring02.wav");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }*/




    // SAMPLE
    public FileChooser chooseSampleFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.acac")
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
            //sampleReady = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        //return path;??
    }

    public String getSample() {
        return samplePath;
    }

    public GranularSamplePlayer mountGspSample() {

        ac.stop();
        ac.out.kill();
        ac = new AudioContext();

        Sample sourceSample = null;
        boolean sampleReady = false;//??
        try {
            sourceSample = new Sample(getSample());
            System.out.println("Sample was set to: " + getSample());
            sampleReady = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            sampleReady = false;
        }

        //resize slider - value is in seconds;do it in new thread bc
        // https://www.reddit.com/r/javahelp/comments/7qvqau/problem_with_updating_gui_javafx/
        Sample finalSourceSample = sourceSample;
        Platform.runLater(() -> {
            this.setMaxValue(finalSourceSample.getLength() / 1000);
        });

        // instantiate a GranularSamplePlayer

        // tell gsp to loop the file
        gsp = new GranularSamplePlayer(ac, sourceSample);
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);

        // connect gsp to ac
        ac.out.addInput(gsp);

        // begin audio processing
        //ac.start();
        System.out.println("Does it reach here*");

        this.gsp.setSample(sourceSample);
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
        ac.out.clearInputConnections();//??
        ac.out.addInput(gsp);
        ac.start();
        return gsp;



    }



    public void updateAudioContext() {
        newSampleSelected = true;
    }//??

    public void pauseSample() {
        gsp.pause(true);
    }
    public void startSample () {
        gsp.pause(false);
    }

    @Override
    public void run() {
        System.out.println("OVERRIDE HAPPENED");

        // load the source sample from a file
        //this.setSample("C:\\Users\\kaese47\\OneDrive\\Dokumenter\\SourceTree\\SynthFX\\Sinus.wav");
        //gsp = this.mountGspSample();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //initialize a current value counter to count time up to max time
                final double[] currentValueCounter = {0.0};
                //initialize a max times counter which counts how many times you have went over
                //set max times counter to 1
                final int[] maxTimeCounter = {0};


                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //set current value counter to getTime()
                            //check if the current value counter is >= the max time
                            //IF YES
                            //encreses max times counter with 1
                            //set current value to (ac.getTime - (maxTime() * maxTimesCounter)
                            //IF NO
                            //???
                            currentValueCounter[0] = (ac.getTime() / 1000);
                            System.out.println("Current counter value" + currentValueCounter[0]);

                            boolean isAtEnd = currentValueCounter[0] >= (maxTimeCounter[0] * getMaxValueProperty());

                            if (isAtEnd) {
                                System.out.println("Max time counter " + maxTimeCounter[0]);
                                maxTimeCounter[0] = maxTimeCounter[0] + 1;
                                isAtEnd = false;

                            }
                            System.out.println("Current counter value 2 " + currentValueCounter[0]);

                            var newValue = currentValueCounter[0] - (getMaxValueProperty() * maxTimeCounter[0]);
                            setCurrentValue(newValue + getMaxValueProperty());
                            System.out.println("Setting current counter to" + newValue);

                        }
                    });
                    //System.out.println("Inside task");
                    //System.out.println();
                    TimeUnit.MILLISECONDS.sleep(100);
                }
            }
        };
        new Thread(() -> task.run()).start();

        System.out.println("After task run");

        // while-loop to configure modifiers live
        //while (gsp != null) {

            if (newSampleSelected) {
                //if a new sample is selected, load it and clear the flag
                gsp = this.mountGspSample();
                newSampleSelected = false;
            }


        //}
    }

    // KNOB
    public void receiveKnobMidi(byte[] a) {
        if (a[1] > 0 && a[1] <= knobValues.length) {
            knobValues[a[1]] = a[2];
            System.out.println("Knob " + a[1] + " value is set to " + knobValues[a[1]]);
        } else {
            System.out.println("Something went wrong");
        }
        if (a[1] == 1) {
            setPitch(a[2]);
        } else if (a[1] == 2) {
            setGrainSize(a[2]);
        } else if (a[1] == 3) {
            setGrainInterval(a[2]);
        } else if (a[1] == 4) {
            setRandomness(a[2]);
        } else if (a[1] == 5) {
            setStart(a[2]);
        } else if (a[1] == 6) {
            setEnd(a[2]);
        } else if (a[1] == 7) {
            setSpray(a[2]);
        }
        Platform.runLater(() -> {
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

    public void setKnobValue(int knobTransmitter, int value) {
        knobValues[knobTransmitter] = value;
    }

    // PAD
    public void receivePadMidi(byte[] a) {
        if (a[1] >= 0 && a[1] < 8) {
            System.out.println(a[1]);
            padValues[0] = a[1];
            System.out.println("Active pad is " + padValues[0]);
        }
        if (a[1] == 0) {
            setLoopForwards();
        }
        if (a[1] == 1) {
            setLoopBackwards();
        }
        if (a[1] == 2) {
            setLoopAlternating();
        }
        if (a[1] == 3) {
            setReset();
        }
    }

    public int getPadValue() {
        return padValues[0];
    }

    public void setPadValue(int i) {
        padValues[0] = i;
    }

    // KEYS
    public void receiveKeysMidi(byte[] a) {
        keyValues[0] = a[1];
        System.out.println("Key value is set to " + a[1]);
        setPitch(a[1]);
    }

    public float getKeysValue() {
        return keyValues[0];
    }

    public void setKeysValue(int value) {
        keyValues[0] = value;
    }

    // Pitch
    public void setPitch(float f) {
        gsp.setPitch(new Static((float) ((f) * (pitchOffset))));
        System.out.println(spray);
    }

    // Grain Size
    public void setGrainSize(float f) {
        gsp.setGrainSize(new Static((float) ((f) * (sizeOffset))));
    }

    // Grain Interval
    public void setGrainInterval(float f) {
        gsp.setGrainInterval(new Static((float) ((f) * intervalOffset)));
    }

    // Randomness
    public void setRandomness(float f) {
        gsp.setRandomness(new Static(f));
    }

    // Start
    public void setStart(float f) {

        //gsp.setLoopStart(new Static( (f)*100));
        if (getKnobValue(5) >= getKnobValue(6)) {
            setKnobValue(6, (int) f);
            gsp.setLoopStart(new Static((float) ((f) * (loopOffset))));
        } else {
            gsp.setLoopStart(new Static((float) ((f) * loopOffset)));
        }
    }

    // End
    public void setEnd(float f) {
        //gsp.setLoopEnd(new Static((f)*100));

        if (getKnobValue(6) <= getKnobValue(5)) {
            setKnobValue(5, (int) f);
            gsp.setLoopStart(new Static((float) ((f) * (loopOffset))));
        } else {
            gsp.setLoopEnd(new Static((float) ((f) * loopOffset)));
        }
    }

    // Spray
    public void setSpray(float f) {
        if (getKnobValue(7) > 0) {
            System.out.println("Spray through if, is set to: " + spray);
        }
    }

    public void setLoopForwards() {
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
    }

    public void setLoopBackwards() {
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_BACKWARDS);
    }

    public void setLoopAlternating() {
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
    }

    public void setReset() {
        gsp.reset();
    }



    // UPDATE View
    public void GUIUpdate(Label label, Spinner text, int knob) {
        if (text != null) {
            if ((Float.valueOf(text.getValue().toString())) >= 0) {
                try {

                    label.setText(String.valueOf(text.getValue()));
                    setKnobValue(knob, ((Integer) text.getValue()));
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number");
                } catch (NullPointerException n) {
                    System.out.println("Please enter an integer number");
                }
            }
        } else {
            System.out.println("Please enter something valid");
        }
    }




    }
