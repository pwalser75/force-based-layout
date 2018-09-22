/**
 * <h1>API / Framework for Genetic Algorithms</h1>
 * <p>
 * Quick guide:
 * <ul>
 * <li>Express a solution of a problem using a {@link ch.frostnova.ai.genetic.Genom}, and define how solutions can be
 * varied (mutation) or recombined (crossover)</li>
 * <li>Define a {@link ch.frostnova.ai.genetic.FitnessFunction} which assesses the quality of the solutions</li>
 * <li>Solve the problem (or rather: find increasingly better solutions) using a {@link
 * ch.frostnova.ai.genetic.Population}, which can <b>evolve</b> over many <b>generations</b></li>
 * </ul>
 * <p>
 * Example: <blockquote>find integer solutions in the form <code>a + b * c = x</code>, where <code>x</code> is a
 * constant to be
 * approximated.</blockquote>
 * <p>
 * <b>Genom:</b>
 * <pre><code>
 * public class TestGenom implements Genom&lt;TestGenom&gt; {
 *
 *     final int a, b, c, d;
 *
 *     public TestGenom(int a, int b, int c, int d) {
 *         this.a = a;  this.b = b;  this.c = c;  this.d = d;
 *     }
 *
 *     public static TestGenom random() {
 *         Supplier&lt;Integer&gt; random = () -&gt; ThreadLocalRandom.current().nextInt(-100, 100);
 *         return new TestGenom(random.get(), random.get(), random.get(), random.get());
 *     }
 *
 *     &#064;Override
 *     public TestGenom mutate() {
 *         Supplier&lt;Integer&gt; randomDelta = () -&gt; (int) (ThreadLocalRandom.current().nextGaussian() * 3);
 *         return new TestGenom(a + randomDelta.get(), b + randomDelta.get(), c + randomDelta.get(), d + randomDelta.get());
 *     }
 *
 *     &#064;Override
 *     public TestGenom crossover(TestGenom other) {
 *         return new TestGenom((a + other.a) / 2, (b + other.b) / 2, (c + other.c) / 2, (d + other.d) / 2);
 *     }
 *
 *     public double getValue() {
 *         return d == 0 ? Integer.MAX_VALUE : a + (double) b * c / d;
 *     }
 *
 *     &#064;Override
 *     public String toString() {
 *         return String.format("%d + %d * %d / %d = %f", a, b, c, d, getValue());
 *     }
 * }
 * </code></pre>
 * <p>
 * <b>Fitness Function:</b>
 * <pre><code>
 * public static FitnessFunction&lt;TestGenom&gt; approximate(double number) {
 *  	return g -&gt; -Math.abs(number - g.getValue());
 * }
 * </code></pre>
 * <p>
 * <b>Evolution:</b>
 * <pre><code>
 * Population&lt;TestGenom&gt; population = new Population&lt;&gt;(50, TestGenom::random, approximate(1234.5));
 * int generations=100;
 * while (generations-- &gt;0){
 * 	population.evolve();
 * }
 * System.out.println(population.getBestSolution());
 * </code></pre>
 *
 * @author pwalser
 * @since 22.09.2018.
 */
package ch.frostnova.ai.genetic;