package pipes.c2ka.behaviors;

import java.util.Collection;

/**
 * Representation of a composite of C2KA behaviors
 */
public abstract class CompositeBehavior extends Behavior {
    protected final Collection<Behavior> behaviors;

    protected CompositeBehavior(Collection<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
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
    protected abstract String getCompositeOperator();
}
