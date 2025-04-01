package sinks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pipelines.Main;
import pipes.c2ka.Stimulus;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//import static sinks.SpecificationDiffTool.specificationDiff;
import static testUtils.AgentSpecificationDiffTool.specificationDiff;
import static testUtils.TestPaths.WASTEWATER_SYSTEM;

public class WastewaterSystemTest {
    //private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    //private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/KnownC2KASystems/WastewaterSystem");

    @BeforeEach
    void clearStimuli(){
        Stimulus.getSystemStimuli().clear();
    }

    @Test
    void testWastewaterSystem() throws Exception {
        List<String> inputs = new ArrayList<>();

        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Sample Pump.uml");//FIXME: Partially Sequential Error
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "SO3 Analyzer.uml");
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Sample Flow Meter.uml");//FIXME: Partially Sequential Error
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Programmable Logic Controller.uml");
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Lead Chemical Feed Pump1.uml");
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Lead Chemical Feed Pump2.uml");
        inputs.add(WASTEWATER_SYSTEM+ File.separator + "Operator.uml");
        // Parse system - Generalize main later?
        Set<C2KASpecifications> specs = Main.runMainPipeline(inputs);

        for (C2KASpecifications spec : specs) {
            // TODO: name specification in assertion
            specificationDiff(WASTEWATER_SYSTEM, spec);
        }
    }
}

