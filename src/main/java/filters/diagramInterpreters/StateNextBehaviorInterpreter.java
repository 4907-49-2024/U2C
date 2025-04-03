package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.behaviours.*;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.HashSet;
import java.util.Set;

import static filters.diagramInterpreters.utils.TransitionBehaviorHandler.getSourceAtomic;
import static filters.diagramInterpreters.utils.TransitionBehaviorHandler.getTargetAtomic;

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
            Behaviour source = StateBehaviorConverter.getStateBehavior(t.source());
            Behaviour target = StateBehaviorConverter.getStateBehavior(t.target());

            AtomicBehaviour initial = getSourceAtomic(source);
            AtomicBehaviour next = getTargetAtomic(target);
            mappings.add(new NextBehaviorMap(t.input(), initial, next));
        }

        output = new NextBehaviorSpecification(mappings);
    }
}
