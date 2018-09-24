package ch.frostnova.ai.genetic;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Test for rules of life
 *
 * @author pwalser
 * @since 22.09.2018.
 */
public class RulesOfLifeTest {

    @Test
    @Ignore
    public void testFindOptimalRulesOfLife() {

        FitnessFunction<RulesOfLifeGenom> f = g -> -benchmark(g.getRulesOfLife());
        RulesOfLife optimalRulesOfLife = new RulesOfLife(0, 0.5, 0.5);

        Population<RulesOfLifeGenom> population = new Population<>(50, RulesOfLifeGenom::new, f, optimalRulesOfLife);
        for (int i = 0; i < 100; i++) {
            population.evolve();
            System.out.println(population.getCurrentGeneration() + ": " + population.getFittestMember() + " -> " + population.getFitness(population.getFittestMember()));
        }

    }

    private double benchmark(RulesOfLife rulesOfLife) {
        int cycles = 100;
        double sumDeltas = 0;

        for (int i = 0; i < cycles; i++) {
            Population<TestGenom> population = new Population<>(50, TestGenom::random, TestFitnessFunctions.approximate(1234.5), rulesOfLife);

            int generations = 50;
            while (generations-- > 0) {
                population.evolve();
            }
            sumDeltas += Math.abs(1234.5 - population.getFittestMember().getValue());
        }
        return sumDeltas / cycles;
    }

    private static class RulesOfLifeGenom implements Genom<RulesOfLifeGenom> {

        private RulesOfLife rulesOfLife;

        public RulesOfLifeGenom() {
            rulesOfLife = new RulesOfLife(Math.random(), Math.random(), 2 * Math.random());
        }

        public RulesOfLifeGenom(RulesOfLife rulesOfLife) {
            this.rulesOfLife = rulesOfLife;
        }

        @Override
        public RulesOfLifeGenom mutate() {

            double survivingRate = mutate(rulesOfLife.getSurvivingRate(), 0.1, 0, 0.9);
            double reproducingRate = mutate(rulesOfLife.getReproducingRate(), 0.1, 0.1, 1);
            double matingGamma = mutate(rulesOfLife.getMatingGamma(), 0.1, 0.1, 2);

            return new RulesOfLifeGenom(new RulesOfLife(survivingRate, reproducingRate, matingGamma));
        }

        @Override
        public RulesOfLifeGenom crossover(RulesOfLifeGenom other) {

            double survivingRate = avg(rulesOfLife.getSurvivingRate(), other.rulesOfLife.getSurvivingRate());
            double reproducingRate = avg(rulesOfLife.getReproducingRate(), other.rulesOfLife.getReproducingRate());
            double matingGamma = avg(rulesOfLife.getMatingGamma(), other.rulesOfLife.getMatingGamma());

            return new RulesOfLifeGenom(new RulesOfLife(survivingRate, reproducingRate, matingGamma));
        }

        public RulesOfLife getRulesOfLife() {
            return rulesOfLife;
        }

        private double avg(double a, double b) {
            return (a + b) / 2;
        }

        private double mutate(double value, double rate, double min, double max) {
            return clamp(value + rnd(-rate, rate), min, max);
        }

        private double rnd(double min, double bound) {
            return min + (bound - min) * ThreadLocalRandom.current().nextDouble();
        }

        private double clamp(double number, double min, double max) {
            return number < min ? min : (number > max ? max : number);
        }

        @Override
        public String toString() {
            return rulesOfLife.toString();
        }
    }
}
