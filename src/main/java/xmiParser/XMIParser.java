package xmiParser;

import com.sdmetrics.model.*;
import com.sdmetrics.util.XMLParser;

import java.util.*;

public class XMIParser {
    private final Model model; // This is where data gets stored after parsing

    public XMIParser(XMIParserConfig config) throws Exception {
        // Parse the metamodel file
        XMLParser parser = new XMLParser();
        MetaModel metaModel = new MetaModel();
        parser.parse(config.metaModel(), metaModel.getSAXParserHandler());

        // Parse the XMI transformations file
        XMITransformations trans = new XMITransformations(metaModel);
        parser.parse(config.xmiTrans(), trans.getSAXParserHandler());

        // Parse the input XMI file -> Stores result in model object.
        model = new Model(metaModel);
        XMIReader xmiReader = new XMIReader(trans, model);
        parser.parse(config.xmiFileName(), xmiReader);
    }

    /**
     * Get elements from the model matching the given type.
     * Pre-reduces "invalid" elements this includes:
     *  - Elements with no names.
     *
     * @param typeName The type of elements to get from the model.
     */
    public List<ModelElement> getTypedElements(String typeName){
        // Store given element type for parsing
        MetaModelElement type = model.getMetaModel().getType(typeName);

        List<ModelElement> typedElements = model.getAcceptedElements(type);
        // Remove blank name elements
        typedElements.removeIf(e -> e.getName().isBlank());
        return typedElements;
    }

    /**
     * Parses the XMI file and returns the model as a string.
     * <p>
     * Note: This is a debug/logging method. Don't use this in production code. Out of testing scope.
     */
    public String getFullModel() {
        StringBuilder result = new StringBuilder();

        // Build the parsed model as a string
        for (MetaModelElement type : model.getMetaModel()) {
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