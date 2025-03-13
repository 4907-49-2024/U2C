package filters.diagramInterpreters;

import pipes.c2ka.primitives.*;
import pipes.diagrams.state.State;
import pipes.diagrams.state.StateDiagram;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.Set;


/**
 * The StateAbstractBehaviorInterpreter takes in a StateDiagram, and returns a representation of its AbstractBehaviors.
 */
public class StateAbstractBehaviorInterpreter implements Runnable {
    // INPUT
    private final StateDiagram diagram;
    // OUTPUT (top level behavior: string representation = Abstract Behavior Specification)
    private Behavior topBehavior;

    public StateAbstractBehaviorInterpreter(StateDiagram diagram) {
        this.diagram = diagram;
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
        Set<State> roots = diagram.getRoots();
//        if (roots.size() > 1) {
//
//        }
    }
}
