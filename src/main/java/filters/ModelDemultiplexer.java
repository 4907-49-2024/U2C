package filters;

import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;
import pipes.primitives.Primitives;
import pipes.primitives.Stimulus;
import filters.xmiParser.XMIParser;

import java.util.List;

/**
 * The model demultiplexer takes in the parser input and uses it to set up the different primitive collections.
 */
public class ModelDemultiplexer implements Runnable {
    private final Model model;
    private final Primitives primitives;

    public ModelDemultiplexer(Model model, Primitives primitives) {
        this.model = model;
        this.primitives = primitives;
    }


    /**
     * Get elements from the model matching the given type.
     * Pre-reduces "invalid" elements this includes:
     *  - Elements with no names.
     *
     * @param typeName The type of elements to get from the model.
     */
    private List<ModelElement> getTypedElements(String typeName){
        // Store given element type for parsing
        MetaModelElement type = model.getMetaModel().getType(typeName);

        List<ModelElement> typedElements = model.getAcceptedElements(type);
        // Remove blank name elements
        typedElements.removeIf(e -> e.getName().isBlank());
        return typedElements;
    }


    /**
     * Packs the stimuli, by parsing the message strings.
     * PRECONDITION: Exactly 1 ":" character in message name, with the sequence number before it!
     *
     */
    private void packStimuli(){
        List<ModelElement> elems = getTypedElements("message");
        for (ModelElement elem : elems) {
            // String format: (Sequence):(Name)
            String[] stimComponents = elem.getName().split(":");
            primitives.registerStimulus(new Stimulus(stimComponents[1], Integer.parseInt(stimComponents[0])));
        }
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // DEMUX the parser
        packStimuli();
    }
}
