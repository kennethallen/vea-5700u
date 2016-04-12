package hvcs.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;


class ConfigPanel extends JPanel {

    final AbstractAction des = new AbstractAction("DES") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            veaRoundsLbl.setEnabled(false);
            veaRoundsSldr.setEnabled(false);
        }
    }, vea = new AbstractAction("VEA") {
        @Override
        public void actionPerformed(final ActionEvent e) {
            veaRoundsLbl.setEnabled(true);
            veaRoundsSldr.setEnabled(true);
        }
    };

    final JRadioButton dhRdio = new JRadioButton("Diffie–Hellman"),
            rsaRdio = new JRadioButton("RSA"),
            desRdio = new JRadioButton(des),
            veaRdio = new JRadioButton(vea);

    final JLabel veaRoundsLbl = new JLabel("VEA rounds:");
    final JSlider veaRoundsSldr = new JSlider(JSlider.HORIZONTAL, 2, 12, 8);

    public ConfigPanel() {
        final ButtonGroup asym = new ButtonGroup(), sym = new ButtonGroup();
        asym.add(dhRdio);
        asym.add(rsaRdio);
        dhRdio.setSelected(true);

        sym.add(desRdio);
        sym.add(veaRdio);
        desRdio.setSelected(true);
        des.actionPerformed(null);

        veaRoundsSldr.setMajorTickSpacing(2);
        veaRoundsSldr.setMinorTickSpacing(1);
        veaRoundsSldr.setPaintLabels(true);

        new GroupLytExpress(this) {
            @Override
            protected GroupLayout.Group horiz() {
                return prlLeading(
                        sequential(dhRdio, rsaRdio),
                        sequential(desRdio, veaRdio),
                        sequential(veaRoundsLbl, veaRoundsSldr));
            }
            @Override
            protected GroupLayout.Group vert() {
                return sequential(
                        prlOnBaseline(dhRdio, rsaRdio),
                        prlOnBaseline(desRdio, veaRdio),
                        prlOnBaseline(veaRoundsLbl, veaRoundsSldr));
            }
        };

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Configuration"
        ));
    }
}
