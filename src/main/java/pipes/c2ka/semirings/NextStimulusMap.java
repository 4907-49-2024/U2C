package pipes.c2ka.semirings;

import pipes.c2ka.behaviors.Behavior;

/**
 * Individual map of a NextStimulus, the full NextStimulus specification is a set of these.
 *
 * @param initialBehavior The behavior before the stimulus is received.
 * @param inputStim The stimulus received by the agent which causes a behavior change.
 * @param nextStimulus The stimulus sent out before the behavior changes.
 */
public record NextStimulusMap(Behavior initialBehavior, String inputStim, String nextStimulus) {
    @Override
    public String toString() {
        return "("+initialBehavior+","+inputStim+") = "+nextStimulus;
    }
}
