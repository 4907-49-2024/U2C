package pipes.c2ka.behaviours;

import java.util.HashSet;

/**
 * Representation of a choice composite of C2KA behaviors
 */
public class ChoiceBehavior extends CompositeBehavior {
    public ChoiceBehavior() {
        super(new HashSet<>()); // Choice order does not matter
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    @Override
    protected String getCompositeOperator() {
        return "+";
    }
}
