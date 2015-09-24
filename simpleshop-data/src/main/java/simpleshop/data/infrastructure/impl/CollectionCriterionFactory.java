package simpleshop.data.infrastructure.impl;

import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;
import simpleshop.data.util.DomainUtils;

import java.util.Collection;

/**
 * Create criterion for collection properties.
 */
public abstract class CollectionCriterionFactory implements CriterionFactory {

    @Override
    public Criterion createCriterion(CriterionContext criterionContext, String alias, String propertyName, Class<?> targetType, Object value, boolean negate) {

        if(!Collection.class.isAssignableFrom(targetType)){
            throw new SpongeConfigurationException(String.format("Operator does not apply to non-collection property '%s'", propertyName));
        }

        //load metadata
        Session session = criterionContext.getSession();
        Pair<ModelMetadata, PropertyMetadata> pair = criterionContext.getMetadata(alias, propertyName);
        ModelMetadata targetModelMetadata = pair.getKey();
        PropertyMetadata targetPropertyMetadata = pair.getValue();
        String collectionRoleName = targetModelMetadata.getModelClass().getName() + "." + targetPropertyMetadata.getPropertyName();
        CollectionMetadata collectionMetadata = session.getSessionFactory().getCollectionMetadata(collectionRoleName);
        Class<?> elementType = collectionMetadata.getElementType().getReturnedClass();
        ClassMetadata elementMetadata = session.getSessionFactory().getClassMetadata(elementType);

        int subQueryLevel = criterionContext.getNestedLevel() + 1;
        String levelLeadAlias = CriterionContext.getLevelAlias(SUB_QUERY_LEAD_ALIAS, subQueryLevel);
        DetachedCriteria subQueryLeadCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), levelLeadAlias);
        String levelAlias = criterionContext.getLevelAlias(alias);
        subQueryLeadCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), levelAlias + "." + targetModelMetadata.getIdPropertyName()));
        subQueryLeadCriteria.setProjection(Property.forName(targetModelMetadata.getIdPropertyName()));

        String levelRootAlias = CriterionContext.getLevelAlias(AliasDeclaration.ROOT_CRITERIA_ALIAS, subQueryLevel);
        DetachedCriteria subQueryRootCriteria = subQueryLeadCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), levelRootAlias);

        Criterion subQueryCriterion;
        if(elementMetadata == null){ //collection element is not domain model
            subQueryCriterion = createSimpleElementCriterion(elementType, value);

        } else {
            String entityName = elementMetadata.getEntityName();
            String modelName = StringUtils.subStrAfterLast(entityName, ".");
            ModelMetadata elementModelMetadata = DomainUtils.getModelMetadata(modelName);
            ModelMetadata elementSearchModelMetadata = DomainUtils.getModelMetadata(modelName + "Search");
            NestedCriterionContext nestedCriterionContext = new NestedCriterionContext(elementSearchModelMetadata, elementModelMetadata, criterionContext);
            nestedCriterionContext.addCriteria(subQueryRootCriteria);
            subQueryCriterion = createModelElementCriterion(nestedCriterionContext, elementMetadata, elementType, value);
        }
        if(subQueryCriterion != null){
            subQueryRootCriteria.add(subQueryCriterion);
        }

        Criterion criterion = Subqueries.exists(subQueryLeadCriteria);
        if(negate)
            criterion = Restrictions.not(criterion);
        return criterion;
    }

    protected abstract Criterion createSimpleElementCriterion(Class<?> elementType, Object value);

    protected abstract Criterion createModelElementCriterion(CriterionContext nestedCriterionContext, ClassMetadata elementMetadata, Class<?> elementType,  Object value);

}
