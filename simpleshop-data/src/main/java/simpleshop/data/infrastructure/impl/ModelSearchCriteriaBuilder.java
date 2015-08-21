package simpleshop.data.infrastructure.impl;

import org.hibernate.Session;
import simpleshop.data.infrastructure.CriteriaBuilder;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.metadata.ModelMetadata;

/**
 * An object that contains
 */
public class ModelSearchCriteriaBuilder extends AbstractCriterionContext implements CriteriaBuilder, CriterionContext {

    /**
     * This is always the root context.
     * @return 0 for root context.
     */
    @Override
    public int getNestedLevel() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public ModelSearchCriteriaBuilder(ModelMetadata searchMetadata, ModelMetadata modelMetadata, Session session){
        super(searchMetadata, modelMetadata, session);
    }

}
