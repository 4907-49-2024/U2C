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
}
