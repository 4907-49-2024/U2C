package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.c2ka.primitives.Behavior;
import pipes.diagrams.state.StateDiagram;
import pipes.diagrams.state.StateType;

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
    private Behavior runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        // Filter 1
        XMIParser parser = new XMIParser(new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel));
        UMLModel model = parser.getModel();
        // Assume single state diagram, get first one
        List<ModelElement> stateDiagramsElems = model.getTypedElements(StateType.statemachine.name());
        // Filter 2
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramsElems.getFirst());

        // Start Thread (run filter)
        Thread t = new Thread(linker);
        t.start();
        t.join();
        StateDiagram d = linker.getStateDiagram();

        // Filter 3 - Filter under test!
        StateAbstractBehaviorInterpreter interpreter = new StateAbstractBehaviorInterpreter(d);        // Start Thread (run filter)
        Thread filter3 = new Thread(interpreter);
        filter3.start();
        filter3.join();

        return interpreter.getTopBehavior();
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