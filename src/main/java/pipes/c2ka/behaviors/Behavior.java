package pipes.c2ka.behaviors;

import pipes.c2ka.semirings.NextBehaviorMap;

/**
 * Representation of a C2KA Behavior
 */
public abstract class Behavior implements Comparable<Behavior>{
    @Override
    public int compareTo(Behavior o) {
        return this.toString().compareTo(o.toString());
    }
}
