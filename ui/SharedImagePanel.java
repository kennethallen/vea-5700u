package hvcs.ui;

import hvcs.Client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


class SharedImagePanel extends JPanel {

    public static final Color COORD_HIGHLIGHT_COLOR = Color.yellow;

    final BufferedImage image;
    final Dimension imageDim;
    private final ArrayList<Point> coords = new ArrayList<>(Client.MAX_COORDS);

    final JPanel canvas;
    final JLabel statusLbl = new JLabel();

    SharedImagePanel(final BufferedImage _image) {
        image = _image;
        imageDim = new Dimension(image.getWidth(), image.getHeight());

        // Buttons that clear and randomize the selected coordinates.
        final JButton clearBtn = new JButton(new AbstractAction("Clear") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                coords.clear();
                coordsChanged();
            }
        }), randomizeBtn = new JButton(new AbstractAction("Randomize") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                coords.clear();

                final Random rand = ThreadLocalRandom.current();
                while (coords.size() < Client.MAX_COORDS) {
                    coords.add(new Point(
                            rand.nextInt(imageDim.width),
                            rand.nextInt(imageDim.height)));
                }
                coordsChanged();
            }
        });

        // The canvas that shows the image and coord highlights, and listens for clicks.
        canvas = new JPanel() {
            {
                setMinimumSize(imageDim);
                setMaximumSize(imageDim);
                setPreferredSize(imageDim);

                // On click, add coordinate, replacing oldest if full.
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(final MouseEvent e) {
                        if (coords.size() >= Client.MAX_COORDS) {
                            coords.remove(0);
                        }
                        coords.add(new Point(e.getX(), e.getY()));
                        coordsChanged();
                    }
                });
            }

            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);

                g.drawImage(image, 0, 0, this);

                g.setColor(COORD_HIGHLIGHT_COLOR);
                for (final Point p : coords) {
                    g.drawRect(p.x - 1, p.y - 1, 2, 2);
                }
            }
        };

        // The following controls the layout with a utility class I wrote.
        new GroupLytExpress(this, true, false) {
            @Override
            protected GroupLayout.Group horiz() {
                return sequential(
                        canvas,
                        gap(10),
                        prlLeading(statusLbl, clearBtn, randomizeBtn));
            }
            @Override
            protected GroupLayout.Group vert() {
                return prlTrailing(
                        canvas,
                        sequential(statusLbl, gap(5), clearBtn, randomizeBtn));
            }
        };

        // Styling details.
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                "Image-Based Secret Sharing"));

        coordsChanged();
    }

    /**
     * Called whenever coordinate list is modified to update UI.
     */
    private void coordsChanged() {
        // Repaint highlights
        canvas.repaint();

        final StringBuilder sb = new StringBuilder("<html><body><strong>");
        sb.append(coords.size()).append('/').append(Client.MAX_COORDS).append(" coordinate");
        if (coords.size() != 1) {
            sb.append('s');
        }
        sb.append(" selected:</strong>");

        if (!coords.isEmpty()) {
            sb.append("<br/>");
            for (final Point p : coords) {
                sb.append('(').append(p.x).append(',').append(p.y).append(") ");
            }
            sb.setLength(sb.length() - 1); // Trim trailing space
        }
        statusLbl.setText(sb.append("</body></html>").toString());
    }

    /**
     * Returns the current selection of coordinates on the image selected by the user
     * (or at random).
     */
    java.util.List<Point> getSelectedCoords() {
        return Collections.unmodifiableList(coords);
    }

}
