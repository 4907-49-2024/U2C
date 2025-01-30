package unittest.pipes;

import org.junit.jupiter.api.Test;
import pipes.diagrams.Transition;

/**
 * Test class for transition records.
 * <p>
 * Mostly tests that the string parsing behaves as expected for the different use cases.
 */
public class TransitionTest {

    @Test
    public void testInitialState() {
        Transition initialState = new Transition(null, null, "");
        assert initialState.input().isEmpty();
        assert initialState.output().isEmpty();
        assert initialState.guard().isEmpty();
    }


    @Test
    public void testNormalState() {
        Transition normalState =  new Transition(null, null, "stimIn/stimOut");
        assert normalState.input().equals("stimIn");
        assert normalState.output().equals("stimOut");
        assert normalState.guard().isEmpty();
    }


    @Test
    public void testGuardedState() {
        Transition guardedState = new Transition (null, null, "stim1[x=1]/stim2");
        assert guardedState.input().equals("stim1");
        assert guardedState.output().equals("stim2");
        assert guardedState.guard().equals("x=1");
    }
}
