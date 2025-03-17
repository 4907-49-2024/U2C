package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.behaviors.Behavior;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.HashSet;
import java.util.Set;

public class StateNextBehaviorInterpreter extends Filter<SuperState, NextBehaviorSpecification> {
    public StateNextBehaviorInterpreter(SuperState input) {
        super(input);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        Set<NextBehaviorMap> mappings = new HashSet<>();
        for(Transition t: input.getAllTransitions()){
            // FIXME: (#101) Implement logic to map composite destinations to AtomicBehaviors
            AtomicBehavior initial = (AtomicBehavior) StateBehaviorConverter.getStateBehavior(t.source());
            AtomicBehavior next = (AtomicBehavior) StateBehaviorConverter.getStateBehavior(t.target());
            mappings.add(new NextBehaviorMap(t.input(), initial, next));
        }

        output = new NextBehaviorSpecification(mappings);
    }
}
