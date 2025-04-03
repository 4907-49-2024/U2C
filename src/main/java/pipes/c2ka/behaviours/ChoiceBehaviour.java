package pipes.c2ka.behaviours;

import java.util.HashSet;

/**
 * Representation of a choice composite of C2KA behaviors
 */
public class ChoiceBehaviour extends CompositeBehaviour {
    public ChoiceBehaviour() {
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
