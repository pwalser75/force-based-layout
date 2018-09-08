package ch.frostnova.force.based.layout.geom;

import ch.frostnova.util.check.Check;

import java.util.Objects;

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
