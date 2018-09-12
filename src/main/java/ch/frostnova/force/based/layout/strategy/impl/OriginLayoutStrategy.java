package ch.frostnova.force.based.layout.strategy.impl;

import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;

/**
 * Simple strategy: the bounding box of all shapes is attracted by the coordinate system origin (0/0 -> upper left corner).
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public class OriginLayoutStrategy implements SceneLayoutStrategy {

    @Override
    public ShapeForces calculateForces(Scene scene) {

        ShapeForces forces = new ShapeForces();

        Rectangle boundingBox = scene.boundingBox();
        Vector force = boundingBox.getLocation().inverted();
        scene.shapes().forEach(s -> forces.add(s, force));
        return forces;
    }
}
