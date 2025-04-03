package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.behaviours.*;
import pipes.c2ka.specifications.AbstractBehaviorSpecification;
import pipes.diagrams.state.*;


/**
 * The StateAbstractBehaviorInterpreter takes in a StateDiagram, and returns a representation of its AbstractBehaviors.
 * The output's toString returns the AbstractBehavior of the entire agent.
 */
public class StateAbstractBehaviorInterpreter extends Filter<SuperState, AbstractBehaviorSpecification> {
    public StateAbstractBehaviorInterpreter(SuperState diagram) {
        super(diagram);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // Can assume the input given is a composite because the input is a SuperState
        CompositeBehaviour topLevelBehavior = (CompositeBehaviour) StateBehaviorConverter.getStateBehavior(input);
        output = new AbstractBehaviorSpecification(input.name(), topLevelBehavior);
    }
}
