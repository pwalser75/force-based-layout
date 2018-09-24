package ch.frostnova.ai.genetic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Test genom for: a+b*c/d
 *
 * @author pwalser
 * @since 18.09.2018.
 */
public class TestGenom implements Genom<TestGenom> {
    private final static Supplier<Integer> randomDelta = () -> (int) (ThreadLocalRandom.current().nextGaussian() * 3);
    private final static BiFunction<Integer, Integer, Integer> randomMinMax = (a, b) -> (int) (a + (b - a + 1) * ThreadLocalRandom.current().nextDouble());

    final int a, b, c, d;

    public TestGenom(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }


    public static TestGenom random() {
        Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt(-100, 100);
        return new TestGenom(random.get(), random.get(), random.get(), random.get());
    }


    @Override
    public TestGenom mutate() {
        return new TestGenom(a + randomDelta.get(), b + randomDelta.get(), c + randomDelta.get(), d + randomDelta.get());
    }

    @Override
    public TestGenom crossover(TestGenom other) {
        return new TestGenom(randomMinMax.apply(a, other.a), randomMinMax.apply(b, other.b), randomMinMax.apply(c, other.c), randomMinMax.apply(d, other.d));
    }

    public double getValue() {
        return d == 0 ? Integer.MAX_VALUE : a + (double) b * c / d;
    }

    @Override
    public String toString() {
        return String.format("%d + %d * %d / %d = %f", a, b, c, d, getValue());
    }
}
