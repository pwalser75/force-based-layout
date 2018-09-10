package ch.frostnova.force.based.layout.render.strategy;

import ch.frostnova.force.based.layout.geom.Line;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Shape;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Default render strategy, adds visual hints on scene metrics.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class DebugRenderStrategy implements SceneRenderStrategy {

    private final static int GRID_SPACING = 25;

    private final Color backgroundColor = new Color(0x163F73);

    private final Color baseColor = new Color(0xFFFFFF);

    private final Color shapedColor = new Color(0x44000000 | baseColor.getRGB() & 0xFFFFFF, true);
    private final Color shapeOutlineColor = baseColor;
    private final Color connectorColor = new Color(0xFFFFFF);
    private final Color gridColor = new Color(0x22000000 | baseColor.getRGB() & 0xFFFFFF, true);

    private final Stroke solidStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    private final Stroke dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{4}, 0);

    private final Font identifierFont = new Font("Fira Sans", Font.BOLD, 18);
    private final Font metricsFont = new Font("Fira Sans", Font.BOLD, 12);

    private BufferedImage cachedBackground;

    private BufferedImage getBackgroundImage(int width, int height) {

        if (cachedBackground == null || cachedBackground.getWidth() != width || cachedBackground.getHeight() != height) {
            cachedBackground = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = cachedBackground.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g.setColor(backgroundColor);
            g.fillRect(0, 0, width, height);

            g.setPaint(new RadialGradientPaint((float) width / 2, (float) height / 2, (float) Math.min(width, height) * 0.7f, new float[]{0, 1f}, new Color[]{new Color(0x55FFFFFF, true), new Color(0x00FFFFFF, true)}));
            g.fillRect(0, 0, width, height);

            g.setColor(gridColor);

            for (int x = 0; x < width; x += GRID_SPACING) {
                g.drawLine(x, 0, x, height);
            }
            for (int y = 0; y < height; y += GRID_SPACING) {
                g.drawLine(0, y, width, y);
            }
        }
        return cachedBackground;
    }

    @Override
    public void renderBackground(Graphics2D g, int width, int height) {
        if (width > 0 && height > 0) {
            g.drawImage(getBackgroundImage(width, height), 0, 0, null);
        }
    }

    @Override
    public void render(Graphics2D g, Shape shape) {

        Rectangle2D rect = new Rectangle2D.Double(shape.getLocation().getX(), shape.getLocation().getY(),
                shape.getSize().getWidth(), shape.getSize().getHeight());

        g.setColor(shapedColor);
        g.fill(rect);
        g.setColor(shapeOutlineColor);
        g.draw(rect);

        java.awt.Shape originalClip = g.getClip();
        g.setClip(rect);

        String identifier = shape.getIdentifier().orElse("?");
        g.setFont(identifierFont);
        FontMetrics fontMetrics = g.getFontMetrics();
        g.drawString(identifier, (int) rect.getX() + 4, (int) rect.getY() + fontMetrics.getAscent() + 4);

        String metrics = shape.toString();
        g.setFont(metricsFont);
        fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(metrics);
        g.drawString(metrics, (int) (rect.getX() + rect.getWidth() - 4 - stringWidth), (int) (rect.getY() + rect.getHeight() - 4 - fontMetrics.getDescent()));

        g.setClip(originalClip);
    }

    @Override
    public void render(Graphics2D g, Connector connector) {

        debug(connector);

        final Shape from = connector.getFrom();
        final Shape to = connector.getTo();

        final Point start = from.getBounds().getCenter();
        final Point end = to.getBounds().getCenter();

        final Line connectorStartEnd = new Line(start, end);
        final Line connectorEndStart = new Line(end, start);

        // find intersections on the two shapes with the connector line

        final Point lineStart = from.getBounds().nearestIntersection(connectorStartEnd).orElse(start);
        if (lineStart == null) {
            return;
        }
        final Point lineEnd = to.getBounds().nearestIntersection(connectorEndStart).orElse(end);
        if (lineEnd == null) {
            return;
        }
        // check if reversed vector from start to end
        double lenght1 = start.distance(lineStart).length();
        double lenght2 = start.distance(lineEnd).length();

        if (lenght1 >= lenght2) {

            g.setColor(Color.red);
            g.setStroke(solidStroke);
            g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
        } else {
            g.setColor(connectorColor);
            g.setStroke(dashedStroke);
            g.draw(new Line2D.Double(start.getX(), start.getY(), lineStart.getX(), lineStart.getY()));
            g.draw(new Line2D.Double(lineEnd.getX(), lineEnd.getY(), end.getX(), end.getY()));

            g.setStroke(solidStroke);
            g.draw(new Line2D.Double(lineStart.getX(), lineStart.getY(), lineEnd.getX(), lineEnd.getY()));
        }
    }

    private void debug(Connector connector) {

        System.out.println("CONNECTOR: " + connector.getFrom().getIdentifier().orElse("?") + " -> " + connector.getTo().getIdentifier().orElse("?"));
        System.out.println("- from: " + connector.getFrom());
        System.out.println("- to: " + connector.getTo());

        Rectangle a = connector.getFrom().getBounds();
        Rectangle b = connector.getTo().getBounds();

        double dx1 = a.getLocation().getX() - b.getLocation().getX() - b.getSize().getWidth();
        double dx2 = b.getLocation().getX() - a.getLocation().getX() - a.getSize().getWidth();

        if (dx1 * dx2 < 0) {
            double distance = dx2 > 0 ? dx2 : -dx1;
            System.out.println("- distance X: " + distance);
        } else {
            double qx1 = Math.max(a.getLocation().getX(), b.getLocation().getX());
            double qx2 = Math.min(a.getLocation().getX() + a.getSize().getWidth(), b.getLocation().getX() + b.getSize().getWidth());
            double overlap = qx2 - qx1;
            System.out.println("- overlap X: " + overlap);
        }
    }
}
