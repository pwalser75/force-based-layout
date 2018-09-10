package ch.frostnova.force.based.layout.geom;

import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

import java.util.Objects;

/**
 * A dimension in a 2D coordinate system, with width and height.
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Dimension {

    private final double width;
    private final double height;

    /**
     * Create a new dimension.
     *
     * @param width  width, must be positive
     * @param height height, must be positive
     */
    public Dimension(double width, double height) {
        this.width = Check.required(width, "width", CheckNumber.min(0));
        this.height = Check.required(height, "height", CheckNumber.min(0));
    }

    /**
     * Width
     *
     * @return width
     */
    public final double getWidth() {
        return width;
    }

    /**
     * Height
     *
     * @return height
     */
    public final double getHeight() {
        return height;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Dimension other = (Dimension) obj;
        return other.width == width && other.height == height;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), width, height);
    }

    @Override
    public String toString() {
        return Constants.NUMBER_FORMAT.format(width) + "x" + Constants.NUMBER_FORMAT.format(height);
    }
}
