package xmiParser.UMLMappings;

public class PapyrusMapping implements UMLMapping {
    @Override
    public String getTypeStimulus() {
        return "message";
    }

    @Override
    public String getTypeAgent() {
        return "lifeline";
    }

    @Override
    public String getTypeComment() { //not used
        return "comment";
    }
}
