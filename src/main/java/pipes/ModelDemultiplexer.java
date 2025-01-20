package pipes;

import com.sdmetrics.model.ModelElement;
import primitives.Primitives;
import primitives.Stimulus;
import xmiParser.XMIParser;

import java.util.List;

/**
 * The model demultiplexer takes in the parser input and uses it to set up the different primitive collections.
 */
public class ModelDemultiplexer implements Runnable {
    private final XMIParser parser;
    private final Primitives primitives;

    public ModelDemultiplexer(XMIParser parser, Primitives primitives) {
        this.parser = parser;
        this.primitives = primitives;
    }

    /**
     * Packs the stimuli, by parsing the message strings.
     * PRECONDITION: Exactly 1 ":" character in message name, with the sequence number before it!
     *
     */
    private void packStimuli(){
        List<ModelElement> elems = parser.getTypedElements("message");
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
