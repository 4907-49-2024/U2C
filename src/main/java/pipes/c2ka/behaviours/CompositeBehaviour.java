package pipes.c2ka.behaviours;

import java.util.Collection;

/**
 * Representation of a composite of C2KA behaviors
 */
public abstract class CompositeBehaviour extends Behaviour {
    protected final Collection<Behaviour> behaviours;

    protected CompositeBehaviour(Collection<Behaviour> behaviours) {
        this.behaviours = behaviours;
    }

    public void addBehavior(Behaviour behaviour) {
        behaviours.add(behaviour);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Every composite encapsulated in ()
        sb.append("(");
        sb.append(" ");
        for(Behaviour behaviour : behaviours) {
            sb.append(behaviour.toString()); // Recursively build inner behaviors
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
