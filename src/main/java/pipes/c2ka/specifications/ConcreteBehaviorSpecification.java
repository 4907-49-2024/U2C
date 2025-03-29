package pipes.c2ka.specifications;

import pipes.c2ka.behaviors.AtomicBehavior;

import java.util.HashSet;
import java.util.Set;

public class ConcreteBehaviorSpecification extends Specification {
    private final Set<AtomicBehavior> atomicBehaviors;

    public ConcreteBehaviorSpecification() {
        this.atomicBehaviors = new HashSet<>();
    }

    /**
     * Add an atomic behavior to this concrete behavior specification
     * @param atomicBehavior The atomic behavior to add to the spec.
     */
    public void add(AtomicBehavior atomicBehavior) {
        atomicBehaviors.add(atomicBehavior);
    }

    @Override
    protected String getSpecificationContents() {
        StringBuilder sb = new StringBuilder();

        // Note: Order is alphabetically sorted, and no whitespace to separate sections.
        for(AtomicBehavior specElement: atomicBehaviors.stream().sorted().toList()){
            sb.append(specElement.getConcreteBehavior());
            sb.append("\n\t");
        }

        return sb.toString();
    }

    @Override
    protected String getSpecificationName() {
        return "CONCRETE_BEHAVIOUR";
    }
}
