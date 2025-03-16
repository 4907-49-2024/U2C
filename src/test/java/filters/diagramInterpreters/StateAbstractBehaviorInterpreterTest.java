package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.c2ka.behaviors.*;
import pipes.diagrams.state.SuperState;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateAbstractBehaviorInterpreterTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private CompositeBehavior runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        UMLModel model = parser.getOutput();
        // Filter 2
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3 - FUT
        StateAbstractBehaviorInterpreter interpreter = new StateAbstractBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Atomic.uml");

        assert behavior.toString().equals("( <name> )");
    }

//    @Test
//    void testAtomicAssignment() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Atomic-Assignment.uml");
//        //assertTrue(behavior instanceof AtomicBehavior);
//        assertTrue(behavior.toString().equals("ready:=1"));
//    }
//
//    @Test
//    void testAtomicConditional() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Atomic-Conditional.uml");
//        //assertTrue(behavior instanceof AtomicBehavior);
//        String expected = "[if (material = 1 && state > 2 && status < 0) -> ready:=0 | (material >= 1 && state <= 2 || status = 0) -> ready:=1; do (material = 1 && state = 3) -> ready:= 3 od | ~ ((material = 1 && state > 2 && status < 0) || (material >= 1 && state <= 2 || status = 0)) -> ready:=0 fi]";
//        assert (behavior.toString().equals(expected));
//    }
//
//    @Test
//    void testChoice() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Choice.uml");
//        //assertTrue(behavior instanceof ChoiceBehavior);
//        assertTrue(behavior.toString().equals("a+b"));
//    }
//
//    @Test
//    void testSequential() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Sequential.uml");
//        //assertTrue(behavior instanceof SequentialBehavior);
//        assertTrue(behavior.toString().equals("a|b"));
//    }
//
//    @Test
//    void testParallel() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/Parallel.uml");
//        //assertTrue(behavior instanceof ParallelBehavior);
//        assertTrue(behavior.toString().equals("a||b"));
//    }
//
//    @Test
//    void testNextMappings() throws Exception {
//        Behavior behavior = runTestPipeline("C2KA-BaseRepresentations/NextMappings.uml");
//        //assertTrue(behavior instanceof CompositeBehavior);
//        assertTrue(behavior.toString().equals("Next Mapping"));
//    }
}