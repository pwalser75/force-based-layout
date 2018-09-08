package ch.frostnova.force.based.layout.geom;

import ch.frostnova.force.based.layout.util.Lazy;
import ch.frostnova.util.check.Check;

/**
 * An rectangle in a 2D coordinate system, whose axes are aligned with the coordinate system (not rotated).
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Rectangle {

    private final Point location;
    private final Dimension size;

    private final Lazy<Point> p1, p2, p3, p4, center;

    public Rectangle(double width, double height) {
        this(new Dimension(width, height));
    }

    public Rectangle(Dimension size) {
        this(new Point(0, 0), size);
    }

    public Rectangle(double x, double y, double width, double height) {
        this(new Point(x, y), new Dimension(width, height));
    }

    public Rectangle(Point location, Dimension size) {
        this.location = Check.required(location, "location");
        this.size = Check.required(size, "size");
        p1 = new Lazy<>(() -> new Point(location.getX(), location.getY()));
        p2 = new Lazy<>(() -> new Point(location.getX() + size.getWidth(), location.getY()));
        p3 = new Lazy<>(() -> new Point(location.getX(), location.getY() + getSize().getHeight()));
        p4 = new Lazy<>(() -> new Point(location.getX() + size.getWidth(), location.getY() + size.getHeight()));
        center = new Lazy<>(() -> new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2));
    }

    /**
     * Location
     *
     * @return location
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Size
     *
     * @return size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Center of the shape
     *
     * @return center
     */
    public Point getCenter() {
        return center.get();
    }

    /**
     * Upper left corner of the shape
     *
     * @return point
     */
    public Point getP1() {
        return p1.get();
    }

    /**
     * Upper right corner of the shape
     *
     * @return point
     */
    public Point getP2() {
        return p2.get();
    }

    /**
     * Lower left corner of the shape
     *
     * @return point
     */
    public Point getP3() {
        return p3.get();
    }

    /**
     * Lower right corner of the shape
     *
     * @return point
     */
    public Point getP4() {
        return p4.get();
    }


    @Override
    public String toString() {
        return location + ", " + size;
    }

    /**
     * Determine if the given point is inside the rectangle
     *
     * @param p point, required
     * @return inside
     */
    public boolean contains(Point p) {
        Check.required(p, "point");

        double dx = p.getX() - getLocation().getX();
        double dy = p.getY() - getLocation().getY();
        return dx >= 0 && dy >= 0 && dx <= getSize().getWidth() && dy <= getSize().getHeight();
    }
}
