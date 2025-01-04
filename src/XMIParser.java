import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.util.XMLParser;

import java.util.Collection;
import java.util.List;

public class XMIParser {
    private final XMIParserConfig config;

    public XMIParser(XMIParserConfig config) {
        this.config = config;
    }

    /**
     * Parses the XMI file and returns the model as a string.
     */
    public String parseAndReturnModel() throws Exception {
        StringBuilder result = new StringBuilder();

        // Parse the metamodel file
        XMLParser parser = new XMLParser();
        MetaModel metaModel = new MetaModel();
        parser.parse(config.metaModel(), metaModel.getSAXParserHandler());

        // Parse the XMI transformations file
        XMITransformations trans = new XMITransformations(metaModel);
        parser.parse(config.xmiTrans(), trans.getSAXParserHandler());

        // Parse the input XMI file
        Model model = new Model(metaModel);
        XMIReader xmiReader = new XMIReader(trans, model);
        parser.parse(config.xmiFileName(), xmiReader);

        // Build the parsed model as a string
        for (MetaModelElement type : metaModel) {
            result.append("Elements of type: ").append(type.getName()).append("\n");

            // Iterate over all model elements of the current type
            List<ModelElement> elements = model.getAcceptedElements(type);
            for (ModelElement me : elements) {
                result.append("  Element: ").append(me.getFullName()).append("\n");

                // Display the value of each attribute of the element
                Collection<String> attributeNames = type.getAttributeNames();
                for (String attr : attributeNames) {
                    result.append("     Attribute '").append(attr);
                    if (type.isSetAttribute(attr)) {
                        result.append("' has set value: ").append(me.getSetAttribute(attr)).append("\n");
                    } else if (type.isRefAttribute(attr)) {
                        result.append("' references ");
                        ModelElement referenced = me.getRefAttribute(attr);
                        result.append((referenced == null) ? "nothing" : referenced.getFullName()).append("\n");
                    } else {
                        result.append("' has value: ").append(me.getPlainAttribute(attr)).append("\n");
                    }
                }
            }
        }

        return result.toString();
    }
}