package sinks;

import com.sdmetrics.model.ModelElement;
import filters.diagramInterpreters.StateAbstractBehaviorInterpreter;
import filters.diagramInterpreters.StateConcreteBehaviorInterpreter;
import filters.diagramInterpreters.StateNextBehaviorInterpreter;
import filters.diagramInterpreters.StateNextStimInterpreter;
import filters.diagramLinkers.StateDiagramLinker;
import filters.xmiParser.XMIParser;
import org.junit.jupiter.api.Test;
import pipes.UMLModel;
import pipes.XMIParserConfig;
import pipes.diagrams.state.SuperState;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManufacturingCellTest {
    private static final Path projectRoot = Paths.get(System.getProperty("user.dir"));
    private static final Path TEST_DIR = projectRoot.resolve("src/test/java/TestInputs/KnownC2KASystems/ManufacturingCell");
    private static final Path OUTPUT_DIR = projectRoot.resolve("Output");

    /**
     * Define test pipeline
     *
     * @param inputDiagramXMI Reference to input diagram file
     * @return The data sink (Tested output!)
     * @throws Exception In case of thread or input exceptions
     */
    private static C2KASpecifications runTestPipeline(String inputDiagramXMI) throws Exception {
        // Setup Input
        String metaModel = "custom/stateMetaModel.xml";
        String xmiTrans = "custom/xmiStateTrans.xml";
        XMIParserConfig config = new XMIParserConfig(TEST_DIR, inputDiagramXMI, xmiTrans, metaModel);
        // Filter 1
        XMIParser parser = new XMIParser(config);
        UMLModel model = parser.getOutput();
        // Filter 2
        List<ModelElement> stateDiagramsElems = model.getStateDiagrams();
        ModelElement stateDiagramElem = stateDiagramsElems.getFirst(); // Assume single state diagram for test case.
        StateDiagramLinker linker = new StateDiagramLinker(stateDiagramElem);
        SuperState stateDiagram = linker.getOutput();
        // Filter 3s
        StateAbstractBehaviorInterpreter abstractB = new StateAbstractBehaviorInterpreter(stateDiagram);
        StateNextBehaviorInterpreter nextB = new StateNextBehaviorInterpreter(stateDiagram);
        StateNextStimInterpreter nextS = new StateNextStimInterpreter(stateDiagram);
        StateConcreteBehaviorInterpreter concreteB = new StateConcreteBehaviorInterpreter(stateDiagram);
        // Sink - Build then output to file!
        return new C2KASpecifications(stateDiagram.name(), abstractB.getOutput(),
                nextB.getOutput(), nextS.getOutput(), concreteB.getOutput());
    }

//    private void specificationDiff(String umlFileName, String expectedFileName) throws Exception {
//        // Run pipeline and output actual file
//        C2KASpecifications specifications = runTestPipeline(umlFileName);
//        specifications.fillMappingSpecs();
//        specifications.outputToFile();
//
//        // Paths to actual and expected files
//        Path actualFilePath = OUTPUT_DIR.resolve(specifications.agentName() + ".txt");
//        Path expectedFilePath = TEST_DIR.resolve(expectedFileName);
//
//        // Read actual and expected file content
//        String actualContent = Files.readString(actualFilePath).replaceAll("\\r\\n", "\n").trim();
//        String expectedContent = Files.readString(expectedFilePath).replaceAll("\\r\\n", "\n").trim();
//
//        // Perform simple equality check
//        if (actualContent.equals(expectedContent)) {
//            System.out.println("✅ Test passed: " + umlFileName);
//            return;
//        }
//
//        // Detailed line-by-line diff for debugging
//        List<String> actualLines = List.of(actualContent.split("\n"));
//        List<String> expectedLines = List.of(expectedContent.split("\n"));
//
//        StringBuilder diffReport = new StringBuilder();
//        diffReport.append("❌ Differences found in file: ").append(umlFileName).append("\n\n");
//
//        int maxLines = Math.max(actualLines.size(), expectedLines.size());
//        for (int i = 0; i < maxLines; i++) {
//            String actualLine = i < actualLines.size() ? actualLines.get(i).trim() : "<missing>";
//            String expectedLine = i < expectedLines.size() ? expectedLines.get(i).trim() : "<missing>";
//
//            if (!actualLine.equals(expectedLine)) {
//                diffReport.append(String.format("Line %d:\n  Expected: %s\n  Actual:   %s\n\n",
//                        (i + 1), expectedLine, actualLine));
//            }
//        }
//
//        throw new AssertionError(diffReport.toString());
//    }

//    private void specificationDiff(String umlFileName, String expectedFileName) throws Exception {
//        // Run pipeline and output actual file
//        C2KASpecifications specifications = runTestPipeline(umlFileName);
//        specifications.fillMappingSpecs();
//        specifications.outputToFile();
//
//        // Paths to actual and expected files
//        Path actualFilePath = OUTPUT_DIR.resolve(specifications.agentName() + ".txt");
//        Path expectedFilePath = TEST_DIR.resolve(expectedFileName);
//
//        // Read actual and expected file content
//        String actualContent = Files.readString(actualFilePath).replaceAll("\\r\\n", "\n").trim();
//        String expectedContent = Files.readString(expectedFilePath).replaceAll("\\r\\n", "\n").trim();
//
//        // Split by section first
//        String[] actualSections = actualContent.split("begin ");
//        String[] expectedSections = expectedContent.split("begin ");
//
//        StringBuilder diffReport = new StringBuilder();
//        boolean hasDifferences = false;
//
//        // Iterate through each section separately (AGENT, NEXT_BEHAVIOR, NEXT_STIMULI, then CONCRETE_BEHAVIOUR)
//        for (int i = 1; i < expectedSections.length; i++) {
//            String expectedSection = expectedSections[i].trim();
//            String actualSection = (i < actualSections.length) ? actualSections[i].trim() : "";
//
//            String sectionName = expectedSection.substring(0, expectedSection.indexOf("where")).trim();
//
//            if (!compareSection(expectedSection, actualSection, sectionName, diffReport)) {
//                hasDifferences = true;
//            }
//        }
//
//        if (hasDifferences) {
//            throw new AssertionError(diffReport.toString());
//        } else {
//            System.out.println("✅ Test passed: " + umlFileName);
//        }
//    }
//
//    private boolean compareSection(String expected, String actual, String sectionName, StringBuilder diffReport) {
//        if (sectionName.equalsIgnoreCase("CONCRETE_BEHAVIOUR")) {
//            return compareConcreteBehaviorSection(expected, actual, diffReport);
//        } else if (sectionName.equalsIgnoreCase("NEXT_BEHAVIOUR") || sectionName.equalsIgnoreCase("NEXT_STIMULUS")) {
//            return compareNextBehaviorSection(expected, actual, diffReport, sectionName);
//        } else {
//            return compareDefaultSection(expected, actual, diffReport, sectionName);
//        }
//    }
//
//    private boolean compareNextBehaviorSection(String expected, String actual, StringBuilder diffReport, String sectionName) {
//        Set<Set<String>> expectedGroups = parseNextBehaviorGroups(expected);
//        Set<Set<String>> actualGroups = parseNextBehaviorGroups(actual);
//
//        boolean matched = expectedGroups.equals(actualGroups);
//
//        if (!matched) {
//            diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");
//
//            Set<Set<String>> missingGroups = new HashSet<>(expectedGroups);
//            missingGroups.removeAll(actualGroups);
//
//            Set<Set<String>> unexpectedGroups = new HashSet<>(actualGroups);
//            unexpectedGroups.removeAll(expectedGroups);
//
//            if (!missingGroups.isEmpty()) {
//                diffReport.append("Missing groupings:\n");
//                missingGroups.forEach(group -> {
//                    diffReport.append("\n");
//                    group.forEach(line -> diffReport.append("\t").append(line).append("\n"));
//                });
//                diffReport.append("\n");
//            }
//
//            if (!unexpectedGroups.isEmpty()) {
//                diffReport.append("Unexpected groupings:\n");
//                unexpectedGroups.forEach(group -> {
//                    diffReport.append("\n");
//                    group.forEach(line -> diffReport.append("\t").append(line).append("\n"));
//                });
//                diffReport.append("\n");
//            }
//        }
//
//        return matched;
//    }
//
//    private Set<Set<String>> parseNextBehaviorGroups(String content) {
//        Set<Set<String>> groups = new HashSet<>();
//        String[] rawGroups = content.trim().split("\\n\\s*\\n"); // split groups by empty lines
//
//        for (String rawGroup : rawGroups) {
//            Set<String> group = Arrays.stream(rawGroup.trim().split("\\n"))
//                    .map(String::trim)                       // remove leading/trailing whitespace
//                    .filter(line -> !line.isEmpty())
//                    .map(line -> line.replaceAll("\\s*=\\s*", "=")) // normalize spaces around '='
//                    .map(line -> line.replaceAll("\\s*,\\s*", ",")) // normalize spaces around ','
//                    .collect(Collectors.toSet());
//
//            groups.add(group);
//        }
//
//        return groups;
//    }
//
//    private boolean compareDefaultSection(String expected, String actual, StringBuilder diffReport, String sectionName) {
//        List<String> expectedLines = expected.lines().map(String::trim).filter(line -> !line.isBlank()).toList();
//        List<String> actualLines = actual.lines().map(String::trim).filter(line -> !line.isBlank()).toList();
//
////        if (expectedLines.size() != actualLines.size()) {
////            diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");
////            diffReport.append("Line count mismatch. Expected ")
////                    .append(expectedLines.size()).append(" lines but got ")
////                    .append(actualLines.size()).append(" lines.\n\n");
////            return false;
////        }
//
//        Set<String> expectedSet = expectedLines.stream().map(this::normalizeLine).collect(Collectors.toSet());
//        Set<String> actualSet = actualLines.stream().map(this::normalizeLine).collect(Collectors.toSet());
//
//        if (!expectedSet.equals(actualSet)) {
//            Set<String> missing = new HashSet<>(expectedSet);
//            missing.removeAll(actualSet);
//            Set<String> unexpected = new HashSet<>(actualSet);
//            unexpected.removeAll(expectedSet);
//
//            diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");
//
//            if (!missing.isEmpty()) {
//                diffReport.append("Missing lines:\n");
//                missing.forEach(line -> diffReport.append("\t").append(line).append("\n"));
//                diffReport.append("\n");
//            }
//
//            if (!unexpected.isEmpty()) {
//                diffReport.append("Unexpected lines:\n");
//                unexpected.forEach(line -> diffReport.append("\t").append(line).append("\n"));
//                diffReport.append("\n");
//            }
//
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean compareConcreteBehaviorSection(String expected, String actual, StringBuilder diffReport) {
//        // Extract behavior definitions, ignoring headers/footers
//        expected = expected.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();
//        actual = actual.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();
//
//        Map<String, String> expectedBehaviors = parseConcreteBehaviors(expected);
//        Map<String, String> actualBehaviors = parseConcreteBehaviors(actual);
//
//        boolean pass = true;
//
//        for (String state : expectedBehaviors.keySet()) {
//            String expectedBehavior = normalizeConcreteBehavior(expectedBehaviors.get(state));
//            String actualBehavior = actualBehaviors.containsKey(state) ?
//                    normalizeConcreteBehavior(actualBehaviors.get(state)) : "<missing>";
//
//            if (!expectedBehavior.equals(actualBehavior)) {
//                pass = false;
//                diffReport.append(String.format("\n❌ Concrete behavior mismatch for state '%s':\n", state));
//                diffReport.append(String.format("  Expected: %s\n", expectedBehaviors.get(state).trim()));
//                diffReport.append(String.format("  Actual:   %s\n", actualBehaviors.getOrDefault(state, "<missing>").trim()));
//            }
//        }
//
//        for (String unexpectedState : actualBehaviors.keySet()) {
//            if (!expectedBehaviors.containsKey(unexpectedState)) {
//                pass = false;
//                diffReport.append(String.format("\n❌ Unexpected concrete behavior found for state '%s':\n", unexpectedState));
//                diffReport.append(String.format("  Actual: %s\n", actualBehaviors.get(unexpectedState).trim()));
//            }
//        }
//
//        return pass;
//    }
//
//    private Map<String, String> parseConcreteBehaviors(String content) {
//        Map<String, String> behaviorMap = new HashMap<>();
//        Pattern pattern = Pattern.compile("(\\w+)\\s*=>\\s*\\[(.*?)]", Pattern.DOTALL);
//        Matcher matcher = pattern.matcher(content);
//
//        while (matcher.find()) {
//            String stateName = matcher.group(1).trim();
//            String behavior = matcher.group(2).trim();
//            behaviorMap.put(stateName, behavior);
//        }
//
//        return behaviorMap;
//    }
//
//    private String normalizeConcreteBehavior(String behavior) {
//        return behavior
//                .replaceAll("\\s+", " ")        // Replace multiple whitespace chars with single space
//                .replaceAll("\\s?([()\\[\\];:=|&])\\s?", "$1")  // Remove spaces around special symbols
//                .trim();
//    }
//
//    private String normalizeLine(String line) {
//        if (line.contains(":=")) {
//            String[] parts = line.split(":=");
//            String left = parts[0].trim();
//            String right = parts[1].trim();
//
//            // Split and sort the right side if "+" present
//            if (right.contains("+")) {
//                List<String> elements = List.of(right.split("\\+")).stream()
//                        .map(String::trim)
//                        .sorted()
//                        .toList();
//                right = String.join(" + ", elements);
//            }
//
//            return left + " := " + right;
//        } else if (line.contains("=>")) {
//            String[] parts = line.split("=>");
//            String left = parts[0].trim();
//            String right = parts[1].trim();
//
//            return left + " => " + right; // Concrete behaviors assumed deterministic; no further sorting.
//        } else {
//            // Simple lines without special syntax
//            return line.trim();
//        }
//    }

    private void specificationDiff(String umlFileName, String expectedFileName) throws Exception {
        // Run pipeline and output actual file
        C2KASpecifications specifications = runTestPipeline(umlFileName);
        specifications.fillMappingSpecs();
        specifications.outputToFile();

        // Paths to actual and expected files
        Path actualFilePath = OUTPUT_DIR.resolve(specifications.agentName() + ".txt");
        Path expectedFilePath = TEST_DIR.resolve(expectedFileName);

        // Read actual and expected file content
        String actualContent = Files.readString(actualFilePath).replaceAll("\\r\\n", "\n").trim();
        String expectedContent = Files.readString(expectedFilePath).replaceAll("\\r\\n", "\n").trim();

        String[] actualSections = actualContent.split("begin ");
        String[] expectedSections = expectedContent.split("begin ");

        StringBuilder diffReport = new StringBuilder();
        boolean hasDifferences = false;

        for (int i = 1; i < expectedSections.length; i++) {
            String expectedSection = expectedSections[i].trim();
            String actualSection = (i < actualSections.length) ? actualSections[i].trim() : "";

            String sectionName = expectedSection.substring(0, expectedSection.indexOf("where")).trim();

            if (!compareSection(expectedSection, actualSection, sectionName, diffReport)) {
                hasDifferences = true;
            }
        }

        if (hasDifferences) {
            throw new AssertionError(diffReport.toString());
        } else {
            System.out.println("✅ Test passed: " + umlFileName);
        }
    }

    private boolean compareSection(String expected, String actual, String sectionName, StringBuilder diffReport) {
        return switch (sectionName.toUpperCase()) {
            case "CONCRETE_BEHAVIOUR" -> compareConcreteBehaviour(expected, actual, diffReport, sectionName);
            case "NEXT_BEHAVIOUR", "NEXT_STIMULUS" -> compareNextStimuliAndBehavior(expected, actual, diffReport, sectionName);
            default -> compareAgent(expected, actual, diffReport, sectionName);
        };
    }

    private boolean compareNextStimuliAndBehavior(String expected, String actual, StringBuilder diffReport, String sectionName) {
        Set<Set<String>> expectedGroups = parseNextStimuliAndBehaviour(expected);
        Set<Set<String>> actualGroups = parseNextStimuliAndBehaviour(actual);

        if (expectedGroups.equals(actualGroups)) return true;

        Set<Set<String>> missingGroups = new HashSet<>(expectedGroups);
        missingGroups.removeAll(actualGroups);

        Set<Set<String>> unexpectedGroups = new HashSet<>(actualGroups);
        unexpectedGroups.removeAll(expectedGroups);

        diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");

        if (!missingGroups.isEmpty()) {
            diffReport.append("Missing groupings:\n");
            missingGroups.forEach(group -> {
                diffReport.append("\n");
                group.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            });
            diffReport.append("\n");
        }

        if (!unexpectedGroups.isEmpty()) {
            diffReport.append("Unexpected groupings:\n");
            unexpectedGroups.forEach(group -> {
                diffReport.append("\n");
                group.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            });
            diffReport.append("\n");
        }

        return false;
    }

    private Set<Set<String>> parseNextStimuliAndBehaviour(String content) {
        return Arrays.stream(content.trim().split("\\n\\s*\\n"))
                .map(rawGroup -> Arrays.stream(rawGroup.trim().split("\\n"))
                        .map(line -> line.replaceAll("\\s*=\\s*", "=").replaceAll("\\s*,\\s*", ",").trim())
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
    }

    private boolean compareAgent(String expected, String actual, StringBuilder diffReport, String sectionName) {
        Set<String> expectedSet = expected.lines().map(this::normalizeLine).filter(l -> !l.isBlank()).collect(Collectors.toSet());
        Set<String> actualSet = actual.lines().map(this::normalizeLine).filter(l -> !l.isBlank()).collect(Collectors.toSet());

        if (expectedSet.equals(actualSet)) return true;

        Set<String> missing = new HashSet<>(expectedSet);
        missing.removeAll(actualSet);
        Set<String> unexpected = new HashSet<>(actualSet);
        unexpected.removeAll(expectedSet);

        diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");

        if (!missing.isEmpty()) {
            diffReport.append("Missing lines:\n");
            missing.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            diffReport.append("\n");
        }

        if (!unexpected.isEmpty()) {
            diffReport.append("Unexpected lines:\n");
            unexpected.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            diffReport.append("\n");
        }

        return false;
    }

    private boolean compareConcreteBehaviour(String expected, String actual, StringBuilder diffReport, String sectionName) {
        // Extract behavior definitions, ignoring headers/footers
        expected = expected.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();
        actual = actual.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();

        Map<String, String> expectedBehaviors = parseConcreteBehaviour(expected);
        Map<String, String> actualBehaviors = parseConcreteBehaviour(actual);

        boolean pass = true;

        diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n");

        for (String state : expectedBehaviors.keySet()) {
            String expectedBehavior = normalizeConcreteBehavior(expectedBehaviors.get(state));
            String actualBehavior = actualBehaviors.containsKey(state) ?
                    normalizeConcreteBehavior(actualBehaviors.get(state)) : "<missing>";

            if (!expectedBehavior.equals(actualBehavior)) {
                pass = false;
                diffReport.append(String.format("\n❌ Concrete behaviour mismatch for state '%s':\n", state));
                diffReport.append(String.format("  Expected: %s\n", expectedBehaviors.get(state).trim()));
                diffReport.append(String.format("  Actual:   %s\n", actualBehaviors.getOrDefault(state, "<missing>").trim()));
            }
        }

        for (String unexpectedState : actualBehaviors.keySet()) {
            if (!expectedBehaviors.containsKey(unexpectedState)) {
                pass = false;
                diffReport.append(String.format("\n❌ Unexpected concrete behaviour found for state '%s':\n", unexpectedState));
                diffReport.append(String.format("  Actual: %s\n", actualBehaviors.get(unexpectedState).trim()));
            }
        }

        return pass;
    }

    private Map<String, String> parseConcreteBehaviour(String content) {
        Map<String, String> behaviorMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*=>\\s*\\[(.*?)]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String stateName = matcher.group(1).trim();
            String behavior = matcher.group(2).trim();
            behaviorMap.put(stateName, behavior);
        }

        return behaviorMap;
    }

    private String normalizeConcreteBehavior(String behavior) {
        return behavior
                .replaceAll("\\s+", " ")        // Replace multiple whitespace chars with single space
                .replaceAll("\\s?([()\\[\\];:=|&])\\s?", "$1")  // Remove spaces around special symbols
                .trim();
    }

    private String normalizeLine(String line) {
        line = line.trim();
        if (line.contains(":=")) {
            String[] parts = line.split(":=");
            List<String> elements = Arrays.stream(parts[1].split("\\+")).map(String::trim).sorted().toList();
            return parts[0].trim() + " := " + String.join(" + ", elements);
        }
        return line;
    }

    @Test
    void testControlAgent() throws Exception {
        specificationDiff("Control Agent.uml", "C.txt");
    }

    @Test
    void testHandlingAgent() throws Exception {
        specificationDiff("Handling Agent.uml", "H.txt");
    }
    @Test
    void testProcessingAgent() throws Exception {
        specificationDiff("Processing Agent.uml", "P.txt");
    }

    @Test
    void testStorageAgent() throws Exception {
        specificationDiff("Storage Agent.uml", "S.txt");
    }
}
