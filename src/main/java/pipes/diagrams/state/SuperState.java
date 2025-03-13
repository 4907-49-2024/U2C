package pipes.diagrams.state;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of a super state in a state diagram.
 * Immutable, components calculated before creation
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
}
