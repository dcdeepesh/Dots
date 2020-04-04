package deepe.sh.dots;

import static deepe.sh.dots.Game.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

class BoardComponent extends JComponent {
    private static final long serialVersionUID = 1L;

    public BoardComponent() {
        addMouseListener(new MouseEventListener());
        addMouseMotionListener(new MouseEventListener());
    }

    private void drawBox(Box box, Graphics2D g2d) {
        // reference points
        Point2D topLeft =
                new Point2D.Double(MARGIN + RAD + box.getX() * (DIST + 2*RAD),
                                   MARGIN + RAD + box.getY() * (DIST + 2*RAD));
        Point2D bottomRight =
                new Point2D.Double(topLeft.getX() + DIST + 2*RAD,
                                   topLeft.getY() + DIST + 2*RAD);

        if (box.getX() == 0 && box.isClosed(Box.Side.LEFT))
            g2d.draw(new Line2D.Double
                    (topLeft, new Point2D.Double(topLeft.getX(), bottomRight.getY())));

        if (box.getY() == 0 && box.isClosed(Box.Side.UP))
            g2d.draw(new Line2D.Double
                    (topLeft, new Point2D.Double(bottomRight.getX(), topLeft.getY())));

        if (box.isClosed(Box.Side.DOWN))
            g2d.draw(new Line2D.Double
                    (new Point2D.Double(topLeft.getX(), bottomRight.getY()), bottomRight));

        if (box.isClosed(Box.Side.RIGHT))
            g2d.draw(new Line2D.Double
                    (new Point2D.Double(bottomRight.getX(), topLeft.getY()), bottomRight));
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke defaultStroke = g2d.getStroke();

        // draw the hintline if it exists
        g2d.setPaint(HINTLINE_COLOUR);
        if (Hintline.exists()) {
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_ROUND, 0, new float[]{3}, 0));

            if (Hintline.getType() == Hintline.Type.HORIZONTAL)
                g2d.draw(new Line2D.Double(Hintline.getX1(), Hintline.getY1(),
                                           Hintline.getX2(), Hintline.getY1()));
            else
                g2d.draw(new Line2D.Double(Hintline.getX1(), Hintline.getY1(),
                                           Hintline.getX1(), Hintline.getY2()));
        }

        // draw the links (if any)
        g2d.setPaint(CIRCLE_COLOUR);
        g2d.setStroke(new BasicStroke(RAD-1, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
        for (int y = 0; y < Board.size-1; y++)
            for (int x = 0; x < Board.size-1; x++)
                drawBox(Board.boxAt(x, y), g2d);

        // draw the circles
        g2d.setPaint(CIRCLE_COLOUR);
        g2d.setStroke(defaultStroke);
        Ellipse2D circle;
        for (int x = 0; x < Board.size; x++) {
            for (int y = 0; y < Board.size; y++) {
                circle = new Ellipse2D.Double(Board.Xs[x]-RAD, Board.Ys[y]-RAD, 2*RAD, 2*RAD);
                g2d.fill(circle);
            }
        }

        // display owners in completed boxes
        for (int y = 0; y < Board.size-1; y++) {
            for (int x = 0; x < Board.size-1; x++) {
                if (Board.boxAt(x, y).isCompleted()) {
                    if (Board.boxAt(x, y).getOwner() == Box.Owner.PLAYER)
                        g2d.setPaint(new Color(0, 255, 0, 150));
                    else
                        g2d.setPaint(new Color(255, 0, 0, 150));

                    g2d.fill(new Rectangle2D.Double(
                        MARGIN + 2*RAD + x*(DIST+2*RAD) + INDICATOR_MARGIN,
                        MARGIN + 2*RAD + y*(DIST+2*RAD) + INDICATOR_MARGIN,
                        DIST - 2*INDICATOR_MARGIN,
                        DIST - 2*INDICATOR_MARGIN
                    ));
                }
            }
        }

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        int size = (Board.size - 1) * (2*RAD + DIST) + 2*RAD + 2*MARGIN;
        return new Dimension(size, size);
    }
}
