package filters.diagramInterpreters.utils;

import pipes.c2ka.behaviours.*;

import java.util.function.Function;

public class TransitionBehaviorHandler {
    /**
     * Convert behavior to its AtomicBehavior equivalent
     * Atomic Behavior: Returns itself, type-casted.
     * Sequential Composite: Gets atomic behavior in sequence based on search function.
     * Parallel Composite: Throws error, unsupported - make another agent instead of modelling parallel composition.
     * Choice Composite: Unsupported - avoid source from choice composite, indeterministic behavior.
     *
     * @param target The target behavior
     * @param search The recursive function to search the sequential state.
     * @return The atomic behavior representing the target behavior.
     */
    private static AtomicBehaviour getAtomicBehavior(Behaviour target, Function<SequentialBehaviour, Behaviour> search) {
        // Error cases
        if (target instanceof ChoiceBehaviour)
            throw new IllegalArgumentException("Unsupported choice transition - avoid source from choice composite, indeterministic behavior.");
        if (target instanceof ParallelBehaviour)
            throw new IllegalArgumentException("unsupported parallel composition - can make another agent instead of modelling parallel composition.");
        // Find first behavior in sequence
        if (target instanceof SequentialBehaviour seqTarget)
            return getAtomicBehavior(search.apply(seqTarget), search) ;

        // Has to be atomic
        return (AtomicBehaviour) target;
    }

    /**
     * Convert source behavior to its AtomicBehavior Source
     * Atomic Behavior: Returns itself, type-casted.
     * Sequential Composite: Gets last atomic behavior in sequence.
     * Parallel Composite: Throws error, unsupported - make another agent instead of modelling parallel composition.
     * Choice Composite: Unsupported - avoid source from choice composite, indeterministic behavior.
     *
     * @param source The source behavior
     * @return The atomic behavior representing the initial state
     */
    public static AtomicBehaviour getSourceAtomic(Behaviour source) {
        return getAtomicBehavior(source, SequentialBehaviour::getLastBehavior);
    }

    /**
     * Convert target behavior to its AtomicBehavior Target
     * Atomic Behavior: Returns itself, type-casted.
     * Sequential Composite: Gets first atomic behavior in sequence.
     * Parallel Composite: Throws error, unsupported - make another agent instead of modelling parallel composition.
     * Choice Composite: Unsupported - avoid source from choice composite, indeterministic behavior.
     *
     * @param source The target behavior
     * @return The atomic behavior representing the initial state
     */
    public static AtomicBehaviour getTargetAtomic(Behaviour source) {
        return getAtomicBehavior(source, SequentialBehaviour::getInitialBehavior);
    }
}
