package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import simpleshop.Constants;
import simpleshop.common.Pair;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.metadata.PropertyMetadata;
import java.util.*;

/**
 * Abstract criterion context.
 */
public abstract class AbstractCriterionContext implements CriterionContext {

    //region CriterionContext implementation

    protected final ModelMetadata searchMetadata;
    protected final ModelMetadata modelMetadata;
    protected final Session session;

    /**
     * A map of all created criteria. The key is the alias.
     */
    protected final Map<String, DetachedCriteria> criteriaMap;

    /**
     * Create an instance.
     * @param searchMetadata search model metadata.
     * @param modelMetadata return model metadata.
     * @param session the session.
     */
    protected AbstractCriterionContext(ModelMetadata searchMetadata, ModelMetadata modelMetadata, Session session){
        this.searchMetadata = searchMetadata;
        this.modelMetadata = modelMetadata;
        this.session = session;
        criteriaMap = new HashMap<>();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public String getLevelAlias(String alias){

        return CriterionContext.getLevelAlias(alias, getNestedLevel());
    }

    @Override
    public Pair<ModelMetadata, PropertyMetadata> getMetadata(String alias, String propertyName){
        String fullPath = this.getFullPropertyPath(alias, propertyName);
        return modelMetadata.getPathMetadata(fullPath);
    }

    /**
     * Get the full property path from the root object.
     * @param alias alias.
     * @param propertyName property name.
     * @return e.g. ctn.elements -> ct.contactNumbers.elements -> root.contact.contactNumbers.elements.
     */
    private String getFullPropertyPath(String alias, String propertyName) {
        Map<String, AliasDeclaration> aliases = searchMetadata.getAliases();
        String fullPath = propertyName;
        while (!AliasDeclaration.ROOT_CRITERIA_ALIAS.equals(alias)){
            AliasDeclaration declaration = aliases.get(alias);
            if(declaration == null)
                throw new SpongeConfigurationException(String.format("Alias '%s' is not declared on search model '%s'.", alias, searchMetadata.getName()));
            fullPath = declaration.propertyName() + "." + fullPath;
            alias = declaration.parentAlias();
        }
        return fullPath;
    }

    //endregion


    //region criteria builder implementation

    /**
     * Get the criteria identified by the given alias.
     * @param alias an alias declared on the search model.
     * @return the criteria.
     */
    public DetachedCriteria getCriteria(String alias){

        String levelAlias = getLevelAlias(alias);
        if(!criteriaMap.containsKey(levelAlias)){
            DetachedCriteria criteria = loadCriteria(alias);
            if(criteria == null)
                throw new SpongeConfigurationException(String.format("Alias '%s' is not found in search model '%s'.", alias, searchMetadata.getName()));
        }
        return criteriaMap.get(levelAlias);
    }


    /**
     * Lazy load a criteria into the criteria hierarchy.
     * @param alias pass in the original alias, load the level alias.
     */
    protected DetachedCriteria loadCriteria(String alias){

        String levelAlias = getLevelAlias(alias);

        //base case
        if(AliasDeclaration.ROOT_CRITERIA_ALIAS.equals(alias)){
            if(!criteriaMap.containsKey(levelAlias)){
                criteriaMap.put(levelAlias, DetachedCriteria.forClass(modelMetadata.getModelClass(), levelAlias));
            }
            return criteriaMap.get(levelAlias);
        }

        //otherwise
        if(!criteriaMap.containsKey(levelAlias)){
            //load
            Map<String, AliasDeclaration> aliases = searchMetadata.getAliases();
            if(!aliases.containsKey(alias))
                throw new SpongeConfigurationException(String.format("Alias '%s' is not found declared on search model '%s'.", alias, searchMetadata.getName()));

            criteriaMap.put(levelAlias, null);//loading mark
            AliasDeclaration declaration = aliases.get(alias);
            DetachedCriteria parent = loadCriteria(declaration.parentAlias());
            DetachedCriteria child = parent.createCriteria(declaration.propertyName(), levelAlias, declaration.joinType());
            criteriaMap.put(levelAlias, child);
        }
        DetachedCriteria loaded = criteriaMap.get(levelAlias);
        if(loaded == null)
            throw new SpongeConfigurationException(String.format("Circular reference is found in the alias declarations for search model %s.", searchMetadata.getName()));
        return loaded;
    }

    /**
     * Build the criteria for the given search object.
     * @param searchObject search dto.
     */
    public void buildCriteria(PageInfo searchObject){

        //add criteria from each search parameter
        List<List<Pair<Criterion, DetachedCriteria>>> result = createCriterionObjects(searchObject);
        result.forEach(this::addOrClauses);
    }

    protected List<List<Pair<Criterion, DetachedCriteria>>> createCriterionObjects(PageInfo searchObject){

        List<List<Pair<Criterion, DetachedCriteria>>> result = new ArrayList<>();
        for(PropertyMetadata propertyMetadata : searchMetadata.getPropertyMetadataMap().values()){

            //get property filters
            List<PropertyFilter> propertyFilters = propertyMetadata.getPropertyFilters();
            if(propertyFilters == null)
                continue;

            //get property value
            Object value = org.springframework.util.ReflectionUtils.invokeMethod(propertyMetadata.getGetter(), searchObject);
            if(value == null)
                continue;

            //create the clauses of the OR condition
            List<Pair<Criterion, DetachedCriteria>> clauses = new ArrayList<>();
            for(PropertyFilter propertyFilter : propertyFilters) {

                //resolve target property name
                String targetPropertyName = propertyFilter.property();
                if (targetPropertyName == null || Constants.REFLECTED_PROPERTY_NAME.equals(targetPropertyName))
                    targetPropertyName = propertyMetadata.getPropertyName();

                //get target property type
                DetachedCriteria parent = loadCriteria(propertyFilter.alias());
                String fullPropertyPath = getFullPropertyPath(parent.getAlias(), targetPropertyName);
                PropertyMetadata targetPropertyMetadata = modelMetadata.getPropertyMetadata(fullPropertyPath);
                Class<?> targetType = targetPropertyMetadata.getReturnType();

                //create Criterion

                CriterionFactory factory = CriterionFactoryLocator.getCriteriaFactory(propertyFilter.operator());
                Criterion criterion = factory.createCriterion(this, propertyFilter.alias(), targetPropertyName, targetType, value, propertyFilter.negate());
                clauses.add(new Pair<>(criterion, parent));
            }
            result.add(clauses);
        }

        return result;
    }

    /**
     * All criterion objects created for a single search parameter will be added as a disjunction criterion.
     * i.e. (clause1 or clause2 or clause3 ...). The result will be added to the criteria identified by the first Filter's alias.
     * A criterion usually is added to its parent criteria. However, when multiple criterion objects have different parent criteria object, we will choose the first.
     * This is not a problem unless a criterion object other than the first one is not qualified.
     * This means we have have at most one idEq per search object property and it has to be the first filter.
     * @param clauses list of criterion objects build from a single search object property.
     */
    private void addOrClauses(List<Pair<Criterion, DetachedCriteria>> clauses) {

        if(clauses.size() > 0) {
            Disjunction disjunction = Restrictions.disjunction();
            for(Pair<Criterion, DetachedCriteria> pair : clauses){
                disjunction.add(pair.getKey());
            }
            DetachedCriteria owner = clauses.get(0).getValue();
            owner.add(disjunction);
        }
    }

}
