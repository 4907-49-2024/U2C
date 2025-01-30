package unittest.pipes.diagrams.state;

import org.junit.jupiter.api.Test;
import pipes.diagrams.state.State;
import pipes.diagrams.state.StateDiagram;
import pipes.diagrams.state.Transition;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StateDiagramTest {
    @Test
    public void registerElementTest() {
        StateDiagram diagram = new StateDiagram("name");
        Set<State> states = new HashSet<>();
        Set<Transition> transitions = new HashSet<>();

        // Test state registering
        State initialState1 = new State("", "", "");
        State normalState = new State("stateName", "N/A", "processing");

        diagram.registerElement(initialState1);
        diagram.registerElement(normalState);
        states.add(initialState1);
        states.add(normalState);

        assert states.equals(diagram.getStates());

        // Test transition registering
        Transition t1 = new Transition(initialState1, normalState, "");
        // No explicit prohibition of multiple transitions from initial state - although there should be, probably
        Transition t1_2 = new Transition(null, null, "");
        Transition t2 = new Transition(null, null, "in/out");
        Transition t3 = new Transition(null, null, "in2[guard]/out2");

        diagram.registerElement(t1);
        diagram.registerElement(t1_2);
        diagram.registerElement(t2);
        diagram.registerElement(t3);
        transitions.add(t1);
        transitions.add(t1_2);
        transitions.add(t2);
        transitions.add(t3);

        assert transitions.equals(diagram.getTransitions());

        // Also check for illegal initial state add
        State initialState2 = new State("", "", null);
        assertThrows(IllegalStateException.class, () -> diagram.registerElement(initialState2));
    }
}
