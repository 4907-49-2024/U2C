package pipes.diagrams.state;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the AtomicState implementation.
 */
public class AtomicStateTest {

    @Test
    public void testAtomicStateCreation() {
        AtomicState atomicState = new AtomicState("Idle", "state", "waiting");
        assertEquals("Idle", atomicState.name());
        assertEquals("state", atomicState.kind());
        assertEquals("waiting", atomicState.doActivity());
        assertEquals("Idle", atomicState.getKey());
    }

    @Test
    public void testIsInitialState() {
        AtomicState atomicState = new AtomicState("", "", "");

        assertTrue(AtomicState.isInitialState(atomicState));
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
}