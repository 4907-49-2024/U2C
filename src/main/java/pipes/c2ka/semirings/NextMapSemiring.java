package pipes.c2ka.semirings;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviors.AtomicBehavior;

public abstract class NextMapSemiring<outT> implements Comparable<NextMapSemiring<outT>> {
    private final outT output;
    //Assumption: We only care to map atomic behaviors
    private final AtomicBehavior initialBehavior;
    private final Stimulus inputStim;

    protected NextMapSemiring(Stimulus inputStim, AtomicBehavior initialBehavior, outT output) {
        this.initialBehavior = initialBehavior;
        this.inputStim = inputStim;
        this.output = output;
    }

    /**
     * @return A map with the neutral output for the given output type
     */
    public abstract NextMapSemiring<?> createNeutralMap(Stimulus inputStim, AtomicBehavior initialBehavior);

    /**
     * @return initial behavior of this mapping
     */
    public AtomicBehavior getInitialBehavior() {
        return initialBehavior;
    }

    /**
     * @param inputStim The inputstim to check
     * @param initialBehavior The initial Behavior to check
     * @return True IFF the given pair matches the mapping input
     */
    public boolean isKeyEqual(Stimulus inputStim, AtomicBehavior initialBehavior) {
        return inputStim.equals(this.inputStim) && initialBehavior.equals(this.initialBehavior);
    }

    @Override
    public String toString() {
        return "("+inputStim+","+initialBehavior+") = "+output;
    }

    @Override
    public boolean equals(Object o) {
        // Same instance
        if (this == o) return true;
        // Null check, class match
        if (o == null || getClass() != o.getClass()) return false;
        return toString().equals(o.toString()); // Equivalent toString means functionally equivalent records.
    }

    @Override
    public int hashCode() {
        return toString().hashCode(); // Based on equals mechanism
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for
     * all {@code x} and {@code y}.  (This implies that {@code
     * x.compareTo(y)} must throw an exception if and only if {@code
     * y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z))
     * == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     * @apiNote It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     */
    @Override
    public int compareTo(NextMapSemiring o) {
        // Note, this allows comparison across output types, although it's not really intended.
        // Equals -> 0
        if(this.equals(o)) return 0;
        // Compare stimuli, behavior
        int stimCmp = inputStim.compareTo(o.inputStim);
        int behaviorCmp = initialBehavior.compareTo(o.initialBehavior);

        // Return stimulus comparison if not equal, otherwise compare behaviors (Should not be 0 at this point in theory)
        return stimCmp != 0 ? stimCmp : behaviorCmp;
    }
}
