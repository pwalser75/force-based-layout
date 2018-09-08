package ch.frostnova.force.based.layout.geom;

import ch.frostnova.util.check.Check;

import java.util.Objects;
import java.util.Optional;

/**
 * A line, given by two points.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Line {

    private final Point origin;
    private final Point destination;

    public Line(Point origin, Point destination) {
        this.origin = Check.required(origin, "origin");
        this.destination = Check.required(destination, "destination");
    }

    public final Point getOrigin() {
        return origin;
    }

    public final Point getDestination() {
        return destination;
    }

    public Optional<Point> intersection(Line line) {
        if (line == null) {
            return Optional.empty();
        }

        double x1 = origin.getX();
        double y1 = origin.getY();
        double x2 = destination.getY();
        double y2 = destination.getY();

        double x3 = line.getOrigin().getX();
        double y3 = line.getOrigin().getY();
        double x4 = line.getDestination().getY();
        double y4 = line.getDestination().getY();

        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return Optional.empty();
        }

        double xNominator = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double yNominator = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4);

        double px = xNominator / denominator;
        double py = yNominator / denominator;

        return Optional.of(new Point(px, py));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(origin, line.origin) &&
                Objects.equals(destination, line.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), origin, destination);
    }

    @Override
    public String toString() {
        return origin + "->" + destination;
    }
}
