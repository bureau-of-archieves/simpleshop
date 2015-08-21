package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.DetachedCriteria;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.metadata.ModelMetadata;

/**
 * Criteria context at a nested level.
 */
public class NestedCriterionContext extends AbstractCriterionContext {

    private int nestedLevel;

    @Override
    public int getNestedLevel() {
        return nestedLevel;
    }

    /**
     * Create a new instance.
     */
    public NestedCriterionContext(ModelMetadata searchMetadata, ModelMetadata modelMetadata, CriterionContext parentContext){
        super(searchMetadata, modelMetadata, parentContext.getSession());
        nestedLevel = parentContext.getNestedLevel() + 1;
    }

    public void addCriteria(DetachedCriteria criteria){
        this.criteriaMap.put(criteria.getAlias(), criteria);
    }

}
