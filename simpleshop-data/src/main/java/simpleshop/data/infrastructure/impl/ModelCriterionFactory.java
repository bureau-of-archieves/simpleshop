package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.*;
import simpleshop.common.Pair;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.ModelType;
import simpleshop.data.metadata.PropertyMetadata;
import simpleshop.data.util.DomainUtils;


/**
 * Criterion factory for properties whose value is a domain model.
 */
public abstract class ModelCriterionFactory implements CriterionFactory {

    @Override
    public Criterion createCriterion(CriterionContext criterionContext, String alias, String propertyName, Class<?> targetType, Object value, boolean negate) {

        String valueModelName = targetType.getSimpleName();
        ModelMetadata valueModelMetadata = DomainUtils.getModelMetadata(valueModelName);
        if(valueModelMetadata == null || valueModelMetadata.getType() != ModelType.DOMAIN)
            throw new SpongeConfigurationException("Operator is only applicable to properties whose value is a domain object.");

        if(!(value instanceof PageInfo))
            throw new SpongeConfigurationException("Operator is only applicable to Search Object parameter.");
        PageInfo searchObject = (PageInfo)value;

        Pair<ModelMetadata, PropertyMetadata> pair = criterionContext.getMetadata(alias, propertyName);
        ModelMetadata targetModelMetadata = pair.getKey();
        assert pair.getValue().getReturnTypeMetadata() == valueModelMetadata;
        int subQueryLevel = criterionContext.getNestedLevel() + 1;
        String levelLeadAlias = CriterionContext.getLevelAlias(SUB_QUERY_LEAD_ALIAS, subQueryLevel);
        DetachedCriteria subQueryLeadCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), levelLeadAlias);
        String levelAlias = criterionContext.getLevelAlias(alias);
        subQueryLeadCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), levelAlias + "." + targetModelMetadata.getIdPropertyName()));
        subQueryLeadCriteria.setProjection(Property.forName(targetModelMetadata.getIdPropertyName()));

        String levelRootAlias = CriterionContext.getLevelAlias(AliasDeclaration.ROOT_CRITERIA_ALIAS, subQueryLevel);
        DetachedCriteria subQueryRootCriteria = subQueryLeadCriteria.createCriteria(propertyName, levelRootAlias);
        ModelMetadata valueSearchModelMetadata = DomainUtils.getModelMetadata(searchObject.getClass().getSimpleName());
        NestedCriterionContext nestedCriterionContext = new NestedCriterionContext(valueSearchModelMetadata, valueModelMetadata, criterionContext);
        nestedCriterionContext.addCriteria(subQueryRootCriteria);
        Criterion subQueryCriterion = createModelValueCriterion(nestedCriterionContext, targetModelMetadata, targetType, searchObject);

        if(subQueryCriterion != null){
            subQueryRootCriteria.add(subQueryCriterion);
        }

        Criterion criterion = Subqueries.exists(subQueryLeadCriteria);
        if(negate)
            criterion = Restrictions.not(criterion);
        return criterion;
    }

    protected abstract Criterion createModelValueCriterion(NestedCriterionContext nestedCriterionContext, ModelMetadata targetModelMetadata, Class<?> targetType, PageInfo searchObject);
}
