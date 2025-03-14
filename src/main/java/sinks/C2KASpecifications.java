package sinks;

import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.behaviors.CompositeBehavior;
import pipes.c2ka.semirings.NextBehaviorMap;
import pipes.c2ka.semirings.NextStimulusMap;

import java.util.Set;

/**
 * A data sink object, final output of a pipeline.
 * Collects data from a few different pipes and provides a method to output this data to a file.
 *
 * @param agentName The name of the agent the specs are written for
 * @param AbstractBehaviorSpec The abstract behavior specification of the agent
 * @param nextBehaviorSpec The next behavior specification of the agent
 * @param nextStimulusSpec The next stimulus specification of the agent
 * @param ConcreteBehaviorSpec The concrete behavior specification of the agent
 */
public record C2KASpecifications(String agentName,
                             CompositeBehavior AbstractBehaviorSpec,
                             Set<NextBehaviorMap> nextBehaviorSpec, Set<NextStimulusMap> nextStimulusSpec,
                             Set<AtomicBehavior> ConcreteBehaviorSpec) {

    /**
     * Output formal specifications to an output file.
     * Will output it to Output/{agentName}.txt
     */
    public void outputToFile(){
        // TODO: build a string incrementally then dump it into the file
    }
}
