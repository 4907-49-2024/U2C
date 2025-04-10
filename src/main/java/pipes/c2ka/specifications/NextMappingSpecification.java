package pipes.c2ka.specifications;

import pipes.c2ka.Stimulus;
import pipes.c2ka.behaviours.AtomicBehaviour;
import pipes.c2ka.semirings.NextMapSemiring;

import java.util.HashSet;
import java.util.Set;

/**
 * Specifications parent class for NextMappings. Takes in sets of NextMap elements to make a specification out of them.
 * @param <mapType> The NextMapping type, nextBehavior or nextStimulus
 */
public abstract class NextMappingSpecification<mapType extends NextMapSemiring<?>> extends Specification {
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
    protected Set<AtomicBehaviour> getAgentBehaviours() {
        Set<AtomicBehaviour> agentBehaviours = new HashSet<>();
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

        Set<AtomicBehaviour> agentBehaviors = getAgentBehaviours();
        Set<Stimulus> systemStimuli = Stimulus.getSystemStimuli();

        for (AtomicBehaviour b : agentBehaviors) {
            for (Stimulus s : systemStimuli) {
                if(!keyInSpec(s, b)){
                    // Casting should work
                    this.mappings.add((mapType) map.createNeutralMap(s, b));
                }
            }
        }
    }

    /**
     * @return True if the given key pair does not have a matching map in this specification yet
     */
    private boolean keyInSpec(Stimulus s, AtomicBehaviour b) {
        return mappings.stream().anyMatch(m -> m.isKeyEqual(s, b));
    }


    /**
     * @return The contents of the specification, may be multiple lines long.
     */
    @Override
    protected String getSpecificationContents() {
        StringBuilder sb = new StringBuilder();
        String lineSeparator = "\n\t";

        Stimulus lastStim = null;
        // Note: Order is completely random, and no whitespace to separate sections. Hopefully not a problem?
        for(mapType mapping: mappings.stream().sorted().toList()){
            if (lastStim == null || !lastStim.equals(mapping.getInputStim())) {
                lastStim = mapping.getInputStim();
                sb.append(lineSeparator);
            }
            sb.append(mapping);
            sb.append(lineSeparator);
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
