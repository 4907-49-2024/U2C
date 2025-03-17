package pipes.c2ka.semirings;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviors.Behavior;

/**
 * Individual map of a NextBehavior, the full NextBehavior specification is a set of these.
 */
public class NextBehaviorMap extends NextMapSemiring<Behavior> {
    public NextBehaviorMap(String inputStim, Behavior initialBehavior, Behavior output) {
        super(new Stimulus(inputStim), initialBehavior, output);
    }
}
