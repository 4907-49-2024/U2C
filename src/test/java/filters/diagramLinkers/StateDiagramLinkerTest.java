package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import filters.xmiParser.XMIParser;
import pipes.XMIParserConfig;
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
     * Define test pipeline for Filter Under Test (FUT)
     * @param inputDiagramXMI Reference to input diagram file
     * @return StateDiagram type pipe object (FUT's output)
     * @throws Exception In case of thread or input exceptions
     */
    private StateDiagram runTestPipeline(String inputDiagramXMI) throws Exception{
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        UMLModel model = parser.getOutput();
        // Filter 2 - FUT
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        return linker.getOutput();
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

    /***
     * Tests if Atomic Conditional state diagram is correctly parsed and represented.
     * @throws Exception
     */
    @Test
    public void testAtomicConditional() throws Exception {
        // Get output
        StateDiagram d = runTestPipeline("C2KA-BaseRepresentations/Atomic-Conditional.uml");

        // Check name
        assert d.getName().equals("Atomic Conditional");

        // Setup state Checks
        Set<State> roots = d.getRoots();
        String conditionalExpression = "[if (material = 1 && state > 2 && status < 0) -> ready:=0 | (material >= 1 &&" +
                " state <= 2 || status = 0) -> ready:=1; do (material = 1 && state = 3) -> ready:= 3 od | ~ ((material" +
                " = 1 && state > 2 && status < 0) || (material >= 1 && state <= 2 || status = 0)) -> ready:=0 fi]";
        State conditionalState = new AtomicState("<name>", "state", conditionalExpression);

        // Run state Checks
        assert roots.size() == 1;
        assert roots.contains(conditionalState);

        // Check Transitions
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
    }

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
        State parent = new SuperState("a||b", children, new HashSet<>(), 2); //
        // Run state Checks
        assert roots.size() == 1;
        assert roots.contains(parent);

        // Run transition checks
        Set<Transition> transitions = d.getTransitions();
        assert transitions.isEmpty();
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
        assert transitions.contains(transition1);
    }

}