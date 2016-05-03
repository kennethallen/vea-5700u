package hvcs;


import hvcs.net.SimNetOutputStream;
import hvcs.net.SimNetwork;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;

public abstract class Client {

    protected Client(final SimNetwork.SimSocket socket) {
        this.socket = socket;

        final Thread t = new Thread(() -> {
            try {
                final InputStream in = socket.fromNet;
                while (true) {
                    final int width = in.read() | (in.read() >> 8),
                            height = in.read() | (in.read() >> 8);
                    final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    for (int x = 0; x < width; x++)
                        for (int y = 0; y < height; y++)
                            img.setRGB(x, y, (in.read() << 16) | (in.read() << 8) | in.read());

                    imageReceived(img);
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    enum AsymMode {
        Plaintext, DH, RSA
    }

    enum SymMode {
        Plaintext, Caeser, DES, VEA
    }

    public int veaRounds = 8, veaBlockSize;

    public static final int MAX_COORDS = 5;
    public final ArrayDeque<Point> coords = new ArrayDeque<>(5);

    public BufferedImage toSend, encrypted;

    public abstract void printStatus(final String msg);

    public final SimNetwork.SimSocket socket;

    public abstract void imageReceived(final BufferedImage img);

    public void sendImage(final int target) {
        try {
            final SimNetOutputStream out = socket.openByteStream(target);
            final int width = toSend.getWidth(), height = toSend.getHeight();

            out.writeShort(width);
            out.writeShort(height);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    final int rgb = toSend.getRGB(x, y);
                    out.write(rgb >> 16);
                    out.write(rgb >> 8);
                    out.write(rgb);
                }
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
