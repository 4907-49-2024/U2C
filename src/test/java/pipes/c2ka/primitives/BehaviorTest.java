package pipes.c2ka.primitives;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for Behavior classes.
 * <p>
 * For composites: tests string representations, and nesting behavior
 * For Atomic: tests string representations, and concrete details
 */
public class BehaviorTest {

    @Test
    public void testAtomicBehavior() {
        // We just want to make sure in = out. It should have no other unexpected behavior.
        // Try using some special chars to see if anything breaks
        AtomicBehavior b = new AtomicBehavior("name_opsdhoipu21370!)@(#*&", "cd_>}|QW{E\">?\".");
        assert b.toString().equals("name_opsdhoipu21370!)@(#*&");
        assert b.getConcreteBehavior().equals("name_opsdhoipu21370!)@(#*& => [ cd_>}|QW{E\">?\". ]");
    }


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

    @Test
    public void testParallelBehavior() {
        ParallelBehavior behavior = new ParallelBehavior();
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
        containedStrings.add("||");
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

        // Nested Behavior
        ChoiceBehavior behaviorNested = new ChoiceBehavior();
        behaviorNested.addBehavior(new AtomicBehavior("F", "_"));
        behavior.addBehavior(behaviorNested);
        containedStrings.add("( F )");
        assert behaviorNested.toString().equals("( F )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }
    }

    @Test
    public void testSequentialBehavior() {
        SequentialBehavior behavior = new SequentialBehavior();

        // Empty
        assert behavior.toString().equals("( )");

        // One
        behavior.addBehavior(new AtomicBehavior("A", "_"));
        assert behavior.toString().equals("( A )");

        // Two
        behavior.addBehavior(new AtomicBehavior("B", "_"));
        assert behavior.toString().equals("( A ; B )");

        // Many
        behavior.addBehavior(new AtomicBehavior("C", "_"));
        behavior.addBehavior(new AtomicBehavior("D", "_"));
        assert behavior.toString().equals("( A ; B ; C ; D )");

        // Nested Behavior
        ChoiceBehavior behaviorNested = new ChoiceBehavior();
        behaviorNested.addBehavior(new AtomicBehavior("F", "_"));
        behavior.addBehavior(behaviorNested);
        assert behavior.toString().equals("( A ; B ; C ; D ; ( F ) )");

        // Nested Sequential
        SequentialBehavior sequentialNested = new SequentialBehavior();
        sequentialNested.addBehavior(new AtomicBehavior("G", "_"));
        sequentialNested.addBehavior(new AtomicBehavior("H", "_"));
        behavior.addBehavior(sequentialNested);
        assert behavior.toString().equals("( A ; B ; C ; D ; ( F ) ; ( G ; H ) )");
    }
}
