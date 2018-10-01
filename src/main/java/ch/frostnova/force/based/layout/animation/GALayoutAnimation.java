package ch.frostnova.force.based.layout.animation;

import ch.frostnova.force.based.layout.genetic.SceneOptimizer;
import ch.frostnova.force.based.layout.model.Scene;

/**
 * GA (Genetic algorithm) layout animation.
 *
 * @author pwalser
 * @since 24.09.2018.
 */
public class GALayoutAnimation implements LayoutAnimation {

    private SceneOptimizer sceneOptimizer;

    @Override
    public Scene animate(Scene scene, double elapsedTimeSec, double timeDeltaSec) {

        if (sceneOptimizer == null) {
            sceneOptimizer = new SceneOptimizer(scene);
        }

        sceneOptimizer.optimize(100);
        System.out.println(sceneOptimizer);
        return sceneOptimizer.getScene();
    }
}
