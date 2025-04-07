package filters.diagramInterpreters;

import com.sdmetrics.model.ModelElement;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.behaviours.ChoiceBehaviour;
import pipes.c2ka.specifications.AbstractBehaviorSpecification;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.parserConfig.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.c2ka.behaviours.CompositeBehaviour;
import pipes.diagrams.state.SuperState;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    void testAtomicConditional() throws Exception {
        AbstractBehaviorSpecification spec = runTestPipeline("Atomic-Conditional.uml");

        String activity = "[if (material = 1 && state > 2 && status < 0) -> ready:=0 | " +
                "(material >= 1 && state <= 2 || status = 0) -> ready:=1; do (material = 1 && state = 3) -> ready:= 3 " +
                "od | ~ ((material = 1 && state > 2 && status < 0) || (material >= 1 && state <= 2 || status = 0)) " +
                "-> ready:=0 fi]";

        CompositeBehaviour choice = new ChoiceBehaviour();
        choice.addBehavior(new AtomicBehaviour("<name>", activity.strip()));
        AbstractBehaviorSpecification expected = new AbstractBehaviorSpecification("Atomic Conditional", choice);

        assert spec.equals(expected);
    }


    @Test
    void testAtomicAssignment() throws Exception {
        AbstractBehaviorSpecification spec = runTestPipeline("Atomic-Assignment.uml");

        CompositeBehaviour choice = new ChoiceBehaviour();
        choice.addBehavior(new AtomicBehaviour("<name>", "<behavior-expression>"));
        AbstractBehaviorSpecification expected = new AbstractBehaviorSpecification("Atomic Assignment", choice);

        assert spec.equals(expected);
    }

    @Test
    void testChoice() throws Exception {
        AbstractBehaviorSpecification spec = runTestPipeline("Choice.uml");

        CompositeBehaviour choice = new ChoiceBehaviour();
        choice.addBehavior(new AtomicBehaviour("a", "<behavior-expression>"));
        choice.addBehavior(new AtomicBehaviour("b", "<behavior-expression>"));

        AbstractBehaviorSpecification expected = new AbstractBehaviorSpecification("Behaviour Choice", choice);
        // Extract string representation
        String Expected = expected.toString();
        String actual = spec.toString(); // remove parens and whitespace

        // Normalize both sides (sort operands)
        Set<String> actualParts = new HashSet<>(Arrays.asList(actual.split(":=")[0].split("\\+")));
        Set<String> expectedParts = new HashSet<>(Arrays.asList(Expected.split(":=")[0].split("\\+")));

        assert actualParts.equals(expectedParts) :
                "Expected behaviors " + expectedParts + " but found " + actualParts;
    }

    @Test
    void testParallel() throws Exception {
        AbstractBehaviorSpecification spec = runTestPipeline("Parallel.uml");

        CompositeBehaviour parallel = new pipes.c2ka.behaviours.ParallelBehaviour();

        parallel.addBehavior(new AtomicBehaviour("a", "<behavior-expression>"));
        parallel.addBehavior(new AtomicBehaviour("b", "<behavior-expression>"));


        AbstractBehaviorSpecification expected = new AbstractBehaviorSpecification("Parallel Composition", parallel);

        String expectedStr = expected.toString();
        String actualStr = spec.toString();
        assert actualStr.contains("a || b") || actualStr.contains("b || a");
        assert expectedStr.contains("a || b") || expectedStr.contains("b || a");
    }

    @Test
    void testSequential() throws Exception {
        AbstractBehaviorSpecification spec = runTestPipeline("Sequential.uml");

        CompositeBehaviour sequential = new pipes.c2ka.behaviours.SequentialBehaviour();
        sequential.addBehavior(new AtomicBehaviour("", ""));
        sequential.addBehavior(new AtomicBehaviour("a", "<behavior-expression>"));
        sequential.addBehavior(new AtomicBehaviour("b", "<behavior-expression>"));

        AbstractBehaviorSpecification expected = new AbstractBehaviorSpecification("Sequential Composition", sequential);

        String expectedStr = expected.toString();
        String actualStr = spec.toString();
        assert actualStr.contains("; a ; b") || actualStr.contains("; b ; a");
        assert expectedStr.contains("; b ; a") || expectedStr.contains("; a ; b");
    }

}
