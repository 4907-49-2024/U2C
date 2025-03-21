package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.parserConfig.XMIParserConfig;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.diagrams.state.SuperState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateConcreteBehaviorInterpreterTest {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/C2KA-BaseRepresentations");

    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private Set<AtomicBehavior> runTestPipeline(String inputDiagramXMI) throws Exception {
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
        StateConcreteBehaviorInterpreter interpreter = new StateConcreteBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        Set<AtomicBehavior> behavior = runTestPipeline("Atomic.uml");

        for (AtomicBehavior b: behavior) {
            // Need to do check a switch every time because of the indeterministic nature of sets.
            switch (b.toString()){
                case "<name>" -> {assert b.getConcreteBehavior().equals("<name> => [ <behavior-expression> ]");}
                default -> {assert false;} // Should not have any extra cases
            }
        }
    }

    // TODO: implement rest of tests... Add cases to switch like above, the final string is deterministic,
    //  but you don't know the test sequence order!
}