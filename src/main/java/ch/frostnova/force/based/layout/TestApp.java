package ch.frostnova.force.based.layout;

import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.render.SwingSceneRenderer;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.AttractionLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.OriginLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.RepulsionLayoutStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test app (Swing UI).
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class TestApp extends JFrame {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeAndWait(TestApp::new);
    }

    private SwingSceneRenderer sceneRenderer = new SwingSceneRenderer();
    private final Map<SceneLayoutStrategy, Double> weightedStrategies = new HashMap<>();
    private Thread animationThread;
    private boolean running;

    private static double GENERAL_FORCE_FACTOR_PER_SECOND = 10;
    private static double MIN_FORCE = 0.1;

    private long animationStartTime;

    private TestApp() {

        setTitle("Force Based Layout Test");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int size = Math.min(screenSize.width, screenSize.height) * 2 / 3;
        setSize(size, size);
        setMinimumSize(new Dimension(400, 400));
        centerOnScreen();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        setContentPane(main);
        main.setLayout(new BorderLayout());

        JToolBar toolBar = createToolbar();
        main.add(toolBar, BorderLayout.NORTH);

        main.add(sceneRenderer, BorderLayout.CENTER);

        Scene scene = initScene();
        sceneRenderer.setScene(scene);

        sceneRenderer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                animationStartTime = System.nanoTime();
            }
        });

        weightedStrategies.put(new OriginLayoutStrategy(new Point(20, 20)), 1d);
        weightedStrategies.put(new RepulsionLayoutStrategy(25), 1d);
        weightedStrategies.put(new AttractionLayoutStrategy(40), 2d);

        setVisible(true);
        toggleAnimation();
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(createAction("Start/stop animation", () -> toggleAnimation()));

        return toolBar;
    }

    private Action createAction(String label, Runnable runnable) {

        AbstractAction action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }
        };
        action.putValue(Action.NAME, label);
        return action;
    }

    private Scene initScene() {
        Scene scene = new Scene();

        Shape a = randomShape("A", 100, 300, getSize());
        Shape b = randomShape("B", 100, 300, getSize());
        Shape c = randomShape("C", 100, 300, getSize());
        Shape d = randomShape("D", 100, 300, getSize());
        Shape e = randomShape("E", 100, 300, getSize());

        scene.add(a);
        scene.add(b);
        scene.add(c);
        scene.add(d);
        scene.add(e);

        scene.add(new Connector(a, b));
        scene.add(new Connector(a, c));
        scene.add(new Connector(a, d));
        scene.add(new Connector(b, d));
        scene.add(new Connector(b, e));
        scene.add(new Connector(c, e));

        for (int i = 0; i < 5; i++) {
            //    scene.add(randomShape("X" + i, 50, 200, getSize()));
        }

        return scene;
    }

    private synchronized void toggleAnimation() {

        if (running) {
            running = false;
            Thread terminating = animationThread;
            animationThread = null;
            try {
                terminating.join(1000);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Failed to terminate animation thread");
            }
        } else {
            running = true;
            animationStartTime = System.nanoTime();
            animationThread = new Thread(() -> {

                long timeLastFrame = System.nanoTime();

                while (running) {

                    try {
                        Thread.sleep(25);
                    } catch (Exception ex) {
                        throw new RuntimeException("Animation terminated", ex);
                    }
                    long timestamp = System.nanoTime();
                    double elapsedSeconds = (timestamp - timeLastFrame) * 1e-9;
                    timeLastFrame = timestamp;

                    double generalForceFactor = elapsedSeconds * getAnimationForceFactor();

                    ShapeForces effectiveForces = new ShapeForces();
                    for (SceneLayoutStrategy strategy : weightedStrategies.keySet()) {
                        double weight = weightedStrategies.get(strategy);

                        ShapeForces forces = strategy.calculateForces(sceneRenderer.getScene());
                        effectiveForces.addAll(forces, weight);
                    }

                    effectiveForces.forEach((shape, shapeForce) -> {
                        Vector force = shapeForce.scaled(generalForceFactor);
                        if (force.length() >= MIN_FORCE) {
                            shape.setLocation(new Point(shape.getLocation().add(force)));
                        }
                    });

                    try {
                        SwingUtilities.invokeAndWait(() -> sceneRenderer.repaint());
                        Thread.sleep(10);
                    } catch (Exception ex) {
                        throw new RuntimeException("Animation terminated", ex);
                    }
                }
            });
            animationThread.setDaemon(true);
            animationThread.start();
        }
    }

    private double getAnimationForceFactor() {

        double elapsedSeconds = (System.nanoTime() - animationStartTime) * 1e-9;
        return GENERAL_FORCE_FACTOR_PER_SECOND / Math.pow(1 + elapsedSeconds, 2);
    }

    private void centerOnScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    private Shape randomShape(String identifier, int minScale, int maxScale, Dimension area) {

        int w = rnd(minScale, maxScale);
        int h = rnd(minScale, maxScale);
        int x = rnd(0, area.width - w);
        int y = rnd(0, area.height - h);
        return new BaseShape(identifier, x, y, w, h);
    }

    private int rnd(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, bound);
    }
}
