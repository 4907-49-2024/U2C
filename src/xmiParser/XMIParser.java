package xmiParser;

import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.util.XMLParser;
import xmiParser.UMLMappings.UMLMapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XMIParser {
    private final Model model; // This is where data gets stored after parsing
    private final UMLMapping mappings;

    public XMIParser(XMIParserConfig config) throws Exception {
        this.mappings = config.umlMapping();
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
     * Gets set of names of all elements corresponding to given type name.
     *
     * @param typeName The name of the type in the metamodel desired (gotten through UMLMappings as good practice)
     * @return set of names of all elements corresponding to given type name
     */
    private Set<String> getElementNames(String typeName){
        // Store given element type for parsing
        MetaModelElement type = model.getMetaModel().getType(typeName);

        // Add all element names to set (duplicates discarded)
        Set<String> elemNames = new HashSet<>();
        List<ModelElement> elements = model.getAcceptedElements(type);
        for (ModelElement me : elements) {
            elemNames.add(me.getName());
        }

        return elemNames;
    }

    /**
     * Parse model to return set of stimuli found.
     *
     * @return A set of names of stimuli in the model.
     */
    public Set<String> parseStimuli(){
        return getElementNames(mappings.getTypeStimulus());
    }

    /**
     * Parse model to return set of agents found.
     *
     * @return A set of names of agents in the model.
     */
    public Set<String> parseAgents(){
        return getElementNames(mappings.getTypeAgent());
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