package pipes.diagrams.state;

import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.ModelElement;

/**
 * The relevant MetaModel data types for state diagram models.
 * May be somewhat dependent on the modelling tool, based on Papyrus.
 */
public enum StateType {
    statemachine,
    state,
    transition,
    activity,
    region,
    unsupported;

    public static StateType getType(ModelElement modelElement) {
        return switch (modelElement.getType().getName()) {
            case "statemachine" -> StateType.statemachine;
            case "state" -> StateType.state;
            case "transition" -> StateType.transition;
            case "activity" -> StateType.activity;
            case "region" -> StateType.region;
            default -> StateType.unsupported;
        };
    }
}
