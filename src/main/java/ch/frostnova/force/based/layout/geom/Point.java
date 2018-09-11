package ch.frostnova.force.based.layout.geom;

import ch.frostnova.util.check.Check;

/**
 * A point, with x/y position in a 2D coordinate system.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Point extends Vector {

    public Point(double x, double y) {
        super(x, y);
    }

    public Point(Vector vector) {
        super(Check.required(vector, "vector").getX(), vector.getY());
    }
}
