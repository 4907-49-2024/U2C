package filters.xmiParser;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

class XMIParserConfigTest {
    // Want to test that the default implementation is equivalent to passing in the default file
    private static final XMIParserConfig defaultConfig = new XMIParserConfig();
    private static final XMIParserConfig inputConfig = new XMIParserConfig(XMIParserConfig.DEFAULT_INPUT);
    private static final XMIParserConfig fullConfig = new XMIParserConfig(XMIParserConfig.DEFAULT_INPUT, XMIParserConfig.XMI_TRANSFO_NAME, XMIParserConfig.META_MODEL_NAME);

    @Test
    void testMetaModel() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.metaModel(), inputConfig.metaModel());
        assert Objects.equals(defaultConfig.metaModel(), fullConfig.metaModel());

        // Assert equivalence to expected name
        Path metaModelPath = Paths.get(defaultConfig.metaModel());
        assert metaModelPath.getFileName().toString().equals(XMIParserConfig.META_MODEL_NAME);
    }

    @Test
    void testXmiTrans() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.xmiTrans(), inputConfig.xmiTrans());
        assert Objects.equals(defaultConfig.xmiTrans(), fullConfig.xmiTrans());

        // Assert equivalence to expected name
        Path path = Paths.get(defaultConfig.xmiTrans());
        assert path.getFileName().toString().equals(XMIParserConfig.XMI_TRANSFO_NAME);
    }

    @Test
    void testXmiFileName() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.xmiInputFile(), inputConfig.xmiInputFile());
        assert Objects.equals(defaultConfig.xmiInputFile(), fullConfig.xmiInputFile());

        // Assert equivalence to expected name
        Path path = Paths.get(defaultConfig.xmiInputFile());
        assert path.getFileName().toString().equals(XMIParserConfig.DEFAULT_INPUT);
    }
}