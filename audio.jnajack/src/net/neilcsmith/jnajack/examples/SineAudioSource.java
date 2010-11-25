
package net.neilcsmith.jnajack.examples;

import java.nio.FloatBuffer;
import java.util.EnumSet;
import net.neilcsmith.jnajack.Jack;
import net.neilcsmith.jnajack.JackPortFlags;
import net.neilcsmith.jnajack.JackPortType;
import net.neilcsmith.jnajack.util.SimpleAudioClient;

/**
 *
 * @author Neil C Smith
 */
public class SineAudioSource implements SimpleAudioClient.Processor {

    private final static int TABLE_SIZE = 200;
    private int left_phase = 0;
    private int right_phase = 0;
    private float[] data;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        SimpleAudioClient client = SimpleAudioClient.create("sine", new String[0],
                new String[]{"output-L", "output-R"}, new SineAudioSource());
        client.activate();
        Jack jack = Jack.getInstance();
        String[] ports = jack.getPorts(null, JackPortType.AUDIO,
                EnumSet.of(JackPortFlags.JackPortIsInput, JackPortFlags.JackPortIsPhysical));
        if (ports.length > 1) {
            jack.connect("sine:output-L", ports[0]);
            jack.connect("sine:output-R", ports[1]);
        } else {
            System.out.println("Can't connect ports");
        }
        while (true) {
            Thread.sleep(1000);
        }
    }

    public void setup(float samplerate, int buffersize) {
        data = new float[TABLE_SIZE];
        for (int i=0; i < TABLE_SIZE; i++) {
            data[i] = (float) (0.2 * Math.sin( ((double)i/(double)TABLE_SIZE) * Math.PI * 2.0 ));
        }
    }

    public void process(FloatBuffer[] inputs, FloatBuffer[] outputs) {
//        for (FloatBuffer buf : outputs) {
//            int size = buf.capacity();
//            for (int i=0; i < size; i++) {
//                buf.put(i, (float) Math.random() - 0.5f);
//            }
//        }
        FloatBuffer left = outputs[0];
        FloatBuffer right = outputs[1];
        int size = left.capacity();
        for (int i=0; i<size; i++) {
            left.put(i, data[left_phase]);
            right.put(i, data[right_phase]);
            left_phase += 2;
            right_phase += 3;
            if (left_phase >= TABLE_SIZE) {
                left_phase -= TABLE_SIZE;
            }
            if (right_phase >= TABLE_SIZE) {
                right_phase -= TABLE_SIZE;
            }
        }
    }

    public void shutdown() {
        System.out.println("Sine Audio Source shutdown");
    }
}
