package pipes.c2ka.specifications;

import pipes.c2ka.semirings.NextMapSemiring;

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
