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

    public static void main(String[] args) throws Exception {
        // Define relative paths based on the directory structure
        Path projectRoot = Paths.get(System.getProperty("user.dir")); // Root directory of the project

        // Paths for resource files
        Path resourcesDir = projectRoot.resolve("lib/SDMetricsOpenCore/src/com/sdmetrics/resources");
        String metaModelURL = resourcesDir.resolve("metamodel2.xml").toString(); // Metamodel file
        String xmiTransURL = resourcesDir.resolve("xmiTrans2_0.xml").toString(); // XMI transformations file

        // Path for the input XMI file
        Path inputDir = projectRoot.resolve("Input");
        String xmiFile = inputDir.resolve("project1.xmi").toString(); // XMI input file

        System.out.println("Using the following files:");
        System.out.println("Metamodel: " + metaModelURL);
        System.out.println("XMI Transformations: " + xmiTransURL);
        System.out.println("Input XMI File: " + xmiFile);

        // Parse the metamodel file
        XMLParser parser = new XMLParser();
        MetaModel metaModel = new MetaModel();
        parser.parse(metaModelURL, metaModel.getSAXParserHandler());

        // Parse the XMI transformations file
        XMITransformations trans = new XMITransformations(metaModel);
        parser.parse(xmiTransURL, trans.getSAXParserHandler());

        // Parse the input XMI file
        Model model = new Model(metaModel);
        XMIReader xmiReader = new XMIReader(trans, model);
        parser.parse(xmiFile, xmiReader);

        // Optionally, specify element filters to exclude unnecessary elements
        String[] filters = { "#.java", "#.javax", "#.org.xml" };
        model.setFilter(filters, false, true);

        // Display the parsed model
        viewModel(model, metaModel);
    }

    /**
     * Displays the contents of the model.
     */
    private static void viewModel(Model model, MetaModel metaModel) {
        for (MetaModelElement type : metaModel) {
            System.out.println("Elements of type: " + type.getName());

            // Iterate over all model elements of the current type
            List<ModelElement> elements = model.getAcceptedElements(type);
            for (ModelElement me : elements) {
                System.out.println("  Element: " + me.getFullName() + " ");

                // Display the value of each attribute of the element
                Collection<String> attributeNames = type.getAttributeNames();
                for (String attr : attributeNames) {
                    System.out.print("     Attribute '" + attr);
                    if (type.isSetAttribute(attr)) {
                        System.out.println("' has set value: " + me.getSetAttribute(attr));
                    } else if (type.isRefAttribute(attr)) {
                        System.out.print("' references ");
                        ModelElement referenced = me.getRefAttribute(attr);
                        System.out.println((referenced == null) ? "nothing" : referenced.getFullName());
                    } else {
                        System.out.println("' has value: " + me.getPlainAttribute(attr));
                    }
                }
            }
        }
    }
}