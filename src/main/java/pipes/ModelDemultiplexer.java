package pipes;

import xmiParser.XMIParser;

/**
 * The model demultiplexer takes in the parser input and uses it to set up the different primitive collections.
 */
public class ModelDemultiplexer implements Runnable {
    private XMIParser parser;

    ModelDemultiplexer(XMIParser parser) {
        this.parser = parser;
    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // DEMUX the parser
        parser.getTypedElements("message");
    }
}
