package hvcs.ui;

import hvcs.Client;
import hvcs.net.SimNetwork;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


class GuiClient extends Client {

    final JFrame frame;
    final JTextArea console = new JTextArea();

    final SharedImagePanel sharedImage;
    final ConfigPanel config = new ConfigPanel();

    final JLabel imgLeftLbl = new JLabel(),
            imgRightLbl = new JLabel();

    final JLabel targetSocketLbl = new JLabel("to target socket:");
    final JSpinner targetSocketSpnr = new JSpinner(new AbstractSpinnerModel() {
        int target;
        @Override
        public Object getValue() {
            return target;
        }
        @Override
        public void setValue(Object value) {
            if (!(value instanceof Number))
                throw new IllegalArgumentException();
            target = ((Number) value).intValue();
            fireStateChanged();
        }
        @Override
        public Object getNextValue() {
            if (target >= socket.network().numClients() - 1)
                return null;
            return target + 1;
        }
        @Override
        public Object getPreviousValue() {
            if (target == 0)
                return null;
            else
                return target - 1;
        }
    });

    final Action loadImg = new AbstractAction("Load Image") {
        @Override
        public void actionPerformed(final ActionEvent ae) {
            try {
                final JFileChooser fc = new JFileChooser();
                final int status = fc.showDialog(frame, "Open");
                if (status == JFileChooser.APPROVE_OPTION) {
                    toSend = ImageIO.read(fc.getSelectedFile());
                    imgLeftLbl.setIcon(new ImageIcon(toSend));
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }
    };
    final JButton loadImgBtn = new JButton(loadImg);

    final Action transmit = new AbstractAction("Transmit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> sendImage(((Number) targetSocketSpnr.getValue()).intValue())).start();
        }
    };
    final JButton transmitBtn = new JButton(transmit);

    public GuiClient(final SimNetwork.SimSocket socket) {
        super(socket);

        frame = new JFrame("Image Transmitter - Client ID=" + socket.id);

        sharedImage = new SharedImagePanel(App.sharedSecret());

        console.setEditable(false);

        new GroupLytExpress(frame.getContentPane()) {
            @Override
            protected GroupLayout.Group horiz() {
                return sequential(
                        prlCentered(
                                sequential(sharedImage),
                                sequential(imgLeftLbl, imgRightLbl)),
                        prlCentered(loadImgBtn, config, sequential(transmitBtn, targetSocketLbl, targetSocketSpnr), console));
            }
            @Override
            protected GroupLayout.Group vert() {
                return prlLeading(
                        sequential(
                                prlCentered(sharedImage),
                                prlCentered(imgLeftLbl, imgRightLbl)),
                        sequential(loadImgBtn, config, prlOnBaseline(transmitBtn, targetSocketLbl, targetSocketSpnr), console));
            }
        };
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                socket.close();
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void printStatus(final String msg) {
        SwingUtilities.invokeLater(() -> {
            console.setText(console.getText() + '\n' + msg);
        });
    }

    @Override
    public void imageReceived(final BufferedImage img) {
        SwingUtilities.invokeLater(() -> {
            final JDialog dialog = new JDialog(frame, "Image received!");
            dialog.add(new JLabel(new ImageIcon(img)));
            dialog.pack();
            dialog.setVisible(true);
        });
    }

}