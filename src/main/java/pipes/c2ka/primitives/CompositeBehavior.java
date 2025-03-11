package pipes.c2ka.primitives;

import java.util.Collection;

/**
 * Representation of a composite of C2KA behaviors
 */
public abstract class CompositeBehavior implements Behavior {
    protected final Collection<Behavior> behaviors;

    protected CompositeBehavior(Collection<Behavior> behaviors) {
        this.behaviors = behaviors;
    }

    public void addBehavior(Behavior behavior) {
        behaviors.add(behavior);
    }

    /**
     * @return The operator tied to this composite, as a String
     */
    protected abstract String getCompositeOperator();
}
