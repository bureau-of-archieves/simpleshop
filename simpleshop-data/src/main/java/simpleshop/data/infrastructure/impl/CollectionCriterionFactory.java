package simpleshop.data.infrastructure.impl;

import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;

import java.util.Collection;

/**
 * Create criterion for collection properties.
 */
public abstract class CollectionCriterionFactory implements CriterionFactory {

    private ModelSearchCriteriaBuilder criteriaBuilder;

    public CollectionCriterionFactory(ModelSearchCriteriaBuilder criteriaBuilder){
        this.criteriaBuilder = criteriaBuilder;
    }

    @Override
    public Criterion createCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {

        if(!Collection.class.isAssignableFrom(targetType)){
            throw new SpongeConfigurationException(String.format("Operator does not apply to non-collection property '%s'", qualifiedPropertyName));
        }

        Session session = criteriaBuilder.getSession();
        Pair<ModelMetadata, PropertyMetadata> pair = criteriaBuilder.getMetadata(qualifiedPropertyName);

        ModelMetadata targetModelMetadata = pair.getKey();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), "sub1");
        String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
        detachedCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), alias + "." + targetModelMetadata.getIdPropertyName()));
        detachedCriteria.setProjection(Property.forName(targetModelMetadata.getIdPropertyName()));

        PropertyMetadata targetPropertyMetadata = pair.getValue();
        DetachedCriteria subPropertyCriteria = detachedCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), "sub1prop");
        String collectionRoleName = targetModelMetadata.getModelClass().getName() + "." + targetPropertyMetadata.getPropertyName();
        Class<?> elementType = session.getSessionFactory().getCollectionMetadata(collectionRoleName).getElementType().getReturnedClass();
        ClassMetadata elementMetadata = session.getSessionFactory().getClassMetadata(elementType);
        Criterion elementCriterion = createElementCriterion(elementType, elementMetadata, value);
        if(elementCriterion == null){ // === false
            elementCriterion = Restrictions.neProperty("sub1." + targetModelMetadata.getIdPropertyName(), "sub1." + targetModelMetadata.getIdPropertyName());
        }
        subPropertyCriteria.add(elementCriterion);
        Criterion criterion = Subqueries.exists(detachedCriteria);
        if(negate)
            criterion = Restrictions.not(criterion);
        return criterion;
    }

    protected abstract Criterion createElementCriterion(Class<?> elementType, ClassMetadata elementMetadata, Object value);

}
