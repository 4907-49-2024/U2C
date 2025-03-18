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
}