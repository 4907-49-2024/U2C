package pipes.diagrams;

/**
 * The transition record representation for state diagram transitions.
 * <br>
 * For our C2KA Parser:
 * All transitions have inputs, outputs except initial state transitions.
 * Some transitions have guards, to indicate a choice in concrete behaviours.
 *
 * @param source The source state for the transition.
 * @param target The target state for the transition.
 * @param input The transition trigger. Note: Initial state transitions have no triggers!
 * @param guard The transition guard, if it exists.
 * @param output The transition output.
 */
public record Transition (State source, State target, String input, String guard, String output) implements StateDiagramElement {
    /**
     * It's intended at the moment to use this constructor.
     * Parses the name assuming standard transition notation: input[guard]/output
     *  - Ignore whitespace.
     *  - Uses [] and / to identify guard and output
     */
    public Transition(State source, State target, String name){
        this(source, target, parseInput(name), parseGuard(name), parseOutput(name));
    }

    /**
     * Parse the transition name to get the input from it, if it exists.
     * <br>
     * Valid Name formats:
     * Initial State Transition: ""
     * Normal Transition: input/output
     * Guarded Transition: input[guard]/output
     *
     * @return Input stimuli from a given name String
     */
    private static String parseInput(String name){
        // Check for cases components
        if(name.contains("[")) // Guarded
            return name.substring(0, name.indexOf('[')).trim(); // Up to and excluding guard
        else if (name.contains("/")) // Normal
            return name.substring(0, name.indexOf('/')).trim();
        return ""; // Initial
    }

    /**
     * Parse the transition name to get the guard from it, if it exists.
     * <br>
     * Valid Name formats:
     * Initial State Transition: ""
     * Normal Transition: input/output
     * Guarded Transition: input[guard]/output
     *
     * @return Guard from a given name String
     */
    private static String parseGuard(String name){
        // Check for cases components
        if(name.contains("[")) // Guarded
            return name.substring(name.indexOf('[')+1, name.indexOf(']')).trim(); // Everything within []
        return ""; // Others
    }

    /**
     * Parse the transition name to get the output from it, if it exists.
     * <br>
     * Valid Name formats:
     * Initial State Transition: ""
     * Normal Transition: input/output
     * Guarded Transition: input[guard]/output
     *
     * @return Guard from a given name String
     */
    private static String parseOutput(String name){
        // Check for cases components
        if (name.contains("/")) // Normal or Guarded
            return name.substring(name.indexOf('/')+1).trim(); // Everything after /
        return ""; // Initial
    }
}
