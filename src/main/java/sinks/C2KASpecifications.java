package sinks;

import pipes.c2ka.specifications.AbstractBehaviorSpecification;
import pipes.c2ka.specifications.ConcreteBehaviorSpecification;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.c2ka.specifications.NextStimulusSpecification;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A data sink object, final output of a pipeline.
 * Collects data from a few different pipes and provides a method to output this data to a file.
 *
 * @param abstractBehaviorSpec The abstract behavior specification of the agent
 * @param nextBehaviorSpec The next behavior specification of the agent
 * @param nextStimulusSpec The next stimulus specification of the agent
 * @param concreteBehaviorSpec The concrete behavior specification of the agent
 */
public record C2KASpecifications(AbstractBehaviorSpecification abstractBehaviorSpec,
                                 NextBehaviorSpecification nextBehaviorSpec, NextStimulusSpecification nextStimulusSpec,
                                 ConcreteBehaviorSpecification concreteBehaviorSpec) {
    private static final String OUTPUT_DIR = "Output/"; // Starts at project root
    private static final String FILETYPE_SUFFIX = ".txt";

    /**
     * @return The filename for this C2KA specification
     */
    public Path getFilepath(){
        return Paths.get(OUTPUT_DIR + abstractBehaviorSpec.getAgentName() + FILETYPE_SUFFIX);
    }

    /**
     *  Fills the mapping type specification with their missing neutral outcomes.
     *  Precondition: System has been fully analyzed, no stimuli are missing.
     *  (Should be called once before any format of output processing)
     */
    public void fillMappingSpecs(){
        nextBehaviorSpec.fillSpecification();
        nextStimulusSpec.fillSpecification();
    }

    /**
     * @return The full specification string of this C2KASpecification sink
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(abstractBehaviorSpec);
        sb.append("\n\n\n");
        sb.append(nextBehaviorSpec);
        sb.append("\n\n\n");
        sb.append(nextStimulusSpec);
        sb.append("\n\n\n");
        sb.append(concreteBehaviorSpec);

        return sb.toString();
    }

    /**
     * Output formal specifications to an output file.
     * Will output it to Output/{agentName}.txt
     * Precondition: "fillMappingSpecs" has been called before this, respecting its own preconditions
     */
    public void outputToFile() {
        try (PrintWriter out = new PrintWriter(getFilepath().toFile())) {
            out.println(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
