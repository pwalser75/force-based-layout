package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.FitnessFunction;
import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.util.check.Check;

import java.util.List;
import java.util.Optional;

/**
 * Fitness function for an optimally layouted scene
 *
 * @author pwalser
 * @since 24.09.2018.
 */
public class SceneFitnessFunction implements FitnessFunction<SceneGenom> {

    private final List<Dimension> shapeDimensions;
    private int shapeMargin = 10;

    public SceneFitnessFunction(List<Dimension> shapeDimensions) {
        this.shapeDimensions = Check.required(shapeDimensions, "shapeDimensions");
    }

    @Override
    public double evaluate(SceneGenom genom) {

        Scene scene = new Scene();
        final List<Point> shapeLocations = genom.getShapeLocations();
        for (int i = 0; i < shapeDimensions.size(); i++) {
            BaseShape shape = new BaseShape(shapeDimensions.get(i));
            shape.setLocation(shapeLocations.get(i));
            scene.add(shape);
        }
        return evaulateFitness(scene);
    }

    private double evaulateFitness(Scene scene) {

        double cost = 0;

        // distance from origin

        double originDistanceWeight = 10;
        Rectangle boundingBox = scene.boundingBox();
        cost += Math.abs(boundingBox.getLocation().getX()) * originDistanceWeight;
        cost += Math.abs(boundingBox.getLocation().getY()) * originDistanceWeight;

        // margin between shapes

        double overlapWeight = 10;
        for (Shape a : scene.getShapes()) {
            for (Shape b : scene.getShapes()) {
                if (System.identityHashCode(a) < System.identityHashCode(b)) {

                    Optional<Double> overlapLength = calculateOverlapLength(a, b);
                    if (overlapLength.isPresent()) {
                        double length = overlapLength.get();

                        cost += length * overlapWeight;
                    }
                }
            }
        }

        // size of scene (more compact = better)

        cost += Math.abs(boundingBox.getSize().getWidth());
        cost += Math.abs(boundingBox.getSize().getHeight());

        return -cost;
    }

    private Optional<Double> calculateOverlapLength(Shape first, Shape second) {

        Rectangle a = first.getBounds().addMargin(shapeMargin / 2);
        Rectangle b = second.getBounds().addMargin(shapeMargin / 2);

        double qx1 = Math.max(a.getLocation().getX(), b.getLocation().getX());
        double qx2 = Math.min(a.getLocation().getX() + a.getSize().getWidth(), b.getLocation().getX() + b.getSize().getWidth());
        double overlapX = qx2 - qx1;

        double qy1 = Math.max(a.getLocation().getY(), b.getLocation().getY());
        double qy2 = Math.min(a.getLocation().getY() + a.getSize().getHeight(), b.getLocation().getY() + b.getSize().getHeight());
        double overlapY = qy2 - qy1;

        if (overlapX > 0 && overlapY > 0) {
            return Optional.of(new Vector(overlapX, overlapY).length());
        }
        return Optional.empty();
    }
}
