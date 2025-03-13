package pipes.diagrams.state;

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
 * @param output The transition output.
 */
public record Transition (State source, State target, String input, String output) implements StateDiagramElement {
    /**
     * It's intended at the moment to use this constructor.
     * Parses the name assuming standard transition notation: input[guard]/output
     *  - Ignore whitespace.
     *  - Uses [] and / to identify guard and output
     */
    public Transition(State source, State target, String name){
        this(source, target, parseInput(name), parseOutput(name));
    }

    /**
     * Parse the transition name to get the input from it, if it exists.
     * <br>
     * Valid Name formats:
     * Initial State Transition: ""
     * Normal Transition: input/output
     * Sequential Transition (one stimulus for both): both
     *
     * @return Input stimuli from a given name String
     */
    private static String parseInput(String name){
        // Check for cases components
        if (name.contains("/")) // Normal
            return name.substring(0, name.indexOf('/')).trim();
        return name; // Initial and sequential
    }

    /**
     * Parse the transition name to get the output from it, if it exists.
     * <br>
     * Valid Name formats:
     * Initial State Transition: ""
     * Normal Transition: input/output
     * Sequential Transition: both
     *
     * @return Output from a given name String
     */
    private static String parseOutput(String name){
        // Check for cases components
        if (name.contains("/")) // Normal
            return name.substring(name.indexOf('/')+1).trim();
        return name; // Initial and sequential
    }

    /**
     * Sequential transitions defined as stimuli where the inputs != output.
     * @param transition The transition to check for the sequential composition property
     * @return True if the transition is sequential, false otherwise.
     */
    public static boolean isSequentialTransition(Transition transition){
        return transition.input.equals(transition.output);
    }
}
