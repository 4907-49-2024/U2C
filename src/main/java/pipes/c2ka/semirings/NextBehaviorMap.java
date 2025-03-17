package pipes.c2ka.semirings;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.behaviors.Behavior;

/**
 * Individual map of a NextBehavior, the full NextBehavior specification is a set of these.
 */
public class NextBehaviorMap extends NextMapSemiring<AtomicBehavior> {
    public NextBehaviorMap(Stimulus inputStim, AtomicBehavior initialBehavior, AtomicBehavior output) {
        super(inputStim, initialBehavior, output);
    }

    /**
     * @param inputStim The initial stimulus for the neutral map
     * @param initialBehavior The initial behavior for the neutral map
     * @return A map with the neutral output for the given output type
     */
    @Override
    public NextBehaviorMap createNeutralMap(Stimulus inputStim, AtomicBehavior initialBehavior) {
        return new NextBehaviorMap(inputStim, initialBehavior, initialBehavior);
    }
}
