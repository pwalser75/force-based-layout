package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.render.strategy.DebugRenderStrategy;
import ch.frostnova.force.based.layout.render.strategy.SceneRenderStrategy;
import ch.frostnova.util.check.Check;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Optional;

/**
 * Java Swing scene renderer
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class SwingSceneRenderer extends JPanel {

    private final Java2DSceneRenderer sceneRenderer = new Java2DSceneRenderer();
    private SceneRenderStrategy renderStrategy = new DebugRenderStrategy();

    private Scene scene = new Scene();

    private Shape selectedShape;
    private ch.frostnova.force.based.layout.geom.Point dragLocation;
    private Point lastMousePosition;

    public SwingSceneRenderer() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedShape = null;
                lastMousePosition = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point mousePosition = new Point(e.getPoint());

                if (lastMousePosition == null) {
                    selectedShape = scene.getShapeAt(e.getX(), e.getY()).orElse(null);
                    dragLocation = Optional.ofNullable(selectedShape).map(Shape::getLocation).orElse(null);
                    lastMousePosition = e.getPoint();
                } else {
                    Point delta = new Point(mousePosition.x - lastMousePosition.x, mousePosition.y - lastMousePosition.y);
                    if (selectedShape != null) {
                        dragLocation = new ch.frostnova.force.based.layout.geom.Point(dragLocation.getX() + delta.getX(), dragLocation.getY() + delta.getY());
                        selectedShape.setLocation(dragLocation);
                        repaint();
                    }
                    lastMousePosition = mousePosition;
                }
            }
        });
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = Check.required(scene, "scene");
        repaint();
    }

    public void setRenderStrategy(SceneRenderStrategy shapeRenderStrategy) {
        renderStrategy = Check.required(shapeRenderStrategy, "renderStrategy");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        final Dimension size = getSize();

        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);

        sceneRenderer.render(g2, size.width, size.height, scene, renderStrategy);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
}
