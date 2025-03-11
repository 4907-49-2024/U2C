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


public class StateChoiceTest {
    @Test
    public void testStateChoice() throws Exception {
        // Setup Input
        String xmiFile = "C2KA-BaseRepresentations/Choice.uml";
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

        assert d.getName().equals("Behaviour Choice");

        //Check States
        Set<State> states = d.getStates();
        assert states.size() == 3;
        State initial = new State("a+b", "state", "", null); //a+b
        assert states.contains(initial);
        State a = new State("a", "state", "", initial);
        assert states.contains(a);
        State b = new State("b", "state", "", initial);
        assert states.contains(b);
//        for (State state : states) {
//            System.out.println(state.name());
//            System.out.println(state.kind());
//            System.out.println(state.doActivity());
//            System.out.println(state.parent());
//        }
        //CHeck Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();

    }

}
