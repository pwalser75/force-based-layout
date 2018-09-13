package ch.frostnova.force.based.layout.strategy.impl;

import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;

/**
 * Center layout strategy: shapes are attracted by the scene bounding boxes' center.
 *
 * @author pwalser
 * @since 13.09.2018.
 */
public class CenterLayoutStrategy implements SceneLayoutStrategy {

    @Override
    public ShapeForces calculateForces(Scene scene) {

        ShapeForces forces = new ShapeForces();

        Rectangle boundingBox = scene.boundingBox();
        Point center = boundingBox.getCenter();

        scene.shapes().forEach(shape -> {

            Vector force = shape.getBounds().getCenter().distance(center);
            forces.add(shape, force);
        });
        return forces;
    }

}
