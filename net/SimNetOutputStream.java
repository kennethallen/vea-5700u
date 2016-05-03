package hvcs.net;

import java.io.FilterOutputStream;
import java.io.IOException;


public class SimNetOutputStream extends FilterOutputStream {

    final int from, to;

    final byte[] buf = new byte[SimNetwork.MAX_PACKET_CONTENTS];
    int bufUsed = 0;

    public SimNetOutputStream(final PacketOutputStream out, final int from, final int to) {
        super(out);
        this.from = from;
        this.to = to;
    }

    @Override
    public void write(int b) throws IOException {
        buf[bufUsed++] = (byte) b;
        if (bufUsed >= buf.length)
            flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int toMove = Math.min(buf.length - bufUsed, len);
            System.arraycopy(b, off, buf, bufUsed, toMove);
            bufUsed += toMove;
            off += toMove;
            len -= toMove;

            if (bufUsed >= buf.length)
                flush();
        }
    }

    @Override
    public void flush() throws IOException {
        final PacketOutputStream out = (PacketOutputStream) this.out;
        out.writeShort(from);
        out.writeShort(to);
        out.writeShort(bufUsed);
        out.write(buf, 0, bufUsed);
        bufUsed = 0;
        super.flush();
    }

    public void writeShort(final int s) throws IOException {
        write(s);
        write(s >> 8);
    }

}
