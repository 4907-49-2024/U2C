package pipes.c2ka.specifications;

import pipes.c2ka.semirings.NextStimulusMap;

import java.util.Set;

public class NextStimulusSpecification extends NextMappingSpecification<NextStimulusMap> {
    public NextStimulusSpecification(Set<NextStimulusMap> mappings) {
        super(mappings);
    }

    @Override
    public String getSpecificationName() {
        return "NEXT_STIMULUS";
    }
}
