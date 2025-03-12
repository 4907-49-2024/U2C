package pipes.diagrams.state;

import java.util.HashSet;
import java.util.Set;

public class SuperState implements State {
    private final Set<State> children;
    private final Set<Transition> innerTransitions;

    public SuperState() {
        this.children = new HashSet<>();
        this.innerTransitions = new HashSet<>();
    }
}
