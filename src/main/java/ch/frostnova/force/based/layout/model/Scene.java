package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Shape;
import ch.frostnova.util.check.Check;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A scene with shapes and connectors
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Scene {

    private final Set<Shape> shapes = new HashSet<>();

    public void add(Shape shape) {
        Check.required(shape, "shape");
        shapes.add(shape);
    }

    public void remove(Shape shape) {
        Check.required(shape, "shape");
        shapes.remove(shape);
    }

    public boolean contains(Shape shape) {
        return shape != null & shapes.contains(shape);
    }

    public void clear() {
        shapes.clear();
    }

    public Stream<Shape> shapes() {
        return shapes.stream();
    }

    /**
     * Calculate the bounding box (minimal region containing all the shapes) of the scene.
     *
     * @return bounding box
     */
    public Shape boundingBox() {

        if (shapes.isEmpty()) {
            return new BaseShape(0, 0, 0, 0);
        }

        double minX = Double.NaN;
        double maxX = Double.NaN;
        double minY = Double.NaN;
        double maxY = Double.NaN;

        for (Shape shape : shapes) {
            Point location = shape.getLocation();
            Dimension size = shape.getSize();

            minX = Math.min(minX, location.getX());
            maxX = Math.max(maxX, location.getX() + size.getWidth());
            minY = Math.min(minX, location.getY());
            maxY = Math.max(maxX, location.getY() + size.getHeight());
        }
        return new BaseShape(minX, minY, maxX - minX, maxY - minY);
    }
}
