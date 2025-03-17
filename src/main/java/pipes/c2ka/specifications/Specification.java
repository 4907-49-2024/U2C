package pipes.c2ka.specifications;

/**
 * Specifications represent collections of some C2KA atomic elements (Stimuli, behaviors, semi-ring maps).
 * <br>
 * toString representations of them format them to the appropriate IIAT format.
 */
public interface Specification {
    String getSpecificationName();
}
