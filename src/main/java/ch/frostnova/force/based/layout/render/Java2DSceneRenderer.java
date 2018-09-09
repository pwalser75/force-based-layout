package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.render.strategy.SceneRenderStrategy;
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

    public void render(Graphics2D g, Rectangle2D bounds, Scene scene, SceneRenderStrategy shapeRenderStrategy) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        Check.required(g, "Graphics2D context");
        Check.required(bounds, "bounds");
        Check.required(scene, "scene");
        Check.required(shapeRenderStrategy, "shapeRenderStrategy");

        g.setClip(bounds);

        scene.shapes().forEach(shape -> {
            shapeRenderStrategy.render(g, shape);
        });

        scene.connectors().forEach(connector -> {
            shapeRenderStrategy.render(g, connector);
        });
    }

}
