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
 * Repulsion layout strategy: shapes repel each other if they are too near (or even overlapping).
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public class RepulsionLayoutStrategy implements SceneLayoutStrategy {

    private final double minDistance;

    /**
     * Create a new repulsion layout strategy, using the given minimal distance between shapes.
     *
     * @param minDistance minimum distance between shapes, must not be negative
     */
    public RepulsionLayoutStrategy(double minDistance) {
        this.minDistance = Check.required(minDistance, "minDistance", CheckNumber.min(0));
    }

    @Override
    public ShapeForces calculateForces(Scene scene) {

        ShapeForces forces = new ShapeForces();

        scene.shapes().forEach(a ->
                scene.shapes().forEach(b -> {

                    if (System.identityHashCode(a) < System.identityHashCode(b)) {

                        calculateOverlapLength(a, b).ifPresent(length -> {

                            Vector centerA = a.getBounds().getCenter();
                            Vector centerB = b.getBounds().getCenter();
                            Vector middle = centerA.add(centerA.distance(centerB).scaled(0.5));

                            Vector forceA = middle.distance(centerA).normalized().scaled(length);
                            Vector forceB = middle.distance(centerB).normalized().scaled(length);
                            forces.add(a, forceA);
                            forces.add(b, forceB);
                        });
                    }
                })
        );
        return forces;
    }

    private Optional<Double> calculateOverlapLength(Shape first, Shape second) {

        Rectangle a = first.getBounds().addMargin(minDistance / 2);
        Rectangle b = second.getBounds().addMargin(minDistance / 2);

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
