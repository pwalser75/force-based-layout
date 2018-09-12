package ch.frostnova.force.based.layout.strategy.impl;

import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.util.Optional;

/**
 * Attraction layout strategy: shapes attract each other.
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public class AttractionLayoutStrategy implements SceneLayoutStrategy {

    private final double minDistance;

    /**
     * Create a new repulsion layout strategy, using the given minimal distance between shapes.
     *
     * @param minDistance minimum distance between shapes, must not be negative
     */
    public AttractionLayoutStrategy(double minDistance) {
        this.minDistance = Check.required(minDistance, "minDistance", CheckNumber.min(0));
    }

    @Override
    public ShapeForces calculateForces(Scene scene) {

        ShapeForces forces = new ShapeForces();

        scene.shapes().forEach(a ->
                scene.shapes().forEach(b -> {

                    if (System.identityHashCode(a) < System.identityHashCode(b)) {

                        calculateDistance(a, b).ifPresent(distance -> {

                            if (distance > minDistance) {

                                double delta = distance - minDistance;
                                double attraction = delta / (1 + delta);

                                Vector centerA = a.getBounds().getCenter();
                                Vector centerB = b.getBounds().getCenter();
                                Vector middle = centerA.add(centerA.distance(centerB).scaled(0.5));

                                Vector forceA = centerA.distance(middle).normalized().scaled(attraction / 2);
                                Vector forceB = centerB.distance(middle).normalized().scaled(attraction / 2);
                                forces.add(a, forceA);
                                forces.add(b, forceB);
                            }
                        });
                    }
                })
        );
        return forces;
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
