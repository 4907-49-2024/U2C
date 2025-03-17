package sinks;

import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.StateAbstractBehaviorInterpreter;
import filters.diagramInterpreters.StateConcreteBehaviorInterpreter;
import filters.diagramInterpreters.StateNextBehaviorInterpreter;
import filters.diagramInterpreters.StateNextStimInterpreter;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.SuperState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ManufacturingCellTest {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/KnownC2KASystems/ManufacturingCell");

    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return The data sink (Tested output!)
     * @throws Exception In case of thread or input exceptions
     */
    private C2KASpecifications runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(TEST_DIR, inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        UMLModel model = parser.getOutput();
        // Filter 2
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3s
        StateAbstractBehaviorInterpreter abstractB = new StateAbstractBehaviorInterpreter(stateDiagram);
        StateNextBehaviorInterpreter nextB = new StateNextBehaviorInterpreter(stateDiagram);
        StateNextStimInterpreter nextS = new StateNextStimInterpreter(stateDiagram);
        StateConcreteBehaviorInterpreter concreteB = new StateConcreteBehaviorInterpreter(stateDiagram);
        // Sink - Test Output!
        return new C2KASpecifications(stateDiagram.name(), abstractB.getOutput(),
                nextB.getOutput(), nextS.getOutput(), concreteB.getOutput());
    }

    @Test
    void testControlAgent() throws Exception {
        C2KASpecifications specifications = runTestPipeline("Control Agent.uml");
        specifications.outputToFile(); // TODO: Augment this test with a diff tool/mechanism
    }
    @Test
    void testHandlingAgent() throws Exception {
        C2KASpecifications specifications = runTestPipeline("Handling Agent.uml");
        specifications.outputToFile(); // TODO: Augment this test with a diff tool/mechanism
    }
    @Test
    void testProcessingAgent() throws Exception {
        C2KASpecifications specifications = runTestPipeline("Processing Agent.uml");
        specifications.outputToFile(); // TODO: Augment this test with a diff tool/mechanism
    }
    @Test
    void testStorageAgent() throws Exception {
        C2KASpecifications specifications = runTestPipeline("Storage Agent.uml");
        specifications.outputToFile(); // TODO: Augment this test with a diff tool/mechanism
    }
}
