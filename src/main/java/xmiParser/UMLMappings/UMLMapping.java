package xmiParser.UMLMappings;

/**
 * Returns a "constant" defined by the modelling tool we use.
 * This is done as a way to essentially have an extensible enum, if we want to try swapping modelling tools for parsing later.
 */
public interface UMLMapping {
    // TODO: fix these to be static, might save a little bit of memory?
    // These can be static methods in theory, but it makes passing the class around complicated.
    String getTypeStimulus();
    String getTypeAgent();
    String getTypeComment(); //not used
}
