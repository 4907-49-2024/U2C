package pipes.c2ka.specifications;

import pipes.c2ka.behaviours.AtomicBehaviour;

import java.util.HashSet;
import java.util.Set;

public class ConcreteBehaviorSpecification extends Specification {
    private final Set<AtomicBehaviour> atomicBehaviours;

    public ConcreteBehaviorSpecification() {
        this.atomicBehaviours = new HashSet<>();
    }

    /**
     * Add an atomic behavior to this concrete behavior specification
     * @param atomicBehaviour The atomic behavior to add to the spec.
     */
    public void add(AtomicBehaviour atomicBehaviour) {
        atomicBehaviours.add(atomicBehaviour);
    }

    @Override
    protected String getSpecificationContents() {
        StringBuilder sb = new StringBuilder();

        // Note: Order is alphabetically sorted, and no whitespace to separate sections.
        for(AtomicBehaviour specElement: atomicBehaviours.stream().sorted().toList()){
            sb.append(specElement.getConcreteBehavior());
            sb.append("\n\t");
        }

        return sb.toString();
    }

    @Override
    protected String getSpecificationName() {
        return "CONCRETE_BEHAVIOUR";
    }

    @Override
    public boolean equals(Object o){
        return o.toString().equals(this.toString());
    }
}
