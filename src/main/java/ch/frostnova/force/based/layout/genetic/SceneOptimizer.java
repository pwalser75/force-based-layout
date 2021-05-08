package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.Population;
import ch.frostnova.ai.genetic.RulesOfLife;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

/**
 * @author pwalser
 * @since 24.09.2018.
 */
public class SceneOptimizer {

    private final static RulesOfLife rulesOfLife = new RulesOfLife(0.5, 0.5, 0.75);

    private SceneFitnessFunction fitnessFunction;

    private Population<SceneGenom> population;

    private final int numberOfShapes;

    public SceneOptimizer(Scene scene) {
        Check.required(scene, "scene");
        fitnessFunction = new SceneFitnessFunction();
        numberOfShapes = (int) scene.shapes().count();
        population = new Population<>(100, () -> new SceneGenom(scene), fitnessFunction, rulesOfLife);
    }

    public void optimize(int generations) {
        Check.required(generations, "generations", CheckNumber.min(1));
        for (int i = 0; i < generations; i++) {
            population.evolve();
        }
    }

    public Scene getScene() {
        return population.getFittestMember().getScene();
    }

    @Override
    public String toString() {
        return population.toString();
    }
}
