package sinks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pipelines.Main;
import pipes.c2ka.Stimulus;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static testUtils.AgentSpecificationDiffTool.specificationDiff;
import static testUtils.TestPaths.MANUFACTURING_CELL;

public class ManufacturingCellTest {
    @BeforeEach
    void clearStimuli(){
        Stimulus.getSystemStimuli().clear();
    }

    @Test
    void testManufacturingCell() throws Exception {
        List<String> inputs = new ArrayList<>();
        inputs.add(MANUFACTURING_CELL+ File.separator + "Control Agent.uml");
        inputs.add(MANUFACTURING_CELL+ File.separator + "Handling Agent.uml");
        inputs.add(MANUFACTURING_CELL+ File.separator + "Processing Agent.uml");
        inputs.add(MANUFACTURING_CELL+ File.separator + "Storage Agent.uml");
        // Parse system - Generalize main later?
        Set<C2KASpecifications> specs = Main.runMainPipeline(inputs);

        for (C2KASpecifications spec : specs) {
            specificationDiff(MANUFACTURING_CELL, spec);
        }
    }
}
