package unittest.xmiParser;

import com.sdmetrics.model.ModelElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xmiParser.XMIParser;
import xmiParser.XMIParserConfig;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test that all diagrams have the elements we expect.
 */
class XMIParserTest {
    // Assumes default mappings! No need to change until we're using more than 1.
    private static XMIParser simplestDiagram;
    private static XMIParser multiRcvDiagram;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        simplestDiagram = new XMIParser(new XMIParserConfig("Simplest.uml"));
        multiRcvDiagram = new XMIParser(new XMIParserConfig("multiRcv.uml"));
    }

    /**
     * Test collaboration diagram elements. These include:
     *  - Messages
     *  - Lifelines
     */
    @Test
    public void testGetTypedElementsCollab() {
        List<ModelElement> messages = simplestDiagram.getTypedElements("message");
        List<ModelElement> lifelines = simplestDiagram.getTypedElements("lifeline");

        assertEquals(1, messages.size());
        assertEquals("1:OneToTwo", messages.getFirst().getName());

        // The model is not ordered (doesn't have to be), not checking specific contents
        assertEquals(2, lifelines.size());


        // Repeat test for 2nd diagram
        messages = multiRcvDiagram.getTypedElements("message");
        lifelines = multiRcvDiagram.getTypedElements("lifeline");
        assertEquals(3, messages.size());

        // The model is not ordered (doesn't have to be), not checking specific contents
        assertEquals(3, lifelines.size());
    }

    /**
     * Test state diagram elements. These include:
     *  - State...
     */
    @Test
    public void testGetTypedElementsState() {
        // TODO: When states get implemented, add this test.
    }
}