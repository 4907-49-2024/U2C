package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.behaviors.Behavior;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.HashSet;
import java.util.Set;

public class StateNextBehaviorInterpreter extends Filter<SuperState, Set<NextBehaviorMap>> {
    public StateNextBehaviorInterpreter(SuperState input) {
        super(input);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        output = new HashSet<>();
        for(Transition t: input.getAllTransitions()){
            Behavior initial = StateBehaviorConverter.getStateBehavior(t.source());
            Behavior next = StateBehaviorConverter.getStateBehavior(t.target());
            output.add(new NextBehaviorMap(initial, t.input(), next));
        }
    }
}
