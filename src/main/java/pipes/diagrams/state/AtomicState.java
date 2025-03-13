package pipes.diagrams.state;

/**
 * Representation of an atomic state in a state diagram (no children, not a super state)
 *
 * @param name The name of the state - UNIQUE, can be used safely as a key
 * @param kind The kind of the pseudo-state, "state" if normal state (For Papyrus, Empty = Initial)
 * @param doActivity The doActivity, if set. Null otherwise.
 */
public record AtomicState(String name, String kind, String doActivity) implements State {
    /**
     * Converts null objects to empty strings, otherwise stores as is.
     */
    public AtomicState(String name, String kind, String doActivity) {
        this.name = name == null ? "" : name;
        this.kind = kind == null ? "" : kind;
        this.doActivity = doActivity == null ? "" : doActivity;
    }

    public static boolean isInitialState(AtomicState state) {
        return state != null && state.kind.isEmpty();
    }

    /**
     * @return Key used to uniquely identify state.
     */
    @Override
    public String getKey() {
        return name;
    }
}
