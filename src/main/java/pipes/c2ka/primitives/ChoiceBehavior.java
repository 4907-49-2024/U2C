package pipes.c2ka.primitives;

import java.util.HashSet;

/**
 * Representation of a choice composite of C2KA behaviors
 */
public class ChoiceBehavior extends CompositeBehavior {
    public ChoiceBehavior() {
        super(new HashSet<>()); // Choice order does not matter
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Every composite encapsulated in ()
        sb.append("(");
        sb.append(" ");
        for(Behavior behavior : behaviors) {
            sb.append(behavior.toString()); // Recursively build inner behaviors
            sb.append(" ");
            sb.append(getCompositeOperator());
            sb.append(" ");
        }
        int opIndex = sb.lastIndexOf(getCompositeOperator());
        sb.setLength(opIndex > 0 ? opIndex : sb.length()); // Remove last operator occurrence
        sb.append(")");

        return sb.toString();
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    @Override
    protected String getCompositeOperator() {
        return "+";
    }
}
