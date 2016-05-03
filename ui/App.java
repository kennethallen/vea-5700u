package hvcs.ui;

import hvcs.net.SimNetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.UIManager.*;

/**
 * Executable entry point for GUI.
 */
public class App {

    public static void main(final String[] args) {
        initLookAndFeel();

        final NetFrame net = new NetFrame(new SimNetwork());
        net.setLocationRelativeTo(null);
        net.setVisible(true);
    }

    private static BufferedImage sharedSecret;
    static synchronized BufferedImage sharedSecret() {
        if (sharedSecret == null) {
            try {
                sharedSecret = ImageIO.read(App.class.getResource("/hvcs/ui/shared-secret.png"));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return sharedSecret;
    }

    /**
     * Use something better-looking than the basic L&F.
     */
    static void initLookAndFeel() {
        try {
            for (final LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    setLookAndFeel(info.getClassName());
                    return;
                }
            }
        } catch (final Exception e) {
        }

        // Failed to set Nimbus; set system L&F instead.
        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (final Exception e) {
        }
    }

}
