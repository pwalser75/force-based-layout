package ch.frostnova.force.based.layout.animation;

import ch.frostnova.force.based.layout.geom.Constants;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapeForces;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.CenterLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.OriginLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.RepulsionLayoutStrategy;
import ch.frostnova.force.based.layout.strategy.impl.SpringLayoutStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Force-based layout animation.
 *
 * @author pwalser
 * @since 24.09.2018.
 */
public class ForceLayoutAnimation implements LayoutAnimation {

    private static double GENERAL_FORCE_FACTOR_PER_SECOND = 5;
    private static double MIN_FORCE = 0.5;

    private final Map<SceneLayoutStrategy, Double> weightedStrategies = new HashMap<>();


    public ForceLayoutAnimation() {

        weightedStrategies.put(new OriginLayoutStrategy(new Point(20, 20)), 10d);
        weightedStrategies.put(new RepulsionLayoutStrategy(25), 5d);
        weightedStrategies.put(new CenterLayoutStrategy(), 0.01d);
        weightedStrategies.put(new SpringLayoutStrategy(50), 2d);
    }

    @Override
    public Scene animate(Scene scene, double elapsedTimeSec, double timeDeltaSec) {

        ShapeForces effectiveForces = new ShapeForces();
        for (SceneLayoutStrategy strategy : weightedStrategies.keySet()) {
            double weight = weightedStrategies.get(strategy);

            ShapeForces forces = strategy.calculateForces(scene);
            effectiveForces.addAll(forces, weight);
        }

        System.out.println("Total forces: " + Constants.NUMBER_FORMAT.format(effectiveForces.getTotalForces()));

        double generalForceFactor = timeDeltaSec * GENERAL_FORCE_FACTOR_PER_SECOND / Math.pow(1 + 2 * elapsedTimeSec, 2);

        effectiveForces.forEach((shape, shapeForce) -> {
            Vector force = shapeForce.scaled(generalForceFactor);
            if (force.length() >= MIN_FORCE) {
                shape.setLocation(new Point(shape.getLocation().add(force)));
            }
        });

        return scene;
    }
}
