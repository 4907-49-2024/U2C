package pipes.c2ka.primitives;

import java.util.HashSet;

/**
 * Representation of a parallel composite of C2KA behaviors
 */
public class ParallelBehavior extends CompositeBehavior {
    public ParallelBehavior() {
        super(new HashSet<>()); // Choice order does not matter
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    @Override
    protected String getCompositeOperator() {
        return "||";
    }
}
