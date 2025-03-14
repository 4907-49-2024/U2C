package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import filters.Filter;
import pipes.diagrams.state.*;
import utils.ModelElementUtils;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * The StateDiagramLinker takes in a UMLModel, returns a collection of StateDiagrams.
 * <br>
 * Every state diagram is an individual StateDiagram object,
 * All the internal components (states, transitions) also need to be registered to its parent diagram by the linker.
 */
public class StateDiagramLinker extends Filter<ModelElement, SuperState> {
    public StateDiagramLinker(ModelElement stateDiagramElem) {
        super(stateDiagramElem);
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
        throw new InvalidParameterException("State not found: " + stateElement.getName());
    }


    /**
     * From a ModelElement, build a Transition object
     *
     * @param element The model element representing the transition
     * @param stateDomain The domain of states to find a transition within
     * @return The Transition representation of the ModelElement
     *
     * @throws InvalidObjectException if a transition from an initial state is found. Should not be counted
     */
    private Transition createTransition(Set<State> stateDomain, ModelElement element) throws InvalidObjectException {
        // If a state is not found, assume its due to weird behavior of initial state ownership being always top-level.
        //  This may hide bugs if they occur later on, careful
        State source;
        try {
            source = findState(stateDomain, element.getRefAttribute("source"));
        } catch (InvalidParameterException e) { //
            throw new InvalidObjectException("No state found, likely due to initial state. Ignore");
        }

        // If at top level, ignore initial state transitions
        if (source instanceof AtomicState s) {
            if (AtomicState.isInitialState(s))
                throw new InvalidObjectException("Initial state transition found, ignore");
        }

        // No issues, find dest state and link
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
        Set<ModelElement> regions = new HashSet<>(); // Regions containing states

        // Get state regions
        for(ModelElement me : ModelElementUtils.getOwnedElements(stateElement)){
            if(StateType.getType(me) == StateType.region) {
                regions.add(me);
            }
        }

        // Search regions for states
        for(ModelElement region: regions){
            for(ModelElement me : ModelElementUtils.getOwnedElements(region)) {
                if(StateType.getType(me) == StateType.state) {
                    children.add(buildStateRecursive(me));
                }
            }
        }

        // Search regions for their internal transitions
        for(ModelElement region: regions) {
            for(ModelElement me : ModelElementUtils.getOwnedElements(region)){
                if(StateType.getType(me) == StateType.transition) {
                    try {
                        internalTransitions.add(createTransition(children, me));
                    } catch (InvalidObjectException ignored){}
                }
            }
        }

        // Base Case - No children
        if (children.isEmpty()){
            return buildAtomicState(stateElement);
        }
        // Recursive Case
        return new SuperState(stateElement.getName(), children, internalTransitions, regions.size());
    }

    @Override
    public void run() {
        // Given a diagram model element, treat it as the root superstate and recursively fill it
        output = (SuperState) buildStateRecursive(input);
    }
}
