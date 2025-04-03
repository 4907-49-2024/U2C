package pipes.c2ka.behaviours;

import java.util.HashSet;

/**
 * Representation of a parallel composite of C2KA behaviors
 */
public class ParallelBehaviour extends CompositeBehaviour {
    public ParallelBehaviour() {
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
