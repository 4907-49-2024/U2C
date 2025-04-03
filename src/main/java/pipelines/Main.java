package pipelines;

import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.StateAbstractBehaviorInterpreter;
import filters.diagramInterpreters.StateConcreteBehaviorInterpreter;
import filters.diagramInterpreters.StateNextBehaviorInterpreter;
import filters.diagramInterpreters.StateNextStimInterpreter;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import pipes.parserConfig.XMIParserConfig;
import pipes.diagrams.state.SuperState;
import sinks.C2KASpecifications;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main executable flow of the program.
 * <br>
 * Reads all files in the input directory, and produces output files for each agent provided.
 * Precondition: Every file provided in input contains a single state diagram, representing the agent desired.
 */
public class Main {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path INPUT_DIR = projectRoot.resolve("Input");
    private static final Path OUTPUT_DIR = projectRoot.resolve("Output");

    private static List<String> getInputs(){
        // Get all input files.
        List<String> input_files = new ArrayList<>();

        //If this pathname does not denote a directory, then listFiles() returns null.
        File[] files = INPUT_DIR.toFile().listFiles();

        if (files == null) {
            throw new RuntimeException("Input directory has no files");
        }

        for (File file : files) {
            // Only accept files with .uml extension
            // FIXME: This assumes all .uml files are state diagrams, no way to distinguish other diagrams yet.
            if (file.isFile() && (file.getName().endsWith(".uml") || file.getName().endsWith(".xmi"))) {
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
    private static C2KASpecifications runSpecificationsPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        ModelElement stateDiagramElem = parser.getOutput();
        // Filter 2
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3s
        StateAbstractBehaviorInterpreter abstractB = new StateAbstractBehaviorInterpreter(stateDiagram);
        StateNextBehaviorInterpreter nextB = new StateNextBehaviorInterpreter(stateDiagram);
        StateNextStimInterpreter nextS = new StateNextStimInterpreter(stateDiagram);
        StateConcreteBehaviorInterpreter concreteB = new StateConcreteBehaviorInterpreter(stateDiagram);
        // Sink - Build then output to file!
        return new C2KASpecifications(abstractB.getOutput(),
                nextB.getOutput(), nextS.getOutput(), concreteB.getOutput());
    }

    // TODO: document/rename? -> report has a good detailed explanation on this.
    public static Set<C2KASpecifications> runMainPipeline(List<String> inputDiagrams) throws Exception {
        Set<C2KASpecifications> specifications = new HashSet<>();

        // Produce one specification per input file.
        for (String input : inputDiagrams) {
            specifications.add(runSpecificationsPipeline(input));
        }

        // Output specifications (needs to be done once full system is analyzed)
        for (C2KASpecifications spec : specifications) {
            spec.fillMappingSpecs();
            spec.outputToFile();
        }

        return specifications;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Reading agent files from " + INPUT_DIR);
        runMainPipeline(getInputs());
        System.out.println("Finished writing output to " + OUTPUT_DIR);
    }
}
