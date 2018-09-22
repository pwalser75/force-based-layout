package ch.frostnova.ai.genetic;

import ch.frostnova.util.check.Check;

import java.util.function.Function;

/**
 * Fitness function, evaluates fitness for genoms.
 *
 * @author pwalser
 * @since 22.09.2018.
 */
@FunctionalInterface
public interface FitnessFunction<G extends Genom<?>> {

    /**
     * Evaluate the fitness of the given genom
     *
     * @param genom genom, not null
     * @return fitness (the higher it is, the fitter the genom is for survival and reproduction).
     */
    double evaluate(G genom);

    /**
     * Builds a fitness function based on the given cost function (the lower the cost, the higher the fitness).
     *
     * @param costFunction cost function, required
     * @param <G>          genom type
     * @return fitness function
     */
    static <G extends Genom<?>> FitnessFunction<G> ofCost(Function<G, Double> costFunction) {
        Check.required(costFunction, "costFunction");
        return g -> -costFunction.apply(g);
    }
}
