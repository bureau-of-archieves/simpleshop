package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import simpleshop.data.infrastructure.CriteriaBuilder;
import simpleshop.data.infrastructure.ModelDAO;
import simpleshop.data.PageInfo;
import simpleshop.data.SortInfo;
import simpleshop.data.metadata.*;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.*;


@Repository
public abstract class ModelDAOImpl<T> extends BaseDAOImpl implements ModelDAO<T> {

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the proxy to the domain object.
     */
    @Override
    public T load(Serializable id){
        return load(getModelClass(), id);
    }

    /**
     * Cannot implement at ModelDAOImpl level because Java generic type erasure.
     * @param id id of the domain object.
     * @return the domain object or null if cannot find.
     */
    @Override
    public T get(Serializable id){
        return get(getModelClass(), id);
    }

    public void save(T domainObject) {
        super.saveDomainObject(domainObject);
    }

    public void delete(T domainObject) {
        super.deleteDomainObject(domainObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> search(ModelMetadata searchMetadata, ModelMetadata modelMetadata, PageInfo searchObject){

        CriteriaBuilder builder = new ModelSearchCriteriaBuilder(searchMetadata, modelMetadata, getSession());

        builder.buildCriteria(searchObject);

        //sorting
        DetachedCriteria rootCriteria = builder.getCriteria(AliasDeclaration.ROOT_CRITERIA_ALIAS);
        if(searchObject.getSortInfoList() != null){
            for(SortInfo sortInfo : searchObject.getSortInfoList()){
                DetachedCriteria parentCriteria = builder.getCriteria(sortInfo.getAlias());
                Order sortOrder = sortInfo.isAscending() ? Order.asc(sortInfo.getProperty()) : Order.desc(sortInfo.getProperty());
                parentCriteria.addOrder(sortOrder);
            }
        }

        //paging
        Criteria executableCriteria = rootCriteria.getExecutableCriteria(getSession());
        if(searchObject.getPageSize() > 0){
            executableCriteria.setMaxResults(searchObject.getPageSize() + (searchObject.isPageSizePlusOne() ? 1 : 0));
            executableCriteria.setFirstResult(searchObject.getPageIndex() * searchObject.getPageSize());
        }
        return executableCriteria.list();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<T> quickSearch(String keywords, PageInfo pageInfo){
        throw new UnsupportedOperationException("Quick search is not implemented for " + this.getClass().getSimpleName());
    }

    /**
     * Get the model class of this DAO class.
     * @return model class.
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getModelClass(){
        ParameterizedType concreteSuperClass = (ParameterizedType)this.getClass().getGenericSuperclass();
        return (Class<T>)concreteSuperClass.getActualTypeArguments()[0];
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

}
