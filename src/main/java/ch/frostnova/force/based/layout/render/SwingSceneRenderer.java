package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.render.strategy.DefaultRenderStrategy;
import ch.frostnova.force.based.layout.render.strategy.ShapeRenderStrategy;
import ch.frostnova.util.check.Check;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Java Swing scene renderer
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class SwingSceneRenderer extends JPanel {

    private final Java2DSceneRenderer sceneRenderer = new Java2DSceneRenderer();
    private ShapeRenderStrategy renderStrategy = new DefaultRenderStrategy();

    private Scene scene = new Scene();

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = Check.required(scene, "scene");
        repaint();
    }

    public void setRenderStrategy(ShapeRenderStrategy shapeRenderStrategy) {
        renderStrategy = Check.required(shapeRenderStrategy, "renderStrategy");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        final Dimension size = getSize();

        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);

        Rectangle2D bounds = new Rectangle2D.Float(0, 0, size.width, size.height);
        sceneRenderer.render(g2, bounds, scene, renderStrategy);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
}
