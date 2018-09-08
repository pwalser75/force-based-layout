package ch.frostnova.force.based.layout.geom;

/**
 * A shape in a 2D coordinate system.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public interface Shape {

    /**
     * Current location of the shape
     *
     * @return location, never null
     */
    Point getLocation();

    /**
     * Set the current location of the shape
     *
     * @param newLocation new location, never null
     */
    void setLocation(Point newLocation);

    /**
     * Size of the shape
     *
     * @return size, never null
     */
    Dimension getSize();

    /**
     * Center of the shape
     *
     * @return center
     */
    default Point getCenter() {
        return new Point(getLocation().getX() + getSize().getWidth() / 2, getLocation().getY() + getSize().getHeight() / 2);
    }

    /**
     * Upper left corner of the shape
     *
     * @return point
     */
    default Point getP1() {
        return new Point(getLocation().getX(), getLocation().getY());
    }

    /**
     * Upper right corner of the shape
     *
     * @return point
     */
    default Point getP2() {
        return new Point(getLocation().getX() + getSize().getWidth(), getLocation().getY());
    }

    /**
     * Lower left corner of the shape
     *
     * @return point
     */
    default Point getP3() {
        return new Point(getLocation().getX(), getLocation().getY() + getSize().getHeight());
    }

    /**
     * Lower right corner of the shape
     *
     * @return point
     */
    default Point getP4() {
        return new Point(getLocation().getX() + getSize().getWidth(), getLocation().getY() + getSize().getHeight());
    }


}
