package ch.frostnova.force.based.layout.strategy.impl;

import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;
import ch.frostnova.util.check.Check;

/**
 * Simple strategy: the bounding box of all shapes is attracted by the coordinate system origin (0/0 is upper left
 * corner).
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public class OriginLayoutStrategy implements SceneLayoutStrategy {

    private Point origin;

    public OriginLayoutStrategy() {
        this(Point.ORIGIN);
    }

    public OriginLayoutStrategy(Point origin) {
        this.origin = Check.required(origin, "origin");
    }

    @Override
    public ShapeForces calculateForces(Scene scene) {

        ShapeForces forces = new ShapeForces();

        Rectangle boundingBox = scene.boundingBox();
        Vector force = boundingBox.getLocation().distance(origin);
        scene.shapes().forEach(s -> forces.add(s, force));
        return forces;
    }
}
