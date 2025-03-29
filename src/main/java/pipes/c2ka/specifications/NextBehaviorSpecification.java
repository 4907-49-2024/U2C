package pipes.c2ka.specifications;

import pipes.c2ka.semirings.NextBehaviorMap;

import java.util.Set;

public class NextBehaviorSpecification extends NextMappingSpecification<NextBehaviorMap> {
    public NextBehaviorSpecification(Set<NextBehaviorMap> mappings) {
        super(mappings);
    }

    @Override
    protected String getSpecificationName() {
        return "NEXT_BEHAVIOUR";
    }
}
