package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Shape;
import ch.frostnova.force.based.layout.util.Lazy;
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

    private final Lazy<Point> p1, p2, p3, p4, center;

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
        p1 = new Lazy<>(() -> Shape.super.getP1());
        p2 = new Lazy<>(() -> Shape.super.getP2());
        p3 = new Lazy<>(() -> Shape.super.getP3());
        p4 = new Lazy<>(() -> Shape.super.getP4());
        center = new Lazy<>(() -> Shape.super.getCenter());
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public void setLocation(Point location) {
        this.location = location;
        center.reset();
        p1.reset();
        p2.reset();
        p3.reset();
        p4.reset();
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public Point getCenter() {
        return center.get();
    }

    @Override
    public Point getP1() {
        return p1.get();
    }

    @Override
    public Point getP2() {
        return p2.get();
    }

    @Override
    public Point getP3() {
        return p3.get();
    }

    @Override
    public Point getP4() {
        return p4.get();
    }

    @Override
    public String toString() {
        return location + ", " + size;
    }
}
