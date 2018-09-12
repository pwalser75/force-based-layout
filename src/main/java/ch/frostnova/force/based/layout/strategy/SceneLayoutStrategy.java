package ch.frostnova.force.based.layout.strategy;

import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;

/**
 * Strategy for laying out the scene by means of applying forces to the scene's shapes.
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public interface SceneLayoutStrategy {

    /**
     * Calculate layout forces for the scene's shapes according to the strategy.
     *
     * @param scene scene, required
     * @return map of forces, must not be null, and can contain forces for (none, some or all of) the scene's shapes.
     */
    ShapeForces calculateForces(Scene scene);
}
