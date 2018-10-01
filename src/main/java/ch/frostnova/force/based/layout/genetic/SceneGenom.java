package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.Genom;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.util.check.Check;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Genom for a scene solutionn (finding optimal locations for shapes)
 *
 * @author pwalser
 * @since 24.09.2018.
 */
public class SceneGenom implements Genom<SceneGenom> {

    private final Scene scene;

    public SceneGenom(Scene original) {
        scene = Check.required(original, "original").clone();
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public SceneGenom mutate() {

        double mutationProbability = 0.3;

        Scene offspring = scene.clone();

        offspring.shapes().forEach(shape -> {
            if (ThreadLocalRandom.current().nextDouble() < mutationProbability) {
                Point location = shape.getLocation();
                double x = location.getX() + ThreadLocalRandom.current().nextGaussian() * 100;
                double y = location.getY() + ThreadLocalRandom.current().nextGaussian() * 100;
                shape.setLocation(new Point(x, y));
            }
        });
        return new SceneGenom(offspring);
    }

    @Override
    public SceneGenom crossover(SceneGenom other) {

        if (getScene().getShapes().size() != other.getScene().getShapes().size()) {
            throw new IllegalArgumentException("Incompatible genom (differnt number of shapes");
        }
        Scene offspring = scene.clone();

        Iterator<Shape> i1 = offspring.getShapes().iterator();
        Iterator<Shape> i2 = other.getScene().getShapes().iterator();
        while (i1.hasNext() && i2.hasNext()) {
            Shape s1 = i1.next();
            Shape s2 = i2.next();
            double x = rnd(s1.getLocation().getX(), s2.getLocation().getX());
            double y = rnd(s1.getLocation().getY(), s2.getLocation().getY());
            s1.setLocation(new Point(x, y));

        }
        return new SceneGenom(offspring);
    }

    private static double rnd(double origin, double bound) {
        if (origin == bound) {
            return origin;
        }
        return ThreadLocalRandom.current().nextDouble(Math.min(origin, bound), Math.max(origin, bound));
    }

    @Override
    public String toString() {
        return scene.toString();
    }
}
