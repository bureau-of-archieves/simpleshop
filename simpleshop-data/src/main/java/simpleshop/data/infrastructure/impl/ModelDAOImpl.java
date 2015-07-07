package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import simpleshop.Constants;
import simpleshop.common.StringUtils;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SortInfo;
import simpleshop.data.metadata.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;


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
        List<Criterion> propertyCriteria = new ArrayList<>(); //expressions for OR clause
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

                //get property type
                String fullPropertyPath = getFullPropertyPath(aliasDeclarations, propertyName, propertyFilter.alias());
                PropertyMetadata targetPropertyMetadata = modelMetadata.getPropertyMetadata(fullPropertyPath);
                Class<?> targetType = targetPropertyMetadata.getReturnType();

                //add
                String alias = createAssociationPath(aliases, propertyFilter.alias(), propertyName);
                String lastPart = propertyName.substring(propertyName.lastIndexOf('.') + 1);
                Criterion criterion = createCriterion((StringUtils.isNullOrEmpty(alias) ? "" : alias + ".") + lastPart, propertyFilter.operator(), targetType, value, propertyFilter.negate());
                propertyCriteria.add(criterion);
            }
            addCriteria(criteria, propertyCriteria);
            propertyCriteria.clear();
        }

        if(searchObject.getSortInfoList() != null){
            for(SortInfo sortInfo : searchObject.getSortInfoList()){
                aliases.get(sortInfo.getAlias()).addOrder(sortInfo.isAscending() ? Order.asc(sortInfo.getProperty()) : Order.desc(sortInfo.getProperty()));
            }
        }

        if(searchObject.getPageSize() > 0){
            criteria.setMaxResults(searchObject.getPageSize());
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

    private void addCriteria(Criteria parentCriteria, List<Criterion> propertyCriteria) {
        if(propertyCriteria.size() == 1){
            parentCriteria.add(propertyCriteria.get(0));
        } else if(propertyCriteria.size() > 1) {
            Criterion[] expressions = new Criterion[propertyCriteria.size()];
            propertyCriteria.toArray(expressions);
            parentCriteria.add(Restrictions.or(expressions));
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
        //todo change negate=true op= less than => >=
        Criterion criterion;
        Object parsedObject;
        switch (operator){
            case LIKE:
                String pattern = StringUtils.wrapLikeKeywords(value.toString());
                criterion = Restrictions.like(propertyName, pattern);
                break;
            case IN:
                Object[] values = value.toString().split(",");
                for(int i=0; i<values.length;i++)
                    values[i] = simpleshop.common.ReflectionUtils.parseObject(values[i],targetType);
                criterion = Restrictions.in(propertyName, values);
                break;
            case EQUAL:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value,targetType);
                criterion = Restrictions.eq(propertyName, parsedObject);
                break;
            case GREATER:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value,targetType);
                criterion = Restrictions.gt(propertyName, parsedObject);
                break;
            case LESS:
                parsedObject = simpleshop.common.ReflectionUtils.parseObject(value,targetType);
                criterion = Restrictions.lt(propertyName, parsedObject);
                break;
            case IS_NULL:
                if(Objects.equals("false", value) )
                    criterion = Restrictions.isNull(propertyName);
                else
                    criterion = Restrictions.isNotNull(propertyName);
                break;
            default:
                throw new IllegalArgumentException("operator");
        }
        if(negate)
            criterion = Restrictions.not(criterion);
        return criterion;
    }

}
