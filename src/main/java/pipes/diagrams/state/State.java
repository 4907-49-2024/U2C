package pipes.diagrams.state;

/**
 * Representation of a state in a state diagram
 *
 * @param name The name of the state - Note: not unique (children of guarded clause superstates inherit the name!)
 * @param kind The kind of the pseudo-state, N/A if not a pseudo-state (For Papyrus, Empty = Initial)
 * @param doActivity The doActivity, if set. Null otherwise.
 */
public record State(String name, String kind, String doActivity) implements StateDiagramElement {
    /**
     * Converts null objects to empty strings, otherwise stores as is.
     */
    public State(String name, String kind, String doActivity){
        this.name = name == null ? "" : name;
        this.kind = kind == null ? "" : kind;
        this.doActivity = doActivity == null ? "" : doActivity;
    }
}
