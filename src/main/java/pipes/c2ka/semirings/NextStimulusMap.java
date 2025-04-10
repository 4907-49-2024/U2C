package pipes.c2ka.semirings;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviours.AtomicBehaviour;

/**
 * Individual map of a NextStimulus, the full NextStimulus specification is a set of these.
 */
public class NextStimulusMap extends NextMapSemiring<Stimulus> {
    public NextStimulusMap(Stimulus inputStim, AtomicBehaviour initialBehavior, Stimulus output) {
        super(inputStim, initialBehavior, output);
    }

    // Kept for legacy parity
    public NextStimulusMap(String inputStim, AtomicBehaviour initialBehavior, String output) {
        this(Stimulus.createStimulus(inputStim), initialBehavior, Stimulus.createStimulus(output));
    }

    /**
     * @param inputStim The initial stimulus for the neutral map
     * @param initialBehavior The initial behavior for the neutral map
     * @return A map with the neutral output for the given output type
     */
    @Override
    public NextStimulusMap createNeutralMap(Stimulus inputStim, AtomicBehaviour initialBehavior) {
        return new NextStimulusMap(inputStim, initialBehavior, Stimulus.NEUTRAL_STIMULUS);
    }
}
