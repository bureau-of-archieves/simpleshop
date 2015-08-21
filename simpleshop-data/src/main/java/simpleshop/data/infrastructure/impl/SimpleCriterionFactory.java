package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.Criterion;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;

/**
 * Created simple criterion objects.
 */
public abstract class SimpleCriterionFactory implements CriterionFactory {

    protected String getQualifiedProperty(CriterionContext criterionContext, String alias, String property){
        String levelAlias = criterionContext.getLevelAlias(alias);
        return levelAlias + "." + property;
    }

    @Override
    public Criterion createCriterion(CriterionContext criterionContext, String alias, String propertyName, Class<?> targetType, Object value, boolean negate) {
        String qualifiedPropertyName = getQualifiedProperty(criterionContext, alias, propertyName);
        return createSimpleCriterion(qualifiedPropertyName, targetType, value, negate);
    }

    protected abstract Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate);
}
