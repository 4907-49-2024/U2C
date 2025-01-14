package xmiParser;

import com.sdmetrics.model.*;
import com.sdmetrics.util.XMLParser;
import xmiParser.UMLMappings.UMLMapping;

import java.util.*;

public class XMIParser {
    private final Model model; // This is where data gets stored after parsing
    private final UMLMapping mappings;

    public XMIParser(XMIParserConfig config) throws Exception {
        this.mappings = config.umlMapping();
        // Parse the metamodel file
        XMLParser parser = new XMLParser();
        MetaModel metaModel = new MetaModel();
        parser.parse(config.metaModel(), metaModel.getSAXParserHandler());

        MetaModelElement commentType = metaModel.getType("comment");
        commentType.addAttribute("annotatedElement", false, false); // Adding missing attribute

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

    /**
     * Parse the XMI file and return behaviours (states) associated with UML agents (objects).
     *
     * @return A String to Set map of object names and their states.
     */
    public  Map<String, Set<String>> parseBehaviours() {
        Map<String, Set<String>> agentsToBehaviours = new HashMap<>();

        // Retrieve types from UMLMappings for modularity
        String lifelineTypeName = mappings.getTypeAgent();
        String commentTypeName = mappings.getTypeComment();

        // Retrieve lifeline and comment types from the model
        MetaModelElement lifelineType = model.getMetaModel().getType(lifelineTypeName);
        MetaModelElement commentType = model.getMetaModel().getType(commentTypeName);

        if (lifelineType == null || commentType == null) {
            System.err.println("Error: 'lifeline' or 'comment' type not found in metamodel.");
            return agentsToBehaviours; // Return empty output if types are not defined
        }

        // Map lifeline IDs to their names
        Map<String, String> lifelineIdToName = new HashMap<>();
        for (ModelElement lifeline : model.getAcceptedElements(lifelineType)) {
            String lifelineId = lifeline.getPlainAttribute("id");
            String lifelineName = lifeline.getName();
            if (lifelineId != null && lifelineName != null) {
                lifelineIdToName.put(lifelineId, lifelineName);
            }
        }

        // Process comments
        for (ModelElement comment : model.getAcceptedElements(commentType)) {
            String annotatedElementId = comment.getPlainAttribute("annotatedElement");
            String commentBody = comment.getPlainAttribute("body");

            if (annotatedElementId != null && commentBody != null) {
                String lifelineName = lifelineIdToName.get(annotatedElementId);
                if (lifelineName != null) {
                    // Extract states from the comment body
                    String[] lines = commentBody.split("\n");
                    if (lines.length > 0) {
                        agentsToBehaviours.computeIfAbsent(lifelineName, k -> new LinkedHashSet<>()).add(lines[0].trim()); // Default state
                        for (int i = 1; i < lines.length; i++) { // Transitions
                            int colonIndex = lines[i].indexOf(":");
                            if (colonIndex != -1) {
                                agentsToBehaviours.get(lifelineName).add(lines[i].substring(colonIndex + 1).trim());
                            }
                        }
                    }
                }
            }
        }
        return agentsToBehaviours;
    }

    /**
     * Formats the behaviours map for display.
     *
     * @param agentsToBehaviours Map of agents (lifelines) to their behaviours (states).
     * @return A formatted string representation of the behaviours.
     */
    public static String viewBehaviours(Map<String, Set<String>> agentsToBehaviours) {
        StringBuilder result = new StringBuilder("[");

        agentsToBehaviours.forEach((agent, states) -> {
            result.append(agent)
                    .append(":(")
                    .append(String.join(", ", states))
                    .append("), ");
        });

        // Remove trailing comma and space, close the bracket
        if (result.length() > 1) {
            result.setLength(result.length() - 2);
        }
        result.append("]");

        return result.toString();
    }



}