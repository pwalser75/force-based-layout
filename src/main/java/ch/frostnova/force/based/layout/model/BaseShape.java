package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Shape;
import ch.frostnova.util.check.Check;

/**
 * Basic shape implementation.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class BaseShape implements Shape {

    private Point location;
    private final Dimension size;

    public BaseShape(double width, double height) {
        this(new Dimension(width, height));
    }

    public BaseShape(Dimension size) {
        this(new Point(0, 0), size);
    }

    public BaseShape(double x, double y, double width, double height) {
        this(new Point(x, y), new Dimension(width, height));
    }

    public BaseShape(Point location, Dimension size) {
        this.location = Check.required(location, "location");
        this.size = Check.required(size, "size");
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(Point location) {
        this.location = location;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public String toString() {
        return location + ", " + size;
    }
}
