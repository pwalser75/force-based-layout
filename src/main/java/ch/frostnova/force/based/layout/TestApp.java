package ch.frostnova.force.based.layout;

import ch.frostnova.force.based.layout.animation.ForceLayoutAnimation;
import ch.frostnova.force.based.layout.animation.GALayoutAnimation;
import ch.frostnova.force.based.layout.animation.LayoutAnimation;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.render.SwingSceneRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
    private volatile AnimationThread animationThread;

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
                stopAnimation();
            }
        });

        setVisible(true);
    }

    private JToolBar createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.add(createAction("Force Layout", () -> startAnimation(new ForceLayoutAnimation())));
        toolBar.add(createAction("GA Layout", () -> startAnimation(new GALayoutAnimation())));
        toolBar.add(createAction("Stop animation", () -> stopAnimation()));
        toolBar.add(createAction("Randomize shape positions", () -> randomizeShapePositions()));

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
        Shape f = randomShape("F", 100, 300, getSize());

        scene.add(a);
        scene.add(b);
        scene.add(c);
        scene.add(d);
        scene.add(e);
        scene.add(f);

        scene.add(new Connector(a, b));
        scene.add(new Connector(a, c));
        scene.add(new Connector(a, d));
        scene.add(new Connector(b, d));
        scene.add(new Connector(b, e));
        scene.add(new Connector(c, e));
        scene.add(new Connector(f, c));

        return scene;
    }

    private void randomizeShapePositions() {

        sceneRenderer.getScene().shapes().forEach(shape -> {
            double x = Math.random() * (sceneRenderer.getSize().width - shape.getSize().getWidth());
            double y = Math.random() * (sceneRenderer.getSize().height - shape.getSize().getHeight());
            shape.setLocation(new Point(x, y));
        });
        sceneRenderer.repaint();
    }

    private boolean isRunning() {
        return animationThread != null;
    }

    private void stopAnimation() {
        if (!isRunning()) {
            return;
        }
        animationThread.stop();
    }

    private void startAnimation(LayoutAnimation animation) {
        stopAnimation();
        animationThread = new AnimationThread(animation);
    }

    private class AnimationThread {

        private final LayoutAnimation animation;
        private boolean running = true;
        private Thread thread;

        public AnimationThread(LayoutAnimation animation) {
            this.animation = animation;
            start();
        }

        public void start() {
            stop();
            thread = new Thread(() -> {

                long timeLastFrame = System.nanoTime();

                long animationStartTime = System.nanoTime();

                while (running) {

                    try {
                        Thread.sleep(50);
                    } catch (Exception ex) {
                        throw new RuntimeException("Animation terminated", ex);
                    }

                    long timestamp = System.nanoTime();
                    double elapsedSeconds = (timestamp - animationStartTime) * 1e-9;
                    double timeDeltaSec = (timestamp - timeLastFrame) * 1e-9;
                    timeLastFrame = timestamp;

                    Scene scene = animation.animate(sceneRenderer.getScene(), elapsedSeconds, timeDeltaSec);
                    sceneRenderer.setScene(scene);

                    try {
                        SwingUtilities.invokeAndWait(() -> sceneRenderer.repaint());
                    } catch (Exception ex) {
                        throw new RuntimeException("Animation terminated", ex);
                    }
                }
            });
            running = true;
            thread.setDaemon(true);
            thread.start();
        }

        public void stop() {
            if (!running) {
                return;
            }
            running = false;
            try {
                if (thread != null) {
                    thread.join(1000);
                }
            } catch (InterruptedException ex) {
                throw new RuntimeException("Failed to terminate animation thread");
            } finally {
                thread = null;
            }
        }
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
        return new BaseShape(new Point(x, y), new ch.frostnova.force.based.layout.geom.Dimension(w, h)).setIdentifier(identifier);
    }

    private int rnd(int min, int bound) {
        if (min == bound) {
            return min;
        }
        return ThreadLocalRandom.current().nextInt(min, bound);
    }
}
