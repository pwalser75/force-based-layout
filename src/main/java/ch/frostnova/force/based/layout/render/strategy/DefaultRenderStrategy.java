package ch.frostnova.force.based.layout.render.strategy;

import ch.frostnova.force.based.layout.model.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Strategy on how to render a shape
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class DefaultRenderStrategy implements ShapeRenderStrategy {

    private Color backgroundColor = new Color(0x775A6E9C, true);
    private Color outlineColor = new Color(0x5A6E9C);

    @Override
    public void render(Graphics2D g, Shape shape) {

        Rectangle2D rect = new Rectangle2D.Double(shape.getLocation().getX(), shape.getLocation().getY(),
                shape.getSize().getWidth(), shape.getSize().getHeight());

        g.setColor(backgroundColor);
        g.fill(rect);
        g.setColor(outlineColor);
        g.draw(rect);
    }
}
