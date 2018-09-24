package ch.frostnova.force.based.layout.genetic;

import ch.frostnova.ai.genetic.FitnessFunction;
import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Connector;
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
    private int springLength = 50;

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

        double originDistanceWeight = 2;
        Rectangle boundingBox = scene.boundingBox();
        cost += Math.abs(boundingBox.getLocation().getX()) * originDistanceWeight;
        cost += Math.abs(boundingBox.getLocation().getY()) * originDistanceWeight;

        // margin between shapes

        double overlapWeight = 1;
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

        // spring forces on connectors

        double springWeight = 5;
        for (Connector connector : scene.getConnectors()) {
            Shape a = connector.getFrom();
            Shape b = connector.getTo();

            Optional<Double> optionalDistance = calculateDistance(a, b);
            if (optionalDistance.isPresent()) {
                // TODO: connectors are not copied yet into the temporary scene -> should rather clone scene and only set locations
                cost += Math.abs(optionalDistance.get() - springLength) * springWeight;
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

    private Optional<Double> calculateDistance(Shape first, Shape second) {

        Rectangle a = first.getBounds();
        Rectangle b = second.getBounds();

        double dx1 = a.getLocation().getX() - b.getLocation().getX() - b.getSize().getWidth();
        double dx2 = b.getLocation().getX() - a.getLocation().getX() - a.getSize().getWidth();

        double dy1 = a.getLocation().getY() - b.getLocation().getY() - b.getSize().getHeight();
        double dy2 = b.getLocation().getY() - a.getLocation().getY() - a.getSize().getHeight();

        double qx1 = Math.max(a.getLocation().getX(), b.getLocation().getX());
        double qx2 = Math.min(a.getLocation().getX() + a.getSize().getWidth(), b.getLocation().getX() + b.getSize().getWidth());
        double overlapX = qx2 - qx1;

        double qy1 = Math.max(a.getLocation().getY(), b.getLocation().getY());
        double qy2 = Math.min(a.getLocation().getY() + a.getSize().getHeight(), b.getLocation().getY() + b.getSize().getHeight());
        double overlapY = qy2 - qy1;

        if (overlapX < 0 || overlapY < 0) {
            double distanceX = overlapX > 0 ? 0 : (dx2 > 0 ? dx2 : -dx1);
            double distanceY = overlapY > 0 ? 0 : (dy2 > 0 ? dy2 : -dy1);
            return Optional.of(new Vector(distanceX, distanceY).length());
        }
        return Optional.empty();
    }
}
