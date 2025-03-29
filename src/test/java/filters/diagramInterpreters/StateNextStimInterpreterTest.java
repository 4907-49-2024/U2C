package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.semirings.NextStimulusMap;
import pipes.c2ka.specifications.NextStimulusSpecification;
import pipes.diagrams.state.SuperState;

import java.util.HashSet;
import java.util.List;
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
        UMLModel model = parser.getOutput();
        // Filter 2
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
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
        expected.add(new NextStimulusMap("inStim", new AtomicBehavior("Current", "<behavior-expression>"), "nextStim"));

        NextStimulusSpecification expectedSpec = new NextStimulusSpecification(expected);
        assert mappings.equals(expectedSpec);
    }

    // Sequential also has a special transition example
    // FIXME: Ignored sequential mappings for now, still unsure how they work
//    @Test
//    void testSequential() throws Exception {
//        // Get output
//        Set<NextStimulusMap> mappings = runTestPipeline("C2KA-BaseRepresentations/Sequential.uml");
//        Set<NextStimulusMap> expected = new HashSet<>();
        // We're missing info, will need to update diagram at some point. I think I understand sequential better now
        // Nevermind I'm still lost somewhat. That's okay though.
//        expected.add(new NextStimulusMap(new AtomicBehavior("a", "<behavior-expression>"), "stimulus (a out, b in)", "stimulus (a out, b in)"));

//        assert expected.equals(mappings);
//    }
}
