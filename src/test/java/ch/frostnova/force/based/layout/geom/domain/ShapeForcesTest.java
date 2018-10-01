package ch.frostnova.force.based.layout.geom.domain;

import ch.frostnova.force.based.layout.geom.Vector;
import ch.frostnova.force.based.layout.model.BaseShape;
import ch.frostnova.force.based.layout.model.Shape;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * Tests for {@link ShapeForces}
 *
 * @author pwalser
 * @since 12.09.2018
 */
public class ShapeForcesTest {

    @Test
    public void testEmpty() {

        ShapeForces forces = new ShapeForces();
        Shape s = randomShape();
        Assert.assertEquals(new Vector(0, 0), forces.get(s));
    }

    @Test
    public void testAdd() {
        ShapeForces forces = new ShapeForces();
        Shape s = randomShape();

        forces.add(s, new Vector(1, 0));
        Assert.assertEquals(new Vector(1, 0), forces.get(s));
        forces.add(s, new Vector(0, -3));
        Assert.assertEquals(new Vector(1, -3), forces.get(s));
        forces.add(s, new Vector(-9.2, 11.5));
        Assert.assertEquals(new Vector(-8.2, 8.5), forces.get(s));
    }

    @Test
    public void testMerge() {

        ShapeForces f1 = new ShapeForces();
        ShapeForces f2 = new ShapeForces();
        Shape s1 = randomShape();
        Shape s2 = randomShape();
        Shape s3 = randomShape();

        f1.add(s1, new Vector(1, -2));
        f1.add(s2, new Vector(-3, 4));

        f2.add(s1, new Vector(5, 6));
        f2.add(s3, new Vector(-7, -8));

        f1.addAll(f2, 1);

        Assert.assertEquals(new Vector(6, 4), f1.get(s1));
        Assert.assertEquals(new Vector(-3, 4), f1.get(s2));
        Assert.assertEquals(new Vector(-7, -8), f1.get(s3));

        Assert.assertEquals(new Vector(5, 6), f2.get(s1));
        Assert.assertEquals(new Vector(0, 0), f2.get(s2));
        Assert.assertEquals(new Vector(-7, -8), f2.get(s3));
    }

    private Shape randomShape() {

        BiFunction<Double, Double, Double> random = (min, max) -> min + ThreadLocalRandom.current().nextDouble() * (max - min);
        return new BaseShape(random.apply(-100d, 100d), random.apply(-100d, 100d), random.apply(0d, 100d), random.apply(0d,
                100d)).setIdentifier(UUID.randomUUID().toString());
    }
}
