package ch.frostnova.force.based.layout.geom;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Line}
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class LineTest {

    @Test
    public void testConstruct() {

        Point origin = new Point(Math.random(), Math.random());
        Point destination = new Point(Math.random(), Math.random());
        Line line = new Line(origin, destination);

        Assert.assertEquals(origin, line.getOrigin());
        Assert.assertEquals(destination, line.getDestination());
    }
}
