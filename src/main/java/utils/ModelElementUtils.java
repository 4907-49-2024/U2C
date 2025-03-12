package utils;

import com.sdmetrics.model.ModelElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Collection of utility functions related to model elements.
 * Since this is a type from an external library, we cannot fix some issues at the root cause,
 * instead use common wrapper utility functions to address them.
 */
public class ModelElementUtils {
    /**
     * Get owned elements of given element, with a null check included (returns empty)
     * @param element The model element to get the owned elements of.
     * @return Empty collection if the owned elements are null, the owned element collection otherwise.
     */
    public static Collection<ModelElement> getOwnedElements(ModelElement element){
        Collection<ModelElement> elements = element.getOwnedElements();
        return Objects.requireNonNullElse(elements, new ArrayList<>()); // Empty collection if null
    }
}
