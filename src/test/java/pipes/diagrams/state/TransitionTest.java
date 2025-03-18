package pipes.diagrams.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testSequentialTransition() {
        Transition sequentialTransition = new Transition(null, null, "stimulus");
        assert sequentialTransition.input().equals("stimulus");
        assert sequentialTransition.output().equals("stimulus");
        assert Transition.isSequentialTransition(sequentialTransition);
    }

    @Test
    public void testWhitespaceParsing() {
        Transition transitionWithWhitespace = new Transition(null, null, "  stimIn  /  stimOut  ");
        assert transitionWithWhitespace.input().equals("stimIn");
        assert transitionWithWhitespace.output().equals("stimOut");
    }

    @Test
    public void testTransitionParsingWithGuard() {
        Transition transition = new Transition(null, null, "inEvent[guardCondition]/outEvent");
        assertEquals("inEvent[guardCondition]", transition.input());
        assertEquals("outEvent", transition.output());
    }
}
