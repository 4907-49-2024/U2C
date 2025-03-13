package pipes.diagrams.state;

/**
 * Interface representing state objects in a state diagram.
 */
public interface State extends StateDiagramElement {
    /**
     * @return Key used to uniquely identify state.
     */
    String getKey();
}
