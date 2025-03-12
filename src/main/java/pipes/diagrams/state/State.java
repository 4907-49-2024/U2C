package pipes.diagrams.state;

/**
 * Representation of a state in a state diagram
 *
 * @param name The name of the state - UNIQUE, can be used safely as a key
 * @param kind The kind of the pseudo-state, "state" if normal state (For Papyrus, Empty = Initial)
 * @param doActivity The doActivity, if set. Null otherwise.
 * @param parent The superstate containing this state, if applicable. Note: if set, name should be null
 */
public record State(String name, String kind, String doActivity, State parent) implements StateDiagramElement {
    /**
     * Converts null objects to empty strings, otherwise stores as is.
     */
    public State(String name, String kind, String doActivity, State parent) {
        this.name = name == null ? "" : name;
        this.kind = kind == null ? "" : kind;
        this.doActivity = doActivity == null ? "" : doActivity;
        this.parent = parent;
    }

    public static boolean isInitialState(State state) {
        return state != null && state.kind.isEmpty();
    }
}
