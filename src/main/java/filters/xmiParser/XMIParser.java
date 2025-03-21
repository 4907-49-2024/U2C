package filters.xmiParser;

import com.sdmetrics.model.*;
import com.sdmetrics.util.XMLParser;
import filters.Filter;
import org.xml.sax.SAXException;
import pipes.XMIParserConfig;
import pipes.diagrams.state.StateType;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

public class XMIParser extends Filter<XMIParserConfig, ModelElement> {
    public XMIParser(XMIParserConfig config) {
        super(config);
    }

    private ModelElement getDiagram(Model model){
        // TODO: Revisit this line when handling multiple diagram types
        // Assumption only handle state machines
        String diagramType = StateType.statemachine.name();

        MetaModelElement type = model.getMetaModel().getType(diagramType);
        List<ModelElement> diagramElems = model.getAcceptedElements(type);

        // Assumption - only one diagram per file (should hold, according to our input specification)
        return  diagramElems.getFirst();
    }

    /**
     * Executes the filter
     */
    @Override
    public void run(){
        // Parse the metamodel file
        XMLParser parser;
        try {
            parser = new XMLParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        MetaModel metaModel = new MetaModel();
        try {
            parser.parse(input.metaModel(), metaModel.getSAXParserHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Parse the XMI transformations file
        XMITransformations trans = new XMITransformations(metaModel);
        try {
            parser.parse(input.xmiTrans(), trans.getSAXParserHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Parse the input XMI file -> Stores result in model object.
        Model model = new Model(metaModel);
        XMIReader xmiReader = new XMIReader(trans, model);
        try {
            parser.parse(input.xmiInputFile(), xmiReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        output = getDiagram(model);
    }
}