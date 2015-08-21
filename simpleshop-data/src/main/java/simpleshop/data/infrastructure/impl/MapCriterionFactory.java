package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.*;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyMetadata;

import java.util.Map;

/**
 * Created criterion on a map property.
 */
public abstract class MapCriterionFactory implements CriterionFactory {


    @Override
    public Criterion createCriterion(CriterionContext criterionContext, String alias, String propertyName, Class<?> targetType, Object value, boolean negate) {

        if(!Map.class.isAssignableFrom(targetType))
            throw new SpongeConfigurationException("Operator is only applicable to Map properties.");

        Pair<ModelMetadata, PropertyMetadata> pair = criterionContext.getMetadata(alias, propertyName);
        ModelMetadata targetModelMetadata = pair.getKey();
        PropertyMetadata targetPropertyMetadata = pair.getValue();
        if(!isOperatorValidForMapKeyValueType(targetPropertyMetadata.getReturnTypeMetadata())){
            throw new SpongeConfigurationException("Operator is not applicable to the map key type or the map value type.");
        }

        int subQueryLevel = criterionContext.getNestedLevel() + 1;
        String levelLeadAlias = CriterionContext.getLevelAlias(SUB_QUERY_LEAD_ALIAS, subQueryLevel);
        DetachedCriteria subQueryLeadCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), levelLeadAlias);
        String levelAlias = criterionContext.getLevelAlias(alias);
        subQueryLeadCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), levelAlias + "." + targetModelMetadata.getIdPropertyName()));

        //alias structure: lead -> root -> others
        String levelRootAlias = CriterionContext.getLevelAlias(AliasDeclaration.ROOT_CRITERIA_ALIAS, subQueryLevel);
        DetachedCriteria subQueryRootCriteria = subQueryLeadCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), levelRootAlias);
        subQueryLeadCriteria.setProjection(Property.forName(levelRootAlias + ".elements"));
        subQueryRootCriteria.add(createEntryCriterion(value));

        Criterion criterion = Subqueries.exists(subQueryLeadCriteria);
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
