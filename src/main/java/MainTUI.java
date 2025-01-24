import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import filters.ModelDemultiplexer;
import pipes.primitives.Primitives;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;

import java.util.Collection;
import java.util.List;

public class MainTUI {
    public static void main(String[] args) throws Exception {
        // PIPE 1
        String xmiFile = "Simplest.uml";

        // FILTER 1
        XMIParser parser = new XMIParser(new XMIParserConfig(xmiFile));
        // PIPE 2
        Model UMLModel = parser.getModel();

        // PIPE 3 (Empty)
        Primitives primitives = new Primitives();

        // FILTER 2
        ModelDemultiplexer demux = new ModelDemultiplexer(UMLModel, primitives);
        Thread t = new Thread(demux);
        // Check stimuli before
        System.out.println(primitives.getStimuliNames());
        System.out.println(primitives.getTimedStimuli());
        System.out.println("--------------------------------------------------");
        t.start();
        t.join();
        // Check stimuli after
        System.out.println(primitives.getStimuliNames());
        System.out.println(primitives.getTimedStimuli());
        System.out.println("--------------------------------------------------");
    }


    /**
     * Parses the XMI file and returns the model as a string. -> useful for active debugging
     * <p>
     * Note: This is a debug/logging method. Don't use this in production code. Out of testing scope.
     */
    private String showFullModel(Model model) {
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
