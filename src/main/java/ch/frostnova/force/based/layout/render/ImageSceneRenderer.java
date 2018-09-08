package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.render.strategy.ShapeRenderStrategy;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Java2D scene renderer
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class ImageSceneRenderer {

    private final Java2DSceneRenderer sceneRenderer = new Java2DSceneRenderer();

    public BufferedImage render(int width, int height, Scene scene, ShapeRenderStrategy renderStrategy) {
        Check.required(width, "width", CheckNumber.min(1));
        Check.required(height, "height", CheckNumber.min(1));
        Check.required(scene, "scene");
        Check.required(renderStrategy, "renderStrategy");

        BufferedImage image = new BufferedImage(BufferedImage.TYPE_INT_ARGB, width, height);
        Graphics2D graphics = image.createGraphics();
        sceneRenderer.render(graphics, new Rectangle2D.Float(0, 0, width, height), scene, renderStrategy);

        image.flush();
        return image;
    }

}
