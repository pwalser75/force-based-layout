package ch.frostnova.force.based.layout.geom.domain;

import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.util.check.Check;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Forces on shapes
 *
 * @author pwalser
 * @since 12.09.2018
 */
public class ShapeForces {

    private final static Vector ZERO = new Vector(0, 0);

    private final Map<Shape, Vector> shapeForces = new HashMap<>();

    /**
     * Iterate over shape/force pairs
     */
    public void forEach(BiConsumer<Shape, Vector> consumer) {
        Check.required(consumer, "consumer");
        shapeForces.forEach(consumer);
    }

    /**
     * Get the force vector affecting the given shape
     *
     * @param shape shape, required
     * @return force, never null, if unknown: trivial (Vector(0,0))
     */
    public Vector get(Shape shape) {
        Check.required(shape, "shape");
        return shapeForces.computeIfAbsent(shape, s -> ZERO);
    }

    /**
     * Add a force for a given shape
     *
     * @param shape shape, required
     * @param force force vector, required
     * @return new effective force vector on shape
     */
    public Vector add(Shape shape, Vector force) {
        Check.required(shape, "shape");
        Check.required(force, "force");

        Vector newForce = get(shape).add(force);
        shapeForces.put(shape, newForce);
        return newForce;
    }

    /**
     * Merge these shape forces with other forces.
     *
     * @param other  other shape forces
     * @param factor factor applied to the other forces
     */
    public void merge(ShapeForces other, double factor) {
        Check.required(other, "other");
        other.forEach((shape, force) -> add(shape, force.scaled(factor)));
    }
}
