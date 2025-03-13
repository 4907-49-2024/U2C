package filters.xmiParser;

import com.sdmetrics.model.ModelElement;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.StateType;

import java.util.List;

/**
 * Test that all diagrams have the elements we expect.
 */
class XMIParserTest {
    /**
     * Define test pipeline for Filter Under Test (FUT)
     * @param inputDiagramXMI Reference to input diagram file
     * @return StateDiagram type pipe object (FUT's output)
     * @throws Exception In case of thread or input exceptions
     */
    private UMLModel runTestPipeline(String inputDiagramXMI) throws Exception{
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1 - FUT
        XMIParser parser = new XMIParser(config);
        return parser.getOutput();
    }

    /**
     * Test for state diagram elements
     */
    @Test
    void testStateElements() throws Exception {
        // Collect output
        UMLModel model = runTestPipeline("superState.uml");
        List<ModelElement> states = model.getTypedElements(StateType.state.name());
        List<ModelElement> transitions = model.getTypedElements(StateType.transition.name());
        List<ModelElement> activity = model.getTypedElements(StateType.activity.name());
        List<ModelElement> statemachines = model.getTypedElements(StateType.statemachine.name());
        List<ModelElement> regions = model.getTypedElements(StateType.region.name());

        // Test States
        assert states.size() == 5; // Includes pseudo-states
        assert states.stream().anyMatch(s -> s.getName().equals("State1"));
        assert states.stream().anyMatch(s -> s.getName().equals("State4"));
        assert states.stream().anyMatch(s -> s.getName().equals("Inner1"));
        assert states.stream().anyMatch(s -> s.getName().equals("Inner2"));
        assert states.stream().anyMatch(s -> s.getName().isEmpty());

        // Test Transitions
        assert transitions.size() == 4; // Includes pseudo-states
        assert transitions.stream().anyMatch(s -> s.getName().equals("nextState"));
        assert transitions.stream().anyMatch(s -> s.getName().isEmpty());

        // Test StateDiagrams
        assert statemachines.size() == 1; // Includes pseudo-states
        assert statemachines.stream().anyMatch(s -> s.getName().equals("StateMachine1"));

        // Test Activites
        assert activity.size() == 3; // Includes pseudo-states
        assert activity.stream().anyMatch(s -> s.getName().equals("sendStim1"));
        assert activity.stream().anyMatch(s -> s.getName().equals("sendStim2"));
        assert activity.stream().anyMatch(s -> s.getName().equals("sendStim3"));

        // Test Regions (we just care about counting them, nothing else)
        assert regions.size() == 2;
    }
}