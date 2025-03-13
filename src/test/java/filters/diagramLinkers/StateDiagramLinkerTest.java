package filters.diagramLinkers;

import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.diagrams.state.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test the stateDiagramLinker filter
 */
public class StateDiagramLinkerTest {
    /**
     * Define test pipeline
     * @param inputDiagramXMI Reference to input diagram file
     * @return StateDiagram type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private StateDiagram runTestPipeline(String inputDiagramXMI) throws Exception{
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParser parser = new XMIParser(new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel));
        UMLModel model = parser.getModel();
        // Assume single state diagram, get first one
        List<ModelElement> stateDiagramsElems = model.getTypedElements(StateType.statemachine.name());
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramsElems.getFirst());

        // Start Thread (run filter)
        Thread t = new Thread(linker);
        t.start();
        t.join();

        // Return output pipe
        return linker.getStateDiagram();
    }


    /***
     * Tests the Atomic(basic) state diagram.
     * @throws Exception
     */
    @Test
    public void testAtomic() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Atomic.uml");

        // Check name
        assert d.getName().equals("Atomic Behavior");

        // Setup state Checks
        Set<State> roots = d.getRoots();
        State state1 = new AtomicState("<name>", "state", "<behavior-expression>");
        // Run state Checks
        assert roots.size() == 1;
        assert roots.contains(state1);

        // Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

    /***
     * Tests Atomic Assignment in UML
     * @throws Exception
     */
    @Test
    public void testAtomicAssignment() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Atomic-Assignment.uml");
        // Check name
        assert d.getName().equals("Atomic Assignment");

        // Setup state Checks
        Set<State> states = d.getRoots();
        State state1 = new AtomicState("<name>", "state", "ready:=1"); //a+b
        // Run State Checks
        assert states.size() == 1;
        assert states.contains(state1);

        // Setup Transition Checks
        Set<Transition> transitions = d.getTransitions();
        // Run Transition Checks
        assert transitions.isEmpty();

    }

//    /***
//     * Conditional data stored in Representation.
//     * @throws Exception
//     */
//    @Test
    // FIXME: Test not implemented
//    public void testAtomicConditional() throws Exception {
//        String xmiFile = "C2KA-BaseRepresentations/Atomic-Conditional.uml";
//        String metaModel = "custom/stateMetaModel.xml";
//        String xmiTrans = "custom/xmiStateTrans.xml";
//        XMIParser parser = new XMIParser(new XMIParserConfig(xmiFile, xmiTrans, metaModel));
//        UMLModel model = parser.getModel();
//        StateDiagramLinker linker = new StateDiagramLinker(model);
//
//        // Start Thread (run filter)
//        Thread t = new Thread(linker);
//        t.start();
//        t.join();
//
//        // Check output
//        StateDiagram d = linker.getStateDiagram();
//        // Assuming single diagram, do not need to match it
//        StateDiagram d = diagrams.iterator().next();
//        //assert d.getName().equals("");
//        Set<State> states = d.getRoots();
//    }

    /***
     * Tests if "choice" of state diagram is stored in representation.
     * @throws Exception
     */
    @Test
    public void testChoice() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Choice.uml");
        // Check name
        assert d.getName().equals("Behaviour Choice");

        // Setup state Checks
        Set<State> roots = d.getRoots();
        Set<State> children = new HashSet<>();
        State a = new AtomicState("a", "state", "<behavior-expression>");
        State b = new AtomicState("b", "state", "<behavior-expression>");
        children.add(a);
        children.add(b);
        State parent = new SuperState("a+b", children, new HashSet<>(), 1); //
        // Run state checks
        assert roots.size() == 1;
        assert roots.contains(parent);

        //Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

    /***
     * Tests if sequence operation is recorded.
     * @throws Exception
     */
    @Test
    public void testSequential() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Sequential.uml");
        // Check name
        assert d.getName().equals("Sequential Composition");



        // Setup state Checks
        Set<State> roots = d.getRoots();
        Set<State> children = new HashSet<>();
        State initial = new AtomicState("", "", "");
        State a = new AtomicState("a", "state", "<behavior-expression>");
        State b = new AtomicState("b", "state", "<behavior-expression>");
        children.add(a);
        children.add(b);
        children.add(initial);

        // Setup transitions checks
        Set<Transition> transitions = d.getTransitions();
        Set<Transition> innerTransitions = new HashSet<>();
        Transition transition1 = new Transition(a, b, "stimulus (a out, b in)");
        innerTransitions.add(transition1);

        // Setup Parent
        State parent = new SuperState("a|b", children, innerTransitions, 1); //


        // Run state Checks
        assert roots.size() == 1;
        assert roots.contains(parent);

        // Run transition checks
        assert transitions.size() == 1;
        assert transitions.contains(transition1);
    }

    /***
     * Tests if Parallel behaviour is recorded in state diagram.
     * @throws Exception
     */
    @Test
    public void testParallel() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Parallel.uml");
        // Check name
        assert d.getName().equals("Parallel Composition");

        // Setup state Checks
        Set<State> roots = d.getRoots();
        Set<State> children = new HashSet<>();
        State a = new AtomicState("b", "state", "<behavior-expression>");
        State b = new AtomicState("a", "state", "<behavior-expression>");
        children.add(a);
        children.add(b);
        State parent = new SuperState("a||b", children, new HashSet<>(), 1); //
        // Run state Checks
        assert roots.size() == 1;
        assert roots.contains(parent);

        // Run transition checks
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();

        // FIXME: New requirement for regions in parallel (issue is up already)
    }

    /***
     * Tests the mapping of transition between 2 states.
     * @throws Exception
     */
    @Test
    public void testNextMapping() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/NextMappings.uml");
        // Check name
        assert d.getName().equals("Next Mapping");

        // Setup state Checks
        Set<State> states = d.getRoots();
        State state1 = new AtomicState("Current", "state", "<behavior-expression>");
        State state2 = new AtomicState("NextBehavior", "state", "<behavior-expression>");
        // Run state Checks
        assert states.size() == 2;
        assert states.contains(state1);
        assert states.contains(state2);

        // Setup transition checks
        Set<Transition> transitions = d.getTransitions();
        Transition transition1 = new Transition(state1, state2, "inStim / nextStim");
        // Run transition checks
        assert transitions.size() == 1;
        assert transitions.contains(new Transition(state1, state2, "inStim / nextStim"));
    }

}