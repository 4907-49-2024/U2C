package pipes.c2ka.behaviours;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Representation of a sequential composite of C2KA behaviors
 */
public class SequentialBehaviour extends CompositeBehaviour {
    public SequentialBehaviour() {
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
    private static AtomicBehaviour getAtomicBehavior(Behaviour target, Function<SequentialBehaviour, Behaviour> searchType) {
        // Error cases
        if (target instanceof ChoiceBehaviour)
            throw new IllegalStateException("Unsupported choice composition placement - indeterministic behavior.");
        if (target instanceof ParallelBehaviour)
            throw new IllegalStateException("unsupported parallel composition placement  - indeterministic behavior.");

        // It's a bit silly to nest a sequential behavior in another, but it's possible.
        if (target instanceof SequentialBehaviour seqFirst)
            return getAtomicBehavior(searchType.apply(seqFirst), searchType);

        // Has to be atomic now
        return (AtomicBehaviour) target;
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
    public static AtomicBehaviour getInitialBehavior(SequentialBehaviour target) {
        Function<SequentialBehaviour, Behaviour> findFirst = t -> t.behaviours.iterator().next();

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
    public static AtomicBehaviour getLastBehavior(SequentialBehaviour target) {
        Function<SequentialBehaviour, Behaviour> findLast =
                t -> ((ArrayList<Behaviour>) t.behaviours).get(t.behaviours.size()-1);

        return getAtomicBehavior(findLast.apply(target), findLast);
    }
}
