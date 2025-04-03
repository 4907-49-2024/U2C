package pipes.c2ka.specifications;

/**
 * Specifications represent collections of some C2KA atomic elements (Stimuli, behaviors, semi-ring maps).
 * <br>
 * toString representations of them format them to the appropriate IIAT format.
 */
public abstract class Specification {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("begin ");
        sb.append(getSpecificationName()); // Type erasure in java makes this tricky to generalize without passing info
        sb.append(" where");
        sb.append("\n\n\t");

        sb.append(getSpecificationContents());

        sb.append("\n");
        sb.append("end");

        return sb.toString();
    }

    /**
     * @return The name of the specification according to IIAT, in all capital letters.
     */
    abstract protected String getSpecificationName();

    /**
     * @return The contents of the specification, may be multiple lines long.
     */
    abstract protected String getSpecificationContents();
}
