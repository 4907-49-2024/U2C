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
     * From a ModelElement, build a State object (Base case)
     * @param element The model element representing the state
     * @return The State object representation of the ModelElement
     */
    private State buildState(ModelElement element, State parent) {
        // Null checked activity
        ModelElement activityElem = element.getRefAttribute("doactivity");
        String activity = activityElem == null ? "" : activityElem.getName();

        return new State(element.getName(),
                element.getPlainAttribute("kind"),
                activity,
                parent);
    }

    /**
     * Build a State object recursively:
     * - Base Case: No state children.
     * - Recursive case: Superstate, has a state child.
     *
     * @param diagram The State Diagram to register states to
     * @param element The model element representing the top level state
     * @param parent The parent state (if applicable, can be null)
     */
    private void registerStateRecursive(StateDiagram diagram, ModelElement element, State parent){
        // Always store own state
        State newState = buildState(element, parent);
        diagram.registerElement(newState);

        // Recursively register contains states if they exist
        for(ModelElement me : ModelElementUtils.getOwnedElements(element)){
            if(StateType.getType(me) == StateType.state) {
                registerStateRecursive(diagram, me, newState);
            }
        }
    }

    /**
     * From a ModelElement, build a Transition object
     * PRECONDITION: States are all linked already to the given diagram.
     *
     * @param element The model element representing the transition
     * @param diagram The diagram containing the states + new transition element
     * @return The Transition representation of the ModelElement
     */
    private Transition registerTransition(StateDiagram diagram, ModelElement element){
        State source = findState(diagram, element.getRefAttribute("source"));
        State target = findState(diagram, element.getRefAttribute("target"));

        return new Transition(source, target, element.getName());
    }

    /**
     * Finds a state element already linked in the given diagram.
     * Uses name as key, as it is assumed to be unique (based on model validity).
     *
     * @param diagram The state diagram model with states fully linked
     * @param stateElement The state element to find in the diagram (as a ModelElement)
     * @return The State object representation of the stateElement found in the diagram.
     */
    private State findState(StateDiagram diagram, ModelElement stateElement) {
        Set<State> states = diagram.getStates();
        String key = stateElement.getName();
        for(State state : states){
            if (key.equals(state.name()))
                return state;
        }
        // Something has gone wrong if we get to here
        throw new RuntimeException("State not found: " + stateElement.getName());
    }

    @Override
    public void run() {
        // For each diagram, add it and register its owned elements to itself
        stateDiagram = new StateDiagram(stateDiagramElement.getName());

        // Add its owned elements... We need to register states first so sort on the first past
        List<ModelElement> stateElements = new ArrayList<>();
        List<ModelElement> transitionElements = new ArrayList<>();
        for(ModelElement ownedElement : stateDiagramElement.getOwnedElements()) {
            switch (StateType.getType(ownedElement)) {
                case StateType.state -> stateElements.add(ownedElement);
                case StateType.transition -> transitionElements.add(ownedElement);
                default -> System.out.println("Unknown state: " + ownedElement.getName());
            }
        }

        // Register states, then transitions (so they can do state lookups)
        for (ModelElement stateElement : stateElements) {
            registerStateRecursive(stateDiagram, stateElement, null);
        }
        for (ModelElement transitionElement : transitionElements) {
            stateDiagram.registerElement(registerTransition(stateDiagram, transitionElement));
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
