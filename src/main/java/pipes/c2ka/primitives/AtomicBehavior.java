package pipes.c2ka.primitives;
/**
 * Representation of an atomic C2KA Behavior
 */
public class AtomicBehavior implements Behavior {
    private final String name;
    private final String concreteDetails;

    public AtomicBehavior(String name, String concreteDetails) {
        this.name = name;
        this.concreteDetails = concreteDetails;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Get concrete details of this atomic behavior
     * @return concrete details of this atomic behavior
     */
    public String getDetails() {
        return concreteDetails;
    }
}
