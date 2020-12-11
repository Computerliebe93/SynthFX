package sample;
import javafx.stage.FileChooser;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.audiofile.FileFormatException;
import net.beadsproject.beads.data.audiofile.OperationUnsupportedException;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Synth implements Runnable {
    boolean pitchToggle = false;
    boolean grainSizeToggle = false;
    boolean grainIntervalToggle = false;
    boolean randomToggle = false;
    boolean startPointToggle = false;
    boolean endPointToggle = false;
    boolean sprayToggle = false;

    private float[] knobValues = new float[9];
    private int[] padValues = new int[]{0};
    private int[] keyValues = new int[]{0};
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
    }

    public float getKeysValue() {
        return keyValues[0];
    }

    public void setKeysValue(int value) {
        keyValues[0] = value;
    }

    public FileChooser loadSample() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.acac")
        );
        return fileChooser;
    }

    public String setSample(File file) {
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

    public String getSample() {
        return samplePath;
    }

    @Override
    public void run() {
        System.out.println("OVERRIDE HAPPENED");
        AudioContext ac = new AudioContext();
        // load the source sample from a file
        Sample sourceSample = null;
        boolean sampleReady = false;
        // instantiate synth and midikeyboard
        String samplePath = getSample();
        try {
            sourceSample = new Sample("JV2080.wav");
            sampleReady = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
            sampleReady = false;
        }
        // instantiate a GranularSamplePlayer
        GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);
        // connect gsp to ac
        ac.out.addInput(gsp);
        ac.start();

        // while-loop to configure modifiers live
         while (sampleReady) {
             // KNOBS //
             // Pitch (Knob 1)
/*
            if (getKeysValue() > 0 && pitchToggle == true) {
                    setKnobValue(1, (int) getKeysValue());
                    setKeysValue(0);
                }
            if (getKnobValue(1) > 0 || pitchToggle == true) {
                gsp.setPitch(new Static(ac, (float) (getKnobValue(1) * (0.1 / 6.35))));
            }
             if (getKnobValue(1) == 0 || pitchToggle == false) {
                 setKnobValue(1,63);
                 // gsp.setPitch(new Static(ac, 1)); // not optimal
                }
          /*
             // Grain size (Knob 2)
             if (getKnobValue(2) > 0 || pitchToggle == true) {
                 gsp.setGrainSize(new Static(ac, (float) (getKnobValue(2) * (sizeOffset))));
             }
             if (grainSizeToggle == false) {
                 setKnobValue(2, 0);
                 gsp.setGrainSize(new Static(ac, (float) ((float) sourceSample.getLength() / 12)));
             }

             // Grain interval (Knob 3)
             if (getKnobValue(3) > 0 || grainIntervalToggle == true) {
                 gsp.setGrainInterval(new Static(ac, (float) (getKnobValue(3) * (intervalOffset))));
             } else if (grainIntervalToggle == false) {
                 gsp.setGrainInterval(new Static(ac, 67));
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
 */
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
                /*
                case 3:
                    System.out.println("3 has been triggered");
                    gsp.reset();
                    setPadValue(padValueDummy);
                    break; */
            }
        }
    }
}