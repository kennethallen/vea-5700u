package hvcs.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

import static javax.swing.UIManager.*;

/**
 * Executable entry point for GUI.
 */
public class App {

    public static void main(final String[] args) {
        initLookAndFeel();

        final JFrame frame = new JFrame("Encrypted Image Transfer Client - Group 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            frame.setContentPane(new ClientPanel(
                    ImageIO.read(App.class.getResource("/hvcs/ui/shared-secret.png"))));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setVisible(true);
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
