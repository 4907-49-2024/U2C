package sinks;

import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.behaviors.CompositeBehavior;
import pipes.c2ka.specifications.NextBehaviorSpecification;
import pipes.c2ka.specifications.NextStimulusSpecification;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * A data sink object, final output of a pipeline.
 * Collects data from a few different pipes and provides a method to output this data to a file.
 *
 * @param agentName The name of the agent the specs are written for
 * @param abstractBehaviorSpec The abstract behavior specification of the agent
 * @param nextBehaviorSpec The next behavior specification of the agent
 * @param nextStimulusSpec The next stimulus specification of the agent
 * @param concreteBehaviorSpec The concrete behavior specification of the agent
 */
public record C2KASpecifications(String agentName,
                                 CompositeBehavior abstractBehaviorSpec,
                                 NextBehaviorSpecification nextBehaviorSpec, NextStimulusSpecification nextStimulusSpec,
                                 Set<AtomicBehavior> concreteBehaviorSpec) {
    private static final String OUTPUT_DIR = "Output/"; // Starts at project root
    private static final String FILETYPE_SUFFIX = ".txt";

    /**
     * Canonical constructor, extends next mappings with neutral outcomes when they are missing.
     *
     * @param agentName The name of the agent the specs are written for
     * @param abstractBehaviorSpec The abstract behavior specification of the agent
     * @param nextBehaviorSpec The next behavior specification of the agent
     * @param nextStimulusSpec The next stimulus specification of the agent
     * @param concreteBehaviorSpec The concrete behavior specification of the agent
     */
    public C2KASpecifications {
        // Automatically sets fields. The rest is post-processing.
        nextBehaviorSpec.fillSpecification();
        nextStimulusSpec.fillSpecification();
    }

    /**
     * @return The filename for this C2KA specification
     */
    private String getFilename(){
        return OUTPUT_DIR + agentName + FILETYPE_SUFFIX;
    }

    /**
     * @return The abstract specification formatted in string form
     */
    private String getFormattedAbstractSpec(){
        StringBuilder sb = new StringBuilder();
        sb.append("begin AGENT where");
        sb.append("\n\n\t");

        // Remove containing parentheses (at index 0, and index length)
        String rightSide = abstractBehaviorSpec.toString().substring(1, abstractBehaviorSpec.toString().length() - 1);
        sb.append(agentName);
        sb.append(" := ");
        sb.append(rightSide);

        sb.append("\n\n");
        sb.append("end");

        return sb.toString();
    }

    /**
     * @return The abstract specification formatted in string form
     */
    private String getFormattedConcreteSpec(){
        StringBuilder sb = new StringBuilder();

        sb.append("begin ");
        sb.append("CONCRETE_BEHAVIOR"); // Type erasure in java makes this tricky to generalize without passing info
        sb.append("  where");
        sb.append("\n\n\t");

        // Note: Order is completely random, and no whitespace to separate sections. Hopefully not a problem?
        for(AtomicBehavior specElement: concreteBehaviorSpec.stream().sorted().toList()){
            sb.append(specElement.getConcreteBehavior());
            sb.append("\n\t");
        }

        sb.append("\n");
        sb.append("end");

        return sb.toString();
    }

    /**
     * @return The abstract specification formatted in string form
     */
    private static <T> String getFormattedMapSpec(Set<T> setSpec, String specName){
        StringBuilder sb = new StringBuilder();

        sb.append("begin ");
        sb.append(specName); // Type erasure in java makes this tricky to generalize without passing info
        sb.append("  where");
        sb.append("\n\n\t");

        // Note: Order is completely random, and no whitespace to separate sections. Hopefully not a problem?
        for(T specElement: setSpec.stream().sorted().toList()){
            sb.append(specElement.toString());
            sb.append("\n\t");
        }

        sb.append("\n");
        sb.append("end");

        return sb.toString();
    }


    /**
     * @return The full specification string of this C2KASpecification sink
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(getFormattedAbstractSpec());
        sb.append("\n\n\n");
        sb.append(nextBehaviorSpec);
        sb.append("\n\n\n");
        sb.append(nextStimulusSpec);
        sb.append("\n\n\n");
        sb.append(getFormattedConcreteSpec());

        return sb.toString();
    }

    /**
     * Output formal specifications to an output file.
     * Will output it to Output/{agentName}.txt
     */
    public void outputToFile() {
        try (PrintWriter out = new PrintWriter(getFilename())) {
            out.println(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
