package inputInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class InputSanitation {
    /**
     * Validates if the selected file is a valid XMI file.
     */
    public static boolean isValidXMIFile(File file) {
        // Check if the file exists and has the correct extension
        if (!file.exists() || (!file.getName().endsWith(".xmi") && !file.getName().endsWith(".xml") && !file.getName().endsWith(".uml"))) {
            return false;
        }

        // Check the file content for XMI-specific elements
        try {
            String content = Files.readString(file.toPath());
            return content.contains("xmi:") || content.contains("<XMI");
        } catch (IOException e) {
            System.err.println("Error reading file content: " + e.getMessage());
            return false;
        }
    }
}
