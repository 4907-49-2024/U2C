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
    unsupported;

    public static StateType getType(ModelElement modelElement) {
        return switch (modelElement.getType().getName()) {
            case "statemachine" -> StateType.statemachine;
            case "state" -> StateType.state;
            case "transition" -> StateType.transition;
            case "activity" -> StateType.activity;
            default -> StateType.unsupported;
        };
    }
}
