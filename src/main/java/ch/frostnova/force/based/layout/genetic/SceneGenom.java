package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.Genom;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Genom for a scene solutionn (finding optimal locations for shapes)
 *
 * @author pwalser
 * @since 24.09.2018.
 */
public class SceneGenom implements Genom<SceneGenom> {

    private final List<Point> shapeLocations;

    public SceneGenom(int shapes) {
        Check.required(shapes, "shapes", CheckNumber.min(0));
        shapeLocations = new ArrayList<>(shapes);
        for (int i = 0; i < shapes; i++) {
            shapeLocations.add(new Point(rnd(0, 1000), rnd(0, 1000)));
        }
    }

    private SceneGenom(List<Point> shapeLocations) {
        this.shapeLocations = shapeLocations;
    }

    public List<Point> getShapeLocations() {
        return Collections.unmodifiableList(shapeLocations);
    }

    @Override
    public SceneGenom mutate() {

        double mutationProbability = 0.2;

        List<Point> newLocations = new LinkedList<>();
        for (Point point : shapeLocations) {

            if (ThreadLocalRandom.current().nextDouble() < mutationProbability) {
                double x = point.getX() + ThreadLocalRandom.current().nextGaussian() * 100;
                double y = point.getY() + ThreadLocalRandom.current().nextGaussian() * 100;
                newLocations.add(new Point(x, y));
            } else {
                newLocations.add(point);
            }
        }
        return new SceneGenom(newLocations);
    }

    @Override
    public SceneGenom crossover(SceneGenom other) {
        List<Point> otherShapeLocations = other.getShapeLocations();
        if (otherShapeLocations.size() != shapeLocations.size()) {
            throw new IllegalArgumentException("Incompatible genom (differnt number of shape locations");
        }
        Iterator<Point> i1 = shapeLocations.iterator();
        Iterator<Point> i2 = other.getShapeLocations().iterator();
        List<Point> newLocations = new LinkedList<>();
        while (i1.hasNext() && i2.hasNext()) {
            Point p1 = i1.next();
            Point p2 = i2.next();
            double x = rnd(p1.getX(), p2.getX());
            double y = rnd(p1.getY(), p2.getY());
            newLocations.add(new Point(x, y));

        }
        return new SceneGenom(newLocations);
    }

    private static double rnd(double origin, double bound) {
        if (origin == bound) {
            return origin;
        }
        return ThreadLocalRandom.current().nextDouble(Math.min(origin, bound), Math.max(origin, bound));
    }

    @Override
    public String toString() {
        return shapeLocations.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
