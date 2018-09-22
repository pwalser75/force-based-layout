package ch.frostnova.ai.genetic;

/**
 * Test fitness functions
 *
 * @author pwalser
 * @since 22.09.2018.
 */
public final class TestFitnessFunctions {

    private TestFitnessFunctions() {

    }

    public static FitnessFunction<TestGenom> approximate(double number) {
        return g -> -Math.abs(number - g.getValue());
    }

    public static FitnessFunction<TestGenom> approximatePI() {
        return approximate(Math.PI);
    }
}
