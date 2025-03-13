package filters;

import pipes.diagrams.state.StateDiagram;

/**
 * Interface to label all filters in our system.
 * - InT: Input type to the filter
 * - OutT: Output type of the filter
 * Provides a method to get the filter output, which automatically executes the filter if the output has not been computed before.
 */
public abstract class Filter<InT, OutT> implements Runnable {
    // Used in run method in some way
    protected final InT input;
    protected OutT output;

    protected Filter(InT input) {
        this.input = input;
    }

    public OutT getOutput() throws InterruptedException {
        if(output == null){
            // Run thread to calculate filter output, if it's not done yet
            Thread t = new Thread(this);
            t.start();
            t.join();
        }
        return output;
    }
}
