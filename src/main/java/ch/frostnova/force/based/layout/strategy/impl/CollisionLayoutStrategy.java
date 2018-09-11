package ch.frostnova.force.based.layout.strategy.impl;

import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.geom.domain.ShapePairMetrics;
import ch.frostnova.force.based.layout.model.Scene;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.force.based.layout.strategy.SceneLayoutStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Simple strategy: the bounding box of all shapes is attracted by the coordinate system origin (0/0 -> upper left
 * corner).
 *
 * @author pwalser
 * @since 11.09.2018.
 */
public class CollisionLayoutStrategy implements SceneLayoutStrategy {

    @Override
    public Map<Shape, Vector> calculateForces(Scene scene) {

        Map<Shape, Vector> forces = new HashMap<>();

        AtomicInteger index = new AtomicInteger();
        Map<Shape, Integer> indexedShapes = scene.shapes().collect(Collectors.toMap(Function.identity(), x -> index.incrementAndGet()));

        Map<String, ShapePairMetrics> uniqueMetrics = new HashMap<>();

        scene.shapes().forEach(a -> {
            scene.shapes().forEach(b -> {

                if (a != b) {
                    int indexA = indexedShapes.get(a);
                    int indexB = indexedShapes.get(b);

                    String id = Math.min(indexA, indexB) + "/" + Math.max(indexA, indexB);
                    if (!uniqueMetrics.containsKey(id)) {
                        uniqueMetrics.put(id, new ShapePairMetrics(a, b));
                    }
                }
            });
        });

        uniqueMetrics.forEach((id, metrics) -> {

            if (metrics.overlap()) {

                double overlapLength = metrics.overlapLength().orElse(0d);

                Shape a = metrics.getFirst();
                Shape b = metrics.getSecond();

                Vector centerA = a.getBounds().getCenter();
                Vector centerB = b.getBounds().getCenter();
                Vector middle = centerA.add(centerA.distance(centerB).scaled(0.5));

                Vector forceA = middle.distance(centerA).normalized().scaled(overlapLength / 2);
                Vector forceB = middle.distance(centerB).normalized().scaled(overlapLength / 2);
                forces.put(a, forces.computeIfAbsent(a, x -> new Vector(0, 0)).add(forceA));
                forces.put(b, forces.computeIfAbsent(b, x -> new Vector(0, 0)).add(forceB));
            }
        });
        return forces;
    }
}
