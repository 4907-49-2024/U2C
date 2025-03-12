package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import pipes.diagrams.state.*;
import utils.ModelElementUtils;

import java.util.*;

/**
 * The StateDiagramLinker takes in a UMLModel, returns a collection of StateDiagrams.
 * <br>
 * Every state diagram is an individual StateDiagram object,
 * All the internal components (states, transitions) also need to be registered to its parent diagram by the linker.
 */
public class StateDiagramLinker implements Runnable {
    // INPUT
    private final ModelElement stateDiagramElement;
    // OUTPUT
    private StateDiagram stateDiagram;

    public StateDiagramLinker(ModelElement stateDiagramElement) {
        this.stateDiagramElement = stateDiagramElement;
        this.stateDiagram = null;
    }

    /**
     * Finds a state element within the given set of state.
     * Uses name as key, as it is assumed to be unique (based on model validity).
     *
     * @param states The set of states to check for the desired state
     * @param stateElement The state element to find in the diagram (as a ModelElement)
     * @return The State object representation of the stateElement found in the diagram.
     */
    private State findState(Set<State> states, ModelElement stateElement) {
        String key = stateElement.getName();
        for(State state : states){
            if (key.equals(state.getKey()))
                return state;
        }
        // Something has gone wrong if we get to here
        throw new RuntimeException("State not found: " + stateElement.getName());
    }


    /**
     * From a ModelElement, build a Transition object
     *
     * @param element The model element representing the transition
     * @param stateDomain The domain of states to find a transition within
     * @return The Transition representation of the ModelElement
     */
    private Transition createTransition(Set<State> stateDomain, ModelElement element){
        State source = findState(stateDomain, element.getRefAttribute("source"));
        State target = findState(stateDomain, element.getRefAttribute("target"));

        return new Transition(source, target, element.getName());
    }

    /**
     * From a ModelElement, build a State object (Base case)
     * @param element The model element representing the state
     * @return The State object representation of the ModelElement
     */
    private State buildAtomicState(ModelElement element) {
        // Null checked activity
        ModelElement activityElem = element.getRefAttribute("doactivity");
        String activity = activityElem == null ? "" : activityElem.getName();

        return new AtomicState(element.getName(),
                element.getPlainAttribute("kind"),
                activity);
    }

    /**
     * Recursively build State objects.
     * Recursive Case: stateElement has children, -> build each child and then build super state with its children.
     * Base Case: stateElement has no children, build an AtomicState
     * Note: This also takes in any element which contain states, which is useful for getting the roots of a diagram.
     *
     * @param stateElement The state element to build
     * @return The recursively built state
     */
    private State buildStateRecursive(ModelElement stateElement) {
        // Check for children (and recursively build them)
        Set<State> children = new HashSet<>();
        Set<Transition> internalTransitions = new HashSet<>();
        int numRegions = 1; // States all have at least one region TODO: this may change to zero once parsing is fixed

        // Process states first - needed
        for(ModelElement me : ModelElementUtils.getOwnedElements(stateElement)){
            if(StateType.getType(me) == StateType.state) {
                children.add(buildStateRecursive(me));
            } else if(StateType.getType(me) == StateType.region) {
                numRegions++; // TODO: Add this to the parser! - rn this will never trigger
            }
        }

        // Find internal transitions between children
        for(ModelElement me : ModelElementUtils.getOwnedElements(stateElement)){
            if(StateType.getType(me) == StateType.transition) {
                internalTransitions.add(createTransition(children, me));
            }
        }

        // Base Case - No children
        if (children.isEmpty()){
            return buildAtomicState(stateElement);
        }
        // Recursive Case
        return new SuperState(stateElement.getName(), children, internalTransitions, numRegions);
    }

    @Override
    public void run() {
        // For each diagram, add it and register its owned elements to itself
        stateDiagram = new StateDiagram(stateDiagramElement.getName());
        // Fake container -> its children are the true roots of the diagram
        SuperState topLevelContainer = (SuperState) buildStateRecursive(stateDiagramElement);

        // Register roots of state diagram
        for (State child : topLevelContainer.children()) {
            stateDiagram.registerRoot(child);
        }
    }

    /**
     * Returns the desired output from this filter, the StateDiagrams representation of the given state diagram model
     * Note: it needs to run/join through a thread before collecting this output!
     *
     * @return The state diagrams linked by the linker.
     *
     * @throws IllegalStateException if the output has not been computed yet due to the filter not being run yet.
     */
    public StateDiagram getStateDiagram() {
        if (stateDiagram == null){
            throw new IllegalStateException("StateDiagram not initialized, run filter before getting output!");
        }
        return stateDiagram;
    }
}
