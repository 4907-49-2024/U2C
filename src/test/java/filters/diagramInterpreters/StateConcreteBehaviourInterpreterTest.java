package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.c2ka.behaviours.ChoiceBehaviour;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.c2ka.specifications.NextStimulusSpecification;
import pipes.diagrams.state.AtomicState;
import pipes.diagrams.state.State;
import pipes.parserConfig.XMIParserConfig;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.diagrams.state.SuperState;

import java.util.HashSet;
import java.util.Set;

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

    @Test
    void testAtomicConditional() throws Exception {
        ConcreteBehaviorSpecification spec = runTestPipeline("Atomic-Conditional.uml");

        String activity = "[if (material = 1 && state > 2 && status < 0) -> ready:=0 | " +
                "(material >= 1 && state <= 2 || status = 0) -> ready:=1; do (material = 1 && state = 3) -> ready:= 3 " +
                "od | ~ ((material = 1 && state > 2 && status < 0) || (material >= 1 && state <= 2 || status = 0)) " +
                "-> ready:=0 fi]";

        ConcreteBehaviorSpecification specExpect = new ConcreteBehaviorSpecification();
        specExpect.add(new AtomicBehaviour("<name>", activity.strip()));

        assert spec.equals(specExpect);
    }


    @Test
    void testAtomicAssignment() throws Exception {
        ConcreteBehaviorSpecification spec = runTestPipeline("Atomic-Assignment.uml");
        ConcreteBehaviorSpecification expected = new ConcreteBehaviorSpecification();
        expected.add(new AtomicBehaviour("<name>", "ready:=1"));
        assert spec.equals(expected);
    }

    @Test
    void testChoice() throws Exception {
        ConcreteBehaviorSpecification spec = runTestPipeline("Choice.uml");

        ConcreteBehaviorSpecification specExpect = new ConcreteBehaviorSpecification();
        specExpect.add(new AtomicBehaviour("a", "<behavior-expression>"));
        specExpect.add(new AtomicBehaviour("b", "<behavior-expression>"));

        assert spec.equals(specExpect);
    }

    @Test
    void testParallel() throws Exception {
        ConcreteBehaviorSpecification mappings = runTestPipeline("Parallel.uml");

        ConcreteBehaviorSpecification specExpect = new ConcreteBehaviorSpecification();
        specExpect.add(new AtomicBehaviour("a", "<behavior-expression>"));
        specExpect.add(new AtomicBehaviour("b", "<behavior-expression>"));

        assert mappings.equals(specExpect);
    }

    @Test
    void testSequential() throws Exception {
        ConcreteBehaviorSpecification spec = runTestPipeline("Sequential.uml");

        ConcreteBehaviorSpecification specExpect = new ConcreteBehaviorSpecification();
        specExpect.add(new AtomicBehaviour("", ""));
        specExpect.add(new AtomicBehaviour("a", "<behavior-expression>"));
        specExpect.add(new AtomicBehaviour("b", "<behavior-expression>"));

        assert spec.equals(specExpect);
    }

}
