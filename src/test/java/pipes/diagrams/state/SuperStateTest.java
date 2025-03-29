package pipes.diagrams.state;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the SuperState implementation.
 */
public class SuperStateTest {

    @Test
    public void testSuperStateCreation() {
        Set<State> children = new HashSet<>();
        AtomicState child1 = new AtomicState("Start", "state", "init");
        AtomicState child2 = new AtomicState("End", "state", "terminate");
        children.add(child1);
        children.add(child2);

        SuperState superState = new SuperState("ProcessFlow", children, new HashSet<>(), 1);

        assertEquals("ProcessFlow", superState.getKey());
        assertEquals(2, superState.children().size());
        assertTrue(superState.children().contains(child1));
        assertTrue(superState.children().contains(child2));
        assertEquals(0, superState.innerTransitions().size());
        assertEquals(1, superState.numRegions());
    }

    @Test
    public void testSuperStateWithTransitions() {
        AtomicState state1 = new AtomicState("A", "state", "activityA");
        AtomicState state2 = new AtomicState("B", "state", "activityB");
        Transition t = new Transition(state1, state2, "eventX/eventY");

        Set<State> children = new HashSet<>();
        children.add(state1);
        children.add(state2);

        Set<Transition> transitions = new HashSet<>();
        transitions.add(t);

        SuperState superState = new SuperState("ParentState", children, transitions, 1);

        assertEquals("ParentState", superState.getKey());
        assertTrue(superState.children().contains(state1));
        assertTrue(superState.innerTransitions().contains(t));
    }

}
