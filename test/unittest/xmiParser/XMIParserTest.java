package unittest.xmiParser;

import org.junit.BeforeClass;
import org.junit.Test;
import xmiParser.XMIParser;
import xmiParser.XMIParserConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
}