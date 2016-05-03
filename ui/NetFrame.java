package hvcs.ui;

import hvcs.net.SimNetwork;

import javax.swing.*;
import java.awt.event.ActionEvent;


class NetFrame extends JFrame {

    final SimNetwork network;

    final JLabel numClientsLbl = new JLabel ("0 Clients");

    final Action spawn = new AbstractAction("Spawn Client") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            new GuiClient(network.makeSocket());

            final int numClients = network.numClients();
            numClientsLbl.setText("" + numClients + (numClients == 1 ? " Client" : " Clients"));
        }
    };

    NetFrame(final SimNetwork network) {
        super("Simulated Network");
        this.network = network;

        final JButton spawnBtn = new JButton(spawn);

        new GroupLytExpress(getContentPane()) {
            @Override
            protected GroupLayout.Group horiz() {
                return prlCentered(numClientsLbl, spawnBtn);
            }

            @Override
            protected GroupLayout.Group vert() {
                return sequential(numClientsLbl, spawnBtn);
            }
        };

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

}
