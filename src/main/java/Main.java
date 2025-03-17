import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.StateAbstractBehaviorInterpreter;
import filters.diagramInterpreters.StateConcreteBehaviorInterpreter;
import filters.diagramInterpreters.StateNextBehaviorInterpreter;
import filters.diagramInterpreters.StateNextStimInterpreter;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.SuperState;
import sinks.C2KASpecifications;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Main executable flow of the program.
 * <br>
 * Reads all files in the input directory, and produces output files for each agent provided.
 * Precondition: Every file provided in input contains a single state diagram, representing the agent desired.
 */
public class Main {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path INPUT_DIR = projectRoot.resolve("Input");

    private static List<String> getInputs(){
        // Get all input files.
        List<String> input_files = new ArrayList<>();

        //If this pathname does not denote a directory, then listFiles() returns null.
        File[] files = INPUT_DIR.toFile().listFiles();

        if (files == null) {
            throw new RuntimeException("Input directory has no files");
        }

        for (File file : files) {
            if (file.isFile()) {
                input_files.add(file.getName());
            }
        }
        // Remove .gitkeep file from inputs
        input_files.remove(".gitkeep");

        return input_files;
    }

    /**
     * Pipeline producing the specifications data sink. Writes to output directly at the end.
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @throws Exception In case of thread or input exceptions
     */
    private static void runSpecificationsPipeline(String inputDiagramXMI) throws Exception {
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
        // Sink - Build then output to file!
        C2KASpecifications specs = new C2KASpecifications(stateDiagram.name(), abstractB.getOutput(),
                nextB.getOutput(), nextS.getOutput(), concreteB.getOutput());
        specs.outputToFile();
    }

    public static void main(String[] args) throws Exception {
        // Produce one output per input file.
        for (String input : getInputs()) {
            runSpecificationsPipeline(input);
        }
    }
}
