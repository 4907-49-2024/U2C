package pipes.c2ka.behaviours;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test class for Behavior classes.
 * <p>
 * For composites: tests string representations, and nesting behavior
 * For Atomic: tests string representations, and concrete details
 */
public class BehaviourTest {

    @Test
    public void testAtomicBehavior() {
        // We just want to make sure in = out. It should have no other unexpected behavior.
        // Try using some special chars to see if anything breaks
        AtomicBehaviour b = new AtomicBehaviour("name_opsdhoipu21370!)@(#*&", "cd_>}|QW{E\">?\".");
        assert b.toString().equals("name_opsdhoipu21370!)@(#*&");
        assert b.getConcreteBehavior().equals("name_opsdhoipu21370!)@(#*& => [ cd_>}|QW{E\">?\". ]");
    }


    @Test
    public void testChoiceBehavior() {
        ChoiceBehaviour behavior = new ChoiceBehaviour();
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
        behavior.addBehavior(new AtomicBehaviour("A", "_"));
        containedStrings.add("A");
        assert behavior.toString().equals("( A )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Two
        behavior.addBehavior(new AtomicBehaviour("B", "_"));
        containedStrings.add("+");
        containedStrings.add("B");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Many
        behavior.addBehavior(new AtomicBehaviour("C", "_"));
        behavior.addBehavior(new AtomicBehaviour("D", "_"));
        containedStrings.add("C");
        containedStrings.add("D");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Nested Choice
        ChoiceBehaviour behaviorNested = new ChoiceBehaviour();
        behaviorNested.addBehavior(new AtomicBehaviour("F", "_"));
        behavior.addBehavior(behaviorNested);
        containedStrings.add("( F )");
        assert behaviorNested.toString().equals("( F )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }
    }

    @Test
    public void testParallelBehavior() {
        ParallelBehaviour behavior = new ParallelBehaviour();
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
        behavior.addBehavior(new AtomicBehaviour("A", "_"));
        containedStrings.add("A");
        assert behavior.toString().equals("( A )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Two
        behavior.addBehavior(new AtomicBehaviour("B", "_"));
        containedStrings.add("||");
        containedStrings.add("B");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Many
        behavior.addBehavior(new AtomicBehaviour("C", "_"));
        behavior.addBehavior(new AtomicBehaviour("D", "_"));
        containedStrings.add("C");
        containedStrings.add("D");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }

        // Nested Behavior
        ChoiceBehaviour behaviorNested = new ChoiceBehaviour();
        behaviorNested.addBehavior(new AtomicBehaviour("F", "_"));
        behavior.addBehavior(behaviorNested);
        containedStrings.add("( F )");
        assert behaviorNested.toString().equals("( F )");
        for (String string : containedStrings) {
            assert behavior.toString().contains(string);
        }
    }

    @Test
    public void testSequentialBehavior() {
        SequentialBehaviour behavior = new SequentialBehaviour();

        // Empty
        assert behavior.toString().equals("( )");

        // One
        behavior.addBehavior(new AtomicBehaviour("A", "_"));
        assert behavior.toString().equals("( A )");

        // Two
        behavior.addBehavior(new AtomicBehaviour("B", "_"));
        assert behavior.toString().equals("( A ; B )");

        // Many
        behavior.addBehavior(new AtomicBehaviour("C", "_"));
        behavior.addBehavior(new AtomicBehaviour("D", "_"));
        assert behavior.toString().equals("( A ; B ; C ; D )");

        // Nested Behavior
        ChoiceBehaviour behaviorNested = new ChoiceBehaviour();
        behaviorNested.addBehavior(new AtomicBehaviour("F", "_"));
        behavior.addBehavior(behaviorNested);
        assert behavior.toString().equals("( A ; B ; C ; D ; ( F ) )");

        // Nested Sequential
        SequentialBehaviour sequentialNested = new SequentialBehaviour();
        sequentialNested.addBehavior(new AtomicBehaviour("G", "_"));
        sequentialNested.addBehavior(new AtomicBehaviour("H", "_"));
        behavior.addBehavior(sequentialNested);
        assert behavior.toString().equals("( A ; B ; C ; D ; ( F ) ; ( G ; H ) )");
    }
}
