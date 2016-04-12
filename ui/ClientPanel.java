package hvcs.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


class ClientPanel extends JPanel {

    final JTextArea console = new JTextArea(
            "Listening on port ####.\n" +
            "Client connected.\n" +
            "Performing handshake.");

    final SharedImagePanel sharedImage;
    final NetworkPanel network = new NetworkPanel();
    final ConfigPanel config = new ConfigPanel();

    final JLabel sendingImg = new JLabel(new ImageIcon(
                  ClientPanel.class.getResource("/hvcs/ui/important-image.png"))),
            receivingImg = new JLabel(new ImageIcon(
                    ClientPanel.class.getResource("/hvcs/ui/important-image.png")));

    final JButton transmitBtn = new JButton("Transmit!");

    public ClientPanel(final BufferedImage _sharedImage) {
        sharedImage = new SharedImagePanel(_sharedImage);

        console.setEditable(false);

        new GroupLytExpress(this) {
            @Override
            protected GroupLayout.Group horiz() {
                return sequential(
                        prlCentered(
                                sequential(sharedImage),
                                sequential(sendingImg, receivingImg)),
                        prlCentered(config, network, transmitBtn, console));
            }
            @Override
            protected GroupLayout.Group vert() {
                return prlLeading(
                        sequential(
                                prlCentered(sharedImage),
                                prlCentered(sendingImg, receivingImg)),
                        sequential(config, network, transmitBtn, console));
            }
        };
    }
}