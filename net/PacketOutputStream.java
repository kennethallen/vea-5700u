package hvcs.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


class PacketOutputStream extends FilterOutputStream {

    public PacketOutputStream() {
        super(new PipedOutputStream());
    }

    PipedInputStream makeConnectedInput() {
        try {
            return new PipedInputStream((PipedOutputStream) out);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeShort(final int s) throws IOException {
        write(s);
        write(s >> 8);
    }

    public SimNetOutputStream targetedByteStream(final int from, final int to) {
        return new SimNetOutputStream(this, from, to);
    }

}
