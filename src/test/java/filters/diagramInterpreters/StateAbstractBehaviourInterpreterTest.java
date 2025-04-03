package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.behaviours.ChoiceBehaviour;
import pipes.c2ka.specifications.AbstractBehaviorSpecification;
import pipes.parserConfig.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.c2ka.behaviours.CompositeBehaviour;
import pipes.diagrams.state.SuperState;

import static testUtils.TestPaths.BASE_C2KA;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateAbstractBehaviourInterpreterTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private AbstractBehaviorSpecification runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(BASE_C2KA, inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        ModelElement stateDiagramElem = parser.getOutput();
        // Filter 2
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3 - FUT
        StateAbstractBehaviorInterpreter interpreter = new StateAbstractBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        AbstractBehaviorSpecification spec = runTestPipeline("Atomic.uml");
        // Simulate output
        CompositeBehaviour choice = new ChoiceBehaviour();
        choice.addBehavior(new AtomicBehaviour("<name>", "<behavior-expression>"));
        AbstractBehaviorSpecification specExpect = new AbstractBehaviorSpecification("Atomic Behavior", choice);

        assert spec.equals(specExpect);
    }

    // TODO: implement rest of tests.
}