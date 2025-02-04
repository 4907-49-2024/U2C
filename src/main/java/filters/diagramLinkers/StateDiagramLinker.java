package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import pipes.UMLModel;
import pipes.diagrams.state.StateDiagram;
import pipes.diagrams.state.StateTypes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The StateDiagramLinker takes in a UMLModel, returns a collection of StateDiagrams.
 * <br>
 * Every state diagram is an individual StateDiagram object,
 * All the internal components (states, transitions) also need to be registered to its parent diagram by the linker.
 */
public class StateDiagramLinker implements Runnable {
    // INPUT
    private final UMLModel model;
    // OUTPUT
    private final Set<StateDiagram> stateDiagrams;

    public StateDiagramLinker(UMLModel model) {
        this.model = model;
        this.stateDiagrams = new HashSet<>();
    }

    /**
     * Define all the state diagrams which exist in the model.
     */
    private void defineStateDiagrams() {
        List<ModelElement> elements = model.getTypedElements(StateTypes.statemachine.name());

        for (ModelElement element : elements) {
            stateDiagrams.add(new StateDiagram(element.getName()));
        }
    }

    public Set<StateDiagram> getStateDiagrams() {
        return stateDiagrams;
    }

    @Override
    public void run() {
        // Step 1: Define state diagrams
        defineStateDiagrams();
    }
}
