package pipes.c2ka.behaviours;
/**
 * Representation of an atomic C2KA Behavior
 */
public class AtomicBehaviour extends Behaviour {
    private final String name;
    private final String concreteDetails;

    public AtomicBehaviour(String name, String concreteDetails) {
        this.name = name;
        this.concreteDetails = concreteDetails;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Get concrete behavior representation of this atomic behavior
     * Format: {name} => [ {concrete-details} ]
     * @return concrete behavior representation of this atomic behavior
     */
    public String getConcreteBehavior() {
        return name + " => [ " + concreteDetails + " ]";
    }
}
