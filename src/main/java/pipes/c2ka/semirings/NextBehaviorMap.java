package pipes.c2ka.semirings;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviours.AtomicBehaviour;

/**
 * Individual map of a NextBehavior, the full NextBehavior specification is a set of these.
 */
public class NextBehaviorMap extends NextMapSemiring<AtomicBehaviour> {
    public NextBehaviorMap(Stimulus inputStim, AtomicBehaviour initialBehavior, AtomicBehaviour output) {
        super(inputStim, initialBehavior, output);
    }

    // Kept for legacy parity
    public NextBehaviorMap(String  inputStim, AtomicBehaviour initialBehavior, AtomicBehaviour output) {
        this(Stimulus.createStimulus(inputStim), initialBehavior, output);
    }

    /**
     * @param inputStim The initial stimulus for the neutral map
     * @param initialBehavior The initial behavior for the neutral map
     * @return A map with the neutral output for the given output type
     */
    @Override
    public NextBehaviorMap createNeutralMap(Stimulus inputStim, AtomicBehaviour initialBehavior) {
        return new NextBehaviorMap(inputStim, initialBehavior, initialBehavior);
    }
}
