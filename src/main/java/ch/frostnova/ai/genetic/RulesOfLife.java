package ch.frostnova.ai.genetic;

import ch.frostnova.util.check.Check;
import ch.frostnova.util.check.CheckNumber;

/**
 * Rules of life (simulating the environment for a {@link Population})
 *
 * @author pwalser
 * @since 22.09.2018.
 */
public class RulesOfLife {

    public final static RulesOfLife DEFAULT = new RulesOfLife(0.5, 0.4, 0.75);

    private final double survivingRate;
    private final double reproducingRate;
    private final double matingGamma;

    /**
     * Constructor
     *
     * @param survivingRate   relative number [0..1 = 0..100%] of the population surviving each generation.
     * @param reproducingRate relative number [0..1 = 0..100%] of the population considered for mating.
     * @param matingGamma     gamma distribution for picking mates (1=linear, 0..1 favouring fitter members, >1
     *                        favouring  lesser fit members)
     */
    public RulesOfLife(double survivingRate, double reproducingRate, double matingGamma) {
        this.survivingRate = Check.required(survivingRate, "survivingRate", CheckNumber.min(0), CheckNumber.max(1));
        this.reproducingRate = Check.required(reproducingRate, "reproducingRate", CheckNumber.min(0), CheckNumber.max(1));
        this.matingGamma = Check.required(matingGamma, "matingGamma", CheckNumber.min(0));
    }

    public double getSurvivingRate() {
        return survivingRate;
    }

    public double getReproducingRate() {
        return reproducingRate;
    }

    public double getMatingGamma() {
        return matingGamma;
    }

    @Override
    public String toString() {
        return "RulesOfLife{" +
                "survivingRate=" + survivingRate +
                ", reproducingRate=" + reproducingRate +
                ", matingGamma=" + matingGamma +
                '}';
    }
}
