package hvcs.net;


import java.io.FilterInputStream;
import java.io.IOException;

public class PacketInputStream extends FilterInputStream {

    public PacketInputStream(final PacketOutputStream out) {
        super(out.makeConnectedInput());
    }

    public Thread sinkPackets(final PacketSink sink) {
        final Thread t = new Thread(() -> {
            byte[] buf = new byte[SimNetwork.MAX_PACKET_CONTENTS];

            try {
                while (true) {
                    final int from = readShort(), to = readShort();
                    int length = readShort(), offset = 0;
                    while (length > 0) {
                        int numRead = read(buf, offset, length);
                        if (numRead < 0)
                            return;
                        offset += numRead;
                        length -= numRead;
                    }

                    sink.sendPacket(from, to, length, buf);
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }


    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    public short readShort() throws IOException {
        return (short) (read() | (read() << 8));
    }

}
