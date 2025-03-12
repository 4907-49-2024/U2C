package pipes.diagrams.state;

import org.junit.jupiter.api.Test;

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
    }


    @Test
    public void testNormalState() {
        Transition normalState =  new Transition(null, null, "stimIn/stimOut");
        assert normalState.input().equals("stimIn");
        assert normalState.output().equals("stimOut");
    }

    // TODO: add isSequential test
}
