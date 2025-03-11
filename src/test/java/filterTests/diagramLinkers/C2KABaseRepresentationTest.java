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

/***
 * Test Suite measures if all information is retained by Linker.
 */
public class C2KABaseRepresentationTest {

    /***
     * Tests Atomic Assignment in UML
     * @throws Exception
     */
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
        State state1 = new State("<name>", "state", "/do Activity ready:=1", null); //a+b
        assert states.contains(state1);
        assert states.size() == 1;

        //Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();

    }

    /***
     * Tests the Atomic(basic) state diagram.
     * @throws Exception
     */
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
        State initial = new State("<name>", "state", "<behaviour-expression>", null);
        assert states.contains(initial);

        //CHeck Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

    /***
     * Tests the mapping of transition between 2 states.
     * @throws Exception
     */
    @Test
    public void testNextMapping() throws Exception {
        String xmiFile = "C2KA-BaseRepresentations/NextMappings.uml";
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

        Set<State> states = d.getStates();
        assert d.getName().equals("Next Mapping");
        assert states.size() == 2;
        // Are the Required states being picked up?
        State state1 = new State("Current", "state", "", null);
        State state2 = new State("NextBehaviour", "state", "", null);
        assert states.contains(state1);
        assert states.contains(state2);

        Set<Transition> transitions = d.getTransitions();
        assert transitions.contains(new Transition(state1, state2, "inStim / nextStim"));
//        for (Transition transition : transitions) {
//            System.out.println(transition);
//            System.out.println(transition.target());
//            System.out.println(transition.source());
//            System.out.println(transition.guard());
//        }
    }

    /***
     * Tests if Parallel behaviour is recorded in state diagram.
     * @throws Exception
     */
    @Test
    public void testParallel() throws Exception {
        String xmiFile = "C2KA-BaseRepresentations/Parallel.uml";
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
        assert d.getName().equals("Parallel Composition");
        Set<State> states = d.getStates();

        //How do we know that this state is composite? through the name having ||
        State state1 = new State("a||b", "state", "", null);
        State state2 = new State("b", "state", "", null);
        State state3 = new State("a", "state", "", null);

        assert states.contains(state1);
        assert states.contains(state2);
        assert states.contains(state3);

        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

    /***
     * Tests if sequence operation is recorded.
     * @throws Exception
     */
    @Test
    public void testSequential() throws Exception {
        String xmiFile = "C2KA-BaseRepresentations/Sequential.uml";
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
        assert d.getName().equals("Sequential Composition");

        Set<State> states = d.getStates();
        State state1 = new State("a|b", "state", "", null);
        State initial = new State("", "", "", state1);
        State a = new State("a", "state", "", state1);
        State b = new State("b", "state", "", state1);
        assert states.contains(state1);
        assert states.contains(initial);
        assert states.contains(a);
        assert states.contains(b);

        Set<Transition> transitions = d.getTransitions();
        //System.out.println("Transitions: " + transitions.iterator().next());
        Transition transition1 = new Transition(a, b, "(a out, b in)");
        //Transition[source=State[name=, kind=, doActivity=, parent=State[name=a|b, kind=state, doActivity=, parent=null]],
        // target=State[name=a, kind=state, doActivity=, parent=State[name=a|b, kind=state, doActivity=, parent=null]],
        // input=, guard=, output=]
        assert transitions.contains(transition1);
    }

    /***
     * Conditional data stored in Representation.
     * @throws Exception
     */
    @Test
    public void testAtomicConditional() throws Exception {
        String xmiFile = "C2KA-BaseRepresentations/Atomic-Conditional.uml";
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
        //assert d.getName().equals("");
        Set<State> states = d.getStates();
        for (State state : states) {
            System.out.println(state);
        }
    }

    /***
     * Tests if "choice" of state diagram is stored in representation.
     * @throws Exception
     */
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

        //Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();

    }

}
