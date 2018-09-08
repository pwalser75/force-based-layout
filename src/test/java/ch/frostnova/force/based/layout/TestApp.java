package ch.frostnova.force.based.layout;

import ch.frostnova.force.based.layout.model.BaseShape;
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

    private final Scene scene;

    private TestApp() {

        setTitle("Force Based Layout Test");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(new Dimension(screenSize.width / 2, screenSize.height * 2 / 3));
        setMinimumSize(new Dimension(400, 400));
        centerOnScreen();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        setContentPane(main);
        main.setLayout(new BorderLayout());

        SwingSceneRenderer sceneRenderer = new SwingSceneRenderer();
        main.add(sceneRenderer, BorderLayout.CENTER);

        scene = new Scene();
        for (int i = 0; i < 20; i++) {
            scene.add(randomShape(getSize()));
        }
        sceneRenderer.setScene(scene);

        setVisible(true);
    }

    private void centerOnScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    private Shape randomShape(Dimension area) {

        int w = rnd(10, 200);
        int h = rnd(10, 200);
        int x = rnd(0, area.width - w);
        int y = rnd(0, area.height - h);
        return new BaseShape(x, y, w, h);

    }

    private int rnd(int min, int bound) {
        return ThreadLocalRandom.current().nextInt(min, bound);
    }
}
