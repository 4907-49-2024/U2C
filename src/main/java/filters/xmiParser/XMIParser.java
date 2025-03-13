package filters.xmiParser;

import com.sdmetrics.model.*;
import com.sdmetrics.util.XMLParser;
import filters.Filter;
import org.xml.sax.SAXException;
import pipes.UMLModel;
import pipes.XMIParserConfig;

import javax.xml.parsers.ParserConfigurationException;

public class XMIParser extends Filter<XMIParserConfig, UMLModel> {
    public XMIParser(XMIParserConfig config) {
        super(config);
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

        output = new UMLModel(model);
    }
}