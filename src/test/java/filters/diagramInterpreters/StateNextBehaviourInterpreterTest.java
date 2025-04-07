package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.c2ka.semirings.NextStimulusMap;
import pipes.c2ka.specifications.NextStimulusSpecification;
import pipes.parserConfig.XMIParserConfig;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.diagrams.state.SuperState;

import java.util.HashSet;
import java.util.Set;

import static testUtils.TestPaths.BASE_C2KA;

/**
 * Test the StateNextBehaviorInterpreter filter
 */
public class StateNextBehaviourInterpreterTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return Behavior type pipe object (filter under test's output)
     * @throws Exception In case of thread or input exceptions
     */
    private NextBehaviorSpecification runTestPipeline(String inputDiagramXMI) throws Exception {
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
        StateNextBehaviorInterpreter interpreter = new StateNextBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    // Atomic should have no transitions, so no next stims
    @Test
    void testAtomic() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("Atomic.uml");

        NextBehaviorSpecification expected = new NextBehaviorSpecification(new HashSet<>());
        assert expected.equals(mappings);
    }

    // Next stim mapping has an explicit example
    @Test
    void testNextMappings() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("NextMappings.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        AtomicBehaviour initial = new AtomicBehaviour("Current", "<behavior-expression>");
        AtomicBehaviour next = new AtomicBehaviour("NextBehavior", "<behavior-expression>");
        behaviorMaps.add(new NextBehaviorMap("inStim", initial,  next));

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testChoice() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("Choice.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testAtomicAssignment() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("Atomic-Assignment.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testAtomicConditional() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("Atomic-Conditional.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testParallel() throws Exception {
        // Get output
        NextBehaviorSpecification mappings = runTestPipeline("Parallel.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        assert expected.equals(mappings);
    }

    @Test
    void testSequential() throws Exception {
        NextBehaviorSpecification spec = runTestPipeline("Sequential.uml");
        Set<NextBehaviorMap> behaviorMaps = new HashSet<>();

        NextBehaviorSpecification expected = new NextBehaviorSpecification(behaviorMaps);
        AtomicBehaviour initial = new AtomicBehaviour("a", "<behavior-expression>");
        AtomicBehaviour next = new AtomicBehaviour("b", "<behavior-expression>");
        behaviorMaps.add(new NextBehaviorMap("stimulus (a out, b in)", initial,  next));

        assert spec.equals(expected);
    }
}
