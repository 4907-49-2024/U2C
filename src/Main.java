// Required imports, according to docs.
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.util.XMLParser;

public class Main {
    // Call the function under prototype
    public static void main(String[] args) {
        // Tutorial: https://www.sdmetrics.com/javadoc/com/sdmetrics/model/package-summary.html

        // Path convenience variable
        final Path resRoot = Paths.get("C:\\Users\\Alex\\IdeaProjects\\LibraryExploration\\lib\\SDMetricsOpenCore\\src\\com\\sdmetrics\\resources");

        String metaModelURL = resRoot.resolve("metamodel2.xml").toString(); // Use UML 2.x as the metamodel
        String xmiTransURL = ...;   // XMI tranformations to use
        String xmiFile = ...;       // XMI file with the UML model



    }

    // ======================== Working methods ========================
    private static void hello(){
        System.out.println("Hello world!");
    }
}