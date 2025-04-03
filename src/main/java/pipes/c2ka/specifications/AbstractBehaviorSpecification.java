package pipes.c2ka.specifications;

import pipes.c2ka.behaviours.CompositeBehaviour;


public class AbstractBehaviorSpecification extends Specification {
    private final CompositeBehaviour topLevelBehavior;
    private final String agentName;

    public AbstractBehaviorSpecification(String agentName, CompositeBehaviour topLevelBehavior) {
        this.topLevelBehavior = topLevelBehavior;
        this.agentName = agentName;
    }

    /**
     * @return The name of the agent associated with this AbstractBehaviorSpecification
     */
    public String getAgentName() {
        return agentName;
    }

    @Override
    public String getSpecificationContents() {
        StringBuilder sb = new StringBuilder();

        // Remove containing parentheses (at index 0, and index length)
        String rightSide = topLevelBehavior.toString().substring(1, topLevelBehavior.toString().length() - 1);
        sb.append(agentName);
        sb.append(" := ");
        sb.append(rightSide);

        return sb.toString();
    }

    @Override
    public String getSpecificationName() {
        return "AGENT";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(this.toString());
    }
}
