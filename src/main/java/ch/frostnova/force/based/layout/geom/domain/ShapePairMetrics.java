package ch.frostnova.force.based.layout.geom.domain;

import ch.frostnova.force.based.layout.geom.Dimension;
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

    /**
     * Create metrics for a pair of rectangles
     *
     * @param first
     * @param second
     */
    private ShapePairMetrics(Shape first, Shape second) {
        this.first = Check.required(first, "first");
        this.second = Check.required(second, "second");
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
        //TODO
        return Optional.empty();
    }

    public Optional<Double> overlapDistance() {
        //TODO
        return Optional.empty();
    }

    public Optional<Double> distance() {
        //TODO
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
