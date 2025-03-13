package filters.diagramInterpreters;

import filters.Filter;
import pipes.c2ka.primitives.*;
import pipes.diagrams.state.*;

import java.util.Set;


/**
 * The StateAbstractBehaviorInterpreter takes in a StateDiagram, and returns a representation of its AbstractBehaviors.
 */
public class StateAbstractBehaviorInterpreter extends Filter<StateDiagram, CompositeBehavior> {
    public StateAbstractBehaviorInterpreter(StateDiagram diagram) {
        super(diagram);
    }

    /**
     * Convert State to behavior
     * Base Case: Atomic State, add as is (convert to atomic behavior)
     * Recursive Case: Super state, add composite after recursively adding contents
     *
     * @param state The state to convert to a behavior
     * @return The behavior representation of the given state
     */
    private Behavior createBehaviorRecursive(State state){
        // Base case
        if (state instanceof AtomicState atState) {
            return new AtomicBehavior(atState.name(), atState.doActivity());
        }
        // Recursive case, add composite (and recursively fill it)
        if (state instanceof SuperState supState) {
            // Get composite type
            CompositeBehavior composite = instantiateComposite(supState);
            // Fill composite
            for (State child : supState.children()) {
                composite.addBehavior(createBehaviorRecursive(child));
            }
            return composite;
        }
        // Should never reach here normally, need a default return, but we also need to typecast above
        return null;
    }

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
    private CompositeBehavior instantiateComposite(SuperState superState) throws IllegalStateException {
        boolean isParallel  = superState.numRegions() > 1;
        // If any transitions are sequential, should be a sequential state
        boolean isSequential  = superState.innerTransitions().stream().anyMatch(Transition::isSequentialTransition);

        // Error checks (model validation)
        if (isParallel && isSequential) {
            throw new IllegalStateException("Simultaneously parallel and sequential composite behaviors are not supported");
        }

        boolean isEntirelySequential = superState.innerTransitions().stream().allMatch(Transition::isSequentialTransition);
        if (isSequential && !isEntirelySequential) {
            throw new IllegalStateException("Partially sequential composite behaviors are not supported");
        }

        // Assume model is valid here, return result based on bools evaluated earlier
        if(isSequential){
            return new SequentialBehavior();
        }
        if(isParallel){
            return new ParallelBehavior();
        }
        return new ChoiceBehavior(); // Default
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // Get all root states
        Set<State> roots = input.getRoots();
        // Check for top level composite
        if (roots.size() == 1) {
            State root = roots.iterator().next();
            if (root instanceof SuperState superRoot) {
                output = instantiateComposite(superRoot);
            }
        }
        // Default top level (>1 root OR single atomic root)
        if (output == null) {
            output = new ChoiceBehavior();
        }
        // Add all roots to top behavior, they build their inner behaviors before being added.
        for (State root : input.getRoots()) {
            output.addBehavior(createBehaviorRecursive(root));
        }
    }
}
