package unittest.xmiParser;

import org.junit.BeforeClass;
import org.junit.Test;
import xmiParser.XMIParser;
import xmiParser.XMIParserConfig;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test that all diagrams have the elements we expect.
 */
public class XMIParserTest {
    // Assumes default mappings! No need to change until we're using more than 1.
    private static XMIParser simplestDiagram;
    private static XMIParser multiRcvDiagram;
    private static XMIParser behaviourEmbedDiagram;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        simplestDiagram = new XMIParser(new XMIParserConfig("Simplest.uml"));
        multiRcvDiagram = new XMIParser(new XMIParserConfig("multiRcv.uml"));
        behaviourEmbedDiagram = new XMIParser(new XMIParserConfig("behaviourEmbedDiagram.uml"));
    }

    @Test
    public void parseStimuli() {
        // Setup expected (Simplest)
        Set<String> expected = new HashSet<>();
        expected.add("1:OneToTwo");

        assert simplestDiagram.parseStimuli().equals(expected);

        // Setup expected (multiRcv)
        expected.clear();
        expected.add("1:sendEvent1");
        expected.add("2:sendEvent2");

        assert multiRcvDiagram.parseStimuli().equals(expected);

        // Setup expected (behaviourEmbedDiagram)
        expected.clear();
        expected.add("1:SendEvent1");
        expected.add("2:SendEvent2");

        assert behaviourEmbedDiagram.parseStimuli().equals(expected);
    }

    @Test
    public void parseAgents() {
        // Setup expected (Simplest)
        Set<String> expected = new HashSet<>();
        expected.add("One");
        expected.add("Two");

        assert simplestDiagram.parseAgents().equals(expected);

        // Setup expected (multiRcv)
        expected.clear();
        expected.add("Obj1");
        expected.add("Obj2");
        expected.add("Obj3");

        assert multiRcvDiagram.parseAgents().equals(expected);

        // Setup expected (behaviourEmbedDiagram)
        expected.clear();
        expected.add("One");
        expected.add("Two");

        assert behaviourEmbedDiagram.parseAgents().equals(expected);
    }

    @Test
    public void parseBehaviours() {
        // Expected for behaviourTest.uml
        String expectedBehaviourTest = "[One:(State1, State2, State5), Two:(State3, State4)]";;
        assertEquals(expectedBehaviourTest, behaviourEmbedDiagram.parseBehaviours());
    }
}