package filters.xmiParser;

import com.sdmetrics.model.ModelElement;
import com.sdmetrics.model.MetaModel;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.XMITransformations;
import com.sdmetrics.model.XMIReader;
import com.sdmetrics.util.XMLParser;
import org.junit.jupiter.api.Test;
import pipes.diagrams.state.StateType;
import pipes.parserConfig.XMIParserConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static testUtils.TestPaths.BASE_C2KA;

public class XMIParserTest {

    private Model runAndGetModel(String inputDiagramXMI) throws Exception {
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";

        XMIParserConfig config = new XMIParserConfig(BASE_C2KA, inputDiagramXMI, xmiTrans, metaModel);

        // Build and parse model
        XMLParser parser = new XMLParser();
        MetaModel mm = new MetaModel();
        parser.parse(config.metaModel(), mm.getSAXParserHandler());

        XMITransformations trans = new XMITransformations(mm);
        parser.parse(config.xmiTrans(), trans.getSAXParserHandler());

        Model model = new Model(mm);
        XMIReader reader = new XMIReader(trans, model);
        parser.parse(config.xmiInputFile(), reader);

        return model;
    }

    @Test
    void testAtomicDiagramElements() throws Exception {
        Model model = runAndGetModel("Atomic.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> activities = model.getAcceptedElements(model.getMetaModel().getType(StateType.activity.name()));
        List<ModelElement> transitions = model.getAcceptedElements(model.getMetaModel().getType(StateType.transition.name()));

        // Minimal expected structure
        assertEquals(1, states.size());
        assertEquals(1, activities.size());
        assertEquals(0, transitions.size());
    }

    @Test
    void testChoiceDiagramElements() throws Exception {
        Model model = runAndGetModel("Choice.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> transitions = model.getAcceptedElements(model.getMetaModel().getType(StateType.transition.name()));

        assertTrue(states.size() >= 2);
        assertTrue(transitions.isEmpty());
    }

    @Test
    void testSequentialDiagramElements() throws Exception {
        Model model = runAndGetModel("Sequential.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> transitions = model.getAcceptedElements(model.getMetaModel().getType(StateType.transition.name()));

        assertTrue(states.size() >= 2);
        assertFalse(transitions.isEmpty());
    }

    @Test
    void testParallelDiagramElements() throws Exception {
        Model model = runAndGetModel("Parallel.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> regions = model.getAcceptedElements(model.getMetaModel().getType(StateType.region.name()));

        assertTrue(states.size() >= 2);
        assertEquals(3, regions.size());
    }

    @Test
    void testNextMappingsDiagramElements() throws Exception {
        Model model = runAndGetModel("NextMappings.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> transitions = model.getAcceptedElements(model.getMetaModel().getType(StateType.transition.name()));

        assertTrue(states.size() >= 2);
        assertFalse(transitions.isEmpty());
    }

    @Test
    void testAtomicConditionalDiagramElements() throws Exception {
        Model model = runAndGetModel("Atomic-Conditional.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> activities = model.getAcceptedElements(model.getMetaModel().getType(StateType.activity.name()));

        assertFalse(states.isEmpty());
        assertFalse(activities.isEmpty());
    }

    @Test
    void testAtomicAssignmentDiagramElements() throws Exception {
        Model model = runAndGetModel("Atomic-Assignment.uml");

        List<ModelElement> states = model.getAcceptedElements(model.getMetaModel().getType(StateType.state.name()));
        List<ModelElement> activities = model.getAcceptedElements(model.getMetaModel().getType(StateType.activity.name()));

        assertFalse(states.isEmpty());
        assertFalse(activities.isEmpty());
    }
}
