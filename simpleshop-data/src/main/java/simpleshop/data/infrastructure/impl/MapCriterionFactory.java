package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.*;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;

import java.util.Map;

/**
 * Created criterion on a map property.
 */
public abstract class MapCriterionFactory implements CriterionFactory{

    private ModelSearchCriteriaBuilder criteriaBuilder;

    public MapCriterionFactory(ModelSearchCriteriaBuilder criteriaBuilder){
        this.criteriaBuilder = criteriaBuilder;
    }

    @Override
    public Criterion createCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {

        if(!Map.class.isAssignableFrom(targetType))
            throw new SpongeConfigurationException("Operator is only applicable to Map properties.");

        Pair<ModelMetadata, PropertyMetadata> pair = criteriaBuilder.getMetadata(qualifiedPropertyName);
        PropertyMetadata targetPropertyMetadata = pair.getValue();
        if(!isOperatorValidForMapKeyValueType(targetPropertyMetadata.getReturnTypeMetadata())){
            throw new SpongeConfigurationException("Operator is not applicable to the map key type or the map value type.");
        }

        ModelMetadata targetModelMetadata = pair.getKey();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), "sub1");
        String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
        detachedCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), alias + "." + targetModelMetadata.getIdPropertyName()));
        DetachedCriteria subPropertyCriteria = detachedCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), "sub1prop");
        detachedCriteria.setProjection(Property.forName("sub1prop.elements"));
        //
        subPropertyCriteria.add(createEntryCriterion(value));

        Criterion criterion = Subqueries.exists(detachedCriteria);
        if(negate)
            criterion = Restrictions.not(criterion);
        return criterion;
    }

    /**
     * If the operator can be applied to the map entries.
     * @param collectionModelMetadata metadata for the map of K, T.
     * @return true if is valid.
     */
    protected boolean isOperatorValidForMapKeyValueType(ModelMetadata collectionModelMetadata){
        return true;
    }

    /**
     * Create the criterion for the map entry.
     * @param value value.
     * @return restriction on map entry.
     */
    protected abstract Criterion createEntryCriterion(Object value);


}
