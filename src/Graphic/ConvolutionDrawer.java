package Graphic;

import DataStructure.Convolution;
import DataStructure.Point;
import DataStructure.Population;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Sven on 23.05.2016.
 */
public class ConvolutionDrawer extends JFrame {

    private Convolution bestConvolution;
    private static int WIDTH = 900;
    private static int HEIGHT = 900;





    public ConvolutionDrawer(Convolution cs) {


        super("Convolution Drawer");
        this.bestConvolution = cs;
        setSize(new Dimension(WIDTH,HEIGHT));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int xOffset = this.getWidth() / 2;
        int yOffset = this.getHeight() / 2;
        int scale = 1;
        int xPos;
        int yPos;
        int xLastPos = xOffset;
        int yLastPos = yOffset;
        Point p;
        Convolution toDraw = bestConvolution;
        ArrayList<Point> coords = toDraw.getCoords();


        for (int i = 0; i < coords.size(); i++) {
            p = coords.get(i);
            xPos = (p.x() * scale * 20) + xOffset;
            yPos = (p.y() * scale * 20) + yOffset;


            if (toDraw.getPopulation().getSequence().get(i)) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillOval(xPos - ((10 * scale) / 2), yPos - ((10 * scale) / 2), 10 * scale, 10 * scale);

            g.setColor(Color.BLACK);
            g.drawLine(xPos, yPos, xLastPos, yLastPos);

            xLastPos = xPos;
            yLastPos = yPos;

        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>Generation: ");
        sb.append(toDraw.getPopulation().getmGeneration());
        sb.append("<br>Fitness: ");
        sb.append(String.format("%.2f",toDraw.getFitness()));
        sb.append("<br>H/H: ");
        sb.append(toDraw.getmPairs());
        sb.append("<br>Collisions: ");
        sb.append(toDraw.getmCollisons());
        sb.append("</body></html>");


        JLabel jLabel = new JLabel(sb.toString(), SwingConstants.LEFT);
        EmptyBorder eBorder = new EmptyBorder(10, 10, 10, 10); // oben, rechts, unten, links
        jLabel.setBorder(eBorder);
        jLabel.setVerticalAlignment(SwingConstants.TOP);
        this.getContentPane().add(jLabel);

    }

    public void setBestConvolution(Convolution cs) {
        this.bestConvolution = cs;
    }

}
