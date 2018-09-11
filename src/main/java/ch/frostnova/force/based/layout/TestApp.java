package ch.frostnova.force.based.layout;

import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Connector;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.render.SwingSceneRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Test app (Swing UI).
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class TestApp extends JFrame {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestApp::new);
    }

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

        SwingSceneRenderer sceneRenderer = new SwingSceneRenderer();
        main.add(sceneRenderer, BorderLayout.CENTER);

        Scene scene = initScene();
        sceneRenderer.setScene(scene);

        setVisible(true);
    }

    private Scene initScene() {
        Scene scene = new Scene();

        Shape a = randomShape("A", getSize());
        Shape b = randomShape("B", getSize());
        Shape c = randomShape("C", getSize());
        Shape d = randomShape("D", getSize());
        Shape e = randomShape("E", getSize());

        scene.add(a);
        scene.add(b);
        scene.add(c);
        scene.add(d);
        scene.add(e);

        scene.add(new Connector(a, b));
        //        scene.add(new Connector(a, c));
        //        scene.add(new Connector(a, d));
        //        scene.add(new Connector(b, d));
        //        scene.add(new Connector(b, e));
        //        scene.add(new Connector(c, e));
        return scene;
    }

    private void centerOnScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    private Shape randomShape(String identifier, Dimension area) {

        int w = rnd(100, 300);
        int h = rnd(100, 300);
        int x = rnd(0, area.width - w);
        int y = rnd(0, area.height - h);
        return new BaseShape(identifier, x, y, w, h);

    }

    private int rnd(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, bound);
    }
}
