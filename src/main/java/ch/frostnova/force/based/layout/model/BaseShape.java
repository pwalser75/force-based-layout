package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.util.Lazy;
import ch.frostnova.util.check.Check;

import java.util.Optional;
import java.util.UUID;

/**
 * Basic shape implementation
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class BaseShape implements Shape {

    private Point location;
    private Dimension size;

    private final Optional<String> identifier;
    private final Lazy<Rectangle> bounds;

    public BaseShape(double width, double height) {
        this(UUID.randomUUID().toString(), new Dimension(width, height));
    }

    public BaseShape(String identifier, double width, double height) {
        this(identifier, new Dimension(width, height));
    }

    public BaseShape(Dimension size) {
        this(UUID.randomUUID().toString(), new Point(0, 0), size);
    }

    public BaseShape(String identifier, Dimension size) {
        this(identifier, new Point(0, 0), size);
    }

    public BaseShape(String identifier, double x, double y, double width, double height) {
        this(identifier, new Point(x, y), new Dimension(width, height));
    }

    public BaseShape(String identifier, Point location, Dimension size) {
        this.identifier = Optional.ofNullable(identifier);
        this.location = Check.required(location, "location");
        this.size = Check.required(size, "size");
        bounds = new Lazy<>(() -> Shape.super.getBounds());
    }

    @Override
    public Optional<String> getIdentifier() {
        return identifier;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void setLocation(Point newLocation) {
        location = Check.required(newLocation, "newLocation");
        bounds.reset();
    }

    public void setSize(Dimension size) {
        this.size = Check.required(size, "size");
        bounds.reset();
    }

    @Override
    public Rectangle getBounds() {
        return bounds.get();
    }

    @Override
    public String toString() {
        return getBounds().toString();
    }
}
