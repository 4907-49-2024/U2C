import xmiParser.XMIParser;
import xmiParser.XMIParserConfig;

public class MainTUI {
    public static void main(String[] args) throws Exception {
        XMIParser parser = new XMIParser(new XMIParserConfig());

        // Print model (with separators)
        System.out.println("--------------------------------------------------");
        System.out.println(parser.parseStimuli());
        System.out.println("--------------------------------------------------");
        System.out.println(parser.parseAgents());
        System.out.println("--------------------------------------------------");
    }
}
