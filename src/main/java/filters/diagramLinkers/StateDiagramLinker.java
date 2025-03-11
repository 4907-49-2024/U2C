package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import pipes.UMLModel;
import pipes.diagrams.state.*;

import java.util.*;

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
     * From a ModelElement, build a State object (Base case)
     * @param element The model element representing the state
     * @return The State object representation of the ModelElement
     */
    private State buildState(ModelElement element, State parent) {
        return new State(element.getName(),
                element.getPlainAttribute("kind"),
                element.getRefAttribute("doactivity").getName(),
                parent);
    }

    /**
     * Build a State object recursively:
     * - Normal state: Shall have no children, should have an activity
     * - Base Case, add the state directly
     * - Superstate: Shall have children (>1), shall not have an activity.
     * 1. Recursively call this method, with all the owned elements
     * 2. Assumption: There are only normal states as children (no other types, single depth)
     *
     * @param diagram The State Diagram to register states to
     * @param element The model element representing the top level state
     * @param parent The parent state (if applicable, can be null)
     *
     * @throws IllegalStateException if the Base Case or the Recursive Case assumption fails.
     */
    private void registerStateRecursive(StateDiagram diagram, ModelElement element, State parent){
        // Always store own state
        State newState = buildState(element, parent);
        diagram.registerElement(newState);

        // Null check, because lib has the bad practice of returning null instead of an empty collection
        Collection<ModelElement> elements = element.getOwnedElements();
        elements = Objects.requireNonNullElse(elements, new ArrayList<>()); // Empty collection if null
        // Store children, if they exist... Normally should only exist at 1 depth but this should go at any depth.
        for(ModelElement me : elements){
            if(StateType.getType(me) == StateType.state){
                registerStateRecursive(diagram, me, newState);
            } else{
                // TODO: Replace prints with proper logging or errors
                System.out.println("Unexpected child type in superstate: "+ StateType.getType(me));
            }
        } // Base case -> element with no children
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
        // Get set of State Diagrams elements
        // TODO: Make state diagram linker take in one model at a time...
        List<ModelElement> diagramElements = model.getTypedElements(StateType.statemachine.name());

        // For each diagram, add it and register its owned elements to itself
        for (ModelElement element : diagramElements) {
            StateDiagram newDiagram = new StateDiagram(element.getName());
            stateDiagrams.add(newDiagram);

            // Add its owned elements... We need to register states first so sort on the first past
            List<ModelElement> stateElements = new ArrayList<>();
            List<ModelElement> transitionElements = new ArrayList<>();
            for(ModelElement ownedElement : element.getOwnedElements()) {
                switch (StateType.getType(ownedElement)) {
                    case StateType.state -> stateElements.add(ownedElement);
                    case StateType.transition -> transitionElements.add(ownedElement);
                    default -> System.out.println("Unknown state: " + ownedElement.getName());
                }
            }

            // Register states, then transitions (so they can do state lookups)
            for (ModelElement stateElement : stateElements) {
                registerStateRecursive(newDiagram, stateElement, null);
            }
            for (ModelElement transitionElement : transitionElements) {
                newDiagram.registerElement(registerTransition(newDiagram, transitionElement));
            }
        }
    }

    /**
     * Returns the desired output from this filter, the set of StateDiagrams in the Model/
     * Note: it needs to run/join through a thread before collecting this output!
     *
     * @return The set of state diagrams linked by the linker.
     */
    public Set<StateDiagram> getStateDiagrams() {
        return stateDiagrams;
    }

    // FIXME: Remember to make StateDiagramLinker take in a single state machine as input
}
