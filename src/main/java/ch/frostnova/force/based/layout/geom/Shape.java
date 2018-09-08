package ch.frostnova.force.based.layout.geom;

/**
 * A shape in a 2D coordinate system.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public interface Shape {

    /**
     * Get the current location of the shape
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
     * Get the size of the shape
     *
     * @return size, never null
     */
    Dimension getSize();


    /**
     * Returns the center of the shape
     *
     * @return center
     */
    default Point getCenter() {
        return new Point(getLocation().getX() + getSize().getWidth() / 2, getLocation().getY() + getSize().getHeight() / 2);
    }
}
