package filters.diagramInterpreters;

import filters.Filter;
import filters.diagramInterpreters.utils.StateBehaviorConverter;
import pipes.c2ka.behaviors.Behavior;
import pipes.c2ka.semirings.NextStimulusMap;
import pipes.diagrams.state.SuperState;
import pipes.diagrams.state.Transition;

import java.util.Set;

public class StateNextStimInterpreter extends Filter<SuperState, Set<NextStimulusMap>> {
    protected StateNextStimInterpreter(SuperState input) {
        super(input);
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        for(Transition t: input.getAllTransitions()){
            Behavior initial = StateBehaviorConverter.getStateBehavior(t.source());
            output.add(new NextStimulusMap(initial, t.input(), t.output()));
        }
    }
}
