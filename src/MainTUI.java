import org.xml.sax.SAXException;
import xmiParser.XMIParser;
import xmiParser.XMIParserConfig;

import javax.xml.parsers.ParserConfigurationException;

public class MainTUI {
    public static void main(String[] args) throws Exception {
        XMIParser parser = new XMIParser(new XMIParserConfig());
        String model = "";
        // Parse
        try {
            model = parser.parseAndReturnModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Print model (with separators)
        System.out.println("--------------------------------------------------");
        System.out.println(model);
        System.out.println("--------------------------------------------------");
    }
}
