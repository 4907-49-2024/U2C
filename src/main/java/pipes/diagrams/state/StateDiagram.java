package pipes.diagrams.state;

import java.util.HashSet;
import java.util.Set;

/**
 * State diagram internal representation.
 * Contains its states, transitions and a name to map it to an agent.
 */
public class StateDiagram {
    private final String name;
    private final Set<State> states;
    private final Set<Transition> transitions;
    private State initialState;

    public StateDiagram(String name) {
        this.name = name;
        this.states = new HashSet<>();
        this.transitions = new HashSet<>();
    }

    /**
     * Registers a state diagram element to its collection
     * <br>
     *
     * @param element The element to register
     */
    public void registerElement(StateDiagramElement element) throws IllegalArgumentException, IllegalStateException {
        if (element instanceof State s) {
            if (s.kind().isEmpty()) {
                if (initialState == null) {
                    initialState = s;
                } else {
                    // This may technically be legal in concurrent regions, which we don't handle yet.
                    throw new IllegalStateException("There exists more than one initial State.");
                }
            }

            this.states.add(s);
        } else if (element instanceof Transition t) {
            this.transitions.add(t);
        } else {
            throw new IllegalArgumentException("Element is not a known StateDiagramElement");
        }
    }

    /**
     * @return Diagram name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Diagram States
     */
    public Set<State> getStates() {
        return states;
    }

    /**
     * @return Diagram Transitions
     */
    public Set<Transition> getTransitions() {
        return transitions;
    }


    @Override
    public String toString() {
        return "StateDiagram{" +
                "name='" + name + '\'' +
                '}';
    }
}
