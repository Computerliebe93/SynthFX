package sample;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

import java.io.File;
import java.io.IOException;
import java.util.Random;


public class Synth {
    float[] knobValues = new float[9];
    int[] padValues = new int[]{0};
    int[] keyValues = new int[120];
    String samplePath;



    public Synth() {
    }

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

    public void receivePadMidi(byte[] a) {
        if (a[1] >= 0 && a[1] < 8) {
            System.out.println(a[1]);
            padValues[0] = a[1];
            System.out.println("Active pad is " + padValues[0]);

        } //else {
            //System.out.println("Something went wrong");
        }
     //}

    public int getPadValue() {
        return padValues[0];
    }
    public void setPadValue(int i){
        padValues[0] = i;
    }

    public void receiveKeysMidi(byte[] a){
        if (a[1] > 0 && a[1] <= keyValues.length) {
            keyValues[a[1]] = a[1];
            System.out.println("Key " + a[1] + " value is set to " + keyValues[a[1]]);
        } else {
            System.out.println("Something went wrong");
        }
    }

    public float getKeysValue(int keyTransmitter) {
            return keyValues[keyTransmitter];
    }
    public void setKeysValue(int keyTransmitter, int value){
        keyValues[keyTransmitter] = value;
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


class Gsp {
    public Gsp() {
        AudioContext ac = new AudioContext();
        // load the source sample from a file
        Sample sourceSample  = null;
        boolean sampleReady = false;
        // instantiate synth and midikeyboard
        Synth synth = new Synth();
        MidiKeyboard midiKeyboard = new MidiKeyboard(synth);

        try {
            if (synth.getSample() != null){
            sourceSample = new Sample(synth.getSample());
            sampleReady = true;}
            else{
                sourceSample = new Sample();
                sampleReady = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        // instantiate a GranularSamplePlayer
        GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);

        // connect gsp to ac
        ac.out.addInput(gsp);



        // while-loop to configure modifiers live
        do {
            // KNOBS //

            // Pitch (Knob 1)
            if (synth.getKnobValue(1) > 0) {
                gsp.setPitch(new Static(ac, (synth.getKnobValue(1) * (0.1f) / (6.3f))));
                //gsp.setPitch(new Static(ac, (float) (synth.getKeysValue(1) * (0.1f) / (63.5))));
                synth.setKeysValue(48, 0);
            } else {
                gsp.setPitch(new Static(1));
            }

            // Grain size (Knob 2)
            if (synth.getKnobValue(2) > 0) {
                gsp.setGrainSize(new Static(ac, synth.getKnobValue(2) * (0.7f)));
            } else if (synth.getKnobValue(1) == 0) {
                synth.setKnobValue(1, 63);
            }

            // Grain interval (Knob 3)
            if (synth.getKnobValue(3) > 0) {
                gsp.setGrainInterval(new Static(ac, (synth.getKnobValue(3) * (4))));
            } else if (synth.getKnobValue(3) == 0) {
                    synth.setKnobValue(3, 63);
                }

            // Random (Knob 4)
            if (synth.getKnobValue(4) == 0) {
                gsp.setRandomness(new Static(synth.getKnobValue(4)));
            } else {
                synth.setKnobValue(4, 0);
            }
            // Spray
            float spray = 100;
            if (synth.getKnobValue(7) > 0) {
                Random random = new Random();
                float max = synth.getKnobValue(7) + 1;
                int min = 1;
                spray = random.nextInt((int) (max - min) * 10000);
            }
                else {
                spray = 100;
            }
                // Loop start/end
                gsp.setLoopStart(new Static(((synth.getKnobValue(5)) * spray)));
                if (synth.getKnobValue(5) > synth.getKnobValue(6)) {
                    synth.setKnobValue(5, (int) synth.getKnobValue(6) - 1);
                }
                gsp.setLoopEnd(new Static((synth.getKnobValue(6)) * (spray)));


            // PADS
            switch (synth.getPadValue()) {
                case 0:
                    System.out.println("0 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
                    synth.setPadValue(10);
                    break;

                case 1:
                    System.out.println("1 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_BACKWARDS);
                    synth.setPadValue(10);
                    break;

                case 2:
                    System.out.println("2 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
                    synth.setPadValue(10);
                    break;

                case 3:
                    System.out.println("3 has been triggered");
                    gsp.reset();
                    synth.setPadValue(10);
                    break;

                case 4:
                    System.out.println("4 has been triggered");
                    gsp.start();
                    ac.start();
                    break;
            }
            ac.start();
        } while (sampleReady);
    }
}






