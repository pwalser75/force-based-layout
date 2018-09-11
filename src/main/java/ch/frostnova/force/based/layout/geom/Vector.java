package ch.frostnova.force.based.layout.geom;

import ch.frostnova.force.based.layout.util.Lazy;
import ch.frostnova.util.check.Check;

import java.util.Objects;

/**
 * A vector in a 2D coordinate system.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Vector {

    private final double x;
    private final double y;
    private final Lazy<Double> length;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
        length = new Lazy<>(() -> Math.sqrt(x * x + y * y));
    }

    /**
     * Returns the x coordinate
     *
     * @return x
     */
    public final double getX() {
        return x;
    }

    /**
     * Returns the y coordinate
     *
     * @return y
     */
    public final double getY() {
        return y;
    }

    /**
     * Returns the length of the vector
     *
     * @return length
     */
    public final double length() {
        return length.get();
    }

    /**
     * Returns the distance vector to the target
     *
     * @param target target vector, required
     * @return distance such that this.add(distance)=target
     */
    public final Vector distance(Vector target) {
        Check.required(target, "target");
        return new Vector(target.getX() - x, target.getY() - y);
    }

    /**
     * Returns an inverted (-x,-y) version of the vector
     *
     * @return inverted vector
     */
    public final Vector inverted() {
        return new Vector(-x, -y);
    }

    /**
     * Returns an normalized version of this vector (with length=1, unless the vector is 0/0).
     *
     * @return normalized vector
     */
    public final Vector normalized() {
        double length = length();
        if (length == 0) {
            return this;
        }
        return scaled(1 / length);
    }

    /**
     * Rotate the vector
     *
     * @param pivot pivot point to rotate around
     * @param angle angle (radians) to rotate
     * @return rotated vector
     */
    public final Vector rotate(Vector pivot, double angle) {

        double px = x - pivot.getX();
        double py = y - pivot.getY();
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double xnew = pivot.getX() + px * c - py * s;
        double ynew = pivot.getY() + px * s + py * c;

        return new Vector(xnew, ynew);
    }

    /**
     * Returns an scaled version of this vector.
     *
     * @return scaled vector
     */
    public final Vector scaled(double factor) {
        return new Vector(x * factor, y * factor);
    }

    /**
     * Addition: add the other vector to this and return the result.
     *
     * @return this + other vector
     */
    public final Vector add(Vector other) {
        Check.required(other, "other");
        return new Vector(x + other.getX(), y + other.getY());
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        return other.x == x && other.y == y;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), x, y);
    }

    @Override
    public String toString() {

        return Constants.NUMBER_FORMAT.format(x) + "/" + Constants.NUMBER_FORMAT.format(y);
    }
}
