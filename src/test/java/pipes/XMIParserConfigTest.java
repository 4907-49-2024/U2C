package pipes;

import org.junit.jupiter.api.Test;
import pipes.parserConfig.XMIParserConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class XMIParserConfigTest {
    private final String testInputFile = "Atomic.uml";
    private final String testTransform = "custom/xmiStateTrans.xml";
    private final String testMetaModel = "custom/stateMetaModel.xml";

    @Test
    void testConfigFromDefaultInputDir() {
        XMIParserConfig config = new XMIParserConfig(testInputFile, testTransform, testMetaModel);

        Path expectedInputPath = Paths.get(System.getProperty("user.dir")).resolve("Input").resolve(testInputFile);
        Path expectedTransformPath = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/sdmetrics").resolve(testTransform);
        Path expectedMetaModelPath = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/sdmetrics").resolve(testMetaModel);

        assertEquals(expectedInputPath.toString(), config.xmiInputFile());
        assertEquals(expectedTransformPath.toString(), config.xmiTrans());
        assertEquals(expectedMetaModelPath.toString(), config.metaModel());
    }

    @Test
    void testConfigFromCustomInputDir() {
        Path customInput = Paths.get("src/test/resources/testInputs");
        XMIParserConfig config = new XMIParserConfig(customInput, testInputFile, testTransform, testMetaModel);

        Path expectedInputPath = customInput.resolve(testInputFile);
        Path expectedTransformPath = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/sdmetrics").resolve(testTransform);
        Path expectedMetaModelPath = Paths.get(System.getProperty("user.dir")).resolve("src/main/resources/sdmetrics").resolve(testMetaModel);

        assertEquals(expectedInputPath.toString(), config.xmiInputFile());
        assertEquals(expectedTransformPath.toString(), config.xmiTrans());
        assertEquals(expectedMetaModelPath.toString(), config.metaModel());
    }
}
