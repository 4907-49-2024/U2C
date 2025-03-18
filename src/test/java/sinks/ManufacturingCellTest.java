package sinks;

import org.junit.jupiter.api.Test;
import pipelines.Main;

import java.util.*;

import static sinks.SpecificationDiffTool.specificationDiff;

public class ManufacturingCellTest {
    @Test
    void testManufacturingCell() throws Exception {
        List<String> inputs = new ArrayList<>();
        inputs.add("Control Agent.uml");
        inputs.add("Handling Agent.uml");
        inputs.add("Processing Agent.uml");
        inputs.add("Storage Agent.uml");
        // Parse system - Generalize main later?
        Set<C2KASpecifications> specs = Main.runMainPipeline(inputs);

        for (C2KASpecifications spec : specs) {
            // TODO: name specification in assertion
            specificationDiff(spec);
        }
    }
}
