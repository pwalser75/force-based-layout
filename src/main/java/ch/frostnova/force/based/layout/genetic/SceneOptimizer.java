package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.Population;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author pwalser
 * @since 24.09.2018.
 */
public class SceneOptimizer {

    private Scene scene;

    private SceneFitnessFunction fitnessFunction;

    private Population<SceneGenom> population;

    private final int numberOfShapes;

    public SceneOptimizer(Scene scene) {
        this.scene = Check.required(scene, "scene");
        fitnessFunction = new SceneFitnessFunction(scene.shapes().map(Shape::getSize).collect(Collectors.toList()));
        numberOfShapes = (int) scene.shapes().count();
        population = new Population<>(50, () -> new SceneGenom(numberOfShapes), fitnessFunction);
    }

    public void optimize(int generations) {
        Check.required(generations, "generations", CheckNumber.min(1));
        for (int i = 0; i < generations; i++) {
            population.evolve();
        }
        SceneGenom fittestMember = population.getFittestMember();
        List<Point> shapeLocations = fittestMember.getShapeLocations();
        final AtomicInteger index = new AtomicInteger();
        scene.shapes().forEach(shape -> shape.setLocation(shapeLocations.get(index.getAndIncrement())));
    }

    public int getGeneration() {
        return population.getCurrentGeneration();
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public String toString() {
        return population.toString();
    }
}
