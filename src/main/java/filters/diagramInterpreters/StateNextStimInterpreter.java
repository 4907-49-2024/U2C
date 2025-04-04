package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.behaviours.Behaviour;
import pipes.c2ka.semirings.NextStimulusMap;
import pipes.c2ka.specifications.NextStimulusSpecification;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.HashSet;
import java.util.Set;

import static filters.diagramInterpreters.utils.TransitionBehaviorHandler.getSourceAtomic;

public class StateNextStimInterpreter extends Filter<SuperState, NextStimulusSpecification> {
    public StateNextStimInterpreter(SuperState input) {
        super(input);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        Set<NextStimulusMap> mappings = new HashSet<>();
        for(Transition t: input.getAllTransitions()){
            Behaviour source = StateBehaviorConverter.getStateBehavior(t.source());
            AtomicBehaviour initial = getSourceAtomic(source);

            mappings.add(new NextStimulusMap(t.input(), initial, t.output()));
        }

        output = new NextStimulusSpecification(mappings);
    }
}
