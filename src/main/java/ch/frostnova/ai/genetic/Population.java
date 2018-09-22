package ch.frostnova.ai.genetic;

import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;
import ch.frostnova.util.check.Verify;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * A population of life forms of the same species, that can be evolved over many generations.<br>
 * In each generation
 * <ul>
 * <li>some members may <b>survive</b> (fitness as survival function)</li>
 * <li>members may <b>produce offspring</b> (fitness as a better chance for mating)</li>
 * <li>some members may <b>die</b></li>
 * <li>the number of members is kept constant (thus as many new members are born as old members die)</li>
 * </ul>
 *
 * @author pwalser
 * @since 22.09.2018.
 */
public class Population<G extends Genom<G>> {

    private List<G> population;
    private Map<G, Double> fitness = new HashMap<>();
    private final FitnessFunction<G> fitnessFunction;

    private final Comparator<G> fitnessComparator = Comparator.comparing(this::fitnessOf).reversed();

    private int generation = 0;

    /**
     * Seed the population with a number of members.
     *
     * @param members         number of members of the population
     * @param spawnFunction   spawn function to create initial members
     * @param fitnessFunction fitness function, required
     */
    public Population(int members, Supplier<G> spawnFunction, FitnessFunction<G> fitnessFunction) {
        Check.required(members, "members", CheckNumber.greaterThan(0));
        Check.required(spawnFunction, "spawnFunction");
        this.fitnessFunction = Check.required(fitnessFunction, "fitnessFunction");

        population = new ArrayList<>(members);
        while (population.size() < members) {
            G newMember = spawnFunction.get();
            Check.required(newMember, "produced spawn");
            population.add(newMember);
        }
        population.sort(fitnessComparator);
    }

    /**
     * Evolve the population: some members survive, some die, population number is kept constant using reproduction
     * (crossover with mutation) of the members. Survival, death and reproduction are driven by the <b>fitness</b> and
     * <b>destiny</b> (accidents and lucky incidents may happen, such are the rules of life).
     */
    public void evolve() {

        List<G> nextGeneration = new ArrayList<>(population.size());

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int surviving = (int) (0.4 * population.size());
        int reproducing = (int) (0.7 * population.size());


        // add survivors
        for (int i = 0; i < surviving; i++) {
            nextGeneration.add(population.get(i));
        }

        // reproduce until population is full again
        while (nextGeneration.size() < population.size()) {

            // pick two mates
            G first = population.get(random.nextInt(0, reproducing));
            G second = population.get(random.nextInt(0, reproducing));

            G offspring = first.crossover(second).mutate();
            nextGeneration.add(offspring);
        }

        Map<G, Double> newFitness = new HashMap<>();
        for (G genom : nextGeneration) {
            newFitness.put(genom, fitness.get(genom));
        }
        fitness = newFitness;
        population = nextGeneration;
        population.sort(fitnessComparator);
        generation++;
    }

    /**
     * Returns the generation number (increased every time the evolve() function is called)
     *
     * @return generation
     */
    public int getCurrentGeneration() {
        return generation;
    }

    /**
     * Returns the current population, sorted by fitness
     *
     * @return population
     */
    public List<G> getCurrentPopulation() {
        return population;
    }

    /**
     * Return the size of the population
     *
     * @return number of members
     */
    public int getSize() {
        return population.size();
    }

    /**
     * Return the currently fittest member of the population
     *
     * @return fittest member
     */
    public G getFittestMember() {
        return population.stream().findFirst().orElseThrow(() -> new IllegalStateException("empty population"));
    }

    /**
     * Get the fitness for the given member of the population
     *
     * @param genom member of the population
     * @return fitness
     */
    public double getFitness(G genom) {
        Check.required(genom, "genom", Verify.that(population::contains, "must be a member of the population"));
        return fitnessOf(genom);
    }

    /**
     * Get fitness for genom (assumed not null and member of the population)
     *
     * @param genom genom
     * @return fitness
     */
    private double fitnessOf(G genom) {
        return fitness.computeIfAbsent(genom, fitnessFunction::evaluate);
    }

    @Override
    public String toString() {
        G fittestMember = getFittestMember();
        return String.format("Population of %d members, generation #%d, fittest: %s with fitness of %s",
                population.size(), generation, fittestMember, fitnessOf(fittestMember));

    }
}
