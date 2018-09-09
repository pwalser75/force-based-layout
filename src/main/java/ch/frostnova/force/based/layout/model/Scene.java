package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.util.check.Check;

import java.util.HashSet;
import java.util.Optional;
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
    private final Set<Connector> connectors = new HashSet<>();

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

    public Stream<Shape> shapes() {
        return shapes.stream();
    }


    public void add(Connector connector) {
        Check.required(connector, "connector");
        connectors.add(connector);
    }

    public void remove(Connector connector) {
        Check.required(connector, "connector");
        connectors.remove(connector);
    }

    public boolean contains(Connector connector) {
        return connector != null & connectors.contains(connector);
    }

    public Stream<Connector> connectors() {
        return connectors.stream();
    }

    public void clear() {
        shapes.clear();
        connectors.clear();
    }


    public Optional<Shape> getShapeAt(double x, double y) {

        Point p = new Point(x, y);

        Shape nearest = null;
        double minDistance = 0;
        for (Shape shape : shapes) {
            if (shape.getBounds().contains(p)) {
                double distance = p.distance(shape.getBounds().getCenter()).length();
                if (nearest == null || distance < minDistance) {
                    nearest = shape;
                    minDistance = distance;
                }
            }

        }
        return Optional.ofNullable(nearest);
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
