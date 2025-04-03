package pipes.c2ka.behaviours;

/**
 * Representation of a C2KA Behavior
 */
public abstract class Behaviour implements Comparable<Behaviour>{
    @Override
    public int compareTo(Behaviour o) {
        return this.toString().compareTo(o.toString());
    }
}
