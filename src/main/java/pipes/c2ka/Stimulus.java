package pipes.c2ka;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent a stimulus in the system.
 * <br>
 * Keeps track of any stimulus created in the system, allowing to get a complete list of them after all agents processed.
 */
public record Stimulus(String name) implements Comparable<Stimulus> {
    public static final Stimulus NEUTRAL_STIMULUS = new Stimulus("N");
    public static Set<Stimulus> systemStimuli = new HashSet<>();

    public Stimulus(String name) {
        this.name = name;
        systemStimuli.add(this);
        // Neutral stimulus should not be part of the system stimuli (keep implicit)
        // Note: if we had a factory we could avoid checking this on every construction
        if (name.equals(NEUTRAL_STIMULUS.name))
            systemStimuli.remove(NEUTRAL_STIMULUS);
    }

    public static Set<Stimulus> getSystemStimuli() {
        return systemStimuli;
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
    public int compareTo(Stimulus o) {
        return this.name.compareTo(o.name);
    }
}
