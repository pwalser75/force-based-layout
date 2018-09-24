package ch.frostnova.ai.genetic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for genetic algorithm
 *
 * @author pwalser
 * @since 22.09.2018.
 */
public class PopulationTest {

    @Test
    public void testEvolution() {
        RulesOfLife rulesOfLife = RulesOfLife.DEFAULT;
        Population<TestGenom> population = new Population<>(50, TestGenom::random, TestFitnessFunctions.approximate(1234.5), rulesOfLife);
        System.out.println(population);

        int generations = 100;
        while (generations-- > 0) {
            population.evolve();
            System.out.println(population);
        }
        System.out.println("best solution: " + population.getFittestMember());
    }

    @Test
    public void testConsistency() {

        Population<TestGenom> population = new Population<>(50, TestGenom::random, TestFitnessFunctions.approximatePI());

        for (int i = 0; i < 20; i++) {
            Assert.assertEquals(i, population.getCurrentGeneration());
            Assert.assertNotNull(population.getFittestMember());
            Assert.assertNotNull(population.getFitness(population.getFittestMember()));
            Assert.assertEquals(50, population.getSize());
            Assert.assertNotNull(population.getCurrentPopulation());
            Assert.assertEquals(population.getSize(), population.getCurrentPopulation().size());

            Double lastFitness = null;
            for (TestGenom member : population.getCurrentPopulation()) {
                Assert.assertNotNull(member);
                if (lastFitness == null) {
                    lastFitness = population.getFitness(member);
                    Assert.assertNotNull(lastFitness);
                } else {
                    double fitness = population.getFitness(member);
                    Assert.assertNotNull(fitness);
                    Assert.assertFalse(fitness > lastFitness);
                    lastFitness = fitness;
                }
            }
            population.evolve();
            Assert.assertEquals(i + 1, population.getCurrentGeneration());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithNegativeSize() {
        new Population<>(-50, TestGenom::random, TestFitnessFunctions.approximatePI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithZeroSize() {
        new Population<>(0, TestGenom::random, TestFitnessFunctions.approximatePI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithoutSpawnFunction() {
        new Population<>(0, null, TestFitnessFunctions.approximatePI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithSpawnFunctionReturningNull() {
        new Population<>(0, () -> null, TestFitnessFunctions.approximatePI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreatePopulationWithoutFitessFunction() {
        new Population<>(0, () -> null, TestFitnessFunctions.approximatePI());
    }

    @Test
    public void testCreatePopulationWithSingleMember() {
        new Population<>(1, TestGenom::random, TestFitnessFunctions.approximatePI());
    }
}
