package simpleshop.data.infrastructure;

import org.hibernate.Criteria;
import simpleshop.data.metadata.ModelMetadata;

/**
 * An object that contains
 */
public class ModelSearchCriteriaBuilder {

    private ModelMetadata searchMetadata;
    private ModelMetadata modelMetadata;

    public ModelSearchCriteriaBuilder(ModelMetadata searchMetadata, ModelMetadata modelMetadata){
        this.searchMetadata = searchMetadata;
        this.modelMetadata = modelMetadata;
    }

    public Criteria getCriteria(String alias){
         throw new RuntimeException();
    }




}
