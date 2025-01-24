package filters.xmiParser;

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
     * @return UML Model representation of the input diagrams.
     */
    public Model getModel(){
        return model;
    }
}