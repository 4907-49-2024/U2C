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

    // Default input dir
    public XMIParserConfig(String xmiInputFile, String xmiTransformsFile, String metaModelFile){
        this.xmiInputFile = DEFAULT_INPUT_DIR.resolve(xmiInputFile).toString();
        this.xmiTransformsFile = resourcesDir.resolve(xmiTransformsFile).toString();
        this.metaModelFile = resourcesDir.resolve(metaModelFile).toString();
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
