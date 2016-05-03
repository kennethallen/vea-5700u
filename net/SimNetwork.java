package hvcs.net;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;


public class SimNetwork implements PacketSink {

    public static final int MAX_PACKET_CONTENTS = 4096;

    final ArrayList<SimSocket> connects = new ArrayList<>();

    public int numClients() {
        return connects.size();
    }

    public class SimSocket {

        public final int id;

        private final PacketOutputStream toNet = new PacketOutputStream();
        private final PacketInputStream fromClient;

        private final PipedOutputStream toClient = new PipedOutputStream();
        public final PipedInputStream fromNet;

        private SimSocket(final int id) {
            this.id = id;
            fromClient = new PacketInputStream(toNet);
            fromClient.sinkPackets(SimNetwork.this);

            try {
                fromNet = new PipedInputStream(toClient);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        public SimNetOutputStream openByteStream(final int target) {
            return toNet.targetedByteStream(id, target);
        }

        public void close() {
            try {
                toNet.close();
                toClient.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        public SimNetwork network() {
            return SimNetwork.this;
        }
    }

    @Override
    public void sendPacket(final int source, final int dest, final int length, final byte[] contents) {
        try {
            connects.get(dest).toClient.write(contents, 0, length);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimSocket makeSocket() {
        final SimSocket sock = new SimSocket(connects.size());
        connects.add(sock);
        return sock;
    }

}