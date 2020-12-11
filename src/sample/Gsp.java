package sample;

import javafx.application.Platform;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;
import java.util.Random;

public class Gsp implements Runnable {

    Synth synth;
    View view;

    public void setSynth(Synth synth){
        this.synth = synth;
    }


    @Override
    public void run() {
        System.out.println("OVERRIDE HAPPENED");
        AudioContext ac = new AudioContext();
        // load the source sample from a file
        Sample sourceSample = null;
        boolean sampleReady = false;
        // instantiate synth and midikeyboard

        try {
            sourceSample = new Sample("Ring02.wav");
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

        //
        // while-loop to configure modifiers live
        while (sampleReady) {
            // KNOBS //
            // Pitch (Knob 1)
            if (!synth.pitchToggle) {
                gsp.getPitchUGen().pause(true);
            }
            if (synth.getKeysValue() > 0 && synth.pitchToggle) {
                synth.setKnobValue(1, (int) synth.getKeysValue());
                synth.setKeysValue(0);
            }

            if (synth.getKnobValue(1) > 0 && synth.pitchToggle == true) {
                gsp.setPitch(new Static(ac, (float) (synth.getKnobValue(1) * (synth.pitchOffset))));
            } else if (synth.getKnobValue(1) == 0) {
                gsp.setPitch(new Static(1));
            }

            // Grain size (Knob 2)
            if (synth.getKnobValue(2) > 0) {
                gsp.setGrainSize(new Static(ac, (float) (synth.getKnobValue(2) * (synth.sizeOffset))));
            } else if (synth.getKnobValue(1) == 0) {
                synth.setKnobValue(1, 63);
            }

            // Grain interval (Knob 3)
            if (synth.getKnobValue(3) > 0) {
                gsp.setGrainInterval(new Static(ac, (float) (synth.getKnobValue(3) * (synth.intervalOffset))));
            } else if (synth.getKnobValue(3) == 0) {
                synth.setKnobValue(3, 63);
            }

            // Random (Knob 4)
            if (synth.getKnobValue(4) > 0) {
                gsp.setRandomness(new Static(synth.getKnobValue(4)));
            } else {
                synth.setKnobValue(4, 0);
            }
            // Spray
            if (synth.getKnobValue(7) > 0) {
                Random random = new Random();
                float max = synth.getKnobValue(7) + 1;
                int min = 1;
                synth.spray = random.nextInt((int) ((max - min) * synth.sprayOffset));
            } else {
                synth.spray = synth.loopOffset;
            }
            // Loop start/end
            gsp.setLoopStart(new Static((float) ((synth.getKnobValue(5)) * synth.spray)));
            if (synth.getKnobValue(5) > synth.getKnobValue(6)) {
                synth.setKnobValue(5, (int) synth.getKnobValue(6) - 1);
            }
            gsp.setLoopEnd(new Static((float) ((synth.getKnobValue(6)) * (synth.spray))));

            // PADS
            switch (synth.getPadValue()) {
                case 0:
                    System.out.println("0 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
                    synth.setPadValue(synth.padValueDummy);
                    break;

                case 1:
                    System.out.println("1 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_BACKWARDS);
                    synth.setPadValue(synth.padValueDummy);
                    break;

                case 2:
                    System.out.println("2 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
                    synth.setPadValue(synth.padValueDummy);
                    break;

                case 3:
                    System.out.println("3 has been triggered");
                    gsp.reset();
                    synth.setPadValue(synth.padValueDummy);
                    break;
            }
        }
    }
}