package pipes.primitives;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class containing collections of primitives
 */
public class Primitives {
    Set<Stimulus> stimuli;

    public Primitives(){
        stimuli = new HashSet<>();
    }

    /**
     * Register a stimuli to the primitives
     *
     * @param s  The stimuli to register
     */
    public void registerStimulus(Stimulus s){
        // TODO: Add safeguards for errors? Define errors
        stimuli.add(s);
    }

    /**
     * Return ordered list of stimuli, based on their time sequence
     *
     * @return stimuli list
     */
    public List<Stimulus> getTimedStimuli(){
        return stimuli.stream().sorted().toList();
    }

    /**
     * Return set of stimuli names, irrespective of time.
     *
     * @return stimuli name list
     */
    public Set<String> getStimuliNames(){
        return stimuli.stream().map(Stimulus::name).collect(Collectors.toSet());
    }
}
