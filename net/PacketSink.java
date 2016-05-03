package hvcs.net;

@FunctionalInterface
public interface PacketSink {

    void sendPacket(final int source, final int dest, final int length, final byte[] contents);

}
