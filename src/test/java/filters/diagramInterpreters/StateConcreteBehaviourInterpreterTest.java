package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.parserConfig.XMIParserConfig;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.diagrams.state.SuperState;

import static testUtils.TestPaths.BASE_C2KA;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateConcreteBehaviourInterpreterTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private ConcreteBehaviorSpecification runTestPipeline(String inputDiagramXMI) throws Exception {
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
        StateConcreteBehaviorInterpreter interpreter = new StateConcreteBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        ConcreteBehaviorSpecification spec = runTestPipeline("Atomic.uml");
        // Expected
        ConcreteBehaviorSpecification specExpect = new ConcreteBehaviorSpecification();
        specExpect.add(new AtomicBehaviour("<name>", "<behavior-expression>"));

        assert spec.equals(specExpect);
    }

    // TODO: implement rest of tests.
}