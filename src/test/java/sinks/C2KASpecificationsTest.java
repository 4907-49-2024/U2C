package sinks;

import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.StateAbstractBehaviorInterpreter;
import filters.diagramInterpreters.StateConcreteBehaviorInterpreter;
import filters.diagramInterpreters.StateNextBehaviorInterpreter;
import filters.diagramInterpreters.StateNextStimInterpreter;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.SuperState;

import java.util.List;

public class C2KASpecificationsTest {
    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return The data sink (Tested output!)
     * @throws Exception In case of thread or input exceptions
     */
    private C2KASpecifications runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        UMLModel model = parser.getOutput();
        // Filter 2
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3s
        StateAbstractBehaviorInterpreter abstractB = new StateAbstractBehaviorInterpreter(stateDiagram);
        StateNextBehaviorInterpreter nextB = new StateNextBehaviorInterpreter(stateDiagram);
        StateNextStimInterpreter nextS = new StateNextStimInterpreter(stateDiagram);
        StateConcreteBehaviorInterpreter concreteB = new StateConcreteBehaviorInterpreter(stateDiagram);
        // Sink - Test Output!
        return new C2KASpecifications(stateDiagram.name(), abstractB.getOutput(),
                nextB.getOutput(), nextS.getOutput(), concreteB.getOutput());
    }

    @Test
    void testAtomic() throws Exception {
        // Get output
        C2KASpecifications specs = runTestPipeline("C2KA-BaseRepresentations/Atomic.uml");
        String expectedFormat = "begin AGENT where\n" +
                "\n" +
                "\tAtomic Behavior :=  <name> \n" +
                "\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin NEXT_BEHAVIOR  where\n" +
                "\n" +
                "\t\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin NEXT_STIMULUS  where\n" +
                "\n" +
                "\t\n" +
                "end\n" +
                "\n" +
                "\n" +
                "begin CONCRETE_BEHAVIOR  where\n" +
                "\n" +
                "\t<name> => [ <behavior-expression> ]\n\t" +
                "\n" +
                "end";

        assert specs.toString().equals(expectedFormat);
    }

    // TODO: implement system tests here.
    //  Maybe the other C2KA_BaseRepresentations?
    //  Mostly interested in the real sample outputs
}
