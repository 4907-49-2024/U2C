package pipes.diagrams.state;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the State implementations (AtomicState, SuperState, Transition).
 */
public class StateDiagramTest {

    @Test
    public void testAtomicStateCreation() {
        AtomicState atomicState = new AtomicState("Idle", "state", "waiting");
        assertEquals("Idle", atomicState.name());
        assertEquals("state", atomicState.kind());
        assertEquals("waiting", atomicState.doActivity());
        assertEquals("Idle", atomicState.getKey());
    }

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
    public void testTransitionParsingNormal() {
        Transition transition = new Transition(null, null, "inputEvent/outputEvent");
        assertEquals("inputEvent", transition.input());
        assertEquals("outputEvent", transition.output());
    }

    @Test
    public void testTransitionParsingInitialState() {
        Transition initialTransition = new Transition(null, null, "");
        assert initialTransition.input().isEmpty();
        assert initialTransition.output().isEmpty();
    }

    @Test
    public void testTransitionParsingWithGuard() {
        Transition transition = new Transition(null, null, "inEvent[guardCondition]/outEvent");
        assertEquals("inEvent[guardCondition]", transition.input());
        assertEquals("outEvent", transition.output());
    }

    @Test
    public void testIsSequentialTransition() {
        Transition sequentialTransition = new Transition(null, null, "eventA/eventA"); // same input and output
        Transition nonSequentialTransition = new Transition(null, null, "eventA/eventB"); // different input and output

        assertTrue(Transition.isSequentialTransition(sequentialTransition));
        assertFalse(Transition.isSequentialTransition(nonSequentialTransition));
    }

    @Test
    public void testInitialTransition() {
        Transition initialTransition = new Transition(null, null, "");
        assertEquals("", initialTransition.input());
        assertEquals("", initialTransition.output());
    }

    @Test
    public void testAtomicState() {
        String conditionalExpression = "[if (material = 1 AND state > 2 AND status < 0) -> ready:=0 | " +
                "(material >= 1 AND state <= 2 || status = 0) -> ready:=1 | " +
                "NOT ((material = 1 AND state > 2 AND status < 0) || (material >= 1 AND state <= 2 || status = 0)) -> ready:=2 fi]";

        AtomicState conditionalState = new AtomicState("ConditionalState", "state", conditionalExpression);

        assertEquals(conditionalExpression, conditionalState.doActivity());
        assertEquals("ConditionalState", conditionalState.name());
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