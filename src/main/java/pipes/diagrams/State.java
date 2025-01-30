package pipes.diagrams;

/**
 * Representation of a state in a state diagram
 *
 * @param name The name of the state
 * @param kind The kind of the pseudo-state, N/A if not a pseudo-state (For Papyrus, Empty = Initial)
 * @param doActivity The doActivity, if set. Null otherwise.
 */
public record State(String name, String kind, String doActivity) {}
