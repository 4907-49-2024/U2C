package sinks;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpecificationDiffTool {
    public static void specificationDiff(Path test_dir, C2KASpecifications specifications) throws Exception {
        // Paths to actual and expected files
        Path actualFilePath = specifications.getFilepath();
        Path expectedFilePath = test_dir.resolve(specifications.agentName()+".txt");

        // Read actual and expected file content
        String actualContent = Files.readString(actualFilePath).replaceAll("\\r\\n", "\n").trim();
        String expectedContent = Files.readString(expectedFilePath).replaceAll("\\r\\n", "\n").trim();

        String[] actualSections = actualContent.split("begin ");
        String[] expectedSections = expectedContent.split("begin ");

        StringBuilder diffReport = new StringBuilder();
        diffReport.append("Testing agent: ");
        diffReport.append(specifications.agentName());
        diffReport.append("\n---------------------------\n");
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
            System.out.println("✅ Test passed: " + specifications.agentName());
        }
    }

    private static boolean compareSection(String expected, String actual, String sectionName, StringBuilder diffReport) {
        return switch (sectionName.toUpperCase()) {
            case "CONCRETE_BEHAVIOUR" -> compareConcreteBehaviour(expected, actual, diffReport, sectionName);
            case "NEXT_BEHAVIOUR", "NEXT_STIMULUS" -> compareNextStimuliAndBehavior(expected, actual, diffReport, sectionName);
            default -> compareAgent(expected, actual, diffReport, sectionName);
        };
    }

    private static boolean compareNextStimuliAndBehavior(String expected, String actual, StringBuilder diffReport, String sectionName) {
        Set<String> expectedLines = parseNormalizedLines(expected);
        Set<String> actualLines = parseNormalizedLines(actual);

        Set<String> missingLines = new HashSet<>(expectedLines);
        missingLines.removeAll(actualLines);

        Set<String> unexpectedLines = new HashSet<>(actualLines);
        unexpectedLines.removeAll(expectedLines);

        if (missingLines.isEmpty() && unexpectedLines.isEmpty()) {
            return true;
        }

        diffReport.append("❌ Differences found in section: ").append(sectionName).append("\n\n");

        if (!missingLines.isEmpty()) {
            diffReport.append("Missing lines:\n");
            missingLines.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            diffReport.append("\n");
        }

        if (!unexpectedLines.isEmpty()) {
            diffReport.append("Unexpected lines:\n");
            unexpectedLines.forEach(line -> diffReport.append("\t").append(line).append("\n"));
            diffReport.append("\n");
        }

        return false;
    }

    private static Set<String> parseNormalizedLines(String content) {
        return Arrays.stream(content.trim().split("\n"))
                .map(line -> line.replaceAll("\\s+", "")) // Remove all spacing
                .filter(line -> !line.isEmpty() && !line.toLowerCase().startsWith("begin") && !line.toLowerCase().startsWith("end"))
                .collect(Collectors.toSet());
    }

//    private static boolean compareNextStimuliAndBehavior(String expected, String actual, StringBuilder diffReport, String sectionName) {
//        Set<String> expectedLines = expected.lines()
//                .map(String::trim)
//                .filter(line -> !line.isBlank() && !line.startsWith("begin") && !line.startsWith("end"))
//                .map(line -> line.replaceAll("\s=\s", "=").replaceAll("\s,\s", ","))
//                .collect(Collectors.toSet());
//
//        Set<String> actualLines = actual.lines()
//                .map(String::trim)
//                .filter(line -> !line.isBlank() && !line.startsWith("begin") && !line.startsWith("end"))
//                .map(line -> line.replaceAll("\s=\s", "=").replaceAll("\s,\s", ","))
//                .collect(Collectors.toSet());
//
//        Set<String> missingLines = actualLines.stream()
//                .filter(line -> !expectedLines.contains(line))
//                .collect(Collectors.toSet());
//
//        if (!missingLines.isEmpty()) {
//            diffReport.append(" Differences found in section: ").append(sectionName).append("\n\n");
//            diffReport.append("The following lines were found in actual output but missing from expected:\n");
//            missingLines.forEach(line -> diffReport.append("\t").append(line).append("\n"));
//            diffReport.append("\n");
//            return false;
//        }
//
//        return true;
//    }

    private static Set<Set<String>> parseNextStimuliAndBehaviour(String content) {
        return Arrays.stream(content.trim().split("\\n\\s*\\n"))
                .map(rawGroup -> Arrays.stream(rawGroup.trim().split("\\n"))
                        .map(line -> line.replaceAll("\\s*=\\s*", "=").replaceAll("\\s*,\\s*", ",").trim())
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
    }

    private static boolean compareAgent(String expected, String actual, StringBuilder diffReport, String sectionName) {
        Set<String> expectedSet = expected.lines().map(SpecificationDiffTool::normalizeLine).filter(l -> !l.isBlank()).collect(Collectors.toSet());
        Set<String> actualSet = actual.lines().map(SpecificationDiffTool::normalizeLine).filter(l -> !l.isBlank()).collect(Collectors.toSet());

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

    private static boolean compareConcreteBehaviour(String expected, String actual, StringBuilder diffReport, String sectionName) {
        // Extract behavior definitions, ignoring headers/footers
        expected = expected.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();
        actual = actual.replaceFirst("(?s)^.*?where", "").replaceFirst("(?s)end\\s*$", "").trim();

        Map<String, String> expectedBehaviors = parseConcreteBehaviour(expected);
        Map<String, String> actualBehaviors = parseConcreteBehaviour(actual);

        boolean pass = true;
        // Temporarily build diff report for concrete, discard if no mismatch
        StringBuilder concreteDiffReport = new StringBuilder();
        concreteDiffReport.append("❌ Differences found in section: ").append(sectionName).append("\n");

        for (String state : expectedBehaviors.keySet()) {
            String expectedBehavior = normalizeConcreteBehavior(expectedBehaviors.get(state));
            String actualBehavior = actualBehaviors.containsKey(state) ?
                    normalizeConcreteBehavior(actualBehaviors.get(state)) : "<missing>";

            if (!expectedBehavior.equals(actualBehavior)) {
                pass = false;
                concreteDiffReport.append(String.format("\n❌ Concrete behaviour mismatch for state '%s':\n", state));
                concreteDiffReport.append(String.format("  Expected: %s\n", expectedBehaviors.get(state).trim()));
                concreteDiffReport.append(String.format("  Actual:   %s\n", actualBehaviors.getOrDefault(state, "<missing>").trim()));
            }
        }

        for (String unexpectedState : actualBehaviors.keySet()) {
            if (!expectedBehaviors.containsKey(unexpectedState)) {
                pass = false;
                concreteDiffReport.append(String.format("\n❌ Unexpected concrete behaviour found for state '%s':\n", unexpectedState));
                concreteDiffReport.append(String.format("  Actual: %s\n", actualBehaviors.get(unexpectedState).trim()));
            }
        }

        if (!pass) {
            diffReport.append(concreteDiffReport);
        }

        return pass;
    }

    private static Map<String, String> parseConcreteBehaviour(String content) {
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

    private static String normalizeConcreteBehavior(String behavior) {
        return behavior
                .replaceAll("\\s+", " ")        // Replace multiple whitespace chars with single space
                .replaceAll("\\s?([()\\[\\];:=|&])\\s?", "$1")  // Remove spaces around special symbols
                .trim();
    }

    private static String normalizeLine(String line) {
        line = line.trim();
        if (line.contains(":=")) {
            String[] parts = line.split(":=");
            List<String> elements = Arrays.stream(parts[1].split("\\+")).map(String::trim).sorted().toList();
            return parts[0].trim() + " := " + String.join(" + ", elements);
        }
        return line;
    }

}
