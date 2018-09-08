package ch.frostnova.force.based.layout.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests for {@link Lazy}
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class LazyTest {

    @Test
    public void testConstant() {
        Lazy<String> lazy = new Lazy("Whooo");
        Assert.assertEquals("Whooo", lazy.get());
    }

    @Test
    public void testNullConstant() {
        String value = null;
        Lazy<String> lazy = new Lazy(value);
        Assert.assertNull(lazy.get());
    }

    @Test
    public void testSupplyEvaluated() {

        Lazy<Double> lazy = new Lazy(() -> ThreadLocalRandom.current().nextDouble());
        Double value = lazy.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value, lazy.get());
    }

    @Test
    public void testSupplyNull() {
        Lazy<Double> lazy = new Lazy(() -> null);
        Assert.assertNull(lazy.get());
    }

    @Test
    public void testResetSupplyEvaluated() {

        Lazy<Long> lazy = new Lazy(() -> System.nanoTime());
        Long value = lazy.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value, lazy.get());
        lazy.reset();
        Assert.assertNotEquals(value, lazy.get());
        Assert.assertNotNull(lazy.get());
    }
}
