package filters.diagramInterpreters;

import pipes.c2ka.primitives.Behavior;
import pipes.diagrams.state.StateDiagram;


/**
 * The StateAbstractBehaviorInterpreter takes in a StateDiagram, and returns a representation of its AbstractBehaviors.
 */
public class StateAbstractBehaviorInterpreter {
    // INPUT
    private final StateDiagram diagram;
    // OUTPUT (top level behavior: string representation = Abstract Behavior Specification)
    private Behavior topBehavior;

    public StateAbstractBehaviorInterpreter(StateDiagram diagram) {
        this.diagram = diagram;
    }
}
