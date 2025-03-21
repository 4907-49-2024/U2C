package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.parserConfig.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.c2ka.behaviors.Behavior;
import pipes.c2ka.behaviors.CompositeBehavior;
import pipes.diagrams.state.SuperState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateAbstractBehaviorInterpreterTest {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/C2KA-BaseRepresentations");

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
        XMIParserConfig config = new XMIParserConfig(TEST_DIR, inputDiagramXMI, xmiTrans, metaModel);
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
        Behavior behavior = runTestPipeline("Atomic.uml");

        assert behavior.toString().equals("( <name> )");
    }

    // TODO: implement rest of tests... look at the behavior tests for reference on how to handle indeterministic output!
    //      There are tricks you need to do because after two behaviors in a choice the order is randomized.
}