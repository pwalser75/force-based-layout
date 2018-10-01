package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.util.check.Check;

import java.util.*;
import java.util.stream.Stream;

/**
 * A scene with shapes and connectors
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Scene implements Cloneable {

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

    public Set<Shape> getShapes() {
        return Collections.unmodifiableSet(shapes);
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

    public Set<Connector> getConnectors() {
        return Collections.unmodifiableSet(connectors);
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
    public Rectangle boundingBox() {

        if (shapes.isEmpty()) {
            return new Rectangle(0, 0, 0, 0);
        }

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Shape shape : shapes) {
            Point location = shape.getLocation();
            Dimension size = shape.getSize();

            minX = Math.min(minX, location.getX());
            maxX = Math.max(maxX, location.getX() + size.getWidth());
            minY = Math.min(minY, location.getY());
            maxY = Math.max(maxY, location.getY() + size.getHeight());
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public Scene clone() {

        Map<Shape, Shape> clones = new HashMap<>();

        Scene cloned = new Scene();
        for (Shape shape : getShapes()) {
            Shape clone = shape.clone();
            clones.put(shape, clone);
            cloned.add(clone);
        }
        for (Connector connector : connectors) {
            Connector clone = new Connector(clones.get(connector.getFrom()), clones.get(connector.getTo()));
            cloned.add(clone);
        }
        return cloned;
    }
}
