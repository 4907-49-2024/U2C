package xmiParser;

import xmiParser.UMLMappings.PapyrusMapping;
import xmiParser.UMLMappings.UMLMapping;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Config for xmiParser.XMIParser, has a default configuration.
 * @param xmiFileName The file name of the XMI file as seen in the input folder (name + extension only, not full path).
 */
public record XMIParserConfig(String xmiFileName, UMLMapping umlMapping) {
    // Default mapping (using papyrus in initial implementation)
    public static final UMLMapping DEFAULT_MAPPING = new PapyrusMapping();
    // Default file name
    public static final String DEFAULT_FILE = "Simplest.uml";
    public static final String META_MODEL_NAME = "metamodel2.xml";
    public static final String XMI_TRANSFO_NAME = "xmiTrans2_0.xml";
    // Directories
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path resourcesDir = projectRoot.resolve("src/main/resources/sdmetrics/");
    private static final Path inputDir = projectRoot.resolve("Input");
    // Needed, default files
    public static final String metaModelURL = resourcesDir.resolve(META_MODEL_NAME).toString(); // UML 2.x metamodel
    public static final String xmiTransURL = resourcesDir.resolve(XMI_TRANSFO_NAME).toString(); // XMI 2.x xmi transformations


    public XMIParserConfig(String xmiFileName, UMLMapping umlMapping){
        this.xmiFileName = inputDir.resolve(xmiFileName).toString();
        this.umlMapping = umlMapping;
    }

    public XMIParserConfig(String xmiFileName){
        this(xmiFileName, DEFAULT_MAPPING);
    }

    /**
     * Default config with a pre-chosen xmi file, for simplicity during development.
     */
    public XMIParserConfig(){
        // HARDCODED TO "default.xmi"
        // Change if desired while testing (but don't commit!)
        this(DEFAULT_FILE, DEFAULT_MAPPING);
    }

    public String metaModel(){
        return metaModelURL;
    }

    public String xmiTrans(){
        return xmiTransURL;
    }
}
