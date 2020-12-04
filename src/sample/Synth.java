package sample;


public class Synth {
    float[] knobValues = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
    int[] padValues = new int[]{0};

    //byte[] synthByte;
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
        if (a[1] > 43 && a[1] < 49) {
            System.out.println(a[1]);
            padValues[0] = a[1];
            System.out.println("Active pad is " + padValues[0]);

        } else {
            System.out.println("Something went wrong");
        }
    }

    public int getPadValue() {
        return padValues[0];
    }
    public void setPadValue(int i){
        padValues[0] = i;
    }

}
