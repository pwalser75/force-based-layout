package ch.frostnova.force.based.layout.render.strategy;

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

    private Color baseColor = new Color(0x0056A7);
    private Color backgroundColor = new Color(0x77000000 | baseColor.getRGB() & 0xFFFFFF, true);
    private Color outlineColor = baseColor;
    private Color connectorColor = new Color(0x000000);


    @Override
    public void render(Graphics2D g, Shape shape) {

        Rectangle2D rect = new Rectangle2D.Double(shape.getLocation().getX(), shape.getLocation().getY(),
                shape.getSize().getWidth(), shape.getSize().getHeight());

        g.setColor(backgroundColor);
        g.fill(rect);
        g.setColor(outlineColor);
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
