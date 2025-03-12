package pipes.diagrams.state;

import java.util.HashSet;
import java.util.Set;

/**
 * State diagram internal representation.
 * Contains its states, transitions and a name to map it to an agent.
 */
public class StateDiagram {
    private final String name;
    private final Set<State> rootStates;

    public StateDiagram(String name) {
        this.name = name;
        this.rootStates = new HashSet<>();
    }

    /**
     * Get all transitions in this state, recursively.
     * Base Case: AtomicState - return empty set
     * Recursive Case: SuperState - Add internal transitions
     *
     * @param state the state to search for transitions in recursively
     * @return Set of transitions in this state and all its substates
     */
    private Set<Transition> getStateTransitionsRecursive(State state){
        // Recursive case - Return super state transitions
        if(state instanceof SuperState superState){
            return superState.innerTransitions();
        }
        // Else, Base case return empty set
        return new HashSet<>();
    }

    /**
     * Registers a state diagram element to its collection
     * <br>
     * @param root The root state to register
     */
    public void registerRoot(State root) {
        rootStates.add(root);
    }

    /**
     * @return Diagram name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Diagram root states - Iteration method should be determined outside
     */
    public Set<State> getRoots() {
        return rootStates;
    }

    /**
     * @return Transitions contained in the state diagram
     */
    public Set<Transition> getTransitions() {
        Set<Transition> transitions = new HashSet<>();
        // Recursively collect transitions in all roots
        for (State root : rootStates) {
            transitions.addAll(getStateTransitionsRecursive(root));
        }
        return transitions;
    }


    @Override
    public String toString() {
        return "StateDiagram{" +
                "name='" + name + '\'' +
                '}';
    }
}
