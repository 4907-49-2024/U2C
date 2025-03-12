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
