package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import simpleshop.Constants;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SortInfo;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.*;
import simpleshop.data.util.DomainUtils;

import java.io.Serializable;
import java.util.*;


@Repository
public abstract class ModelDAOImpl<T> extends BaseDAOImpl implements ModelDAO<T> {

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the proxy to the domain object.
     */
    @Override
    public abstract T load(Serializable id);

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the domain object or null if cannot find.
     */
    @Override
    public abstract T get(Serializable id);

    public void save(T domainObject) {
        super.saveDomainObject(domainObject);
    }

    public void delete(T domainObject) {
        super.deleteDomainObject(domainObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject){
        //create aliases
        Criteria criteria = getSession().createCriteria(modelMetadata.getModelClass());
        Hashtable<String, Criteria> aliases = new Hashtable<>();
        aliases.put("", criteria);

        List<AliasDeclaration> aliasDeclarations = searchMetadata.getAliasDeclarations();
        createAliases(aliases, aliasDeclarations);

        //add criteria from each search parameter
        List<Pair<Criterion, Criteria>> propertyCriteria = new ArrayList<>(); //expressions for OR clause
        for(PropertyMetadata propertyMetadata : searchMetadata.getPropertyMetadataMap().values()){

            //get or filters
            List<PropertyFilter> propertyFilters = propertyMetadata.getPropertyFilters();
            if(propertyFilters == null)
                continue;

            //get property value
            Object value = org.springframework.util.ReflectionUtils.invokeMethod(propertyMetadata.getGetter(), searchObject);
            if(value == null)
                continue;

            for(PropertyFilter propertyFilter : propertyFilters) {//create criteria

                //get property name
                String propertyName = propertyFilter.property();
                if (propertyName == null || Constants.REFLECTED_PROPERTY_NAME.equals(propertyName))
                    propertyName = propertyMetadata.getPropertyName();

                String lastPart = propertyName.substring(propertyName.lastIndexOf('.') + 1);
                String fullPropertyPath = getFullPropertyPath(aliasDeclarations, propertyName, propertyFilter.alias());

                //get property type
                Class<?> targetType;
                if("this".equals(lastPart)){
                    ModelMetadata propertyOwnerMetadata = modelMetadata.getPropertyOwnerMetadata(fullPropertyPath);
                    targetType = propertyOwnerMetadata.getModelClass();
                } else {
                    PropertyMetadata targetPropertyMetadata = modelMetadata.getPropertyMetadata(fullPropertyPath);
                    targetType = targetPropertyMetadata.getReturnType();
                }

                //add
                String alias = createAssociationPath(aliases, propertyFilter.alias(), propertyName);
                String qualifiedPropertyName = "this".equals (lastPart) ? alias : ((StringUtils.isNullOrEmpty(alias) ? "" : alias + ".") + lastPart);
                Criterion criterion = createCriterion(qualifiedPropertyName, propertyFilter.operator(), targetType, value, propertyFilter.negate());
                propertyCriteria.add(new Pair<>(criterion, "this".equals(lastPart) ? aliases.get(propertyFilter.alias()) : criteria));
            }

            addCriteria(propertyCriteria);
            propertyCriteria.clear();
        }

        //sorting
        if(searchObject.getSortInfoList() != null){
            for(SortInfo sortInfo : searchObject.getSortInfoList()){
                aliases.get(sortInfo.getAlias()).addOrder(sortInfo.isAscending() ? Order.asc(sortInfo.getProperty()) : Order.desc(sortInfo.getProperty()));
            }
        }

        //paging
        if(searchObject.getPageSize() > 0){
            criteria.setMaxResults(searchObject.getPageSize() + (searchObject.isPageSizePlusOne() ? 1 : 0));
            criteria.setFirstResult(searchObject.getPageIndex() * searchObject.getPageSize());
        }
        return criteria.list();
    }

    private String createAssociationPath(Hashtable<String, Criteria> aliases, String alias, String propertyName) {

        Criteria parentCriteria = aliases.get(alias);
        String[] parts = propertyName.split("\\.");

        for(int i=0; i<parts.length - 1; i++){
            String subAlias = alias + "_9_" + parts[i];
            if(!aliases.contains(subAlias)){
                aliases.put(subAlias, parentCriteria.createCriteria(parts[i], subAlias));
            }
            alias = subAlias;
            parentCriteria = aliases.get(subAlias);
        }

        return alias;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> quickSearch(String keywords, PageInfo pageInfo){
        throw new UnsupportedOperationException("Quick search is not implemented for " + this.getClass().getSimpleName());
    }

    ///////////////////////// protected methods //////////////////////////////////////////

    @SuppressWarnings("unchecked")
    protected T load(Class<T> clazz, Serializable id) {
        return (T) getSession().load(clazz, id);
    }

    @SuppressWarnings("unchecked")
    protected T get(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    protected List<T> getList(String hql, PageInfo pageInfo, Object... parameters){
        return createQuery(hql, pageInfo, parameters).list();
    }

    private void createAliases(Hashtable<String, Criteria> aliases, List<AliasDeclaration> aliasDeclarations) {
        if(aliasDeclarations != null)
            for(AliasDeclaration aliasDeclaration1 : aliasDeclarations){
                Criteria parentCriteria = aliases.get(aliasDeclaration1.parentAlias());
                Criteria childCriteria = parentCriteria.createCriteria(aliasDeclaration1.propertyName(), aliasDeclaration1.aliasName(), aliasDeclaration1.joinType());
                if(aliases.get(aliasDeclaration1.aliasName()) != null)
                    throw new RuntimeException("Alias '" + aliasDeclaration1.aliasName() + "' is already defined.");
                aliases.put(aliasDeclaration1.aliasName(), childCriteria);
            }
    }

    private String getFullPropertyPath(List<AliasDeclaration> aliasDeclarations, String fullPropertyPath, String alias) {
        while (!"".equals(alias)){
            AliasDeclaration declaration = findAliasDeclaration(aliasDeclarations, alias);
            fullPropertyPath = declaration.propertyName() + "." + fullPropertyPath;
            alias = declaration.parentAlias();
        }
        return fullPropertyPath;
    }

    private void addCriteria(List<Pair<Criterion, Criteria>> propertyCriteria) {
        if(propertyCriteria.size() == 1){
            Pair<Criterion, Criteria> pair = propertyCriteria.get(0);
            pair.getValue().add(pair.getKey());
        } else if(propertyCriteria.size() > 1) {
            Criterion[] expressions = new Criterion[propertyCriteria.size()];
            for(int i=0; i<propertyCriteria.size(); i++){
                Pair<Criterion, Criteria> pair = propertyCriteria.get(i);
                expressions[i] = pair.getKey();
                if(i > 0 && propertyCriteria.get(i-1).getValue() != pair.getValue()){
                    throw new SpongeConfigurationException("OR conditions must be added to the same criteria.");
                }
            }
            propertyCriteria.get(0).getValue().add(Restrictions.or(expressions));
        }
    }

    private AliasDeclaration findAliasDeclaration(List<AliasDeclaration> aliasDeclarations, String alias) {
        int start = 0;
        int end = aliasDeclarations.size() - 1;

        while(end >= start){
            int mid = start + (end - start) / 2;
            if(aliasDeclarations.get(mid).aliasName().equals(alias))
                return aliasDeclarations.get(mid);

            if(aliasDeclarations.get(mid).aliasName().compareTo(alias) < 0)
                end = mid - 1;
            else
                start = mid + 1;
        }

        return null;
    }

    private Criterion createCriterion(String propertyName, PropertyFilter.Operator operator, Class<?> targetType, Object value, boolean negate) {
        Criterion criterion;
        Object parsedObject;
        switch (operator){
            case LIKE:
                if(!CharSequence.class.isAssignableFrom(targetType))
                    throw new SpongeConfigurationException("LIKE operator does not apply to property type: " + targetType.getName());

                String pattern = StringUtils.wrapLikeKeywords(value.toString());
                criterion = Restrictions.like(propertyName, pattern);
                if(negate) {
                    criterion = Restrictions.not(criterion);
                }
                break;

            case IN:
                Object[] values;
                if(value instanceof Iterable<?>){
                    Iterable<?> iterable = (Iterable<?>)value;
                    List<Object> list = new ArrayList<>();
                    for(Object obj : iterable){
                        list.add(obj);
                    }
                    values = list.toArray();
                } else {
                    values = value.toString().split(",");
                    for(int i=0; i<values.length;i++)
                        values[i] = simpleshop.common.ReflectionUtils.parseObject(values[i],targetType);
                }
                criterion = Restrictions.in(propertyName, values);
                if(negate) {
                    criterion = Restrictions.not(criterion);
                }
                break;

            case CONTAINS:
                if(negate)
                    throw new SpongeConfigurationException("Negation is not supported with CONTAINS criteria.");

                if(!DomainUtils.isDomainObject(value)){
                    throw new SpongeConfigurationException("CONTAINS criteria must be applied to a domain object.");

                }
                criterion = Restrictions.idEq(this.getIdentifier(value));
                break;

            case EQUAL:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    criterion = Restrictions.ne(propertyName, parsedObject);
                } else {
                    criterion = Restrictions.eq(propertyName, parsedObject);
                }
                break;

            case GREATER:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    criterion = Restrictions.le(propertyName, parsedObject);
                } else {
                    criterion = Restrictions.gt(propertyName, parsedObject);
                }
                break;

            case LESS:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    criterion = Restrictions.ge(propertyName, parsedObject);
                } else {
                    criterion = Restrictions.lt(propertyName, parsedObject);
                }
                break;

            case IS_NULL:
                if(Objects.equals("false", value) ^ negate)
                    criterion = Restrictions.isNull(propertyName);
                else
                    criterion = Restrictions.isNotNull(propertyName);
                break;

            default:
                throw new IllegalArgumentException("operator");
        }
        return criterion;
    }

}
