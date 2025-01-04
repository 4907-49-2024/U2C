import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Config for XMIParser, has a default configuration.
 * @param xmiFileName The file name of the XMI file as seen in the input folder (name + extension only, not full path).
 */
public record XMIParserConfig(String xmiFileName) {
    // Directories
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path resourcesDir = projectRoot.resolve("lib/SDMetricsOpenCore/src/com/sdmetrics/resources");
    private static final Path inputDir = projectRoot.resolve("Input");
    // Needed, default files
    public static final String metaModelURL = resourcesDir.resolve("metamodel2.xml").toString(); // UML 2.x metamodel
    public static final String xmiTransURL = resourcesDir.resolve("xmiTrans2_0.xml").toString(); // XMI 2.x xmi transformations


    public XMIParserConfig(String xmiFileName){
        this.xmiFileName = inputDir.resolve(xmiFileName).toString();
    }

    /**
     * Default config with a pre-chosen xmi file, for simplicity during development.
     */
    public XMIParserConfig(){
        // HARDCODED TO "default.xmi"
        // Change if desired while testing (but don't commit!)
        this("default.xmi");
    }

    public String metaModel(){
        return metaModelURL;
    }

    public String xmiTrans(){
        return xmiTransURL;
    }
}
