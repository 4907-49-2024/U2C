import java.nio.file.Path;
import java.nio.file.Paths;

// 1. Required imports
import java.util.Collection;
import java.util.List;

import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.util.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class Main {
    // Call the function under prototype
    public static void main(String[] args) throws Exception {
        // Tutorial: https://www.sdmetrics.com/javadoc/com/sdmetrics/model/package-summary.html

        // Path convenience variable
        final Path resRoot = Paths.get("C:\\Users\\Alex\\IdeaProjects\\LibraryExploration\\lib\\SDMetricsOpenCore\\src\\com\\sdmetrics\\resources");
        // 2. Prepare Input files
        String metaModelURL = resRoot.resolve("metamodel2.xml").toString(); // Use UML 2.x as the metamodel
        String xmiTransURL = resRoot.resolve("xmiTrans2_0.xml").toString(); // Use UML+XMI 2.x as the XMI transformations
        String xmiFile = "C:\\Users\\Alex\\IdeaProjects\\LibraryExploration\\Input\\project1.xmi";       // INPUT: XMI file with the UML model

        // 3. Read metamodel - Exceptions unhandled
        XMLParser parser = new XMLParser(); // Can use a different XML reader that we create if we want
        MetaModel metaModel = new MetaModel();
        parser.parse(metaModelURL, metaModel.getSAXParserHandler());

        // 4. Read XMI transformation file
        XMITransformations trans=new XMITransformations(metaModel);
        parser.parse(xmiTransURL, trans.getSAXParserHandler());

        // 5. Read the XMI file with the UML model
        Model model = new Model(metaModel);
        XMIReader xmiReader = new XMIReader(trans, model);
        parser.parse(xmiFile, xmiReader);

        // 6.  Optionally, specify element filters to get rid of standard libraries or 3rd party APIs
        String[] filters = { "#.java", "#.javax", "#.org.xml" };
        model.setFilter(filters, false, true);

        // 7. Access the UML model
        viewModel(model, metaModel);
    }

    private static void viewModel(Model model, MetaModel metaModel){
        // The following example writes all model elements accepted by the element filter to the console,
        // along with the values of their attributes.
        for (MetaModelElement type : metaModel) {
            System.out.println("Elements of type: " + type.getName());

            // iterate over all model elements of the current type
            List<ModelElement> elements = model.getAcceptedElements(type);
            for (ModelElement me : elements) {
                System.out.println("  Element: " + me.getFullName() + " ");

                // write out the value of each attribute of the element
                Collection<String> attributeNames = type.getAttributeNames();
                for (String attr : attributeNames) {
                    System.out.print("     Attribute '" + attr);
                    if (type.isSetAttribute(attr))
                        System.out.println("' has set value "
                                + me.getSetAttribute(attr));
                    else if (type.isRefAttribute(attr)) {
                        System.out.print("' references ");
                        ModelElement referenced = me.getRefAttribute(attr);
                        System.out.println((referenced == null) ? "nothing"
                                : referenced.getFullName());
                    } else
                        System.out.println("' has value: "
                                + me.getPlainAttribute(attr));
                }
            }
        }
    }

    // ======================== Working methods ========================
    private static void hello(){
        System.out.println("Hello world!");
    }
}