package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.parserConfig.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.semirings.NextStimulusMap;
import pipes.c2ka.specifications.NextStimulusSpecification;
import pipes.diagrams.state.SuperState;

import java.util.HashSet;
import java.util.Set;

import static testUtils.TestPaths.BASE_C2KA;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateNextStimInterpreterTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private NextStimulusSpecification runTestPipeline(String inputDiagramXMI) throws Exception {
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
        StateNextStimInterpreter interpreter = new StateNextStimInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    // Atomic should have no transitions, so no next stims
    @Test
    void testAtomic() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("Atomic.uml");

        NextStimulusSpecification expectedSpec = new NextStimulusSpecification(new HashSet<>());
        assert mappings.equals(expectedSpec);
    }

    // Next stim mapping has an explicit example
    @Test
    void testNextMappings() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("NextMappings.uml");

        Set<NextStimulusMap> expected = new HashSet<>();
        expected.add(new NextStimulusMap("inStim", new AtomicBehaviour("Current", "<behavior-expression>"), "nextStim"));

        NextStimulusSpecification expectedSpec = new NextStimulusSpecification(expected);

        assert mappings.equals(expectedSpec);
    }

    @Test
    void testChoice() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("Choice.uml");
        Set<NextStimulusMap> behaviorMaps = new HashSet<>();

        NextStimulusSpecification expected = new NextStimulusSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testAtomicAssignment() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("Atomic-Assignment.uml");
        Set<NextStimulusMap> behaviorMaps = new HashSet<>();

        NextStimulusSpecification expected = new NextStimulusSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testAtomicConditional() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("Atomic-Conditional.uml");
        Set<NextStimulusMap> behaviorMaps = new HashSet<>();

        NextStimulusSpecification expected = new NextStimulusSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testParallel() throws Exception {
        // Get output
        NextStimulusSpecification mappings = runTestPipeline("Parallel.uml");
        Set<NextStimulusMap> behaviorMaps = new HashSet<>();

        NextStimulusSpecification expected = new NextStimulusSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testSequential() throws Exception {
        NextStimulusSpecification spec = runTestPipeline("Sequential.uml");
        Set<NextStimulusMap> behaviorMaps = new HashSet<>();

        NextStimulusSpecification expected = new NextStimulusSpecification(behaviorMaps);
        behaviorMaps.add(new NextStimulusMap("stimulus (a out, b in)", new AtomicBehaviour("a",
                "<behavior-expression>"), "stimulus (a out, b in)"));

        assert spec.equals(expected);
    }
}
