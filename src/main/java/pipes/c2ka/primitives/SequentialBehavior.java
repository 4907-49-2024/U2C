package pipes.c2ka.primitives;

import java.util.ArrayList;

/**
 * Representation of a sequential composite of C2KA behaviors
 */
public class SequentialBehavior extends CompositeBehavior {
    public SequentialBehavior() {
        super(new ArrayList<>()); // Choice order does not matter
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    @Override
    protected String getCompositeOperator() {
        return ";";
    }
}
