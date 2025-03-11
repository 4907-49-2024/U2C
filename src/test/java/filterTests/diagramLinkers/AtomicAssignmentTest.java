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

public class AtomicAssignmentTest {
    @Test
    public void testAtomicAssignment() throws Exception {
        // Setup Input
        String xmiFile = "C2KA-BaseRepresentations/Atomic-Assignment.uml";
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

        assert d.getName().equals("Atomic Assignment");

        //Check States
        Set<State> states = d.getStates();
//        assert states.size() == 3;
        State state1 = new State("<name>", "state", "/do Activity ready:=1", null); //a+b
        assert states.contains(state1);
//
        assert states.size() == 1;

        //Debugging
//        for (State state : states) {
//            System.out.println(state.name());
//            System.out.println(state.kind());
//            System.out.println(state.doActivity());
//            System.out.println(state.parent());
//            System.out.println("````````````````");
//        }
        //CHeck Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();

    }

    @Test
    public void testAtomic() throws Exception {
        // Setup Input
        String xmiFile = "C2KA-BaseRepresentations/Atomic.uml";
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

        assert d.getName().equals("Atomic Behavior");

        //Check States
        Set<State> states = d.getStates();
        assert states.size() == 1;
        //State initial = new State("<name>", "state", "/do Activity <behaviour-expression>", null);
        //assert states.contains(initial);


        for (State state : states) {
            System.out.println(state.name());
            System.out.println(state.kind());
            System.out.println(state.doActivity());
            System.out.println(state.parent());
        }
        //CHeck Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

}
