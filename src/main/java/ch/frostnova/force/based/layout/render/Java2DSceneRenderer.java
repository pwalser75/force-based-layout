package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.render.strategy.ShapeRenderStrategy;
import ch.frostnova.util.check.Check;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Java2D scene renderer
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Java2DSceneRenderer {

    public void render(Graphics2D g, Rectangle2D bounds, Scene scene, ShapeRenderStrategy shapeRenderStrategy) {

        Check.required(g, "Graphics2D context");
        Check.required(bounds, "bounds");
        Check.required(scene, "scene");
        Check.required(shapeRenderStrategy, "shapeRenderStrategy");

        g.setClip(bounds);

        scene.shapes().forEach(shape -> {
            shapeRenderStrategy.render(g, shape);
        });
    }

}
