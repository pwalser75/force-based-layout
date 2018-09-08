package ch.frostnova.force.based.layout.model;

import ch.frostnova.force.based.layout.geom.Dimension;
import ch.frostnova.force.based.layout.geom.Point;
import ch.frostnova.force.based.layout.geom.Rectangle;

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
     * Bounds of the shape
     *
     * @return bounds, never null
     */
    default Rectangle getBounds() {
        return new Rectangle(getLocation(), getSize());
    }
}
