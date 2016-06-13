package Graphic;

import DataStructure.Convolution;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Sven on 13.06.2016.
 */
public class BetterDrawer extends JFrame implements ActionListener {

    JPanel p;
    JButton buttonPlus, buttonMinus;
    double factor = 1;
    private Convolution bestConvolution;

    public BetterDrawer(Convolution cs) {
        super("ZOOM");
        this.bestConvolution = cs;


        p = new DrawingPanel(cs);
        JScrollPane scrollBar = new JScrollPane(p);
        add(scrollBar, BorderLayout.CENTER);

        buttonMinus = new JButton(" - ");
        buttonPlus = new JButton(" + ");
        buttonMinus.addActionListener(this);
        buttonPlus.addActionListener(this);
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(buttonMinus);
        box.add(Box.createHorizontalStrut(20));
        box.add(buttonPlus);
        box.add(Box.createHorizontalGlue());

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>Generation: ");
        sb.append(cs.getPopulation().getmGeneration());
        sb.append("<br>Fitness: ");
        sb.append(String.format("%.2f",cs.getFitness()));
        sb.append("<br>H/H: ");
        sb.append(cs.getmPairs());
        sb.append("<br>Collisions: ");
        sb.append(cs.getmCollisons());
        sb.append("</body></html>");


        JLabel jLabel = new JLabel(sb.toString(), SwingConstants.CENTER);
        EmptyBorder eBorder = new EmptyBorder(10, 10, 10, 10); // oben, rechts, unten, links
        jLabel.setBorder(eBorder);
        jLabel.setVerticalAlignment(SwingConstants.TOP);


        add(box, BorderLayout.SOUTH);
        add(jLabel, BorderLayout.NORTH);
        add(new JLabel("     "), BorderLayout.EAST);
        add(new JLabel("     "), BorderLayout.WEST);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonPlus) {
            factor += 0.1;
        } else {
            factor -= 0.1;
        }
        buttonMinus.setEnabled(factor > 0.1);
        buttonPlus.setEnabled(factor < 4.0);
        p.repaint();
    }

    class DrawingPanel extends JPanel {

        private Convolution bestConvolution;

        DrawingPanel(Convolution cs) {
            super(null);
            this.bestConvolution = cs;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int xOffset = this.getWidth() / 2;
            int yOffset = this.getHeight() / 2;
            int xPos;
            int yPos;
            int width = 0;
            int height = 0;
            int xLastPos = xOffset;
            int yLastPos = yOffset;
            int lineOffset = (int)(10 * factor) / 2;
            DataStructure.Point p;
            Convolution toDraw = bestConvolution;
            ArrayList<DataStructure.Point> coords = toDraw.getCoords();


            for (int i = 0; i < coords.size(); i++) {
                p = coords.get(i);
                xPos = (int)(p.x() * factor * 20) + xOffset;
                yPos = (int)(p.y() * factor * 20) + yOffset;
                width = (int)(10 * factor);
                height = (int)(10 * factor);

                g.setColor(Color.BLACK);
                g.drawLine(xPos , yPos, xLastPos, yLastPos);

                if (toDraw.getPopulation().getSequence().get(i)) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.GREEN);
                }
                g.fillOval(xPos - lineOffset,yPos - lineOffset,width - lineOffset,height - lineOffset);
                //g.fillOval(xPos - ((10 * factor) / 2), yPos - ((10 * factor) / 2), 10 * factor, 10 * factor);


                xLastPos = xPos;
                yLastPos = yPos;

            }
        }

    }
}


