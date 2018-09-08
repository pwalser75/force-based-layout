package ch.frostnova.force.based.layout.geom;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Vector}
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class VectorTest {

    private double EPSILON = 1e-10;

    @Test
    public void testDistance() {

        Assert.assertEquals(new Vector(0, 0), new Vector(5, 7).distance(new Vector(5, 7)));
        Assert.assertEquals(new Vector(2, 3), new Vector(0, 0).distance(new Vector(2, 3)));
        Assert.assertEquals(new Vector(-2, -3), new Vector(2, 3).distance(new Vector(0, 0)));
    }

    @Test
    public void testLength() {

        Assert.assertEquals(0, new Vector(0, 0).length(), EPSILON);

        Assert.assertEquals(5, new Vector(5, 0).length(), EPSILON);
        Assert.assertEquals(7, new Vector(0, 7).length(), EPSILON);

        Assert.assertEquals(5, new Vector(-5, 0).length(), EPSILON);
        Assert.assertEquals(7, new Vector(0, -7).length(), EPSILON);

        Assert.assertEquals(5, new Vector(3, 4).length(), EPSILON);
        Assert.assertEquals(0.55, new Vector(-0.33, 0.44).length(), EPSILON);
        Assert.assertEquals(0.5, new Vector(0.3, -0.4).length(), EPSILON);
        Assert.assertEquals(55.5, new Vector(-33.3, -44.4).length(), EPSILON);
    }

    @Test
    public void testNormalized() {

        Assert.assertEquals(new Vector(0, 0), new Vector(0, 0).normalized());

        Assert.assertEquals(new Vector(1, 0), new Vector(5, 0).normalized());
        Assert.assertEquals(new Vector(0, 1), new Vector(0, 7).normalized());

        Assert.assertEquals(new Vector(-1, 0), new Vector(-5, 0).normalized());
        Assert.assertEquals(new Vector(0, -1), new Vector(0, -7).normalized());

        Assert.assertEquals(new Vector(0.5812381937190965, -0.813733471206735), new Vector(5, -7).normalized());
    }

    @Test
    public void testInverted() {

        Assert.assertEquals(new Vector(0, 0), new Vector(0, 0).inverted());

        Assert.assertEquals(new Vector(-5, 7), new Vector(5, -7).inverted());
        Assert.assertEquals(new Vector(5, -7), new Vector(-5, 7).inverted());
    }
}
