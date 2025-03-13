//import filters.xmiParser.XMIParser;
//import pipes.XMIParserConfig;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//
//import static source.InputSanitation.isValidXMIFile;
//
//public class MainGUI {
//    private static JTextArea outputArea; // Text area to display output
//
//    public static void main(String[] args) {
//        // Create the main application window
//        JFrame frame = new JFrame("XMI Parser");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 400);
//        frame.setLayout(new BorderLayout());
//
//        // Create a panel with a button
//        JPanel panel = new JPanel();
//        JButton selectFileButton = new JButton("Select XMI File");
//        panel.add(selectFileButton);
//
//        // Create a text area for displaying output
//        outputArea = new JTextArea();
//        outputArea.setEditable(false); // Make it read-only
//        JScrollPane scrollPane = new JScrollPane(outputArea);
//
//        // Add components to the frame
//        frame.add(panel, BorderLayout.NORTH);
//        frame.add(scrollPane, BorderLayout.CENTER);
//
//        // Add action listener to the button
//        selectFileButton.addActionListener(e -> selectAndParseFile());
//
//        // Make the frame visible
//        frame.setVisible(true);
//    }
//
//
//
//    /**
//     * Opens the file chooser, validates the file, and parses it.
//     */
//    public static void selectAndParseFile() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Select an XMI File");
//
//        // Show the file chooser dialog
//        int userSelection = fileChooser.showOpenDialog(null);
//        if (userSelection == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            String xmiFilePath = selectedFile.getName();
//
//            // Validate the selected file
//            if (!isValidXMIFile(selectedFile)) {
//                JOptionPane.showMessageDialog(null, "The selected file is not a valid XMI file.", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Parse the file and display the results
//            try {
//                XMIParser parser = new XMIParser(new XMIParserConfig(xmiFilePath));
//                String result = parser.getModel().toString();
//                outputArea.setText(result); // Display the result in the text area
//                JOptionPane.showMessageDialog(null, "Parsing completed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(null, "Error during parsing: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace();
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "No file was selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//}