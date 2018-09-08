package ch.frostnova.force.based.layout.geom;

import ch.frostnova.force.based.layout.model.BaseShape;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ch.frostnova.force.based.layout.geom.Shape}
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class ShapeTest {

    @Test
    public void testCreateShape() {

        Dimension size = new Dimension(300, 200);

        Shape shape = new BaseShape(size);
        Assert.assertEquals(new Point(0, 0), shape.getLocation());
        Assert.assertEquals(size, shape.getSize());
    }

    @Test
    public void testCreateShapeWithLocation() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);

        Shape shape = new BaseShape(location, size);
        Assert.assertEquals(location, shape.getLocation());
        Assert.assertEquals(size, shape.getSize());
    }

    @Test
    public void testCenter() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);
        Shape shape = new BaseShape(location, size);

        Assert.assertEquals(new Point(250, 150), shape.getCenter());

        shape.setLocation(new Point(-444, -555));
        Assert.assertEquals(new Point(-294, -455), shape.getCenter());
    }

    @Test
    public void testPoints() {

        Point location = new Point(100, 50);
        Dimension size = new Dimension(300, 200);
        Shape shape = new BaseShape(location, size);

        Assert.assertEquals(new Point(100, 50), shape.getP1());
        Assert.assertEquals(new Point(400, 50), shape.getP2());
        Assert.assertEquals(new Point(100, 250), shape.getP3());
        Assert.assertEquals(new Point(400, 250), shape.getP4());

        shape.setLocation(new Point(-444, -555));
        Assert.assertEquals(new Point(-444, -555), shape.getP1());
        Assert.assertEquals(new Point(-144, -555), shape.getP2());
        Assert.assertEquals(new Point(-444, -355), shape.getP3());
        Assert.assertEquals(new Point(-144, -355), shape.getP4());
    }
}
