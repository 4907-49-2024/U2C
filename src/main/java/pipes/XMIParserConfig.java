package pipes;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Config for filters.xmiParser.XMIParser, has a default configuration.
 */
public class XMIParserConfig {
    // Default file name
    public static final String DEFAULT_INPUT = "behaviourEmbedDiagram.uml";
    public static final String META_MODEL_NAME = "metamodel2.xml";
    public static final String XMI_TRANSFO_NAME = "xmiTrans2_0.xml";
    // Directories
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path DEFAULT_INPUT_DIR = projectRoot.resolve("Input");
    private static final Path resourcesDir = projectRoot.resolve("src/main/resources/sdmetrics/");
    // Needed, default files
    public static final String DEFAULT_META_MODEL_FILE = resourcesDir.resolve(META_MODEL_NAME).toString(); // UML 2.x metamodel
    public static final String DEFAULT_XMI_TRANSFORMS_FILE = resourcesDir.resolve(XMI_TRANSFO_NAME).toString(); // XMI 2.x xmi transformations

    // Config fields
    private final String xmiInputFile;
    private final String xmiTransformsFile;
    private final String metaModelFile;

    /**
     * @param inputDir The input directory of the xmi file
     * @param xmiInputFile The file name of the XMI file as seen in the input folder (name + extension only, not full path).
     * @param xmiTransformsFile The xmi Transformations configuration file.
     * @param metaModelFile The metamodel configuration file.
     */
    public XMIParserConfig(Path inputDir, String xmiInputFile, String xmiTransformsFile, String metaModelFile){
        this.xmiInputFile = inputDir.resolve(xmiInputFile).toString();
        this.xmiTransformsFile = resourcesDir.resolve(xmiTransformsFile).toString();
        this.metaModelFile = resourcesDir.resolve(metaModelFile).toString();
    }

    public XMIParserConfig(Path inputDir, String xmiFileName){
        this(inputDir, xmiFileName, DEFAULT_XMI_TRANSFORMS_FILE, DEFAULT_META_MODEL_FILE);
    }

    public XMIParserConfig(String xmiFileName){
        this(DEFAULT_INPUT_DIR, xmiFileName, DEFAULT_XMI_TRANSFORMS_FILE, DEFAULT_META_MODEL_FILE);
    }

    public String metaModel(){
        return metaModelFile;
    }

    public String xmiTrans(){
        return xmiTransformsFile;
    }

    public String xmiInputFile(){
        return xmiInputFile;
    }
}
