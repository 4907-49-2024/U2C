package pipes;

import com.sdmetrics.model.MetaModelElement;
import com.sdmetrics.model.Model;
import com.sdmetrics.model.ModelElement;

import java.util.List;

/**
 * The UMLModel pipe serves as a "front controller" to the Model type defined by SDMetrics.
 */
public class UMLModel {
    private final Model model;

    public UMLModel(Model model) {
        this.model = model;
    }


    /**
     * Get elements from the model matching the given type.
     * Pre-reduces "invalid" elements this includes:
     *  - Elements with no names.
     *
     * @param typeName The type of elements to get from the model.
     */
    private List<ModelElement> getTypedElements(String typeName){
        // Store given element type for parsing
        MetaModelElement type = model.getMetaModel().getType(typeName);

        return model.getAcceptedElements(type);
    }
}
