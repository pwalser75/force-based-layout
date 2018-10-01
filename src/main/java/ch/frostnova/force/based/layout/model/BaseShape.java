package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.util.Lazy;
import ch.frostnova.util.check.Check;

/**
 * Basic shape implementation
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class BaseShape implements Shape {

    private Point location;
    private Dimension size;

    private String identifier;
    private final Lazy<Rectangle> bounds;

    public BaseShape(double x, double y, double width, double height) {
        this(new Point(x, y), new Dimension(width, height));
    }

    public BaseShape(Point location, Dimension size) {
        this.location = Check.required(location, "location");
        this.size = Check.required(size, "size");
        bounds = new Lazy<>(() -> Shape.super.getBounds());
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public BaseShape setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
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

    @Override
    public BaseShape clone() {
        BaseShape clone = new BaseShape(getLocation(), getSize());
        clone.setIdentifier(getIdentifier());
        return clone;

    }
}
