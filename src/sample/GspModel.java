package sample;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.Static;

public class GspModel {
boolean engine = true;
    public GspModel() {
        AudioContext ac = new AudioContext();
        // load the source sample from a file
        Sample sourceSample = null;
        boolean sampleReady = false;
        try {
            sourceSample = new Sample("bells.wav");
            sampleReady = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        // instantiate a GranularSamplePlayer
        GranularSamplePlayer gsp = new GranularSamplePlayer(ac, sourceSample);

        // connect gsp to ac
        ac.out.addInput(gsp);

        // instantiate synth and midikeyboard
        Synth synth = new Synth();
        MidiKeyboard midiKeyboard = new MidiKeyboard(synth);
        gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
        ac.start();

        // while-loop to configure modifiers live
         while (sampleReady) {

            // knobs

            // Pitch
            gsp.setPitch(new Static(ac, (synth.getKnobValue(1) * (0.1f) / (6.3f))));

            // Grain size
            if (synth.getKnobValue(2) == 0) {
                synth.setKnobValue(2, 1);
            }
            gsp.setGrainSize(new Static(ac, synth.getKnobValue(2) * (0.7f)));


            // Grain interval
            gsp.setGrainInterval(new Static(ac, (synth.getKnobValue(3) * (4))));
            if (synth.getKnobValue(3) == 0) {
                synth.setKnobValue(3, 1);
            }
            // Random
            gsp.setRandomness(new Static(synth.getKnobValue(4)));

            // Loop start/end
            gsp.setLoopStart(new Static(((synth.getKnobValue(5)) * 100)));
            if (synth.getKnobValue(5) > synth.getKnobValue(6)) {
                synth.setKnobValue(5, (int) synth.getKnobValue(6) - 1);
            }

            if ((synth.getKnobValue(6)) * (100) > sourceSample.getLength()) {
                synth.setKnobValue(6, (int) sourceSample.getLength() / 100);
            }
            gsp.setLoopEnd(new Static((synth.getKnobValue(6)) * (100)));

            // Random pan
            gsp.setRandomPan(new Static(ac, synth.getKnobValue(7) * (0.3f)));

            // Pads
            switch (synth.getPadValue()) {
                case 44:
                    System.out.println("44 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_FORWARDS);
                    synth.setPadValue(0);
                    break;

                case 45:
                    System.out.println("45 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_BACKWARDS);
                    synth.setPadValue(0);
                    break;

                case 46:
                    System.out.println("46 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.LOOP_ALTERNATING);
                    synth.setPadValue(0);
                    break;

                case 47:
                    System.out.println("47 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.NO_LOOP_FORWARDS);
                    synth.setPadValue(0);
                    break;
                case 48:
                    System.out.println("48 has been triggered");
                    gsp.setLoopType(SamplePlayer.LoopType.NO_LOOP_BACKWARDS);
                    synth.setPadValue(0);
                    break;
            }
        }
    }
}



