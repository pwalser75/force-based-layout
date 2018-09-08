package ch.frostnova.force.based.layout.util;

import ch.frostnova.util.check.Check;

import java.util.function.Supplier;

/**
 * A lazy initialized value of a given type
 *
 * @author pwalser
 * @since 08.09.2018.
 */
public class Lazy<T> {

    private T value;
    private boolean evaluated;
    private Supplier<T> supplier;

    /**
     * Pre-initialize with a given (already evaluated) value.
     *
     * @param value value, can be null (still counts as evaluated)
     */
    public Lazy(T value) {
        this.value = value;
        evaluated = true;
    }

    /**
     * Reset the lazy, causing the value to be re-evaluated next time
     */
    public void reset() {
        if (supplier != null) {
            value = null;
            evaluated = false;
        }
    }

    /**
     * Create a lazy whose value will be evaluated with the given supplier.
     *
     * @param supplier supplier for the value, required
     */
    public Lazy(Supplier<T> supplier) {
        this.supplier = Check.required(supplier, "supplier");
    }

    public T get() {
        if (!evaluated) {
            value = supplier.get();
            evaluated = true;
        }
        return value;
    }
}
