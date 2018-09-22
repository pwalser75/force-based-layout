package ch.frostnova.ai.genetic;

/**
 * A genom as a possible solution to a problem, that can be mutated and recombined (crossover) with other genoms.
 *
 * @author pwalser
 * @since 18.09.2018.
 */
public interface Genom<G extends Genom<G>> {

    /**
     * Asexual reproduction: slightly vary the genom's features at random, producing a mutated clone.
     *
     * @return offspring
     */
    G mutate();

    /**
     * Sexual reproduction: mate the genom with another member of the same species, producing an offspring.
     *
     * @param other other genom, required
     * @return offspring
     */
    G crossover(G other);
}
