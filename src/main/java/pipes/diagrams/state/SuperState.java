package pipes.diagrams.state;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of a super state in a state diagram.
 * Immutable, components calculated before creation
 * Note: For us, a state-diagram is represented the same as a SuperState!
 * It's just the special case of the root of the state tree, with no parents.
 *
 * @param children Children states of this super state
 * @param innerTransitions Transitions between the children (but not nested!)
 * @param numRegions Number of regions in the super state. (>1 for concurrent superstate)
 */
public record SuperState(String name, Set<State> children, Set<Transition> innerTransitions, int numRegions) implements State {
    /**
     * @return Key used to uniquely identify state.
     */
    @Override
    public String getKey() {
        return name;
    }

    /**
     * Get all transitions in this state, recursively.
     * Base Case: AtomicState - return empty set
     * Recursive Case: SuperState - Add internal transitions
     *
     * @param state the state to search for transitions in recursively
     * @return Set of transitions in this state and all its substates
     */
    private static Set<Transition> getTransitionsRecursive(State state){
        // Base case - AtomicState, no transitions
        if (state instanceof AtomicState) return new HashSet<>();

        // Recursive case - Return super state transitions and substate transitions
        SuperState superState = (SuperState) state;

        Set<Transition> recursiveTransitions = new HashSet<>(superState.innerTransitions);
        for(State child : superState.children()){
            recursiveTransitions.addAll(getTransitionsRecursive(child));
        }

        return recursiveTransitions;
    }

    /**
     * Get all transitions in this state, including nested ones.
     *
     * @return All transitions contained in this state, including nested transitions.
     */
    public Set<Transition> getAllTransitions(){
        return getTransitionsRecursive(this);
    }

}
