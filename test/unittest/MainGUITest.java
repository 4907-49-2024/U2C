package unittest;

import main.MainGUI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;

class MainGUITest {

    @Test
    void testValidXMIFile() throws IOException {
        // Create a temporary valid XMI file
        File validFile = File.createTempFile("valid", ".xmi");
        Files.writeString(validFile.toPath(), "<XMI xmi:version=\"2.1\"></XMI>");

        Assertions.assertTrue(MainGUI.isValidXMIFile(validFile));

        // Cleanup
        validFile.deleteOnExit();
    }

    @Test
    void testInvalidExtension() throws IOException {
        // Create a file with an invalid extension
        File invalidFile = File.createTempFile("invalid", ".txt");
        Files.writeString(invalidFile.toPath(), "<XMI xmi:version=\"2.1\"></XMI>");

        Assertions.assertFalse(MainGUI.isValidXMIFile(invalidFile));

        // Cleanup
        invalidFile.deleteOnExit();
    }

    @Test
    void testInvalidContent() throws IOException {
        // Create a temporary file with invalid content
        File invalidFile = File.createTempFile("invalidContent", ".xmi");
        Files.writeString(invalidFile.toPath(), "<Invalid></Invalid>");

        Assertions.assertFalse(MainGUI.isValidXMIFile(invalidFile));

        // Cleanup
        invalidFile.deleteOnExit();
    }

    @Test
    void testMissingFile() {
        // Test with a non-existent file
        File missingFile = new File("nonexistent.xmi");
        Assertions.assertFalse(MainGUI.isValidXMIFile(missingFile));
    }
}