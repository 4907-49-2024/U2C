import filters.ModelDemultiplexer;
import pipes.primitives.Primitives;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;

public class MainTUI {
    public static void main(String[] args) throws Exception {
        XMIParser parser = new XMIParser(new XMIParserConfig());
        Primitives primitives = new Primitives();

        // Print model (with separators)
//        System.out.println("--------------------------------------------------");
//        System.out.println(parser.parseStimuli());
//        System.out.println("--------------------------------------------------");
//        System.out.println(parser.parseAgents());
//        System.out.println("--------------------------------------------------");
        System.out.println(parser.getFullModel());
        System.out.println("--------------------------------------------------");

        // Start Processing model
        ModelDemultiplexer demux = new ModelDemultiplexer(parser, primitives);
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
}
