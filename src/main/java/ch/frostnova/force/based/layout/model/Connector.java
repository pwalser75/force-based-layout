package ch.frostnova.force.based.layout.model;

import ch.frostnova.util.check.Check;

import java.util.Objects;

/**
 * A directed connector between two shapes
 *
 * @author pwalser
 * @since 09.09.2018.
 */
public class Connector {

    private final Shape from;
    private final Shape to;

    /**
     * Constructor
     *
     * @param from origin shape
     * @param to   target shape
     */
    public Connector(Shape from, Shape to) {
        this.from = Check.required(from, "from");
        this.to = Check.required(to, "to");
    }

    /**
     * Origin of connector
     *
     * @return from
     */
    public Shape getFrom() {
        return from;
    }

    /**
     * Target of connector
     *
     * @return
     */
    public Shape getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Connector connector = (Connector) o;
        return Objects.equals(from, connector.from) &&
                Objects.equals(to, connector.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }
}
