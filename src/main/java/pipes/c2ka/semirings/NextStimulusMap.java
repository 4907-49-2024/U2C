package pipes.c2ka.semirings;

import pipes.c2ka.behaviors.Behavior;

/**
 * Individual map of a NextStimulus, the full NextStimulus specification is a set of these.
 */
public class NextStimulusMap extends NextMapSemiring<String> {
    public NextStimulusMap(String inputStim, Behavior initialBehavior, String output) {
        super(inputStim, initialBehavior, output);
    }
}
