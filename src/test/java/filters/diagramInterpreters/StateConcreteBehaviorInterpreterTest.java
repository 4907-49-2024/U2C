package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.c2ka.primitives.AtomicBehavior;
import pipes.diagrams.state.SuperState;

import java.util.List;
import java.util.Set;

/**
 * Test the StateAbstractBehaviorInterpreterTest filter
 */
public class StateConcreteBehaviorInterpreterTest {
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
        StateConcreteBehaviorInterpreter interpreter = new StateConcreteBehaviorInterpreter(stateDiagram);
        return interpreter.getOutput();
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        Set<AtomicBehavior> behavior = runTestPipeline("C2KA-BaseRepresentations/Atomic.uml");

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