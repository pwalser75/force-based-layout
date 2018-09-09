package ch.frostnova.force.based.layout.render.strategy;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Shape;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Strategy on how to render a shape
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class DefaultRenderStrategy implements SceneRenderStrategy {

    private final static int GRID_SPACING = 25;

    private Color backgroundColor = new Color(0x163F73);

    private Color baseColor = new Color(0xFFFFFF);

    private Color shapedColor = new Color(0x44000000 | baseColor.getRGB() & 0xFFFFFF, true);
    private Color shapeOutlineColor = baseColor;
    private Color connectorColor = new Color(0xFFFFFF);
    private Color gridColor = new Color(0x22000000 | baseColor.getRGB() & 0xFFFFFF, true);


    @Override
    public void renderBackground(Graphics2D g, Dimension size) {

        g.setColor(backgroundColor);
        g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());

        g.setPaint(new RadialGradientPaint((float) size.getWidth() / 2, (float) size.getHeight() / 2, (float) Math.min(size.getHeight(), size.getWidth()) * 0.7f, new float[]{0, 1f}, new Color[]{new Color(0x55FFFFFF, true), new Color(0x00FFFFFF, true)}));
        g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());

        g.setColor(gridColor);

        for (int x = 0; x < size.getWidth(); x += GRID_SPACING) {
            g.drawLine(x, 0, x, (int) size.getHeight());
        }
        for (int y = 0; y < size.getHeight(); y += GRID_SPACING) {
            g.drawLine(0, y, (int) size.getWidth(), y);
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
    }

    @Override
    public void render(Graphics2D g, Connector connector) {

        Shape from = connector.getFrom();
        Shape to = connector.getTo();

        Point start = from.getBounds().getCenter();
        Point end = to.getBounds().getCenter();

        Line2D line = new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());

        g.setColor(connectorColor);
        g.draw(line);
    }
}
