package hvcs.ui;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

class NetworkPanel extends JPanel {

    final Action clientView = new AbstractAction("Client") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            showCard("client");

            // Switching to client view stops server.
            stopServer.actionPerformed(e);
        }
    }, serverView = new AbstractAction("Server") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            showCard("server");
        }
    }, stopServer = new AbstractAction("Stop") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            serverControlBtn.setAction(startServer);
        }
    }, startServer = new AbstractAction("Listen") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            serverControlBtn.setAction(stopServer);
        }
    }, connectClient = new AbstractAction("Connect") {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    };

    final JRadioButton clientRdio = new JRadioButton(clientView),
            serverRdio = new JRadioButton(serverView);

    final JButton serverControlBtn = new JButton(startServer),
            connectBtn = new JButton(connectClient);

    final JTextField addressFld = new JTextField("127.0.0.1"),
            clientPortFld = new JTextField(),
            serverPortFld = new JTextField();

    final JPanel modalPnl = new JPanel(new CardLayout());
    private void showCard(final String name) {
        final CardLayout lyt = (CardLayout) modalPnl.getLayout();
        lyt.show(modalPnl, name);
    }

    public NetworkPanel() {
        ButtonGroup mode = new ButtonGroup();
        mode.add(clientRdio);
        mode.add(serverRdio);
        clientRdio.setSelected(true);

        final JLabel addressLbl = new JLabel("Address:"),
                serverPortLbl = new JLabel("Port:"),
                clientPortLbl = new JLabel("Port:");

        final JPanel clientCard = new JPanel(), serverCard = new JPanel();
        new GroupLytExpress(clientCard) {
            @Override
            protected GroupLayout.Group horiz() {
                return prlLeading(
                        sequential(addressLbl, addressFld),
                        sequential(clientPortLbl, clientPortFld, connectBtn));
            }
            @Override
            protected GroupLayout.Group vert() {
                return sequential(
                        prlOnBaseline(addressLbl, addressFld),
                        prlOnBaseline(clientPortLbl, clientPortFld, connectBtn));
            }
        };
        new GroupLytExpress(serverCard) {
            @Override
            protected GroupLayout.Group horiz() {
                return sequential(serverPortLbl, serverPortFld, serverControlBtn);
            }
            @Override
            protected GroupLayout.Group vert() {
                return prlOnBaseline(serverPortLbl, serverPortFld, serverControlBtn);
            }
        };
        modalPnl.add(clientCard, "client");
        modalPnl.add(serverCard, "server");

        new GroupLytExpress(this) {
            @Override
            protected GroupLayout.Group horiz() {
                return prlCentered(
                        sequential(clientRdio, serverRdio),
                        modalPnl);
            }
            @Override
            protected GroupLayout.Group vert() {
                return sequential(
                        prlOnBaseline(clientRdio, serverRdio),
                        modalPnl);
            }
        };

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Connection"));
    }
}
