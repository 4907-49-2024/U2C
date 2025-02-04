package filterTests.diagramLinkers;

import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import filters.xmiParser.XMIParserConfig;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.diagrams.state.StateDiagram;

import java.util.Set;

/**
 * Test the stateDiagramLinker filter
 * TODO: May be nice to test multiple state diagrams a bit later too.
 */
public class StateDiagramLinkerTest {

    /**
     * Test a single state diagram (with superState.uml)
     */
    @Test
    void testSingleStateDiagram() throws Exception {
        // Setup Input
        String xmiFile = "superState.uml";
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParser parser = new XMIParser(new XMIParserConfig(xmiFile, xmiTrans, metaModel));
        UMLModel model = parser.getModel();
        StateDiagramLinker linker = new StateDiagramLinker(model);

        // Start Thread (run filter)
        Thread t = new Thread(linker);
        t.start();
        t.join();

        // Check output
        Set<StateDiagram> diagrams = linker.getStateDiagrams();
        // Assuming single diagram, do not need to match it
        StateDiagram d = diagrams.iterator().next();

        // Check statemachine itself
        assert diagrams.size() == 1;
        assert d.getName().equals("StateMachine1");
        // TODO: Check states
        // TODO: Check transitions
    }
}
