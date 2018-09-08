package ch.frostnova.force.based.layout.geom;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Rectangle}
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class RectangleTest {

    @Test
    public void testCreateShape() {

        Dimension size = new Dimension(300, 200);

        Rectangle rectangle = new Rectangle(size);
        Assert.assertEquals(new Point(0, 0), rectangle.getLocation());
        Assert.assertEquals(size, rectangle.getSize());
    }

    @Test
    public void testCreateShapeWithLocation() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);

        Rectangle rectangle = new Rectangle(location, size);
        Assert.assertEquals(location, rectangle.getLocation());
        Assert.assertEquals(size, rectangle.getSize());
    }

    @Test
    public void testCenter() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);
        Rectangle rectangle = new Rectangle(location, size);

        Assert.assertEquals(new Point(250, 150), rectangle.getCenter());
    }

    @Test
    public void testPoints() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);
        Rectangle rectangle = new Rectangle(location, size);

        Assert.assertEquals(new Point(100, 50), rectangle.getP1());
        Assert.assertEquals(new Point(400, 50), rectangle.getP2());
        Assert.assertEquals(new Point(100, 250), rectangle.getP3());
        Assert.assertEquals(new Point(400, 250), rectangle.getP4());
    }
}
