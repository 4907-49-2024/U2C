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
        return "("+inputStim+","+initialBehavior+") = "+nextStimulus;
    }

    @Override
    public boolean equals(Object o) {
        // Same instance
        if (this == o) return true;
        // Null check, class match
        if (o == null || getClass() != o.getClass()) return false;
        return toString().equals(o.toString()); // Equivalent toString means functionally equivalent records.
    }

    @Override
    public int hashCode() {
        return toString().hashCode(); // Based on equals mechanism
    }
}
