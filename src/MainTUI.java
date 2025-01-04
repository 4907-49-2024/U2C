public class MainTUI {
    public static void main(String[] args) {
        XMIParser parser = new XMIParser(new XMIParserConfig("project1.xmi"));
        String model = "";
        // Parse
        try {
            model = parser.parseAndReturnModel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Print model (with separators)
        System.out.println("--------------------------------------------------");
        System.out.println(model);
        System.out.println("--------------------------------------------------");
    }
}
