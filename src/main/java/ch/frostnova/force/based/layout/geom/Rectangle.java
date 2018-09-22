package ch.frostnova.force.based.layout.geom;

import ch.frostnova.force.based.layout.util.Lazy;
import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An rectangle in a 2D coordinate system, whose axes are aligned with the coordinate system (AABB,  axis-aligned
 * bounding box).
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Rectangle {

    private final Point location;
    private final Dimension size;

    private final Lazy<Point> center;

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
     * Create a new rectangle resulting of adding a margin around the given one.
     *
     * @param margin margin, must not be negative
     * @return rectangle
     */
    public Rectangle addMargin(double margin) {
        if (margin == 0) {
            return this;
        }

        Check.required(margin, "margin", CheckNumber.min(0));
        return new Rectangle(getLocation().getX() - margin, getLocation().getY() - margin, getSize().getWidth() + 2 * margin, getSize().getHeight() + 2 * margin);
    }

    @Override
    public String toString() {
        return location + ", " + size;
    }

    public Optional<Point> nearestIntersection(Line line) {

        Optional<Point> topIntersection = horizontalIntersection(line, getLocation().getX(), getLocation().getX() + getSize().getWidth(), getLocation().getY());
        Optional<Point> bottomIntersection = horizontalIntersection(line, getLocation().getX(), getLocation().getX() + getSize().getWidth(), getLocation().getY() + getSize().getHeight());
        Optional<Point> leftIntersection = verticalIntersection(line, getLocation().getX(), getLocation().getY(), getLocation().getY() + getSize().getHeight());
        Optional<Point> rightIntersection = verticalIntersection(line, getLocation().getX() + getSize().getWidth(), getLocation().getY(), getLocation().getY() + getSize().getHeight());

        Set<Point> intersections = Stream.of(topIntersection, bottomIntersection, leftIntersection, rightIntersection)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        Point nearestIntersection = null;
        for (Point p : intersections) {
            if (nearestIntersection == null || line.getOrigin().distance(p).length() < line.getOrigin().distance(nearestIntersection).length()) {
                nearestIntersection = p;
            }
        }

        return Optional.ofNullable(nearestIntersection);
    }

    private Optional<Point> horizontalIntersection(Line line, double x1, double x2, double y) {

        double px = line.getOrigin().getX();
        double py = line.getOrigin().getY();
        double vx = line.getOrigin().distance(line.getDestination()).getX();
        double vy = line.getOrigin().distance(line.getDestination()).getY();

        // parallel
        if (vy == 0) {
            // on the line, otherwise they never meet
            if (py == y && px >= x1 && px <= x2) {
                return Optional.of(new Point(px, py));
            }
            return Optional.empty();
        }

        double f = (y - py) / vy;
        if (f < 0 || f > 1) {
            return Optional.empty();
        }
        double ix = px + vx * f;
        double iy = py + vy * f;
        return Optional.of(new Point(ix, iy));
    }

    private Optional<Point> verticalIntersection(Line line, double x, double y1, double y2) {

        double px = line.getOrigin().getX();
        double py = line.getOrigin().getY();
        double vx = line.getOrigin().distance(line.getDestination()).getX();
        double vy = line.getOrigin().distance(line.getDestination()).getY();

        // parallel
        if (vx == 0) {
            // on the line, otherwise they never meet
            if (px == x && py >= y1 && py <= y2) {
                return Optional.of(new Point(px, py));
            }
            return Optional.empty();
        }

        double f = (x - px) / vx;
        if (f < 0 || f > 1) {
            return Optional.empty();
        }
        double ix = px + vx * f;
        double iy = py + vy * f;
        return Optional.of(new Point(ix, iy));
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
