package ch.frostnova.force.based.layout.geom.domain;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Rectangle;
import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.model.Shape;
import ch.frostnova.util.check.Check;

import java.util.Objects;
import java.util.Optional;

/**
 * Shape-Pair metrics: distance and collision detection metrics of two shapes.
 *
 * @author pwalser
 * @since 11.09.2018
 */
public class ShapePairMetrics {

    private final Shape first;
    private final Shape second;

    private final Optional<Dimension> overlapArea;
    private final Optional<Vector> distance;

    /**
     * Create metrics for a pair of rectangles
     *
     * @param first
     * @param second
     */
    public ShapePairMetrics(Shape first, Shape second) {
        this.first = Check.required(first, "first");
        this.second = Check.required(second, "second");

        Rectangle a = first.getBounds();
        Rectangle b = second.getBounds();

        double dx1 = a.getLocation().getX() - b.getLocation().getX() - b.getSize().getWidth();
        double dx2 = b.getLocation().getX() - a.getLocation().getX() - a.getSize().getWidth();

        double dy1 = a.getLocation().getY() - b.getLocation().getY() - b.getSize().getHeight();
        double dy2 = b.getLocation().getY() - a.getLocation().getY() - a.getSize().getHeight();

        double qx1 = Math.max(a.getLocation().getX(), b.getLocation().getX());
        double qx2 = Math.min(a.getLocation().getX() + a.getSize().getWidth(), b.getLocation().getX() + b.getSize().getWidth());
        double overlapX = qx2 - qx1;

        double qy1 = Math.max(a.getLocation().getY(), b.getLocation().getY());
        double qy2 = Math.min(a.getLocation().getY() + a.getSize().getHeight(), b.getLocation().getY() + b.getSize().getHeight());
        double overlapY = qy2 - qy1;

        if (overlapX > 0 && overlapY > 0) {
            overlapArea = Optional.of(new Dimension(overlapX, overlapY));
            distance = Optional.empty();
        } else {
            double distanceX = overlapX > 0 ? 0 : (dx2 > 0 ? dx2 : -dx1);
            double distanceY = overlapY > 0 ? 0 : (dy2 > 0 ? dy2 : -dy1);
            overlapArea = Optional.empty();
            distance = Optional.of(new Vector(distanceX, distanceY));
        }
    }

    public Shape getFirst() {
        return first;
    }

    public Shape getSecond() {
        return second;
    }

    public boolean overlap() {
        return overlapArea().isPresent();
    }

    public Optional<Dimension> overlapArea() {
        return overlapArea;
    }

    public Optional<Double> overlapLength() {
        return overlapArea.map(a -> new Vector(a.getWidth(), a.getHeight()).length());
    }

    public Optional<Vector> distance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShapePairMetrics that = (ShapePairMetrics) o;
        return Objects.equals(first, that.first) &&
                Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return first + " \u21C4 " + second;
    }
}
