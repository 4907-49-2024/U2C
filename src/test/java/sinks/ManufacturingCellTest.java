package sinks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pipelines.Main;
import pipes.c2ka.Stimulus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static sinks.SpecificationDiffTool.specificationDiff;

public class ManufacturingCellTest {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/KnownC2KASystems/ManufacturingCell");

    @BeforeEach
    void clearStimuli(){
        Stimulus.getSystemStimuli().clear();
    }

    @Test
    void testManufacturingCell() throws Exception {
        List<String> inputs = new ArrayList<>();
        inputs.add(TEST_DIR+"\\Control Agent.uml");
        inputs.add(TEST_DIR+"\\Handling Agent.uml");
        inputs.add(TEST_DIR+"\\Processing Agent.uml");
        inputs.add(TEST_DIR+"\\Storage Agent.uml");
        // Parse system - Generalize main later?
        Set<C2KASpecifications> specs = Main.runMainPipeline(inputs);

        for (C2KASpecifications spec : specs) {
            // TODO: name specification in assertion
            specificationDiff(spec);
        }
    }
}
