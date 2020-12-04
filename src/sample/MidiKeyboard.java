package sample;
import javax.sound.midi.*;
import java.util.List;

public class MidiKeyboard {
    MidiDevice device;

    public MidiKeyboard(Synth synth) {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
                System.out.println(infos[i]);
                List<Transmitter> transmitters = device.getTransmitters();
                for (int j = 0; j < transmitters.size(); j++) {
                    transmitters.get(j).setReceiver(
                            new MidiInputReceiver(device.getDeviceInfo().toString(), synth)
                    );
                }
                Transmitter trans = device.getTransmitter();
                trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString(), synth));
                device.open();
                System.out.println(device.getDeviceInfo() + " Was Opened");

            } catch (MidiUnavailableException e) {
            }
        }
    }

    public class MidiInputReceiver implements Receiver {
        public String name;
        private Synth synth;

        public MidiInputReceiver(String name, Synth synth) {
            this.name = name;
            this.synth = synth;
        }

        public void send(MidiMessage msg, long timeStamp) {
            byte[] aMsg = msg.getMessage();
            for (int i = 0; i < msg.getLength(); i++) {
                // set == to midi-transmitter
                if(aMsg[0] == -80){
                    synth.receiveKnobMidi(aMsg);
                }
                if (aMsg[0] == -112){
                    synth.receivePadMidi(aMsg);
                }
                //  System.out.println(aMsg[i]);
            }
        }

        public void close() {
        }
    }
}