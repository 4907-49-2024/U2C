package filters.xmiParser;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Config for filters.xmiParser.XMIParser, has a default configuration.
 * @param xmiInputFile The file name of the XMI file as seen in the input folder (name + extension only, not full path).
 * @param xmiTransformsFile The xmi Transformations configuration file.
 * @param metaModelFile The metamodel configuration file.
 */
public record XMIParserConfig(String xmiInputFile, String xmiTransformsFile, String metaModelFile) {
    // Default file name
    public static final String DEFAULT_INPUT = "behaviourEmbedDiagram.uml";
    public static final String META_MODEL_NAME = "metamodel2.xml";
    public static final String XMI_TRANSFO_NAME = "xmiTrans2_0.xml";
    // Directories
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path resourcesDir = projectRoot.resolve("src/main/resources/sdmetrics/");
    private static final Path inputDir = projectRoot.resolve("Input");
    // Needed, default files
    public static final String DEFAULT_META_MODEL_FILE = resourcesDir.resolve(META_MODEL_NAME).toString(); // UML 2.x metamodel
    public static final String DEFAULT_XMI_TRANSFORMS_FILE = resourcesDir.resolve(XMI_TRANSFO_NAME).toString(); // XMI 2.x xmi transformations

    public XMIParserConfig(String xmiInputFile, String xmiTransformsFile, String metaModelFile){
        this.xmiInputFile = inputDir.resolve(xmiInputFile).toString();
        this.xmiTransformsFile = resourcesDir.resolve(xmiTransformsFile).toString();
        this.metaModelFile = resourcesDir.resolve(metaModelFile).toString();
    }

    public XMIParserConfig(String xmiFileName){
        this(xmiFileName, DEFAULT_XMI_TRANSFORMS_FILE, DEFAULT_META_MODEL_FILE);
    }

    /**
     * Default config with a pre-chosen xmi file, for simplicity during development.
     */
    public XMIParserConfig(){
        this(DEFAULT_INPUT, DEFAULT_XMI_TRANSFORMS_FILE, DEFAULT_META_MODEL_FILE);
    }

    public String metaModel(){
        return metaModelFile;
    }

    public String xmiTrans(){
        return xmiTransformsFile;
    }
}
