package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.c2ka.behaviors.Behavior;
import pipes.c2ka.behaviors.CompositeBehavior;
import pipes.diagrams.state.SuperState;

import java.util.List;

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

    // TODO: implement rest of tests... look at the behavior tests for reference on how to handle indeterministic output!
    //      There are tricks you need to do because after two behaviors in a choice the order is randomized.
}