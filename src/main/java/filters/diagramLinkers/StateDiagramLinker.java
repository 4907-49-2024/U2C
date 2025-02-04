package filters.diagramLinkers;

import com.sdmetrics.model.ModelElement;
import pipes.UMLModel;
import pipes.diagrams.state.*;

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
     * From a ModelElement, build a State object (Base case)
     * @param element The model element representing the state
     * @return The State object representation of the ModelElement
     */
    private State buildState(ModelElement element, State parent) {
        return new State(element.getName(),
                element.getPlainAttribute("kind"),
                element.getPlainAttribute("doactivity"),
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
     */
    private void registerStateRecursive(StateDiagram diagram, ModelElement element, State parent){
        // Always store own state
        State newState = buildState(element, parent);
        diagram.registerElement(newState);
        // Store children, if they exist... Normally should only exist at 1 depth but this should go at any depth.
        for(ModelElement me : element.getOwnedElements()){
            if(StateType.getType(me) == StateType.state){
                registerStateRecursive(diagram, me, newState);
            } else{
                // TODO: Replace prints with proper logging or errors
                System.out.println("Unexpected child type in superstate");
            }
        } // Base case -> element with no children
    }

    /**
     * From a ModelElement, build a Transition object
     * @param element The model element representing the transition
     * @return The Transition representation of the ModelElement
     */
    private Transition buildTransition(ModelElement element){
        State source = buildState(element.getRefAttribute("source"), null);
        State target = buildState(element.getRefAttribute("target"), null);

        return new Transition(source, target, element.getName());
    }

    @Override
    public void run() {
        // Get set of State Diagrams elements
        List<ModelElement> diagramElements = model.getTypedElements(StateType.statemachine.name());

        // For each diagram, add it and register its owned elements to itself
        for (ModelElement element : diagramElements) {
            StateDiagram newDiagram = new StateDiagram(element.getName());
            stateDiagrams.add(newDiagram);

            // Add its owned elements
            for(ModelElement ownedElement : element.getOwnedElements()) {
                // Choose the StateDiagramElement to construct, based on model type.
                switch (StateType.getType(ownedElement)) {
                    case StateType.state -> registerStateRecursive(newDiagram, ownedElement, null);
                    case StateType.transition ->  newDiagram.registerElement(buildTransition(ownedElement));
                    default -> System.out.println("Unknown state: " + ownedElement.getName());
                }
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
}
