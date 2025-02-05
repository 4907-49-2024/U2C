package filterTests.diagramLinkers;

import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.diagrams.state.State;
import pipes.diagrams.state.StateDiagram;
import pipes.diagrams.state.Transition;

import java.util.Set;

/**
 * Test the stateDiagramLinker filter
 * TODO: May be nice to test multiple state diagrams a bit later too.
 */
public class StateDiagramLinkerTest {

    /**
     * Test a single state diagram (with superState.uml)
     */
    @Test
    void testSingleStateDiagram() throws Exception {
        // Setup Input
        String xmiFile = "superState.uml";
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParser parser = new XMIParser(new XMIParserConfig(xmiFile, xmiTrans, metaModel));
        UMLModel model = parser.getModel();
        StateDiagramLinker linker = new StateDiagramLinker(model);

        // Start Thread (run filter)
        Thread t = new Thread(linker);
        t.start();
        t.join();

        // Check output
        Set<StateDiagram> diagrams = linker.getStateDiagrams();
        // Assuming single diagram, do not need to match it
        StateDiagram d = diagrams.iterator().next();

        // Check statemachine itself
        assert diagrams.size() == 1;
        assert d.getName().equals("StateMachine1");

        // Check states
        Set<State> states = d.getStates();
        assert states.size() == 5;
        State initial = new State("", "", "", null);
        assert states.contains(initial); // Pseudostate-initial
        State s1 = new State("State1", "state", "", null);
        assert states.contains(s1); // Normal State
        State parentState = new State("State4", "state", "", null);
        assert states.contains(parentState); // Parent
        State i1 = new State("Inner1", "state", "", parentState);
        State i2 = new State("Inner2", "state", "", parentState);
        assert states.contains(i1); // Child1
        assert states.contains(i2); // Child2

        // Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.size() == 4;
        assert transitions.contains(new Transition(initial, s1, ""));
        assert transitions.contains(new Transition(parentState, s1, ""));
        assert transitions.contains(new Transition(s1, i1, "nextState"));
        assert transitions.contains(new Transition(s1, i2, "nextState"));
    }
}
