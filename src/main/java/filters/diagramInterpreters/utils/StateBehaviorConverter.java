package filters.diagramInterpreters.utils;

import pipes.c2ka.behaviours.*;
import pipes.diagrams.state.AtomicState;
import pipes.diagrams.state.State;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

/**
 * Utility class to provide methods which convert given states to their respective behavior.
 */
public class StateBehaviorConverter {
    /**
     * Determine what composite to instantiate based on super state properties, and return a new empty instance of it.
     * If the state has multiple regions: Parallel Composition
     * If an internal transition is sequential: Sequential Composition
     * Otherwise: Choice Composition
     *
     * @param superState The super state to evaluate for behavior composition type
     * @return A new composite behavior instance of the appropriate type
     *
     * @throws IllegalStateException If properties for parallel and sequential composition are found at the same time.
     */
    private static CompositeBehaviour instantiateComposite(SuperState superState) throws IllegalStateException {
        boolean isParallel  = superState.numRegions() > 1;
        // If any transitions are sequential, should be a sequential state
        boolean isSequential  = superState.innerTransitions().stream().anyMatch(Transition::isSequential);

        // Error checks (model validation)
        if (isParallel && isSequential) {
            throw new IllegalStateException("Simultaneously parallel and sequential composite behaviors are not supported");
        }

        boolean isEntirelySequential = superState.innerTransitions().stream().allMatch(Transition::isSequential);
        if (isSequential && !isEntirelySequential) {
            throw new IllegalStateException("Partially sequential composite behaviors are not supported");
        }

        // Assume model is valid here, return result based on bools evaluated earlier
        if(isSequential){
            return new SequentialBehaviour();
        }
        if(isParallel){
            return new ParallelBehaviour();
        }
        return new ChoiceBehaviour(); // Default
    }

    /**
     * Convert State to behavior
     * Base Case: Atomic State, add as is (convert to atomic behavior)
     * Recursive Case: Super state, add composite after recursively adding contents
     *
     * @param state The state to convert to a behavior
     * @return The behavior representation of the given state
     */
    public static Behaviour getStateBehavior(State state){
        // Base case
        if (state instanceof AtomicState atState) {
            return new AtomicBehaviour(atState.name(), atState.doActivity());
        }
        // Recursive case, add composite (and recursively fill it)
        if (state instanceof SuperState supState) {
            // Get composite type
            CompositeBehaviour composite = instantiateComposite(supState);
            // Fill composite
            for (State child : supState.children()) {
                composite.addBehavior(getStateBehavior(child));
            }
            return composite;
        }
        // Should never reach here normally, need a default return, but we also need to typecast above
        return null;
    }
}
