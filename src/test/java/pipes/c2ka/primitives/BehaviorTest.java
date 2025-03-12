package pipeTests.c2ka.primitives;

import org.junit.jupiter.api.Test;
import pipes.c2ka.primitives.AtomicBehavior;
import pipes.c2ka.primitives.ChoiceBehavior;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for Behavior classes.
 * <p>
 * For composites: tests string representations, and nesting behavior
 * For Atomic: TODO
 */
public class BehaviorTest {
    @Test
    public void testChoiceBehavior() {
        ChoiceBehavior behavior = new ChoiceBehavior();
        // Due to set collection, the order of the string may be non-deterministic. Cannot check equality always
        Set<String> containedStrings = new HashSet<>();

        // Empty
        containedStrings.add(")");
        containedStrings.add("(");
        assert behavior.toString().equals("( )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // One
        behavior.addBehavior(new AtomicBehavior("A", "_"));
        containedStrings.add("A");
        assert behavior.toString().equals("( A )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Two
        behavior.addBehavior(new AtomicBehavior("B", "_"));
        containedStrings.add("+");
        containedStrings.add("B");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Many
        behavior.addBehavior(new AtomicBehavior("C", "_"));
        behavior.addBehavior(new AtomicBehavior("D", "_"));
        containedStrings.add("C");
        containedStrings.add("D");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Nested Choice
        ChoiceBehavior behaviorNested = new ChoiceBehavior();
        behaviorNested.addBehavior(new AtomicBehavior("F", "_"));
        behavior.addBehavior(behaviorNested);
        containedStrings.add("( F )");
        assert behaviorNested.toString().equals("( F )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }
    }
}
