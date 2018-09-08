package ch.frostnova.force.based.layout.render;

import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Scene;

import javax.swing.*;
import java.awt.*;

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

        scene = new Scene();
        scene.add(new BaseShape(0, 0, 100, 100));
        scene.add(new BaseShape(150, 50, 200, 120));
        scene.add(new BaseShape(50, 150, 200, 180));

        setTitle("Force Based Layout Test");

        JPanel main = new JPanel();
        setContentPane(main);
        main.setLayout(new BorderLayout());

        SwingSceneRenderer sceneRenderer = new SwingSceneRenderer();
        main.add(sceneRenderer, BorderLayout.CENTER);
        sceneRenderer.setScene(scene);

        pack();
        setMinimumSize(new Dimension(400, 400));
        centerOnScreen();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void centerOnScreen() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }
}
