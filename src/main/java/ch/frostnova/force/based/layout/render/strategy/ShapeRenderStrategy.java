package ch.frostnova.force.based.layout.render.strategy;

import ch.frostnova.force.based.layout.geom.Shape;

import java.awt.*;

/**
 * Strategy on how to render a shape
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public interface ShapeRenderStrategy {

    void render(Graphics2D g, Shape shape);
}
