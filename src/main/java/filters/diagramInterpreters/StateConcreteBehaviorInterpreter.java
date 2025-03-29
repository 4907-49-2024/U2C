package filters.diagramInterpreters;

import filters.Filter;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.diagrams.state.AtomicState;
import pipes.diagrams.state.State;
import pipes.diagrams.state.SuperState;

import java.util.HashSet;
import java.util.Set;

/**
 * The StateAbstractBehaviorInterpreter takes in a StateDiagram, and returns a set of AtomicBehaviors.
 * Each individual AtomicBehavior has a method to get its concrete behavior,
 * combine them all on separate lines to get the full spec.
 */
public class StateConcreteBehaviorInterpreter extends Filter<SuperState, ConcreteBehaviorSpecification> {
    public StateConcreteBehaviorInterpreter(SuperState input) {
        super(input);
    }

    /**
     * Register all atomic behaviors contained in given state (including itself if applicable).
     * BaseCase: AtomicState found, register as AtomicBehavior and return.
     * Recursive case: SuperState found, recursively call method on its children.
     * Precondition: Output set has been initialized!
     * @param state The state to recursively search for AtomicStates
     */
    private void registerAtomicBehaviorRecursive(State state){
        // Base Case
        if(state instanceof AtomicState atomic) {
            output.add(new AtomicBehavior(atomic.name(), atomic.doActivity()));
        }
        // Recursive Case
        else if(state instanceof SuperState superState) {
            for(State child : superState.children()){
                registerAtomicBehaviorRecursive(child);
            }
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // Collect all atomic behaviors.
        output = new ConcreteBehaviorSpecification();
        registerAtomicBehaviorRecursive(input);
    }
}
