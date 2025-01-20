package unittest.xmiParser;

import org.junit.Test;
import xmiParser.XMIParserConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class XMIParserConfigTest {
    // Want to test that the default implementation is equivalent to passing in the default file
    private static final XMIParserConfig defaultConfig = new XMIParserConfig();
    private static final XMIParserConfig specificConfig = new XMIParserConfig(XMIParserConfig.DEFAULT_FILE);

    @Test
    public void metaModel() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.metaModel(), specificConfig.metaModel());

        // Assert equivalence to expected name
        Path metaModelPath = Paths.get(defaultConfig.metaModel());
        assert metaModelPath.getFileName().toString().equals(XMIParserConfig.META_MODEL_NAME);
    }

    @Test
    public void xmiTrans() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.xmiTrans(), specificConfig.xmiTrans());

        // Assert equivalence to expected name
        Path path = Paths.get(defaultConfig.xmiTrans());
        assert path.getFileName().toString().equals(XMIParserConfig.XMI_TRANSFO_NAME);
    }

    @Test
    public void xmiFileName() {
        // Assert equivalence for default behavior
        assert Objects.equals(defaultConfig.xmiFileName(), specificConfig.xmiFileName());

        // Assert equivalence to expected name
        Path path = Paths.get(defaultConfig.xmiFileName());
        assert path.getFileName().toString().equals(XMIParserConfig.DEFAULT_FILE);
    }
}