package sinks;

import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.*;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.SuperState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class C2KASpecificationsTest {

    private static final String INPUT_DIR = "C2KA-CompleteExample/ManufacturingCell/";
    private static final String EXPECTED_OUTPUT_DIR = "Output/C2KA-CompleteExample/ManufacturingCell/Output/Expected/";
    private static final String ACTUAL_OUTPUT_DIR = "Output/C2KA-CompleteExample/ManufacturingCell/Output/Actual/";

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
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
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

    private void specificationDiff(String umlFileName, String expectedFileName) throws Exception {
        String inputFilePath = INPUT_DIR + umlFileName;
        String expectedFilePath = EXPECTED_OUTPUT_DIR + expectedFileName;
        String actualFilePath = ACTUAL_OUTPUT_DIR + expectedFileName;

        // Run pipeline
        C2KASpecifications specs = runTestPipeline(inputFilePath);

        // Output to actual file
        Path actualPath = Paths.get(actualFilePath);
        Files.createDirectories(actualPath.getParent());
        Files.writeString(actualPath, specs.toString());

        // Load expected file content
        Path expectedPath = Paths.get(expectedFilePath);
        String expectedContent = Files.readString(expectedPath);

        // Compare files
        assertEquals(expectedContent.trim(), specs.toString().trim(),
                "Differences found between expected and actual outputs for " + umlFileName);
    }

    //Tests for ManufacturingCell Example
    // System Test for Control Agent
    @Test
    void testControlAgent() throws Exception {
        specificationDiff("Control Agent.uml", "C.txt");
    }

    // System Test for Handling Agent
    @Test
    void testHandlingAgent() throws Exception {
        specificationDiff("Handling Agent.uml", "H.txt");
    }

    // System Test for Processing Agent
    @Test
    void testProcessingAgent() throws Exception {
        specificationDiff("Processing Agent.uml", "P.txt");
    }

    // System Test for Storage Agent
    @Test
    void testStorageAgent() throws Exception {
        specificationDiff("Storage Agent.uml", "S.txt");
    }

    // Tests for C2KA-BaseRepresentations
    @Test
    void testAtomic() throws Exception {
        // Get output
        C2KASpecifications specs = runTestPipeline("C2KA-BaseRepresentations/Atomic.uml");
        String expectedFormat = "begin AGENT where\n" +
                "\n" +
                "\tAtomic Behavior :=  <name> \n" +
                "\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin NEXT_BEHAVIOR  where\n" +
                "\n" +
                "\t\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin NEXT_STIMULUS  where\n" +
                "\n" +
                "\t\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin CONCRETE_BEHAVIOR  where\n" +
                "\n" +
                "\t<name> => [ <behavior-expression> ]\n" +
                "\n" +
                "end";

        assert specs.toString().equals(expectedFormat);
    }
    //TODO: tests for other C2KA-BaseRepresentations
}