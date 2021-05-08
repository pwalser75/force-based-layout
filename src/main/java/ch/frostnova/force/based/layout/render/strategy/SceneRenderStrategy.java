package ch.frostnova.force.based.layout.render.strategy;

import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Shape;

import java.awt.Graphics2D;

/**
 * Strategy on how to render a shape
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public interface SceneRenderStrategy {

    /**
     * Render the background
     *
     * @param g      graphics context
     * @param width  width
     * @param height height
     */
    default void renderBackground(Graphics2D g, int width, int height) {
        // no background -> transparent
    }

    /**
     * Render the given shape
     *
     * @param g     graphics context
     * @param shape shape to render
     */
    void render(Graphics2D g, Shape shape);

    /**
     * Render the given connector
     *
     * @param g         graphics context
     * @param connector connector to render
     */
    void render(Graphics2D g, Connector connector);
}
