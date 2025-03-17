package pipes.c2ka.specifications;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviors.AtomicBehavior;
import pipes.c2ka.semirings.NextMapSemiring;

import java.util.HashSet;
import java.util.Set;

/**
 * Specifications parent class for NextMappings. Takes in sets of NextMap elements to make a specification out of them.
 * @param <mapType> The NextMapping type, nextBehavior or nextStimulus
 */
public abstract class NextMappingSpecification<mapType extends NextMapSemiring<?>> implements Specification {
    private final Set<mapType> mappings;

    protected NextMappingSpecification(Set<mapType> mappings) {
        this.mappings = mappings;
    }

    /**
     * Gets all the behaviours for the agent.
     * Assumption: All states (behaviors) have at least one outgoing transition (including reflective ones).
     *
     * @return Set of behaviors of the agent for this specification
     */
    protected Set<AtomicBehavior> getAgentBehaviours() {
        Set<AtomicBehavior> agentBehaviours = new HashSet<>();
        // Add atomic behaviors found in the mapping
        for (mapType m : mappings) {
            agentBehaviours.add(m.getInitialBehavior());
        }

        return agentBehaviours;
    }

    /**
     * Fill specification with all missing "neutral mappings".
     */
    public void fillSpecification(){
        // Skip filling if there are 0 mappings.
        if(mappings.isEmpty())
            return;
        // Limitation of java with static inheritance, need to cheat it by getting an instance
        mapType map = mappings.iterator().next();

        Set<AtomicBehavior> agentBehaviors = getAgentBehaviours();
        Set<Stimulus> systemStimuli = Stimulus.getSystemStimuli();

        for (AtomicBehavior b : agentBehaviors) {
            for (Stimulus s : systemStimuli) {
                if(keyNotInSpec(s, b)){
                    // This should work
                    this.mappings.add((mapType) map.createNeutralMap(s, b));
                }
            }
        }
    }
    /**
     * @return True if the given key pair does not have a matching map in this specification yet
     */
    private boolean keyNotInSpec(Stimulus s, AtomicBehavior b) {
        return false; // FIXME: Implement this
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("begin ");
        sb.append(getSpecificationName()); // Type erasure in java makes this tricky to generalize without passing info
        sb.append("  where");
        sb.append("\n\n\t");

        // Note: Order is completely random, and no whitespace to separate sections. Hopefully not a problem?
        for(mapType mapping: mappings.stream().sorted().toList()){
            sb.append(mapping.toString());
            sb.append("\n\t");
        }

        sb.append("\n");
        sb.append("end");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
