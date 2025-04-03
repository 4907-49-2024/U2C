package pipes.c2ka.behaviours;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Representation of a sequential composite of C2KA behaviors
 */
public class SequentialBehavior extends CompositeBehavior {
    public SequentialBehavior() {
        super(new ArrayList<>()); // Choice order does not matter
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    @Override
    protected String getCompositeOperator() {
        return ";";
    }

    /**
     * Get atomic behavior in sequence, located by search type function.
     * If the behavior is sequential as well, can recursively search.
     * If it is choice/parallel, the behavior is indeterministic. Unsupported.
     *
     * @param target The initial target to look for
     * @param searchType The search function to recursively look for the target
     * @return The first atomic behavior in sequence.
     * @throws IllegalStateException If an indeterministic behavior is found in the diagram due to composition.
     */
    private static AtomicBehavior getAtomicBehavior(Behavior target, Function<SequentialBehavior, Behavior> searchType) {
        // Error cases
        if (target instanceof ChoiceBehavior)
            throw new IllegalStateException("Unsupported choice composition placement - indeterministic behavior.");
        if (target instanceof ParallelBehavior)
            throw new IllegalStateException("unsupported parallel composition placement  - indeterministic behavior.");

        // It's a bit silly to nest a sequential behavior in another, but it's possible.
        if (target instanceof SequentialBehavior seqFirst)
            return getAtomicBehavior(searchType.apply(seqFirst), searchType);

        // Has to be atomic now
        return (AtomicBehavior) target;
    }

    /**
     * Get first atomic behavior in sequence.
     * If the first behavior is sequential as well, can recursively search.
     * If it is choice/parallel, the behavior is indeterministic. Unsupported.
     *
     * @param target The target sequence to find the atomic behavior in.
     * @return The first atomic behavior in sequence.
     * @throws IllegalStateException If an indeterministic behavior is found in the diagram due to composition.
     */
    public static AtomicBehavior getInitialBehavior(SequentialBehavior target) {
        Function<SequentialBehavior, Behavior> findFirst = t -> t.behaviors.iterator().next();

        return getAtomicBehavior(findFirst.apply(target), findFirst);
    }


    /**
     * Get last atomic behavior in sequence.
     * If the last behavior is sequential as well, can recursively search.
     * If it is choice/parallel, the behavior is indeterministic. Unsupported.
     *
     * @param target The target sequence to find the atomic behavior in.
     * @return The last atomic behavior in sequence.
     * @throws IllegalStateException If an indeterministic behavior is found in the diagram due to composition.
     */
    public static AtomicBehavior getLastBehavior(SequentialBehavior target) {
        Function<SequentialBehavior, Behavior> findLast =
                t -> ((ArrayList<Behavior>) t.behaviors).get(t.behaviors.size()-1);

        return getAtomicBehavior(findLast.apply(target), findLast);
    }
}
